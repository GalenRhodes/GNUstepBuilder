#############################################################################
# Find out what flavor of Linux we're working with.
#
export BOARD_NAME=`cat /proc/cpuinfo | grep '^Hardware' | awk '{print $3}'`
export DISTRO_NAME=`lsb_release -is`
export DISTRO_VERSION=`lsb_release -rs`

#############################################################################
function TestOSVersion() {
    local __v
    
    for __v in "$@"; do
        if [ "${DISTRO_VERSION}" = "${__v}" ]; then
            return 0
        fi
    done
    
    return 1
}

#############################################################################
function TestBoardName() {
    local __b
    
    for __b in "$@"; do
        if [ "${BOARD_NAME}" = "${__b}" ]; then
            return 0
        fi
    done
    
    return 1
}

#############################################################################
function TestOdroidXU4() {
    TestBoardName "ODROID-XU3" && TestOSVersion "15.10" "15.04" && return 0
    return 1
}

#############################################################################
function TestOdroidC2() {
    TestBoardName "ODROID-C2" && TestOSVersion "16.04" && return 0
    return 1
}

#############################################################################
function TestOdroidU3() {
    TestBoardName "ODROID-U3" && TestOSVersion "14.04" "14.04.2" && return 0
    return 1
}

##################################################################
# Raspberry Pi Foundation have tried to make things very straight
# forward by keeping all models software compatible with previous
# models.  But, we're going to insist that the later models that
# use the Cortex-* family be running Ubuntu versions 15.04 and
# newer.
#
# Models A, B and B+ use the ARM1176JZFS processor (armv6-a)
# Model B-2 uses Cortex-A7 (armv7-a)
# Model B-3 uses Cortex-A53 (armv8-a)
##################################################################
function TestRPI() {
    TestBoardName "BCM2709" "BCM2708" || return 1
    local cpumodel=`cat /proc/cpuinfo | grep -E '^model name' | awk '{print $4}' | sort -u`
    
    if [ "${cpumodel}" = "ARMv7" ]; then
        if [ "${DISTRO_NAME}" = "Ubuntu" ]; then
            TestOSVersion "15.04" "15.10" "16.04" "16.10" || return 1
        else
            return 1
        fi
    fi
    
    return 0
}

#############################################################################
function zprint() {
    local x=1

    if [ "$1" = "-n" ]; then
        shift
        x=0
    fi

    if [ $# -ge 2 ]; then
        printf "\e[0J\e[0m\e[1;37m[%s\e[1;37m]\e[1;36m" "$1"
        shift
        printf " %s" "$@"
    elif [ $# -eq 1 ]; then
        printf "\e[0J\e[0m\e[1;37m[%s\e[1;37m]" "$1"
    fi

    if [ ${x} -eq 0 ]; then
        printf "\e[0m"
    else
        printf "\e[0m\n"
    fi

    return 0
}

#############################################################################
function zecho() {
    local c=$'\e[1;32m'
    
    if [ "$1" = "-n" ]; then
        local x="$1"
        local t="$2"
        shift 2
        zprint "${x}" "${c}${t}" "$@"
    else
        local t="$1"
        shift
        zprint "${c}${t}" "$@"
    fi

    return 0
}

#############################################################################
function zwarn() {
    local r="$1"
    local c=$'\e[33m'
    
    shift
    zecho "${c}WARNING" "$@"
    return ${r}
}

#############################################################################
function zfail() {
    if [ "$1" = "-ln" -o "$1" = "-nl" ]; then
        shift
        zfail -l -n $@
    elif [ "$1" = "-n" -a "$2" = "-l" ]; then
        shift 2
        zfail -l -n $@
    else
        local a=$'\e[31m'
        local b=$'\e[33m'
        local c=$'\e[0m'
        local cr=true
        
        if [ "$1" = "-l" ]; then
            echo ""
            shift
        fi
        if [ "$1" = "-n" ]; then
            cr=false
            shift
        fi

        local r="$1"
        shift
        
        zecho -n "${a}ERROR" "${b}$@${c}"
        
        if [ "$cr" = true ]; then
            echo ""
            echo ""
        fi
                
        exit ${r}
    fi
}

#############################################################################
function GrepPath() {
    if [ -n "$1" -a -n "$2" ]; then
        if eval "echo \"\${$1}\" | grep -Ee \"^$2:|:$2:|:$2\$|^$2\$\" >/dev/null"; then
            return 1
        fi
    fi
    
    return 0
}

#############################################################################
function AddToAnyPath() {
    if [ "$#" -gt 1 -a -n "$1" ]; then
        local __p="$1"
        shift
        
        while [ "$#" -gt 0 ]; do
            local __a="$1"
            local __q=`eval "echo \"\\\${${__p}}\""`
            shift
            
            if [ -z "${__q}" ]; then
                eval "export ${__p}=\"${__a}\""
            elif [ -n "${__a}" -a -d "${__a}" ]; then
                if GrepPath "${__p}" "${__a}"; then
                    eval "export ${__p}=\"${__a}:\${${__p}}\""
                fi
            fi
        done
        
        return 0
    fi
    
    return 1
}

#############################################################################
function AddToPath() {
    if [ "$#" -gt 0 -a -n "$1" ]; then
        if AddToAnyPath "PATH" "$@"; then
            return 0
        fi
    fi
    
    return 1
}

#############################################################################
function PushDir() {
    pushd "$1" >/dev/null 2>&1
    return "$?"
}

#############################################################################
function PopDir() {
    popd >/dev/null 2>&1
    return "$?"
}

function buildGNUstepMake() {
	local nfabi=""
	local bashpath=""
	local configure=false
	
	if [ "$1" = "-startup-scripts" ]; then
	    configure=true
	    shift
	fi
	
	if [ "${BASH_D}" = true ]; then
	    bashpath="${HOME}/.bash.d/bash04gnustep.sh"
	fi

	if [ "${USE_NONFRAGILE_ABI_FLAG}" = true ]; then
		nfabi="--enable-objc-nonfragile-abi"
	fi

	cd "${COREDIR}/make"
	zecho "CONFIGURING" "GNUstep Make"
	./configure "--prefix=${GSDIR}" "${nfabi}" \
		"--with-library-combo=ng-gnu-gnu" \
		"--enable-objc-arc" \
		"--enable-native-objc-exceptions" \
		"--enable-debug-by-default" \
		"--with-layout=gnustep" \
		"--enable-install-ld-so-conf" \
		"--with-objc-lib-flag=-l${OBJC_NAME}" \
		|| return "$?"
	zecho "BUILDING" "GNUstep Make"
	make "-j${PROCESSORS}"
	sudo -E make install

    if [ "${configure}" = true ]; then
        if [ "${BASH_D}" = true ]; then
	        rm "${bashpath}" 2>/dev/null
	        ln -s "${GNUSTEP}" "${bashpath}"
        else
            echo -e "###############################################################\n# Loads the GNUstep configuration script.\n#\n. \"${GNUSTEP}\"\n\n" >> "${HOME}/.bashrc"
        fi
    fi
    
	showPrompt
	return "$?"
}

function buildLibDispatch() {
	cd "${PRJDIR}/libdispatch"
	mkdir build
	cd build
	zecho "CONFIGURING" "libdispatch"

	if [ "${USE_SWIFT_DISPATCH}" = true ]; then
		cmake -G "Ninja" \
			"-DCMAKE_BUILD_TYPE=Release" \
			"-DCMAKE_CXX_COMPILER=${CXX}" \
			"-DCMAKE_CXX_FLAGS=" \
			"-DCMAKE_CXX_FLAGS_RELEASE=${CFLAGS}" \
			"-DCMAKE_C_COMPILER=${CC}" \
			"-DCMAKE_C_FLAGS=" \
			"-DCMAKE_C_FLAGS_RELEASE=${CFLAGS}" \
			"-DCMAKE_INSTALL_PREFIX=${INSTALL_PATH}" \
			"-DCMAKE_LINKER=${LD}" \
	    	"-DCMAKE_MODULE_LINKER_FLAGS=" \
			.. || zfail  "$?" "CMAKE Failed!"
		zecho "BUILDING" "libdispatch"
		ninja "-j${PROCESSORS}" || zfail "$?"
		sudo ninja install || zfail "$?"
	else
		cmake -G "Unix Makefiles" \
			"-DCMAKE_BUILD_TYPE=Release" \
			"-DCMAKE_CXX_COMPILER=${CXX}" \
			"-DCMAKE_CXX_FLAGS=" \
			"-DCMAKE_CXX_FLAGS_RELEASE=${CFLAGS}" \
			"-DCMAKE_C_COMPILER=${CC}" \
			"-DCMAKE_C_FLAGS=" \
			"-DCMAKE_C_FLAGS_RELEASE=${CFLAGS}" \
			"-DCMAKE_INSTALL_PREFIX=${INSTALL_PATH}" \
			"-DCMAKE_LINKER=${LD}" \
    		"-DCMAKE_MODULE_LINKER_FLAGS=" \
			.. || zfail  "$?" "CMAKE Failed!"
		zecho "BUILDING" "libdispatch"
		make "-j${PROCESSORS}" || zfail "$?" "Build Failed!"
		sudo -E make install || zfail "$?" "Install Failed!"
	fi
	sudo ldconfig
}

function buildLibObjc2() {
	cd "${PRJDIR}/libobjc2"
	mkdir build
	zecho "CONFIGURING" "libobjc"

	cd build
	cmake -G "Unix Makefiles" \
		"-DBUILD_STATIC_LIBOBJC=ON" \
		"-DCMAKE_BUILD_TYPE=Release" \
		"-DCMAKE_CXX_COMPILER=${CXX}" \
		"-DCMAKE_CXX_FLAGS_RELEASE=${CFLAGS}" \
		"-DCMAKE_C_COMPILER=${CC}" \
		"-DCMAKE_C_FLAGS=" "-DCMAKE_CXX_FLAGS=" \
		"-DCMAKE_C_FLAGS_RELEASE=${CFLAGS}" \
		"-DCMAKE_INSTALL_PREFIX=${INSTALL_PATH}" \
		"-DCMAKE_LINKER=${LD}" \
		"-DLIBOBJC_NAME=${OBJC_NAME}" \
		"-DOLDABI_COMPAT=${OBJC_OLDABI_COMPAT}" \
		"-DTESTS=OFF" \
		"-DTYPE_DEPENDENT_DISPATCH=ON" \
		.. || zfail  "$?" "CMAKE Failed!"
	showPrompt
	zecho "BUILDING" "libobjc"
	make "-j${PROCESSORS}" || zfail "$?" "Build Failed!"
	sudo -E make install || zfail "$?" "Install Failed!"
	PushDir "${INSTALL_PATH}/include"

	if [ -e "objc/blocks_runtime.h" ]; then
        sudo rm "Block.h" 2>>/dev/null
        sudo ln -s "objc/blocks_runtime.h" "Block.h"
    fi

    if [ -e "objc/blocks_private.h" ]; then
        sudo rm "Block_private.h" 2>>/dev/null
        sudo ln -s "objc/blocks_private.h" "Block_private.h"
    fi
    
    PopDir
	sudo ldconfig
}

function showPrompt() {
    if [ "${PROMPT}" = true ]; then
        local msg="Press enter to continue..."
        
        if [ $# -ge 1 ]; then
            msg="$@"
        fi
        
        echo ""
        zecho -n "INPUT" $msg
        read -p ""
        echo ""
    fi
}

function svnFiles() {
    local e="${PRJDIR}/SVNLogs.${1// /_}.err"
    local c="0"
    
	zecho -n "DOWNLOADING" "${1}..."
	svn co "$2" "$3" > "${PRJDIR}/SVNLogs.${1// /_}.log" 2>"${e}" || c="$?"
	
	if [ "$c" -ne 0 ]; then
	    echo ""
	    cat "${e}"
	    return "$c"
	else
	    echo " DONE"
	    zecho -n "UPGRADING" "SVN Repository..."
	    svn upgrade "$3"
	    echo " DONE"
	    return 0
	fi
}

function gitFiles() {
    local _description="$1"
    local _url="$2"
    local _path=""
    local _branch=""
	local _before="N"
	local _before_date="${BEFORE_DATE}"
    local _r=""
    local _l=""

    shift 2

    if [ "$1" = "--branch" ]; then
        _branch="$2"
        shift 2
    fi

    _path="$1"

	if [ $# -ge 2 -a "$2" = "Y" ]; then
		_before="Y"
		if [ $# -ge 3 -a -n "$3" ]; then
			_before_date="$3"
		fi
	fi

	zecho "DOWNLOADING" "${_description}..."
	if [ -n "${_branch}" ]; then
	    git clone "${_url}" --branch "${_branch}" --single-branch "${_path}"
	else
	    git clone "${_url}" "${_path}"
	fi
    _r="$?"

	if [ "${_before}" = "Y" ]; then
		if [ "${_before_date}" != "${TIMESTAMP}" ]; then
			pushd "${_path}" >>/dev/null
			zecho "CHECKOUT" "Checking out image dated before ${_before_date}..."
			_l=`git rev-list -n 1 --first-parent --before="${_before_date}" master`
			zecho "CHECKOUT" "Checking out label ${_l}..."
			git checkout "${_l}"
        	_r="$?"
			popd >>/dev/null
			showPrompt
		fi
	fi

	return "${_r}"
}

function pe() {
	printenv | grep --color=auto -v "^LS_COLORS=" | sort | grep -Ee "^[^=]+="
	return 0
}

function ldv() {
	ld --verbose | grep SEARCH_DIR | tr -s ' ;' \\012
	return 0
}

function buildGNUstepSub() {
    local _dir="$1"
    local _name="$2"
    
    shift 2
    cd "${COREDIR}/${_dir}"

    zecho "CONFIGURING" "GNUstep ${_name}"
    ./configure $@ || zfail "$?" "GNUstep ${_name}!"

    zecho "BUILDING" "GNUstep ${_name}"
    make "-j${PROCESSORS}" || zfail "$?" "GNUstep ${_name}!"

    sudo -E make install || zfail "$?" "GNUstep ${_name}!"
    sudo ldconfig

    showPrompt
    return 0
}

#############################################################################
export TestOSVersion
export TestBoardName
export TestOdroidXU4
export TestOdroidC2
export TestOdroidU3
export TestRPI
export zprint
export zecho
export zwarn
export zfail
export GrepPath
export AddToAnyPath
export AddToPath
export PushDir
export PopDir

#############################################################################
AddToPath "${HOME}/bin"
#
#############################################################################
