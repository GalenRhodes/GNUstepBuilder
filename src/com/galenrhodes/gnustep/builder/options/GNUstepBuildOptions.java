package com.galenrhodes.gnustep.builder.options;

import com.galenrhodes.gnustep.builder.common.Tools;
import com.galenrhodes.gnustep.builder.options.data.*;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "GNUstepOptions", namespace = "http://www.projectgalen.com/gnustepbuilder")
public class GNUstepBuildOptions extends com.galenrhodes.gnustep.builder.options.data.GNUstepOptions {

    public static final LibraryComboEnum      DEFAULT_LIB_COMBO          = LibraryComboEnum.NG_GNU_GNU;
    public static final FileSystemLayoutsEnum DEFAULT_FILE_SYSTEM_LAYOUT = FileSystemLayoutsEnum.FHS;
    public static final String                DEFAULT_WORKING_DIR        = "~/Downloads/gnustep/";
    public static final String                DEFAULT_INSTALL_DIR        = "~/gnustep/";

    public synchronized String getCustomLibNameValue() {
        CustomLibNameType cln = getCustomLibName();
        return ((cln == null) ? null : cln.getValue());
    }

    public synchronized void setCustomLibNameValue(String libName) {
        CustomLibNameType cln = getCustomLibName();
        if(cln == null) setCustomLibName(cln = new CustomLibNameType());
        cln.setValue(libName);
    }

    public synchronized String getFileSystemLayoutValue() {
        FileSystemLayoutsEnum l = getLayout();
        return ((l == null) ? DEFAULT_FILE_SYSTEM_LAYOUT.value() : l.value());
    }

    public synchronized void setFileSystemLayoutValue(String fsl) {
        try { setLayout(fsl == null ? DEFAULT_FILE_SYSTEM_LAYOUT : FileSystemLayoutsEnum.fromValue(fsl)); }
        catch(Exception e) { if(getLayout() == null) setLayout(DEFAULT_FILE_SYSTEM_LAYOUT); }
    }

    @Override
    public synchronized String getInstallDirectory() {
        String dir = super.getInstallDirectory();
        return Tools.fixFilePath(Tools.z(dir) ? DEFAULT_INSTALL_DIR : dir, true);
    }

    public synchronized String getLibraryComboValue() {
        LibraryComboEnum lc = getLibraryCombo();
        return ((lc == null) ? DEFAULT_LIB_COMBO.value() : lc.value());
    }

    public synchronized void setLibraryComboValue(String lc) {
        try { setLibraryCombo((lc == null) ? DEFAULT_LIB_COMBO : LibraryComboEnum.fromValue(lc)); }
        catch(Exception e) { if(getLibraryCombo() == null) setLibraryCombo(DEFAULT_LIB_COMBO); }
    }

    @Override
    public synchronized String getWorkingDirectory() {
        String dir = super.getWorkingDirectory();
        return Tools.fixFilePath((Tools.z(dir) ? DEFAULT_WORKING_DIR : dir), true);
    }

    public synchronized boolean hasCustomLibName() {
        CustomLibNameType cln = getCustomLibName();
        return ((cln != null) && cln.isSelected());
    }

    public synchronized boolean isArchARM32() {
        ClangTargetsType targets = getClangTargets();
        return ((targets != null) && targets.isArm32());
    }

    public synchronized void setArchARM32(boolean b) {
        ClangTargetsType targets = getClangTargets();

        if(targets == null) {
            setClangTargets(targets = new ClangTargetsType());
            targets.setArm32(b);
        }
        else if(b != targets.isArm32()) {
            targets.setArm32(b);
        }
    }

    public synchronized boolean isArchARM64() {
        ClangTargetsType targets = getClangTargets();
        return ((targets != null) && targets.isArm64());
    }

    public synchronized void setArchARM64(boolean b) {
        ClangTargetsType targets = getClangTargets();

        if(targets == null) {
            setClangTargets(targets = new ClangTargetsType());
            targets.setArm64(b);
        }
        else if(b != targets.isArm64()) {
            targets.setArm64(b);
        }
    }

    public synchronized boolean isArchX32_64() {
        ClangTargetsType targets = getClangTargets();
        return ((targets != null) && targets.isX8664());
    }

    public synchronized void setArchX32_64(boolean b) {
        ClangTargetsType targets = getClangTargets();

        if(targets == null) {
            setClangTargets(targets = new ClangTargetsType());
            targets.setX8664(b);
        }
        else if(b != targets.isX8664()) {
            targets.setX8664(b);
        }
    }

    @Override
    public synchronized boolean isBuildClang() {
        return Tools.ifNull(super.isBuildClang(), false);
    }

    @Override
    public synchronized boolean isBuildGoogleTest() {
        return Tools.ifNull(super.isBuildGoogleTest(), false);
    }

    @Override
    public synchronized boolean isBuildLibDispatchFirst() {
        return Tools.ifNull(super.isBuildLibDispatchFirst(), false);
    }

    @Override
    public synchronized boolean isBuildMakeTwice() {
        return Tools.ifNull(super.isBuildMakeTwice(), false);
    }

    @Override
    public synchronized boolean isCreateEntriesInLdSoConfD() {
        return Tools.ifNull(super.isCreateEntriesInLdSoConfD(), false);
    }

    @Override
    public synchronized boolean isDebugByDefault() {
        return Tools.ifNull(super.isDebugByDefault(), false);
    }

    @Override
    public synchronized boolean isInstallLibkqueue() {
        return Tools.ifNull(super.isInstallLibkqueue(), false);
    }

    @Override
    public synchronized boolean isInstallPrerequisiteSoftware() {
        return Tools.ifNull(super.isInstallPrerequisiteSoftware(), false);
    }

    @Override
    public synchronized boolean isNativeObjectiveCExceptions() {
        return Tools.ifNull(super.isNativeObjectiveCExceptions(), false);
    }

    @Override
    public synchronized boolean isNoMixedABI() {
        return Tools.ifNull(super.isNoMixedABI(), false);
    }

    @Override
    public synchronized boolean isNonFragileABI() {
        return Tools.ifNull(super.isNonFragileABI(), false);
    }

    @Override
    public synchronized boolean isObjectiveCArc() {
        return Tools.ifNull(super.isObjectiveCArc(), false);
    }

    @Override
    public synchronized boolean isOldABICompat() {
        return Tools.ifNull(super.isOldABICompat(), false);
    }

    public synchronized boolean isRtVer1_8()        { return (getRuntimeABI() == RuntimeABIEnum.GNU); }

    public synchronized void setRtVer1_8(boolean b) { if(b) setRuntimeABI(RuntimeABIEnum.GNU); }

    public synchronized boolean isRtVer1_9()        { return (getRuntimeABI() == RuntimeABIEnum.V1_9); }

    public synchronized void setRtVer1_9(boolean b) { if(b) setRuntimeABI(RuntimeABIEnum.V1_9); }

    public synchronized boolean isRtVer2_0()        { return (getRuntimeABI() == RuntimeABIEnum.V2_0); }

    public synchronized void setRtVer2_0(boolean b) { if(b) setRuntimeABI(RuntimeABIEnum.V2_0); }

    @Override
    public synchronized boolean isUseSwiftLibDispatch() {
        return Tools.ifNull(super.isUseSwiftLibDispatch(), false);
    }

    public synchronized void setHasCustomLibName(boolean b) {
        CustomLibNameType cln = getCustomLibName();
        if(cln == null) setCustomLibName(cln = new CustomLibNameType());
        cln.setSelected(b);
    }

}
