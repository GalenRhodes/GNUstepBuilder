package com.galenrhodes.gnustep;

import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.io.Serializable;
import java.util.Objects;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BuildOptions implements Serializable {

    public static String[] FILESYSTEM_LAYOUTS;
    public static String[] COMBO_VALID_PART_A;
    public static String[] COMBO_VALID_PART_B;
    public static String[] COMBO_VALID_PART_C;

    private static String  DEFAULT_WORKING_PATH;
    private static String  DEFAULT_INSTALL_PATH;
    private static String  DEFAULT_LIB_NAME;
    private static String  COMBO_BUILD_FORMAT;
    private static String  COMBO_BAD_PATTERN;
    private static Pattern COMBO_REGEX;

    private String filesystemLayout = FILESYSTEM_LAYOUTS[0];
    private String installPath      = DEFAULT_INSTALL_PATH;
    private String objcLibName      = DEFAULT_LIB_NAME;
    private String libraryComboA    = COMBO_VALID_PART_A[0];
    private String libraryComboB    = COMBO_VALID_PART_B[0];
    private String libraryComboC    = COMBO_VALID_PART_C[0];
    private String workingPath      = DEFAULT_WORKING_PATH;

    private boolean archARM32                    = false;
    private boolean archARM64                    = false;
    private boolean archX32_64                   = true;
    private boolean buildGTest                   = true;
    private boolean buildLatestLLVM              = true;
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
    private boolean rtVer1_8                     = false;
    private boolean rtVer1_9                     = false;
    private boolean rtVer2_0                     = true;

    static {
        Properties p = new Properties();

        try { p.loadFromXML(Main.class.getResourceAsStream("GNUstepBuilder.xml")); }
        catch(Exception e) { LogManager.getLogger(BuildOptions.class).error("Error loading properties", e); }

        String a           = "com.galenrhodes.gnustepbuilder.";
        String listSepPatt = p.getProperty(a + "ListSeparatorPattern", "\\s*\\|\\s*");
        String s           = p.getProperty(a + "BadComboTestPattern", "(\\w+)-(\\w+)-(\\w+)");

        FILESYSTEM_LAYOUTS = p.getProperty(a + "FilesystemLayouts", "fhs|fhs-system|gnustep|gnustep-with-network|debian|apple|mac|next|standalone").split(listSepPatt);
        COMBO_VALID_PART_A = p.getProperty(a + "ValidComboPartA", "ng|gnu|apple").split(listSepPatt);
        COMBO_VALID_PART_B = p.getProperty(a + "ValidComboPartB", "gnu|apple").split(listSepPatt);
        COMBO_VALID_PART_C = p.getProperty(a + "ValidComboPartC", "gnu|apple").split(listSepPatt);

        DEFAULT_INSTALL_PATH = p.getProperty(a + "DefaultInstallPath", "/opt/objc2");
        DEFAULT_LIB_NAME = p.getProperty(a + "DefaultLibName", "objc2");
        COMBO_BAD_PATTERN = s;
        COMBO_REGEX = Pattern.compile(s);
        COMBO_BUILD_FORMAT = p.getProperty(a + "ComboFormat", "%s-%s-%s");

        String wp = p.getProperty("com.galenrhodes.gnustepbuilder.DefaultWorkingPath", "~/Downloads/gnustep");

        if(wp.startsWith("~")) {
            String home = System.getProperty("user.home");
            wp = home + wp.substring(1);
        }

        DEFAULT_WORKING_PATH = wp;
    }

    public BuildOptions() {
        super();
    }

    public String getFilesystemLayout() {
        return filesystemLayout;
    }

    public String getLibraryComboPartA() { return libraryComboA; }

    public String getInstallPath() {
        return installPath;
    }

    public void setLibraryComboPartA(String a) {
        libraryComboA = (Tools.contains(a, COMBO_VALID_PART_A) ? a : COMBO_VALID_PART_A[0]);
    }

    public String getLibraryCombo() {
        return String.format(COMBO_BUILD_FORMAT, getLibraryComboPartA(), getLibraryComboPartB(), getLibraryComboPartC());
    }

    public void setLibraryCombo(String libraryCombo) {
        if((libraryCombo == null) || (libraryCombo.trim().length() == 0)) {
            setLibraryComboPartA(COMBO_VALID_PART_A[0]);
            setLibraryComboPartB(COMBO_VALID_PART_B[0]);
            setLibraryComboPartC(COMBO_VALID_PART_C[0]);
        }
        else {
            Matcher m0 = COMBO_REGEX.matcher(libraryCombo);

            if(m0.matches()) {
                setLibraryComboPartA(m0.group(1));
                setLibraryComboPartB(m0.group(2));
                setLibraryComboPartC(m0.group(3));
            }
            else {
                setLibraryComboPartA(COMBO_VALID_PART_A[0]);
                setLibraryComboPartB(COMBO_VALID_PART_B[0]);
                setLibraryComboPartC(COMBO_VALID_PART_C[0]);
            }
        }
    }

    public String getLibraryComboPartB() { return libraryComboB; }

    public void setLibraryComboPartB(String b) {
        libraryComboB = (Tools.contains(b, COMBO_VALID_PART_B) ? b : COMBO_VALID_PART_B[0]);
    }

    public String getLibraryComboPartC() { return libraryComboC; }

    public void setLibraryComboPartC(String c) {
        libraryComboC = (Tools.contains(c, COMBO_VALID_PART_C) ? c : COMBO_VALID_PART_C[0]);
    }

    public String getWorkingPath() { return workingPath; }

    public void setWorkingPath(String s) {
        workingPath = fixPathName(s, ".", true);
    }

    public String getObjcLibName() {
        return objcLibName;
    }

    public void setObjcLibName(final String objcLibName) {
        this.objcLibName = objcLibName;
    }

    public boolean isBuildGTest() {
        return buildGTest;
    }

    public boolean isInstallLibKQueue() {
        return installLibKQueue;
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

    public boolean isObjcARC() {
        return objcARC;
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

    public void setFilesystemLayout(String filesystemLayout) {
        this.filesystemLayout = (Tools.contains(filesystemLayout, FILESYSTEM_LAYOUTS) ? filesystemLayout : FILESYSTEM_LAYOUTS[0]);
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

    public void setInstallPath(final String installPath) {
        this.installPath = fixPathName(installPath, ".", true);
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

    public static final String fixPathName(String s, String def, boolean isDirectory) {
        String sep = Matcher.quoteReplacement(File.separator);
        String wp  = Objects.toString(s, def).trim();
        if(wp.length() == 0) wp = def.trim();
        if("\\".equals(sep)) wp = wp.replaceAll("/", sep);
        if("/".equals(sep)) wp = wp.replaceAll("\\\\", sep);
        if(isDirectory && !wp.endsWith(sep)) wp = wp + sep;
        return wp;
    }

}
