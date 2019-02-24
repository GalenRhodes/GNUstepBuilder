package com.galenrhodes.gnustep;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BuildOptions implements Serializable {

    public static final String[] FILESYSTEM_LAYOUTS      = {"fhs", "fhs-system", "gnustep", "gnustep-with-network", "debian", "apple", "mac", "next", "standalone"};
    public static final String   COMBO_VALID_PART_A      = "ng|gnu|apple";
    public static final String   COMBO_VALID_PART_B      = "gnu|apple";
    public static final String   COMBO_VALID_PART_C      = "gnu|apple";
    public static final String   COMBO_DELIMITER_PATTERN = "\\s*\\|\\s*";
    public static final int      NG_INDEX                = 0;

    private static final String DEFAULT_INSTALL_PATH = "/opt/objc";
    private static final String DEFAULT_LIB_NAME     = "objc2";

    private static final String  COMBO_BAD_PATTERN  = "(\\w+)-(\\w+)-(\\w+)";
    private static final Pattern COMBO_REGEX        = Pattern.compile(COMBO_BAD_PATTERN);
    private static final String  COMBO_BUILD_FORMAT = "%s-%s-%s";

    private String filesystemLayout = FILESYSTEM_LAYOUTS[0];
    private String installPath      = DEFAULT_INSTALL_PATH;
    private String objcLibName      = DEFAULT_LIB_NAME;

    private String libraryComboA = getLibraryComboDefault(COMBO_VALID_PART_A);
    private String libraryComboB = getLibraryComboDefault(COMBO_VALID_PART_B);
    private String libraryComboC = getLibraryComboDefault(COMBO_VALID_PART_C);

    private boolean archARM32  = false;
    private boolean archARM64  = false;
    private boolean archX32_64 = true;

    private boolean buildGTest                   = false;
    private boolean buildLatestLLVM              = false;
    private boolean buildLibDispatchFirst        = true;
    private boolean buildMakeTwice               = true;
    private boolean createEntriesInLdSoConfigDir = true;
    private boolean customLIbName                = true;
    private boolean debugByDefault               = true;
    private boolean installLibKQueue             = true;
    private boolean installPrereq                = true;
    private boolean nativeObjcExceptions         = true;
    private boolean noMixedABI                   = true;
    private boolean nonFragileABI                = true;
    private boolean objcARC                      = true;
    private boolean oldABICompat                 = false;
    private boolean useSwiftLibDispatch          = true;

    private boolean rtVer1_8 = false;
    private boolean rtVer1_9 = false;
    private boolean rtVer2_0 = true;

    public BuildOptions() {
        super();
    }

    public String getFilesystemLayout() {
        return filesystemLayout;
    }

    public String getInstallPath() {
        return installPath;
    }

    public void setInstallPath(final String installPath) {
        this.installPath = installPath;
    }

    public String getLibraryCombo() {
        return String.format(COMBO_BUILD_FORMAT, getLibraryComboPartA(), getLibraryComboPartB(), getLibraryComboPartC());
    }

    public void setLibraryCombo(String libraryCombo) {
        if((libraryCombo == null) || (libraryCombo.trim().length() == 0)) {
            setLibraryComboPartA(getLibraryComboDefault(COMBO_VALID_PART_A));
            setLibraryComboPartB(getLibraryComboDefault(COMBO_VALID_PART_B));
            setLibraryComboPartC(getLibraryComboDefault(COMBO_VALID_PART_C));
        }
        else {
            Matcher m0 = COMBO_REGEX.matcher(libraryCombo);

            if(m0.matches()) {
                setLibraryComboPartA(m0.group(1));
                setLibraryComboPartB(m0.group(2));
                setLibraryComboPartC(m0.group(3));
            }
            else {
                setLibraryComboPartA(getLibraryComboDefault(COMBO_VALID_PART_A));
                setLibraryComboPartB(getLibraryComboDefault(COMBO_VALID_PART_B));
                setLibraryComboPartC(getLibraryComboDefault(COMBO_VALID_PART_C));
            }
        }
    }

    public String getLibraryComboPartA() { return libraryComboA; }

    public void setLibraryComboPartA(String a) {
        libraryComboA = (isValidComboPart(a, COMBO_VALID_PART_A) ? a : getLibraryComboDefault(COMBO_VALID_PART_A));
    }

    public String getLibraryComboPartB() { return libraryComboB; }

    public void setLibraryComboPartB(String b) {
        libraryComboB = (isValidComboPart(b, COMBO_VALID_PART_B) ? b : getLibraryComboDefault(COMBO_VALID_PART_B));
    }

    public String getLibraryComboPartC() { return libraryComboC; }

    public void setLibraryComboPartC(String c) {
        libraryComboC = (isValidComboPart(c, COMBO_VALID_PART_C) ? c : getLibraryComboDefault(COMBO_VALID_PART_C));
    }

    public boolean isBuildGTest() {
        return buildGTest;
    }

    public String getObjcLibName() {
        return objcLibName;
    }

    public void setObjcLibName(final String objcLibName) {
        this.objcLibName = objcLibName;
    }

    public boolean isArchARM32() {
        return archARM32;
    }

    public void setArchARM32(final boolean archARM32) {
        this.archARM32 = archARM32;
    }

    public boolean isArchARM64() {
        return archARM64;
    }

    public void setArchARM64(final boolean archARM64) {
        this.archARM64 = archARM64;
    }

    public boolean isArchX32_64() {
        return archX32_64;
    }

    public void setArchX32_64(final boolean archX32_64) {
        this.archX32_64 = archX32_64;
    }

    public void setBuildGTest(final boolean buildGTest) {
        this.buildGTest = buildGTest;
    }

    public boolean isBuildLatestLLVM() {
        return buildLatestLLVM;
    }

    public void setBuildLatestLLVM(final boolean buildLatestLLVM) {
        this.buildLatestLLVM = buildLatestLLVM;
    }

    public boolean isBuildLibDispatchFirst() {
        return buildLibDispatchFirst;
    }

    public void setBuildLibDispatchFirst(final boolean buildLibDispatchFirst) {
        this.buildLibDispatchFirst = buildLibDispatchFirst;
    }

    public boolean isBuildMakeTwice() {
        return buildMakeTwice;
    }

    public void setBuildMakeTwice(final boolean buildMakeTwice) {
        this.buildMakeTwice = buildMakeTwice;
    }

    public boolean isCreateEntriesInLdSoConfigDir() {
        return createEntriesInLdSoConfigDir;
    }

    public void setCreateEntriesInLdSoConfigDir(final boolean createEntriesInLdSoConfigDir) {
        this.createEntriesInLdSoConfigDir = createEntriesInLdSoConfigDir;
    }

    public boolean isInstallLibKQueue() {
        return installLibKQueue;
    }

    public boolean isCustomLIbName() {
        return customLIbName;
    }

    public void setCustomLIbName(final boolean customLIbName) {
        this.customLIbName = customLIbName;
    }

    public boolean isDebugByDefault() {
        return debugByDefault;
    }

    public void setDebugByDefault(final boolean debugByDefault) {
        this.debugByDefault = debugByDefault;
    }

    public void setInstallLibKQueue(final boolean installLibKQueue) {
        this.installLibKQueue = installLibKQueue;
    }

    public boolean isInstallPrereq() {
        return installPrereq;
    }

    public void setInstallPrereq(final boolean installPrereq) {
        this.installPrereq = installPrereq;
    }

    public boolean isNativeObjcExceptions() {
        return nativeObjcExceptions;
    }

    public void setNativeObjcExceptions(final boolean nativeObjcExceptions) {
        this.nativeObjcExceptions = nativeObjcExceptions;
    }

    public boolean isObjcARC() {
        return objcARC;
    }

    public boolean isNoMixedABI() {
        return noMixedABI;
    }

    public void setNoMixedABI(final boolean noMixedABI) {
        this.noMixedABI = noMixedABI;
    }

    public boolean isNonFragileABI() {
        return nonFragileABI;
    }

    public void setNonFragileABI(final boolean nonFragileABI) {
        this.nonFragileABI = nonFragileABI;
    }

    public void setObjcARC(final boolean objcARC) {
        this.objcARC = objcARC;
    }

    public boolean isOldABICompat() {
        return oldABICompat;
    }

    public void setOldABICompat(final boolean oldABICompat) {
        this.oldABICompat = oldABICompat;
    }

    public boolean isRtVer1_8() {
        return rtVer1_8;
    }

    public void setRtVer1_8(boolean rtVer1_8) {
        this.rtVer1_8 = rtVer1_8;
        if(rtVer1_8) this.rtVer1_9 = this.rtVer2_0 = false;
    }

    public boolean isRtVer1_9() {
        return rtVer1_9;
    }

    public void setRtVer1_9(boolean rtVer1_9) {
        this.rtVer1_9 = rtVer1_9;
        if(rtVer1_9) this.rtVer1_8 = this.rtVer2_0 = false;
    }

    public boolean isRtVer2_0() {
        return rtVer2_0;
    }

    public void setRtVer2_0(boolean rtVer2_0) {
        this.rtVer2_0 = rtVer2_0;
        if(rtVer2_0) this.rtVer1_8 = this.rtVer1_9 = false;
    }

    public boolean isUseSwiftLibDispatch() {
        return useSwiftLibDispatch;
    }

    public void setUseSwiftLibDispatch(final boolean useSwiftLibDispatch) {
        this.useSwiftLibDispatch = useSwiftLibDispatch;
    }

    public void setFilesystemLayout(String filesystemLayout) {
        this.filesystemLayout = (Tools.contains(filesystemLayout, FILESYSTEM_LAYOUTS) ? filesystemLayout : FILESYSTEM_LAYOUTS[0]);
    }

    private static String getLibraryComboDefault(String v) {
        return v.split(COMBO_DELIMITER_PATTERN)[0];
    }

    private static boolean isValidComboPart(String a, String v) {
        return Tools.containsAll(v, COMBO_DELIMITER_PATTERN, a);
    }

}
