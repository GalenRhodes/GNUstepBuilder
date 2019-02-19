package com.galenrhodes.gnustep;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GNUstepOptions {
    private JCheckBox buildLatestLLVMCLANGCheckBox;
    private JCheckBox buildGoogleTestCheckBox;
    private JCheckBox buildGNUstepMakeTwiceCheckBox;
    private JCheckBox installPrerequisitSoftwarePackagesCheckBox;
    private JCheckBox installLibkqueueCheckBox;
    private JCheckBox createEntriesInLdSoConfDCheckBox;
    private JCheckBox useLibdispatchFromSWIFTCheckBox;
    private JCheckBox noMixedABICheckBox;
    private JCheckBox useNonFragileABICheckBox;
    private JCheckBox buildLibdispatchFirstCheckBox;
    private JCheckBox oldABICompatabilityCheckBox;
    private JRadioButton a19RadioButton;
    private JRadioButton a20RadioButton;
    private JRadioButton a18RadioButton;
    private JTextField installPathTextField;
    private JButton findButton;
    private JCheckBox archX86_64CheckBox;
    private JCheckBox archARM32CheckBox;
    private JCheckBox archARM64CheckBox;
    private JTextField objc2CustomNameTextField;
    private JCheckBox objc2CustomNameCheckBox;
    private JCheckBox objectiveCARCCheckBox;
    private JCheckBox debugByDefaultCheckBox;
    private JCheckBox nativeObjectiveCExceptionsCheckBox;
    private JComboBox libComboADropDown;
    private JComboBox filesystemLayoutDropDown;
    private JComboBox libComboBDropDown;
    private JComboBox libComboCDown;
    private JPanel optionsPanel;
    private JButton helpButton;
    private JButton cancelButton;
    private JButton startButton;

    public GNUstepOptions() {
        objc2CustomNameCheckBox.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                objc2CustomNameTextField.setEnabled(objc2CustomNameCheckBox.isSelected());
            }
        });
        installPrerequisitSoftwarePackagesCheckBox.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                installLibkqueueCheckBox.setEnabled(installPrerequisitSoftwarePackagesCheckBox.isSelected());
            }
        });
        buildLatestLLVMCLANGCheckBox.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                archARM32CheckBox.setEnabled(buildLatestLLVMCLANGCheckBox.isSelected());
                archARM64CheckBox.setEnabled(buildLatestLLVMCLANGCheckBox.isSelected());
                archX86_64CheckBox.setEnabled(buildLatestLLVMCLANGCheckBox.isSelected());
                buildGoogleTestCheckBox.setEnabled(buildLatestLLVMCLANGCheckBox.isSelected());
            }
        });
        startButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        cancelButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        helpButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public void setData(BuildOptions data) {
        installPrerequisitSoftwarePackagesCheckBox.setSelected(data.isInstallPrereq());
        installLibkqueueCheckBox.setSelected(data.isInstallLibKQueue());
        createEntriesInLdSoConfDCheckBox.setSelected(data.isCreateEntriesInLdSoConfigDir());
        useLibdispatchFromSWIFTCheckBox.setSelected(data.isUseSwiftLibDispatch());
        buildLatestLLVMCLANGCheckBox.setSelected(data.isBuildLatestLLVM());
        buildLibdispatchFirstCheckBox.setSelected(data.isBuildLibDispatchFirst());
        installPathTextField.setText(data.getInstallPath());
        archX86_64CheckBox.setSelected(data.isArchX32_64());
        archARM32CheckBox.setSelected(data.isArchARM32());
        archARM64CheckBox.setSelected(data.isArchARM64());
        objc2CustomNameTextField.setText(data.getObjcLibName());
        objc2CustomNameCheckBox.setSelected(data.isCustomLIbName());
        objectiveCARCCheckBox.setSelected(data.isObjcARC());
        buildGNUstepMakeTwiceCheckBox.setSelected(data.isBuildMakeTwice());
        debugByDefaultCheckBox.setSelected(data.isDebugByDefault());
        noMixedABICheckBox.setSelected(data.isNoMixedABI());
        nativeObjectiveCExceptionsCheckBox.setSelected(data.isNativeObjcExceptions());
        useNonFragileABICheckBox.setSelected(data.isNonFragileABI());
        buildGoogleTestCheckBox.setSelected(data.isBuildGTest());
        oldABICompatabilityCheckBox.setSelected(data.isOldABICompat());
    }

    public void getData(BuildOptions data) {
        data.setInstallPrereq(installPrerequisitSoftwarePackagesCheckBox.isSelected());
        data.setInstallLibKQueue(installLibkqueueCheckBox.isSelected());
        data.setCreateEntriesInLdSoConfigDir(createEntriesInLdSoConfDCheckBox.isSelected());
        data.setUseSwiftLibDispatch(useLibdispatchFromSWIFTCheckBox.isSelected());
        data.setBuildLatestLLVM(buildLatestLLVMCLANGCheckBox.isSelected());
        data.setBuildLibDispatchFirst(buildLibdispatchFirstCheckBox.isSelected());
        data.setInstallPath(installPathTextField.getText());
        data.setArchX32_64(archX86_64CheckBox.isSelected());
        data.setArchARM32(archARM32CheckBox.isSelected());
        data.setArchARM64(archARM64CheckBox.isSelected());
        data.setObjcLibName(objc2CustomNameTextField.getText());
        data.setCustomLIbName(objc2CustomNameCheckBox.isSelected());
        data.setObjcARC(objectiveCARCCheckBox.isSelected());
        data.setBuildMakeTwice(buildGNUstepMakeTwiceCheckBox.isSelected());
        data.setDebugByDefault(debugByDefaultCheckBox.isSelected());
        data.setNoMixedABI(noMixedABICheckBox.isSelected());
        data.setNativeObjcExceptions(nativeObjectiveCExceptionsCheckBox.isSelected());
        data.setNonFragileABI(useNonFragileABICheckBox.isSelected());
        data.setBuildGTest(buildGoogleTestCheckBox.isSelected());
        data.setOldABICompat(oldABICompatabilityCheckBox.isSelected());
    }

    public boolean isModified(BuildOptions data) {
        if (installPrerequisitSoftwarePackagesCheckBox.isSelected() != data.isInstallPrereq()) return true;
        if (installLibkqueueCheckBox.isSelected() != data.isInstallLibKQueue()) return true;
        if (createEntriesInLdSoConfDCheckBox.isSelected() != data.isCreateEntriesInLdSoConfigDir()) return true;
        if (useLibdispatchFromSWIFTCheckBox.isSelected() != data.isUseSwiftLibDispatch()) return true;
        if (buildLatestLLVMCLANGCheckBox.isSelected() != data.isBuildLatestLLVM()) return true;
        if (buildLibdispatchFirstCheckBox.isSelected() != data.isBuildLibDispatchFirst()) return true;
        if (installPathTextField.getText() != null ? !installPathTextField.getText().equals(data.getInstallPath()) : data.getInstallPath() != null)
            return true;
        if (archX86_64CheckBox.isSelected() != data.isArchX32_64()) return true;
        if (archARM32CheckBox.isSelected() != data.isArchARM32()) return true;
        if (archARM64CheckBox.isSelected() != data.isArchARM64()) return true;
        if (objc2CustomNameTextField.getText() != null ? !objc2CustomNameTextField.getText().equals(data.getObjcLibName()) : data.getObjcLibName() != null)
            return true;
        if (objc2CustomNameCheckBox.isSelected() != data.isCustomLIbName()) return true;
        if (objectiveCARCCheckBox.isSelected() != data.isObjcARC()) return true;
        if (buildGNUstepMakeTwiceCheckBox.isSelected() != data.isBuildMakeTwice()) return true;
        if (debugByDefaultCheckBox.isSelected() != data.isDebugByDefault()) return true;
        if (noMixedABICheckBox.isSelected() != data.isNoMixedABI()) return true;
        if (nativeObjectiveCExceptionsCheckBox.isSelected() != data.isNativeObjcExceptions()) return true;
        if (useNonFragileABICheckBox.isSelected() != data.isNonFragileABI()) return true;
        if (buildGoogleTestCheckBox.isSelected() != data.isBuildGTest()) return true;
        return oldABICompatabilityCheckBox.isSelected() != data.isOldABICompat();
    }
}
