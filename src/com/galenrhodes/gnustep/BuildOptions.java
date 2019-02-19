package com.galenrhodes.gnustep;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BuildOptions {
    private static final Pattern COMBO_REGEX = Pattern.compile("(\\w+)-(\\w+)-(\\w+)");
    private boolean installPrereq;
    private boolean installLibKQueue;
    private boolean createEntriesInLdSoConfigDir;
    private boolean useSwiftLibDispatch;
    private boolean buildLatestLLVM;
    private boolean buildLibDispatchFirst;
    private boolean oldABICompat;
    private String objcLibName;
    private boolean archX32;
    private boolean archARM32;
    private boolean archARM64;
    private String installPath;
    private boolean archX32_64;
    private boolean customLIbName;
    private boolean objcARC;
    private boolean buildMakeTwice;
    private boolean debugByDefault;
    private boolean noMixedABI;
    private boolean nativeObjcExceptions;
    private boolean nonFragileABI;
    private boolean buildGTest;
    private String filesystemLayout;
    private String libraryCombo;

    public String getLibraryComboPartA() {
        Matcher m = COMBO_REGEX.matcher(getLibraryCombo());
        return (m.matches() ? m.group(1) : "ng");
    }

    public String getFilesystemLayout() {
        return filesystemLayout;
    }

    public void setFilesystemLayout(String filesystemLayout) {
        this.filesystemLayout = filesystemLayout;
    }

    public String getLibraryCombo() {
        return libraryCombo;
    }

    public void setLibraryCombo(String libraryCombo) {
        this.libraryCombo = libraryCombo;
    }

    public boolean isInstallPrereq() {
        return installPrereq;
    }

    public void setInstallPrereq(final boolean installPrereq) {
        this.installPrereq = installPrereq;
    }

    public boolean isInstallLibKQueue() {
        return installLibKQueue;
    }

    public void setInstallLibKQueue(final boolean installLibKQueue) {
        this.installLibKQueue = installLibKQueue;
    }

    public boolean isCreateEntriesInLdSoConfigDir() {
        return createEntriesInLdSoConfigDir;
    }

    public void setCreateEntriesInLdSoConfigDir(final boolean createEntriesInLdSoConfigDir) {
        this.createEntriesInLdSoConfigDir = createEntriesInLdSoConfigDir;
    }

    public boolean isUseSwiftLibDispatch() {
        return useSwiftLibDispatch;
    }

    public void setUseSwiftLibDispatch(final boolean useSwiftLibDispatch) {
        this.useSwiftLibDispatch = useSwiftLibDispatch;
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

    public boolean isOldABICompat() {
        return oldABICompat;
    }

    public void setOldABICompat(final boolean oldABICompat) {
        this.oldABICompat = oldABICompat;
    }

    public String getObjcLibName() {
        return objcLibName;
    }

    public void setObjcLibName(final String objcLibName) {
        this.objcLibName = objcLibName;
    }

    public boolean isArchX32() {
        return archX32;
    }

    public void setArchX32(final boolean archX32) {
        this.archX32 = archX32;
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

    public String getInstallPath() {
        return installPath;
    }

    public void setInstallPath(final String installPath) {
        this.installPath = installPath;
    }

    public boolean isArchX32_64() {
        return archX32_64;
    }

    public void setArchX32_64(final boolean archX32_64) {
        this.archX32_64 = archX32_64;
    }

    public boolean isCustomLIbName() {
        return customLIbName;
    }

    public void setCustomLIbName(final boolean customLIbName) {
        this.customLIbName = customLIbName;
    }

    public boolean isObjcARC() {
        return objcARC;
    }

    public void setObjcARC(final boolean objcARC) {
        this.objcARC = objcARC;
    }

    public boolean isBuildMakeTwice() {
        return buildMakeTwice;
    }

    public void setBuildMakeTwice(final boolean buildMakeTwice) {
        this.buildMakeTwice = buildMakeTwice;
    }

    public boolean isDebugByDefault() {
        return debugByDefault;
    }

    public void setDebugByDefault(final boolean debugByDefault) {
        this.debugByDefault = debugByDefault;
    }

    public boolean isNoMixedABI() {
        return noMixedABI;
    }

    public void setNoMixedABI(final boolean noMixedABI) {
        this.noMixedABI = noMixedABI;
    }

    public boolean isNativeObjcExceptions() {
        return nativeObjcExceptions;
    }

    public void setNativeObjcExceptions(final boolean nativeObjcExceptions) {
        this.nativeObjcExceptions = nativeObjcExceptions;
    }

    public boolean isNonFragileABI() {
        return nonFragileABI;
    }

    public void setNonFragileABI(final boolean nonFragileABI) {
        this.nonFragileABI = nonFragileABI;
    }

    public boolean isBuildGTest() {
        return buildGTest;
    }

    public void setBuildGTest(final boolean buildGTest) {
        this.buildGTest = buildGTest;
    }
}
