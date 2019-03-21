#!/bin/bash

. "${PDIR}/bash01common.sh"

INSTALL_PATH="/opt/objc"

PROMPT=true
BUILD_CLANG=false
BUILD_GTEST=true
INSTALL_LIBKQUEUE=true
INSTALL_REQUIRED=false
USE_SWIFT_DISPATCH=true
LD_SO_CONF_D=true
BASH_D=true
USE_NONFRAGILE_ABI_FLAG=true
INCLUDE_ARM_SUPPORT=false
BUILD_DISPATCH_FIRST=true
BUILD_INITIAL_GNUSTEP_MAKE=true
GNUSTEP_NO_MIXED_ABI=true

OBJC_NAME="objc2"
OBJC_VERS="2.0"
OBJC_OLDABI_COMPAT="OFF"

GITHUB="https://github.com"

PDIR="$(dirname `readlink -f "$0"`)"
#PDIR="${HOME}/Projects"
PRJDIR="${PDIR}/GNUstep"

COREDIR="${PRJDIR}/core"
GSDIR="${INSTALL_PATH}/gnustep"
GNUSTEP="${GSDIR}/System/Library/Makefiles/GNUstep.sh"
GNUVER="-fobjc-runtime=gnustep-${OBJC_VERS}"

LLVMSVN="http://llvm.org/svn/llvm-project"
LLVM_VER="trunk"
LLVM_INSTALL_PATH="${INSTALL_PATH}/llvm"
#LLVM_TARGETS_TO_BUILD="AArch64;ARM;X86"
LLVM_TARGETS_TO_BUILD="X86"
LIBGTEST_SRC="/usr/src/googletest"

if [ "${INCLUDE_ARM_SUPPORT}" = true ]; then
    LLVM_TARGETS_TO_BUILD="AArch64;ARM;${LLVM_TARGETS_TO_BUILD}"
    USE_SWIFT_DISPATCH=false
fi

TIMESTAMP=`date +%Y-%m-%d`
BEFORE_DATE="2017-04-06"
OBJC2_DATE="${TIMESTAMP}"

showPrompt "Press enter to begin..."

export CC=`which cc`
export CXX=`which c++`
export CFLAGS="-O3 -g0 -w"

JAVAEXE=`which java`
if [ -n "${JAVA_HOME}" ]; then
    JAVAEXE="${JAVA_HOME}/bin/java"
fi

if [ "${LD_SO_CONF_D}" = true ]; then
    local ldfil="objc2.conf"
    local lddir="/etc/ld.so.conf.d"
    
    mkdir -p "${INSTALL_PATH}/lib"
    rm "${INSTALL_PATH}/${ldfil}" 2>/dev/null
    echo "${INSTALL_PATH}/lib" > "${INSTALL_PATH}/${ldfil}"
    sudo rm "${lddir}/${ldfil}"
    sudo mv "${INSTALL_PATH}/${ldfil}" "${lddir}/"
fi

PROCESSORS=`cat /proc/cpuinfo | grep -E '^processor' | nl | awk '{print $1}' | tail -n1`

mkdir -p "${INSTALL_PATH}/bin"

if [ "${INSTALL_REQUIRED}" = true ]; then
    sudo apt-get update
    sudo apt-get -y install build-essential git subversion ninja-build cmake libffi-dev libxml2-dev libgnutls28-dev libicu-dev \
        libblocksruntime-dev autoconf libtool libjpeg-dev libtiff-dev libffi-dev libcairo2-dev libx11-dev libxt-dev libxft-dev \
        python-dev libncurses5-dev doxygen swig libedit-dev
        
    if [ $"{INSTALL_LIBKQUEUE}" = true ]; then
        sudo apt-get -y install libkqueue-dev libpthread-workqueue-dev
    fi
fi

if [ "${BUILD_CLANG}" = true ]; then
    zecho "INFO" "Downloading LLVM/CLANG source code..."

	svnFiles "LLVM Source"          "${LLVMSVN}/llvm/${LLVM_VER}"              "${PRJDIR}/llvm"                                           || zfail -l "$?" "SVN Request Failed!"
	svnFiles "CLANG Source"         "${LLVMSVN}/cfe/${LLVM_VER}"               "${PRJDIR}/llvm/tools/clang"                               || zfail -l "$?" "SVN Request Failed!"
	svnFiles "CLANG LLDB Source"    "${LLVMSVN}/lldb/${LLVM_VER}"              "${PRJDIR}/llvm/tools/lldb"                                || zfail -l "$?" "SVN Request Failed!"
	svnFiles "CLANG Runtime Source" "${LLVMSVN}/compiler-rt/${LLVM_VER}"       "${PRJDIR}/llvm/projects/compiler-rt"                      || zfail -l "$?" "SVN Request Failed!"
	svnFiles "CLANG Tools Source"   "${LLVMSVN}/clang-tools-extra/${LLVM_VER}" "${PRJDIR}/llvm/tools/clang/tools/extra/clang-tools-extra" || zfail -l "$?" "SVN Request Failed!"

    if [ "${BUILD_GTEST}" = true ]; then
        # the GTest library HAS to go into /usr/lib or it WILL NOT BE FOUND! This is very weird!
        local libgtest_install_path="/usr"
        local libgtest_name="Google GTest Framework"
        
        zecho "INFO" "Downloading ${libgtest_name}..."
        sudo apt-get -y install libgtest-dev
        rm -fr "${PRJDIR}/gtest"
        mkdir "${PRJDIR}/gtest"
        cd "${PRJDIR}/gtest"
        zecho "INFO" "Building ${libgtest_name}..."
        cmake -G "Unix Makefiles" -Wno-dev 
     	    "-DCMAKE_BUILD_TYPE=Release" \
	        "-DCMAKE_CXX_COMPILER=${CXX}" \
	        "-DCMAKE_CXX_FLAGS=" \
	        "-DCMAKE_CXX_FLAGS_RELEASE=${CFLAGS}" \
	        "-DCMAKE_C_COMPILER=${CC}" \
	        "-DCMAKE_C_FLAGS=" \
	        "-DCMAKE_C_FLAGS_RELEASE=${CFLAGS}" \
	        "-DCMAKE_INSTALL_PREFIX=${libgtest_install_path}" \
	        "-DCMAKE_LINKER=`which ld.gold`" \
	        "-DCMAKE_MODULE_LINKER_FLAGS=" \
	        "-DBUILD_SHARED_LIBS=ON" \
	        "-DBUILD_GTEST=ON" \
	        "${LIBGTEST_SRC}" || zfail -l "$?" "Failed to build ${libgtest_name}"
        make -j"${PROCESSORS}" || zfail -l "$?" "Failed to build ${libgtest_name}"
        sudo -E make install || zfail -l "$?" "Failed to build ${libgtest_name}"
    fi
    
    showPrompt
    zecho "INFO" "Configuring LLVM/CLANG..."
    
    isclang=`/usr/bin/cc --version | grep 'clang'`
    if [ -n "${isclang}" ]; then
        CFLAGS="-integrated-as -Qunused-arguments ${CFLAGS}"
    fi
    
    rm -fr "${PRJDIR}/llvm/build" 2>/dev/null 
    mkdir "${PRJDIR}/llvm/build"
    cd "${PRJDIR}/llvm/build"
    cmake -G "Unix Makefiles" -Wno-dev \
	    "-DBUILD_SHARED_LIBS=ON" \
	    "-DCLANG_INCLUDE_DOCS=OFF" \
	    "-DCLANG_INCLUDE_TESTS=OFF" \
	    "-DCMAKE_BUILD_TYPE=Release" \
        "-DCMAKE_CXX_COMPILER=${CXX}" \
        "-DCMAKE_CXX_FLAGS=" \
        "-DCMAKE_CXX_FLAGS_RELEASE=${CFLAGS}" \
        "-DCMAKE_C_COMPILER=${CC}" \
	    "-DCMAKE_C_FLAGS=" \
	    "-DCMAKE_C_FLAGS_RELEASE=${CFLAGS}" \
	    "-DCMAKE_INSTALL_PREFIX=${LLVM_INSTALL_PATH}" \
	    "-DCMAKE_LINKER=`which ld.gold`" \
	    "-DCMAKE_MODULE_LINKER_FLAGS=" \
	    "-DCOMPILER_RT_CAN_EXECUTE_TESTS=OFF" \
	    "-DCOMPILER_RT_INCLUDE_TESTS=OFF" \
	    "-DFFI_INCLUDE_DIR=$(dirname "$(find /usr -name "ffi.h")")" \
	    "-DFFI_LIBRARY_DIR=$(dirname "$(find /usr -name "libffi.so")")" \
	    "-DLLVM_BINUTILS_INCDIR=$(dirname "$(find /usr -name "plugin-api.h")")" \
	    "-DLLVM_BUILD_DOCS=OFF" \
	    "-DLLVM_ENABLE_EH=ON" \
	    "-DLLVM_ENABLE_FFI=ON" \
	    "-DLLVM_ENABLE_RTTI=ON" \
	    "-DLLVM_INCLUDE_BENCHMARKS=OFF" \
	    "-DLLVM_INCLUDE_DOCS=OFF" \
	    "-DLLVM_INCLUDE_EXAMPLES=OFF" \
	    "-DLLVM_INCLUDE_GO_TESTS=OFF" \
	    "-DLLVM_INCLUDE_TESTS=OFF" \
	    "-DLLVM_INCLUDE_TOOLS=ON" \
	    "-DLLVM_INCLUDE_UTILS=ON" \
	    "-DLLVM_INSTALL_UTILS=ON" \
	    "-DLLVM_TARGETS_TO_BUILD=${LLVM_TARGETS_TO_BUILD}" \
	    "-DLLVM_TOOL_CLANG_TOOLS_EXTRA_BUILD=ON" \
	    .. || zfail -l "$?" "CMAKE Failed!"
    #cmake-gui .. || zfail -l "$?" "CMAKE Failed!"
	zecho "INFO" "BUILDING LLVM/CLANG"
	make "-j${PROCESSORS}" || zfail "$?" "Build Failed!"
	sudo -E make install || zfail "$?" "Install Failed!"
	
	export CC="${LLVM_INSTALL_PATH}/bin/clang"
	export CXX="${LLVM_INSTALL_PATH}/bin/clang++"
	export CFLAGS="-integrated-as -Qunused-arguments -Ofast -g0 -w"
	
	sudo ldconfig
	zecho "SUCCESS" "LLVM/CLANG built and installed."
	showPrompt
fi

#############################################################################################
# Get the software source straight from the repository...
#
# Apple's implementation of libdispatch for Linux is better but does not compile on all
# platforms.
#
if [ "${USE_SWIFT_DISPATCH}" = true ]; then
	gitFiles "libdispatch from Swift Project"   "${GITHUB}/apple/swift-corelibs-libdispatch.git" "${PRJDIR}/libdispatch" "N" || zfail  "$?" "GIT request failed!"
else
	gitFiles "libdispatch from Nick Hutchinson" "${GITHUB}/nickhutchinson/libdispatch.git"       "${PRJDIR}/libdispatch" "N" || zfail  "$?" "GIT request failed!"
fi

_s="GNUstep Command Line Fixer"
_t="GNUstepCCommandLineFixer"

gitFiles "libobjc"                "${GITHUB}/gnustep/libobjc2.git" --branch "1.9" "${PRJDIR}/libobjc2"  "N"              || zfail  "$?" "GIT request failed!"
#gitFiles "GNUstep Make"          "${GITHUB}/gnustep/tools-make.git"              "${COREDIR}/make"     "Y" "2017-04-06" || zfail  "$?" "GIT request failed!"
gitFiles "GNUstep Make"           "${GITHUB}/gnustep/tools-make.git"              "${COREDIR}/make"     "N"              || zfail  "$?" "GIT request failed!"
gitFiles "GNUstep Base"           "${GITHUB}/gnustep/libs-base.git"               "${COREDIR}/base"     "N"              || zfail  "$?" "GIT request failed!"
gitFiles "GNUstep GUI"            "${GITHUB}/gnustep/libs-gui.git"                "${COREDIR}/gui"      "N"              || zfail  "$?" "GIT request failed!"
gitFiles "GNUstep AppKit Backend" "${GITHUB}/gnustep/libs-back.git"               "${COREDIR}/back"     "N"              || zfail  "$?" "GIT request failed!"
gitFiles "${s}"                   "${GITHUB}/GalenRhodes/${_t}.git"               "${PRJDIR}/${_t}"     "N"              || zfail  "$?" "${_s} GIT request failed!"
showPrompt

zecho -n "Building ${s}"
"${CC}" ${CFLAGS} -Wl,--strip-all -o "${PRJDIR}/gnustep-cclf" `find "${PRJDIR}/${_t}/${_t}" -name "*.c"` || zfail -l "$?" "${_s} build failed!"
sudo mv "${PRJDIR}/gnustep-cclf" "${INSTALL_PATH}/bin/" || zfail -l "${_s} install failed!"
echo " Done"
showPrompt

if [ "${BUILD_APPS}" = "Y" ] ; then
	zecho "INFO" "Getting apps..."
	gitFiles "GNUstep Project Center"     "${GITHUB}/gnustep/apps-projectcenter.git"     "${COREDIR}/apps-projectcenter"     "N" || zfail  "$?" "GIT request failed!"
	gitFiles "GNUstep GORM"               "${GITHUB}/gnustep/apps-gorm.git"              "${COREDIR}/apps-gorm"              "N" || zfail  "$?" "GIT request failed!"
	gitFiles "GNUstep GWorkspace"         "${GITHUB}/gnustep/apps-gworkspace.git"        "${COREDIR}/apps-gworkspace"        "N" || zfail  "$?" "GIT request failed!"
	gitFiles "GNUstep System Preferences" "${GITHUB}/gnustep/apps-systempreferences.git" "${COREDIR}/apps-systempreferences" "N" || zfail  "$?" "GIT request failed!"
	showPrompt
fi

#############################################################################################
# Build GNUstep Make for the first time...
#
if [ "${BUILD_INITIAL_GNUSTEP_MAKE}" = true ]; then
    buildGNUstepMake
    . "${GNUSTEP}"
fi

#############################################################################################
# Build libdispatch and libobjc2...
#
if [ "${BUILD_DISPATCH_FIRST}" = "Y" ]; then
	buildLibDispatch
	export LDFLAGS="-ldispatch ${LDFLAGS}"
	sudo ldconfig
	showPrompt
	
	buildLibObjc2
	export CFLAGS="${CFLAGS} ${GNUVER}"
	export CXXFLAGS="${CXXFLAGS} ${GNUVER}"
	export OBJCFLAGS="-fblocks ${GNUVER} ${OBJCFLAGS}"
	export LDFLAGS="-L${INSTALL_PATH}/lib ${LDFLAGS} -fblocks ${GNUVER}"
	sudo ldconfig
else
	buildLibObjc2
	export CFLAGS="${CFLAGS} ${GNUVER}"
	export CXXFLAGS="${CXXFLAGS} ${GNUVER}"
	export OBJCFLAGS="-fblocks ${GNUVER} ${OBJCFLAGS}"
	export LDFLAGS="-L${INSTALL_PATH}/lib ${LDFLAGS} -fblocks ${GNUVER}"
	sudo ldconfig
	showPrompt
	
	buildLibDispatch
	export LDFLAGS="-ldispatch ${LDFLAGS}"
	sudo ldconfig
fi
showPrompt

#############################################################################################
# Build GNUstep Make for the second time...
#
buildGNUstepMake "-startup-scripts" || zfail "$?" "Building GNUstep Make Failed!"
. "${GNUSTEP}"
sudo ldconfig

#############################################################################################
# Build Foundation...
#
cd "${COREDIR}/base"
showPrompt
if [ "${GNUSTEP_NO_MIXED_ABI}" = "Y" ]; then
	zecho "CONFIGURING" "GNUstep Base WITHOUT Mixed-ABI support..."
	. "${GNUSTEP}"
	./configure --disable-mixedabi || zfail "$?" "Configure Failed!"
else
	zecho "CONFIGURING" "GNUstep Base WITH Mixed-ABI support..."
	. "${GNUSTEP}"
	./configure || zfail "$?" "Configure Failed!"
fi
showPrompt
zecho "BUILDING" "GNUstep Base"
make "-j${PROCESSORS}" || zfail "$?" "Build Failed!"
sudo -E make install || zfail "$?" "Install Failed!"
sudo ldconfig
showPrompt

#############################################################################################
# Build AppKit...
#
cd "${COREDIR}/gui"
zecho "CONFIGURING" "GNUstep GUI"
./configure || zfail "$?" "Configure Failed!"
showPrompt
zecho "BUILDING" "GNUstep GUI"
make "-j${PROCESSORS}" || zfail "$?" "Build Failed!"
sudo -E make install || zfail "$?" "Install Failed!"
sudo ldconfig
showPrompt

#############################################################################################
# Build AppKit Backend...
#
buildGNUstepSub "back" "AppKit Backend"
buildGNUstepSub "gui"  "GUI"
buildGNUstepSub "back" "AppKit Backend"

#############################################################################################
# Make the UI look right...
#
defaults write NSGlobalDomain NSInterfaceStyleDefault NSWindows95InterfaceStyle
defaults write NSGlobalDomain GSSuppressAppIcon YES










exit 0

