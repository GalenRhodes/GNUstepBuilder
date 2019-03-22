package com.galenrhodes.gnustep.options;

import com.galenrhodes.gnustep.common.Tools;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.ResourceBundle;

public class GNUstepOptions {

    private JButton           cancelButton;
    private JButton           installPathFindButton;
    private JButton           helpButton;
    private JButton           startButton;
    private JButton           workingPathFindButton;
    private JCheckBox         archARM32CheckBox;
    private JCheckBox         archARM64CheckBox;
    private JCheckBox         archX86_64CheckBox;
    private JCheckBox         buildGNUstepMakeTwiceCheckBox;
    private JCheckBox         buildGoogleTestCheckBox;
    private JCheckBox         buildLatestLLVMCLANGCheckBox;
    private JCheckBox         buildLibdispatchFirstCheckBox;
    private JCheckBox         createEntriesInLdSoConfDCheckBox;
    private JCheckBox         debugByDefaultCheckBox;
    private JCheckBox         installLibkqueueCheckBox;
    private JCheckBox         installPrerequisitSoftwarePackagesCheckBox;
    private JCheckBox         nativeObjectiveCExceptionsCheckBox;
    private JCheckBox         noMixedABICheckBox;
    private JCheckBox         objc2CustomNameCheckBox;
    private JCheckBox         objectiveCARCCheckBox;
    private JCheckBox         oldABICompatabilityCheckBox;
    private JCheckBox         useLibdispatchFromSWIFTCheckBox;
    private JCheckBox         useNonFragileABICheckBox;
    private JComboBox<String> filesystemLayoutDropDown;
    private JComboBox<String> libComboADropDown;
    private JComboBox<String> libComboBDropDown;
    private JComboBox<String> libComboCDropDown;
    private JPanel            optionsPanel;
    private JRadioButton      a18RadioButton;
    private JRadioButton      a19RadioButton;
    private JRadioButton      a20RadioButton;
    private JTextField        installPathTextField;
    private JTextField        objc2CustomNameTextField;
    private JTextField        workingPathTextField;

    private boolean archARM32CheckBoxOrgState;
    private boolean archARM64CheckBoxOrgState;
    private boolean archX86_64CheckBoxOrgState;
    private boolean buildGoogleTestCheckBoxOrgState;
    private boolean installLibkqueueCheckBoxOrgState;
    private boolean nativeObjectiveCExceptionsCheckBoxOrgState;
    private boolean noMixedABICheckBoxOrgState;
    private boolean objectiveCARCCheckBoxOrgState;
    private boolean oldABICompatabilityCheckBoxOrgState;
    private boolean useNonFragileABICheckBoxOrgState;
    private int     libComboADropDownOrgState;
    private String  objc2CustomNameTextFieldOrgState;

    private boolean isLoading = false;
    private boolean noEvents  = false;

    public GNUstepOptions() {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == cancelButton) {
                    System.exit(0);
                }
                else if(e.getSource() == helpButton) {
                    /* Nothing Yet. */
                }
                else if(e.getSource() == installPathFindButton) {
                    Tools.doFindDialog(installPathTextField, (System.getProperty("user.home") + "/gnustep").replace('/', File.separatorChar), optionsPanel);
                }
                else if(e.getSource() == workingPathFindButton) {
                    Tools.doFindDialog(workingPathTextField, (System.getProperty("user.home") + "/Downloads/gnustepwork").replace('/', File.separatorChar), optionsPanel);
                }
            }
        };

        ItemListener changeListener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(!(isLoading || noEvents)) {
                    if(e.getSource() == a18RadioButton) {
                        if(a18RadioButton.isSelected()) {
                            oldABICompatabilityCheckBoxOrgState = disableComp(oldABICompatabilityCheckBox, true);
                            noMixedABICheckBoxOrgState = disableComp(noMixedABICheckBox, false);
                            useNonFragileABICheckBoxOrgState = disableComp(useNonFragileABICheckBox, false);
                            objectiveCARCCheckBoxOrgState = disableComp(objectiveCARCCheckBox, false);
                            nativeObjectiveCExceptionsCheckBoxOrgState = disableComp(nativeObjectiveCExceptionsCheckBox, false);

                            String d   = BuildOptions.COMBO_VALID_PART_A[0];
                            int    idx = Tools.indexOfDropDownItem(libComboADropDown, d);

                            if(idx >= 0) {
                                libComboADropDownOrgState = libComboADropDown.getSelectedIndex();
                                libComboADropDown.removeItemAt(idx);
                                libComboADropDown.setSelectedIndex(0);
                            }
                        }
                        else {
                            String d   = BuildOptions.COMBO_VALID_PART_A[0];
                            int    idx = Tools.indexOfDropDownItem(libComboADropDown, d);

                            if(idx < 0) {
                                libComboADropDown.insertItemAt(d, 0);
                                libComboADropDown.setSelectedIndex(libComboADropDownOrgState);
                            }

                            enableComp(oldABICompatabilityCheckBox, oldABICompatabilityCheckBoxOrgState);
                            enableComp(noMixedABICheckBox, noMixedABICheckBoxOrgState);
                            enableComp(useNonFragileABICheckBox, useNonFragileABICheckBoxOrgState);
                            enableComp(objectiveCARCCheckBox, objectiveCARCCheckBoxOrgState);
                            enableComp(nativeObjectiveCExceptionsCheckBox, nativeObjectiveCExceptionsCheckBoxOrgState);
                        }
                    }
                    else if(e.getSource() == objc2CustomNameCheckBox) {
                        if(objc2CustomNameCheckBox.isSelected()) {
                            enableComp(objc2CustomNameTextField, objc2CustomNameTextFieldOrgState);
                        }
                        else {
                            objc2CustomNameTextFieldOrgState = disableComp(objc2CustomNameTextField, "");
                        }
                    }
                    else if(e.getSource() == installPrerequisitSoftwarePackagesCheckBox) {
                        if(installPrerequisitSoftwarePackagesCheckBox.isSelected()) {
                            enableComp(installLibkqueueCheckBox, installLibkqueueCheckBoxOrgState);
                        }
                        else {
                            installLibkqueueCheckBoxOrgState = disableComp(installLibkqueueCheckBox, false);
                        }
                    }
                    else if(e.getSource() == buildLatestLLVMCLANGCheckBox) {
                        if(buildLatestLLVMCLANGCheckBox.isSelected()) {
                            enableComp(archARM32CheckBox, archARM32CheckBoxOrgState);
                            enableComp(archARM64CheckBox, archARM64CheckBoxOrgState);
                            enableComp(buildGoogleTestCheckBox, buildGoogleTestCheckBoxOrgState);

                            if(archARM32CheckBoxOrgState || archARM64CheckBoxOrgState) {
                                enableComp(archX86_64CheckBox, archX86_64CheckBoxOrgState);
                            }
                            else {
                                disableComp(archX86_64CheckBox, true);
                            }
                        }
                        else {
                            barfoo();
                        }
                    }
                    else if(e.getSource() == archARM32CheckBox) {
                        foobar(archARM64CheckBox, archARM32CheckBox);
                    }
                    else if(e.getSource() == archARM64CheckBox) {
                        foobar(archARM32CheckBox, archARM64CheckBox);
                    }
                }
            }
        };

        cancelButton.addActionListener(actionListener);
        helpButton.addActionListener(actionListener);
        installPathFindButton.addActionListener(actionListener);
        startButton.addActionListener(actionListener);

        objc2CustomNameCheckBox.addItemListener(changeListener);
        installPrerequisitSoftwarePackagesCheckBox.addItemListener(changeListener);
        buildLatestLLVMCLANGCheckBox.addItemListener(changeListener);
        archARM32CheckBox.addItemListener(changeListener);
        archARM64CheckBox.addItemListener(changeListener);
        a18RadioButton.addItemListener(changeListener);

        enableEverything();

        filesystemLayoutDropDown.removeAllItems();
        libComboADropDown.removeAllItems();
        libComboBDropDown.removeAllItems();
        libComboCDropDown.removeAllItems();

        for(String s : BuildOptions.FILESYSTEM_LAYOUTS) {
            filesystemLayoutDropDown.addItem(s);
        }
        for(String s : BuildOptions.COMBO_VALID_PART_A) {
            libComboADropDown.addItem(s);
        }
        for(String s : BuildOptions.COMBO_VALID_PART_B) {
            libComboBDropDown.addItem(s);
        }
        for(String s : BuildOptions.COMBO_VALID_PART_C) {
            libComboCDropDown.addItem(s);
        }

        this.workingPathTextField.grabFocus();
    }

    public void _disableComp(JToggleButton b, boolean sel) {
        boolean il = noEvents;
        noEvents = true;
        b.setSelected(sel);
        b.setEnabled(false);
        noEvents = il;
    }

    public boolean disableComp(JToggleButton b, boolean sel) {
        boolean cur = b.isSelected();
        _disableComp(b, sel);
        return cur;
    }

    public String disableComp(JTextComponent tf, String def) {
        boolean il = noEvents;
        noEvents = true;
        String ov = tf.getText();
        tf.setText(def);
        tf.setEnabled(false);
        noEvents = il;
        return ov;
    }

    public void enableComp(JToggleButton b, boolean sel) {
        boolean il = noEvents;
        noEvents = true;
        b.setEnabled(true);
        b.setSelected(sel);
        noEvents = il;
    }

    public void enableComp(JTextComponent tf, String val) {
        boolean il = noEvents;
        noEvents = true;
        tf.setEnabled(true);
        tf.setText(val);
        noEvents = il;
    }

    public void getData(BuildOptions data) {
        data.setInstallPrereq(installPrerequisitSoftwarePackagesCheckBox.isSelected());
        data.setInstallLibKQueue(installLibkqueueCheckBox.isSelected());
        data.setCreateEntriesInLdSoConfigDir(createEntriesInLdSoConfDCheckBox.isSelected());
        data.setUseSwiftLibDispatch(useLibdispatchFromSWIFTCheckBox.isSelected());
        data.setBuildLatestLLVM(buildLatestLLVMCLANGCheckBox.isSelected());
        data.setBuildLibDispatchFirst(buildLibdispatchFirstCheckBox.isSelected());
        data.setInstallPath(installPathTextField.getText());
        data.setWorkingPath(workingPathTextField.getText());
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

        if(filesystemLayoutDropDown.getSelectedItem() != null) data.setFilesystemLayout(filesystemLayoutDropDown.getSelectedItem().toString());
        if(libComboADropDown.getSelectedItem() != null) data.setLibraryComboPartA(libComboADropDown.getSelectedItem().toString());
        if(libComboBDropDown.getSelectedItem() != null) data.setLibraryComboPartA(libComboBDropDown.getSelectedItem().toString());
        if(libComboCDropDown.getSelectedItem() != null) data.setLibraryComboPartA(libComboCDropDown.getSelectedItem().toString());

        data.setRtVer1_8(a18RadioButton.isSelected());
        data.setRtVer1_9(a19RadioButton.isSelected());
        data.setRtVer2_0(a20RadioButton.isSelected());
    }

    public JPanel getGNUstepOptionsPanel() { return optionsPanel; }

    public JButton getGNUstepStartButton() { return startButton; }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     *
     */
    public JComponent $$$getRootComponent$$$() { return optionsPanel; }

    protected void barfoo() {
        archX86_64CheckBoxOrgState = disableComp(archX86_64CheckBox, false);
        archARM32CheckBoxOrgState = disableComp(archARM32CheckBox, false);
        archARM64CheckBoxOrgState = disableComp(archARM64CheckBox, false);
        buildGoogleTestCheckBoxOrgState = disableComp(buildGoogleTestCheckBox, false);
    }

    protected void foobar(JCheckBox cb1, JCheckBox cb2) {
        if(!cb1.isSelected()) {
            if(cb2.isSelected()) enableComp(archX86_64CheckBox, archX86_64CheckBoxOrgState);
            else archX86_64CheckBoxOrgState = disableComp(archX86_64CheckBox, true);
        }
    }

    public boolean isModified(BuildOptions data) {
        if(installPrerequisitSoftwarePackagesCheckBox.isSelected() != data.isInstallPrereq()) return true;
        if(installLibkqueueCheckBox.isSelected() != data.isInstallLibKQueue()) return true;
        if(createEntriesInLdSoConfDCheckBox.isSelected() != data.isCreateEntriesInLdSoConfigDir()) return true;
        if(useLibdispatchFromSWIFTCheckBox.isSelected() != data.useSwiftLibDispatch()) return true;
        if(buildLatestLLVMCLANGCheckBox.isSelected() != data.isBuildLatestLLVM()) return true;
        if(buildLibdispatchFirstCheckBox.isSelected() != data.isBuildLibDispatchFirst()) return true;

        if(!Objects.equals(installPathTextField.getText(), data.getInstallPath())) return true;
        if(!Objects.equals(objc2CustomNameTextField.getText(), data.getObjcLibName())) return true;
        if(!Objects.equals(workingPathTextField.getText(), data.getWorkingPath())) return true;

        if(archX86_64CheckBox.isSelected() != data.isArchX32_64()) return true;
        if(archARM32CheckBox.isSelected() != data.isArchARM32()) return true;
        if(archARM64CheckBox.isSelected() != data.isArchARM64()) return true;
        if(objc2CustomNameCheckBox.isSelected() != data.isCustomLIbName()) return true;
        if(objectiveCARCCheckBox.isSelected() != data.isObjcARC()) return true;
        if(buildGNUstepMakeTwiceCheckBox.isSelected() != data.isBuildMakeTwice()) return true;
        if(debugByDefaultCheckBox.isSelected() != data.isDebugByDefault()) return true;
        if(noMixedABICheckBox.isSelected() != data.isNoMixedABI()) return true;
        if(nativeObjectiveCExceptionsCheckBox.isSelected() != data.isNativeObjcExceptions()) return true;
        if(useNonFragileABICheckBox.isSelected() != data.isNonFragileABI()) return true;
        if(buildGoogleTestCheckBox.isSelected() != data.isBuildGTest()) return true;

        if(!Objects.equals(libComboADropDown.getSelectedItem(), data.getLibraryComboPartA())) return true;
        if(!Objects.equals(libComboBDropDown.getSelectedItem(), data.getLibraryComboPartB())) return true;
        if(!Objects.equals(libComboCDropDown.getSelectedItem(), data.getLibraryComboPartC())) return true;
        if(!Objects.equals(filesystemLayoutDropDown.getSelectedItem(), data.getFilesystemLayout())) return true;

        if(a18RadioButton.isSelected() != data.isRtVer1_8()) return true;
        if(a19RadioButton.isSelected() != data.isRtVer1_9()) return true;
        if(a20RadioButton.isSelected() != data.isRtVer2_0()) return true;

        return oldABICompatabilityCheckBox.isSelected() != data.isOldABICompat();
    }

    public void setData(BuildOptions data) {
        isLoading = true;
        enableEverything();

        installPrerequisitSoftwarePackagesCheckBox.setSelected(data.isInstallPrereq());
        installLibkqueueCheckBox.setSelected(data.isInstallLibKQueue());
        createEntriesInLdSoConfDCheckBox.setSelected(data.isCreateEntriesInLdSoConfigDir());
        useLibdispatchFromSWIFTCheckBox.setSelected(data.useSwiftLibDispatch());
        buildLatestLLVMCLANGCheckBox.setSelected(data.isBuildLatestLLVM());
        buildLibdispatchFirstCheckBox.setSelected(data.isBuildLibDispatchFirst());
        installPathTextField.setText(data.getInstallPath());
        workingPathTextField.setText(Objects.toString(data.getWorkingPath(), "./"));
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

        filesystemLayoutDropDown.setSelectedItem(data.getFilesystemLayout());
        libComboADropDown.setSelectedItem(data.getLibraryComboPartA());
        libComboBDropDown.setSelectedItem(data.getLibraryComboPartB());
        libComboCDropDown.setSelectedItem(data.getLibraryComboPartC());

        a18RadioButton.setSelected(data.isRtVer1_8());
        a19RadioButton.setSelected(data.isRtVer1_9());
        a20RadioButton.setSelected(data.isRtVer2_0());

        noMixedABICheckBoxOrgState = noMixedABICheckBox.isSelected();
        useNonFragileABICheckBoxOrgState = useNonFragileABICheckBox.isSelected();
        objectiveCARCCheckBoxOrgState = objectiveCARCCheckBox.isSelected();
        nativeObjectiveCExceptionsCheckBoxOrgState = nativeObjectiveCExceptionsCheckBox.isSelected();
        oldABICompatabilityCheckBoxOrgState = oldABICompatabilityCheckBox.isSelected();
        libComboADropDownOrgState = libComboADropDown.getSelectedIndex();

        objc2CustomNameTextFieldOrgState = objc2CustomNameTextField.getText();

        if(!installPrerequisitSoftwarePackagesCheckBox.isSelected()) {
            installLibkqueueCheckBoxOrgState = disableComp(installLibkqueueCheckBox, false);
        }

        if(buildLatestLLVMCLANGCheckBox.isSelected()) {
            if(!(archARM64CheckBoxOrgState || archARM32CheckBoxOrgState)) {
                _disableComp(archX86_64CheckBox, true);
            }
        }
        else {
            barfoo();
        }

        isLoading = false;
    }

    /**
     *
     */
    private void $$$loadButtonText$$$(AbstractButton component, String text) {
        StringBuffer result        = new StringBuffer();
        boolean      haveMnemonic  = false;
        char         mnemonic      = '\0';
        int          mnemonicIndex = -1;
        for(int i = 0; i < text.length(); i++) {
            if(text.charAt(i) == '&') {
                i++;
                if(i == text.length()) break;
                if(!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if(haveMnemonic) {
            component.setMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     *
     */
    private void $$$loadLabelText$$$(JLabel component, String text) {
        StringBuffer result        = new StringBuffer();
        boolean      haveMnemonic  = false;
        char         mnemonic      = '\0';
        int          mnemonicIndex = -1;
        for(int i = 0; i < text.length(); i++) {
            if(text.charAt(i) == '&') {
                i++;
                if(i == text.length()) break;
                if(!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if(haveMnemonic) {
            component.setDisplayedMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer >>> IMPORTANT!! <<< DO NOT edit this method OR call it in your code!
     */
    private void $$$setupUI$$$() {
        optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayoutManager(26, 5, new Insets(5, 5, 5, 5), -1, -1));
        optionsPanel.setName("optionsPanel");
        final Spacer spacer1 = new Spacer();
        optionsPanel.add(spacer1, new GridConstraints(25, 0,
                                                      1,
                                                      5,
                                                      GridConstraints.ANCHOR_CENTER,
                                                      GridConstraints.FILL_VERTICAL,
                                                      1,
                                                      GridConstraints.SIZEPOLICY_WANT_GROW,
                                                      null,
                                                      null,
                                                      null,
                                                      0,
                                                      false));
        installPrerequisitSoftwarePackagesCheckBox = new JCheckBox();
        installPrerequisitSoftwarePackagesCheckBox.setSelected(false);
        this.$$$loadButtonText$$$(installPrerequisitSoftwarePackagesCheckBox,
                                  ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("install.checkbox.prereq_software"));
        installPrerequisitSoftwarePackagesCheckBox.setToolTipText(ResourceBundle.getBundle("com/galenrhodes/gnustep/localization")
                                                                                .getString("install.checkbox.prereq_software.tooltip"));
        optionsPanel.add(installPrerequisitSoftwarePackagesCheckBox, new GridConstraints(2, 0,
                                                                                         1,
                                                                                         4,
                                                                                         GridConstraints.ANCHOR_WEST,
                                                                                         GridConstraints.FILL_NONE,
                                                                                         GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                                                                         GridConstraints.SIZEPOLICY_FIXED,
                                                                                         null,
                                                                                         null,
                                                                                         null,
                                                                                         0,
                                                                                         false));
        installLibkqueueCheckBox = new JCheckBox();
        installLibkqueueCheckBox.setSelected(false);
        this.$$$loadButtonText$$$(installLibkqueueCheckBox, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("install.checkbox.libkqueue"));
        installLibkqueueCheckBox.setToolTipText(ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("install.checkbox.libkqueue.tooltip"));
        optionsPanel.add(installLibkqueueCheckBox, new GridConstraints(3, 0,
                                                                       1,
                                                                       4,
                                                                       GridConstraints.ANCHOR_WEST,
                                                                       GridConstraints.FILL_NONE,
                                                                       GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                                                       GridConstraints.SIZEPOLICY_FIXED,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       0,
                                                                       false));
        createEntriesInLdSoConfDCheckBox = new JCheckBox();
        createEntriesInLdSoConfDCheckBox.setSelected(false);
        this.$$$loadButtonText$$$(createEntriesInLdSoConfDCheckBox,
                                  ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("install.checkbox.etc_ld_so_conf_d"));
        createEntriesInLdSoConfDCheckBox.setToolTipText(ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("install.checkbox.etc_ld_so_conf_d.tooltip"));
        optionsPanel.add(createEntriesInLdSoConfDCheckBox, new GridConstraints(4, 0,
                                                                               1,
                                                                               4,
                                                                               GridConstraints.ANCHOR_WEST,
                                                                               GridConstraints.FILL_NONE,
                                                                               GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                                                               GridConstraints.SIZEPOLICY_FIXED,
                                                                               null,
                                                                               null,
                                                                               null,
                                                                               0,
                                                                               false));
        useLibdispatchFromSWIFTCheckBox = new JCheckBox();
        useLibdispatchFromSWIFTCheckBox.setSelected(false);
        this.$$$loadButtonText$$$(useLibdispatchFromSWIFTCheckBox,
                                  ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.dispatch.checkbox.dispatch_from_swift"));
        useLibdispatchFromSWIFTCheckBox.setToolTipText(ResourceBundle.getBundle("com/galenrhodes/gnustep/localization")
                                                                     .getString("build.dispatch.checkbox.dispatch_from_swift.tooltip"));
        optionsPanel.add(useLibdispatchFromSWIFTCheckBox, new GridConstraints(12, 0,
                                                                              1,
                                                                              4,
                                                                              GridConstraints.ANCHOR_WEST,
                                                                              GridConstraints.FILL_NONE,
                                                                              GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                                                              GridConstraints.SIZEPOLICY_FIXED,
                                                                              null,
                                                                              null,
                                                                              null,
                                                                              0,
                                                                              false));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("label.section.gnustep_options"));
        optionsPanel.add(label1, new GridConstraints(18, 0,
                                                     1,
                                                     4,
                                                     GridConstraints.ANCHOR_WEST,
                                                     GridConstraints.FILL_NONE,
                                                     GridConstraints.SIZEPOLICY_FIXED,
                                                     GridConstraints.SIZEPOLICY_FIXED,
                                                     null,
                                                     null,
                                                     null,
                                                     0,
                                                     false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("label.section.llvm_clang_options"));
        optionsPanel.add(label2, new GridConstraints(6, 0,
                                                     1,
                                                     4,
                                                     GridConstraints.ANCHOR_WEST,
                                                     GridConstraints.FILL_NONE,
                                                     GridConstraints.SIZEPOLICY_FIXED,
                                                     GridConstraints.SIZEPOLICY_FIXED,
                                                     null,
                                                     null,
                                                     null,
                                                     0,
                                                     false));
        buildLatestLLVMCLANGCheckBox = new JCheckBox();
        buildLatestLLVMCLANGCheckBox.setSelected(false);
        this.$$$loadButtonText$$$(buildLatestLLVMCLANGCheckBox, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.clang.checkbox.latest"));
        buildLatestLLVMCLANGCheckBox.setToolTipText(ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.clang.checkbox.latest.tooltip"));
        optionsPanel.add(buildLatestLLVMCLANGCheckBox, new GridConstraints(7, 0,
                                                                           1,
                                                                           4,
                                                                           GridConstraints.ANCHOR_WEST,
                                                                           GridConstraints.FILL_NONE,
                                                                           GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                                                           GridConstraints.SIZEPOLICY_FIXED,
                                                                           null,
                                                                           null,
                                                                           null,
                                                                           0,
                                                                           false));
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("label.section.dispatch_and_objc2_runtime"));
        optionsPanel.add(label3, new GridConstraints(11, 0,
                                                     1,
                                                     4,
                                                     GridConstraints.ANCHOR_WEST,
                                                     GridConstraints.FILL_NONE,
                                                     GridConstraints.SIZEPOLICY_FIXED,
                                                     GridConstraints.SIZEPOLICY_FIXED,
                                                     null,
                                                     null,
                                                     null,
                                                     0,
                                                     false));
        buildLibdispatchFirstCheckBox = new JCheckBox();
        buildLibdispatchFirstCheckBox.setSelected(false);
        this.$$$loadButtonText$$$(buildLibdispatchFirstCheckBox,
                                  ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.dispatch.checkbox.dispatch_first"));
        buildLibdispatchFirstCheckBox.setToolTipText(ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.dispatch.checkbox.dispatch_first.tooltip"));
        optionsPanel.add(buildLibdispatchFirstCheckBox, new GridConstraints(13, 0,
                                                                            1,
                                                                            4,
                                                                            GridConstraints.ANCHOR_WEST,
                                                                            GridConstraints.FILL_NONE,
                                                                            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                                                            GridConstraints.SIZEPOLICY_FIXED,
                                                                            null,
                                                                            null,
                                                                            null,
                                                                            0,
                                                                            false));
        final JLabel label4 = new JLabel();
        this.$$$loadLabelText$$$(label4, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.objc2.label.version"));
        optionsPanel.add(label4, new GridConstraints(14, 0,
                                                     1,
                                                     2,
                                                     GridConstraints.ANCHOR_EAST,
                                                     GridConstraints.FILL_NONE,
                                                     GridConstraints.SIZEPOLICY_FIXED,
                                                     GridConstraints.SIZEPOLICY_FIXED,
                                                     null,
                                                     null,
                                                     null,
                                                     0,
                                                     false));
        final JLabel label5 = new JLabel();
        this.$$$loadLabelText$$$(label5, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("install.directory.label"));
        label5.setToolTipText(ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("install.directory.tooltip"));
        optionsPanel.add(label5, new GridConstraints(1, 0,
                                                     1,
                                                     1,
                                                     GridConstraints.ANCHOR_WEST,
                                                     GridConstraints.FILL_NONE,
                                                     GridConstraints.SIZEPOLICY_FIXED,
                                                     GridConstraints.SIZEPOLICY_FIXED,
                                                     null,
                                                     null,
                                                     null,
                                                     0,
                                                     false));
        installPathTextField = new JTextField();
        installPathTextField.setText("");
        installPathTextField.setToolTipText(ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("install.directory.tooltip"));
        optionsPanel.add(installPathTextField, new GridConstraints(1, 1,
                                                                   1,
                                                                   3,
                                                                   GridConstraints.ANCHOR_WEST,
                                                                   GridConstraints.FILL_HORIZONTAL,
                                                                   GridConstraints.SIZEPOLICY_WANT_GROW,
                                                                   GridConstraints.SIZEPOLICY_FIXED,
                                                                   null,
                                                                   new Dimension(100, -1),
                                                                   null,
                                                                   0,
                                                                   false));
        final JLabel label6 = new JLabel();
        this.$$$loadLabelText$$$(label6, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.clang.label.targets"));
        optionsPanel.add(label6, new GridConstraints(8, 0,
                                                     1,
                                                     1,
                                                     GridConstraints.ANCHOR_EAST,
                                                     GridConstraints.FILL_NONE,
                                                     GridConstraints.SIZEPOLICY_FIXED,
                                                     GridConstraints.SIZEPOLICY_FIXED,
                                                     null,
                                                     null,
                                                     null,
                                                     0,
                                                     false));
        archX86_64CheckBox = new JCheckBox();
        archX86_64CheckBox.setSelected(false);
        this.$$$loadButtonText$$$(archX86_64CheckBox, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.clang.checkbox.arch_x86_64"));
        archX86_64CheckBox.setToolTipText(ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.clang.checkbox.arch_x64_64.tooltip"));
        optionsPanel.add(archX86_64CheckBox, new GridConstraints(8, 1,
                                                                 1,
                                                                 1,
                                                                 GridConstraints.ANCHOR_WEST,
                                                                 GridConstraints.FILL_NONE,
                                                                 GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                                                 GridConstraints.SIZEPOLICY_FIXED,
                                                                 null,
                                                                 null,
                                                                 null,
                                                                 0,
                                                                 false));
        archARM32CheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(archARM32CheckBox, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.clang.checkbox.arch_arm32"));
        archARM32CheckBox.setToolTipText(ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.clang.checkbox.arch_arm32.tooltip"));
        optionsPanel.add(archARM32CheckBox, new GridConstraints(8, 2,
                                                                1,
                                                                1,
                                                                GridConstraints.ANCHOR_WEST,
                                                                GridConstraints.FILL_NONE,
                                                                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                                                GridConstraints.SIZEPOLICY_FIXED,
                                                                null,
                                                                null,
                                                                null,
                                                                0,
                                                                false));
        archARM64CheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(archARM64CheckBox, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.clang.checkbox.arch_arm64"));
        archARM64CheckBox.setToolTipText(ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.clang.checkbox.arch_arm64.tooltip"));
        optionsPanel.add(archARM64CheckBox, new GridConstraints(8, 3,
                                                                1,
                                                                1,
                                                                GridConstraints.ANCHOR_WEST,
                                                                GridConstraints.FILL_NONE,
                                                                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                                                GridConstraints.SIZEPOLICY_FIXED,
                                                                null,
                                                                null,
                                                                null,
                                                                0,
                                                                false));
        final JToolBar.Separator toolBar$Separator1 = new JToolBar.Separator();
        optionsPanel.add(toolBar$Separator1, new GridConstraints(5, 0,
                                                                 1,
                                                                 5,
                                                                 GridConstraints.ANCHOR_CENTER,
                                                                 GridConstraints.FILL_HORIZONTAL,
                                                                 GridConstraints.SIZEPOLICY_FIXED,
                                                                 GridConstraints.SIZEPOLICY_FIXED,
                                                                 null,
                                                                 null,
                                                                 null,
                                                                 0,
                                                                 false));
        final JToolBar.Separator toolBar$Separator2 = new JToolBar.Separator();
        optionsPanel.add(toolBar$Separator2, new GridConstraints(17, 0,
                                                                 1,
                                                                 5,
                                                                 GridConstraints.ANCHOR_CENTER,
                                                                 GridConstraints.FILL_HORIZONTAL,
                                                                 GridConstraints.SIZEPOLICY_FIXED,
                                                                 GridConstraints.SIZEPOLICY_FIXED,
                                                                 null,
                                                                 null,
                                                                 null,
                                                                 0,
                                                                 false));
        final JToolBar.Separator toolBar$Separator3 = new JToolBar.Separator();
        optionsPanel.add(toolBar$Separator3, new GridConstraints(10, 0,
                                                                 1,
                                                                 5,
                                                                 GridConstraints.ANCHOR_CENTER,
                                                                 GridConstraints.FILL_HORIZONTAL,
                                                                 GridConstraints.SIZEPOLICY_FIXED,
                                                                 GridConstraints.SIZEPOLICY_FIXED,
                                                                 null,
                                                                 null,
                                                                 null,
                                                                 0,
                                                                 false));
        installPathFindButton = new JButton();
        installPathFindButton.setDefaultCapable(false);
        this.$$$loadButtonText$$$(installPathFindButton, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("install.directory.button.find"));
        installPathFindButton.setToolTipText(ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("install.directory.button.find.tooltip"));
        optionsPanel.add(installPathFindButton, new GridConstraints(1, 4,
                                             1,
                                             1,
                                             GridConstraints.ANCHOR_CENTER,
                                             GridConstraints.FILL_HORIZONTAL,
                                             GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                             GridConstraints.SIZEPOLICY_FIXED,
                                             null,
                                             null,
                                             null,
                                             0,
                                             false));
        objc2CustomNameTextField = new JTextField();
        objc2CustomNameTextField.setText("");
        optionsPanel.add(objc2CustomNameTextField, new GridConstraints(15, 1,
                                                                       1,
                                                                       2,
                                                                       GridConstraints.ANCHOR_WEST,
                                                                       GridConstraints.FILL_HORIZONTAL,
                                                                       GridConstraints.SIZEPOLICY_WANT_GROW,
                                                                       GridConstraints.SIZEPOLICY_FIXED,
                                                                       null,
                                                                       new Dimension(150, -1),
                                                                       null,
                                                                       0,
                                                                       false));
        objc2CustomNameCheckBox = new JCheckBox();
        objc2CustomNameCheckBox.setSelected(false);
        this.$$$loadButtonText$$$(objc2CustomNameCheckBox, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.objc2.label.custom_lib_name"));
        optionsPanel.add(objc2CustomNameCheckBox, new GridConstraints(15, 0,
                                                                      1,
                                                                      1,
                                                                      GridConstraints.ANCHOR_WEST,
                                                                      GridConstraints.FILL_NONE,
                                                                      GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                                                      GridConstraints.SIZEPOLICY_FIXED,
                                                                      null,
                                                                      null,
                                                                      null,
                                                                      0,
                                                                      false));
        final Spacer spacer2 = new Spacer();
        optionsPanel.add(spacer2, new GridConstraints(18, 4,
                                                      1,
                                                      1,
                                                      GridConstraints.ANCHOR_CENTER,
                                                      GridConstraints.FILL_HORIZONTAL,
                                                      GridConstraints.SIZEPOLICY_WANT_GROW,
                                                      1,
                                                      null,
                                                      null,
                                                      null,
                                                      0,
                                                      false));
        objectiveCARCCheckBox = new JCheckBox();
        objectiveCARCCheckBox.setSelected(false);
        this.$$$loadButtonText$$$(objectiveCARCCheckBox, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.gnustep.checkbox.use_arc"));
        objectiveCARCCheckBox.setToolTipText(ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.gnustep.checkbox.use_arc.tooltip"));
        optionsPanel.add(objectiveCARCCheckBox, new GridConstraints(19, 2,
                                                                    1,
                                                                    2,
                                                                    GridConstraints.ANCHOR_WEST,
                                                                    GridConstraints.FILL_NONE,
                                                                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                                                    GridConstraints.SIZEPOLICY_FIXED,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    0,
                                                                    false));
        buildGNUstepMakeTwiceCheckBox = new JCheckBox();
        buildGNUstepMakeTwiceCheckBox.setSelected(false);
        this.$$$loadButtonText$$$(buildGNUstepMakeTwiceCheckBox,
                                  ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.gnustep.checkbox.build_make_twice"));
        buildGNUstepMakeTwiceCheckBox.setToolTipText(ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.gnustep.checkbox.build_make_twice.tooltip"));
        optionsPanel.add(buildGNUstepMakeTwiceCheckBox, new GridConstraints(19, 0,
                                                                            1,
                                                                            2,
                                                                            GridConstraints.ANCHOR_WEST,
                                                                            GridConstraints.FILL_NONE,
                                                                            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                                                            GridConstraints.SIZEPOLICY_FIXED,
                                                                            null,
                                                                            null,
                                                                            null,
                                                                            0,
                                                                            false));
        debugByDefaultCheckBox = new JCheckBox();
        debugByDefaultCheckBox.setSelected(false);
        this.$$$loadButtonText$$$(debugByDefaultCheckBox,
                                  ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.gnustep.checkbox.enable_debug_by_default"));
        debugByDefaultCheckBox.setToolTipText(ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.gnustep.checkbox.enable_debug_by_default.tooltip"));
        optionsPanel.add(debugByDefaultCheckBox, new GridConstraints(20, 2,
                                                                     1,
                                                                     2,
                                                                     GridConstraints.ANCHOR_WEST,
                                                                     GridConstraints.FILL_NONE,
                                                                     GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                                                     GridConstraints.SIZEPOLICY_FIXED,
                                                                     null,
                                                                     null,
                                                                     null,
                                                                     0,
                                                                     false));
        noMixedABICheckBox = new JCheckBox();
        noMixedABICheckBox.setSelected(false);
        this.$$$loadButtonText$$$(noMixedABICheckBox, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.gnustep.checkbox.no_mixed_abi"));
        noMixedABICheckBox.setToolTipText(ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.gnustep.checkbox.no_mixed_abi.tooltip"));
        optionsPanel.add(noMixedABICheckBox, new GridConstraints(20, 0,
                                                                 1,
                                                                 2,
                                                                 GridConstraints.ANCHOR_WEST,
                                                                 GridConstraints.FILL_NONE,
                                                                 GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                                                 GridConstraints.SIZEPOLICY_FIXED,
                                                                 null,
                                                                 null,
                                                                 null,
                                                                 0,
                                                                 false));
        nativeObjectiveCExceptionsCheckBox = new JCheckBox();
        nativeObjectiveCExceptionsCheckBox.setSelected(false);
        this.$$$loadButtonText$$$(nativeObjectiveCExceptionsCheckBox,
                                  ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.gnustep.checkbox.native_objc_exceptions"));
        nativeObjectiveCExceptionsCheckBox.setToolTipText(ResourceBundle.getBundle("com/galenrhodes/gnustep/localization")
                                                                        .getString("build.gnustep.checkbox.native_objc_exceptions.tooltip"));
        optionsPanel.add(nativeObjectiveCExceptionsCheckBox, new GridConstraints(21, 2,
                                                                                 1,
                                                                                 3,
                                                                                 GridConstraints.ANCHOR_WEST,
                                                                                 GridConstraints.FILL_NONE,
                                                                                 GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                                                                 GridConstraints.SIZEPOLICY_FIXED,
                                                                                 null,
                                                                                 null,
                                                                                 null,
                                                                                 0,
                                                                                 false));
        useNonFragileABICheckBox = new JCheckBox();
        useNonFragileABICheckBox.setSelected(false);
        this.$$$loadButtonText$$$(useNonFragileABICheckBox,
                                  ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.gnustep.checkbox.use_nonfragile_abi"));
        useNonFragileABICheckBox.setToolTipText(ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.gnustep.checkbox.use_nonfragile_abi.tooltip"));
        optionsPanel.add(useNonFragileABICheckBox, new GridConstraints(21, 0,
                                                                       1,
                                                                       2,
                                                                       GridConstraints.ANCHOR_WEST,
                                                                       GridConstraints.FILL_NONE,
                                                                       GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                                                       GridConstraints.SIZEPOLICY_FIXED,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       0,
                                                                       false));
        final JLabel label7 = new JLabel();
        this.$$$loadLabelText$$$(label7, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.gnustep.label.lib_combo"));
        optionsPanel.add(label7, new GridConstraints(22, 0,
                                                     1,
                                                     1,
                                                     GridConstraints.ANCHOR_EAST,
                                                     GridConstraints.FILL_NONE,
                                                     GridConstraints.SIZEPOLICY_FIXED,
                                                     GridConstraints.SIZEPOLICY_FIXED,
                                                     null,
                                                     null,
                                                     null,
                                                     0,
                                                     false));
        libComboADropDown = new JComboBox();
        libComboADropDown.setEditable(false);
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        libComboADropDown.setModel(defaultComboBoxModel1);
        optionsPanel.add(libComboADropDown, new GridConstraints(22, 1,
                                                                1,
                                                                1,
                                                                GridConstraints.ANCHOR_WEST,
                                                                GridConstraints.FILL_HORIZONTAL,
                                                                GridConstraints.SIZEPOLICY_CAN_GROW,
                                                                GridConstraints.SIZEPOLICY_FIXED,
                                                                null,
                                                                null,
                                                                null,
                                                                0,
                                                                false));
        final JLabel label8 = new JLabel();
        this.$$$loadLabelText$$$(label8, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.gnustep.label.layout"));
        optionsPanel.add(label8, new GridConstraints(23, 0,
                                                     1,
                                                     1,
                                                     GridConstraints.ANCHOR_EAST,
                                                     GridConstraints.FILL_NONE,
                                                     GridConstraints.SIZEPOLICY_FIXED,
                                                     GridConstraints.SIZEPOLICY_FIXED,
                                                     null,
                                                     null,
                                                     null,
                                                     0,
                                                     false));
        buildGoogleTestCheckBox = new JCheckBox();
        buildGoogleTestCheckBox.setSelected(false);
        this.$$$loadButtonText$$$(buildGoogleTestCheckBox, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.clang.checkbox.gtest"));
        buildGoogleTestCheckBox.setToolTipText(ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.clang.checkbox.gtest.tooltip"));
        optionsPanel.add(buildGoogleTestCheckBox, new GridConstraints(9, 0,
                                                                      1,
                                                                      4,
                                                                      GridConstraints.ANCHOR_WEST,
                                                                      GridConstraints.FILL_NONE,
                                                                      GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                                                      GridConstraints.SIZEPOLICY_FIXED,
                                                                      null,
                                                                      null,
                                                                      null,
                                                                      0,
                                                                      false));
        libComboBDropDown = new JComboBox();
        libComboBDropDown.setEditable(false);
        final DefaultComboBoxModel defaultComboBoxModel2 = new DefaultComboBoxModel();
        libComboBDropDown.setModel(defaultComboBoxModel2);
        optionsPanel.add(libComboBDropDown, new GridConstraints(22, 2,
                                                                1,
                                                                1,
                                                                GridConstraints.ANCHOR_WEST,
                                                                GridConstraints.FILL_HORIZONTAL,
                                                                GridConstraints.SIZEPOLICY_CAN_GROW,
                                                                GridConstraints.SIZEPOLICY_FIXED,
                                                                null,
                                                                null,
                                                                null,
                                                                0,
                                                                false));
        libComboCDropDown = new JComboBox();
        libComboCDropDown.setEditable(false);
        final DefaultComboBoxModel defaultComboBoxModel3 = new DefaultComboBoxModel();
        libComboCDropDown.setModel(defaultComboBoxModel3);
        optionsPanel.add(libComboCDropDown, new GridConstraints(22, 3,
                                                                1,
                                                                1,
                                                                GridConstraints.ANCHOR_WEST,
                                                                GridConstraints.FILL_HORIZONTAL,
                                                                GridConstraints.SIZEPOLICY_CAN_GROW,
                                                                GridConstraints.SIZEPOLICY_FIXED,
                                                                null,
                                                                null,
                                                                null,
                                                                0,
                                                                false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        optionsPanel.add(panel1, new GridConstraints(24, 2,
                                                     1,
                                                     3,
                                                     GridConstraints.ANCHOR_CENTER,
                                                     GridConstraints.FILL_BOTH,
                                                     GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                                     GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                                     null,
                                                     null,
                                                     null,
                                                     0,
                                                     false));
        helpButton = new JButton();
        helpButton.setDefaultCapable(false);
        this.$$$loadButtonText$$$(helpButton, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("install.button.help"));
        panel1.add(helpButton,
                   new GridConstraints(0,
                                       3,
                                       1,
                                       1,
                                       GridConstraints.ANCHOR_CENTER,
                                       GridConstraints.FILL_HORIZONTAL,
                                       GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                       GridConstraints.SIZEPOLICY_FIXED,
                                       null,
                                       null,
                                       null,
                                       0,
                                       false));
        final Spacer spacer3 = new Spacer();
        panel1.add(spacer3,
                   new GridConstraints(0,
                                       0,
                                       1,
                                       1,
                                       GridConstraints.ANCHOR_CENTER,
                                       GridConstraints.FILL_HORIZONTAL,
                                       GridConstraints.SIZEPOLICY_WANT_GROW,
                                       1,
                                       null,
                                       null,
                                       null,
                                       0,
                                       false));
        cancelButton = new JButton();
        cancelButton.setDefaultCapable(false);
        this.$$$loadButtonText$$$(cancelButton, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("install.button.cancel"));
        panel1.add(cancelButton,
                   new GridConstraints(0,
                                       2,
                                       1,
                                       1,
                                       GridConstraints.ANCHOR_CENTER,
                                       GridConstraints.FILL_HORIZONTAL,
                                       GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                       GridConstraints.SIZEPOLICY_FIXED,
                                       null,
                                       null,
                                       null,
                                       0,
                                       false));
        startButton = new JButton();
        startButton.setSelected(false);
        this.$$$loadButtonText$$$(startButton, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("install.button.start"));
        panel1.add(startButton,
                   new GridConstraints(0,
                                       1,
                                       1,
                                       1,
                                       GridConstraints.ANCHOR_CENTER,
                                       GridConstraints.FILL_HORIZONTAL,
                                       GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                       GridConstraints.SIZEPOLICY_FIXED,
                                       null,
                                       null,
                                       null,
                                       0,
                                       false));
        oldABICompatabilityCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(oldABICompatabilityCheckBox, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.objc2.checkbox.old_api_compat"));
        optionsPanel.add(oldABICompatabilityCheckBox, new GridConstraints(16, 0,
                                                                          1,
                                                                          4,
                                                                          GridConstraints.ANCHOR_WEST,
                                                                          GridConstraints.FILL_NONE,
                                                                          GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                                                          GridConstraints.SIZEPOLICY_FIXED,
                                                                          null,
                                                                          null,
                                                                          null,
                                                                          0,
                                                                          false));
        a20RadioButton = new JRadioButton();
        a20RadioButton.setSelected(false);
        this.$$$loadButtonText$$$(a20RadioButton, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.objc2.radiobutton.rt_version_2_0"));
        a20RadioButton.setToolTipText(ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.objc2.radiobutton.rt_version_2_0.tooltip"));
        optionsPanel.add(a20RadioButton, new GridConstraints(14, 4,
                                                             1,
                                                             1,
                                                             GridConstraints.ANCHOR_WEST,
                                                             GridConstraints.FILL_NONE,
                                                             GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                                             GridConstraints.SIZEPOLICY_FIXED,
                                                             null,
                                                             null,
                                                             null,
                                                             0,
                                                             false));
        a19RadioButton = new JRadioButton();
        a19RadioButton.setSelected(false);
        this.$$$loadButtonText$$$(a19RadioButton, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.objc2.radiobutton.rt_version_1_9"));
        a19RadioButton.setToolTipText(ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.objc2.radiobutton.rt_version_1_9.tooltip"));
        optionsPanel.add(a19RadioButton, new GridConstraints(14, 3,
                                                             1,
                                                             1,
                                                             GridConstraints.ANCHOR_WEST,
                                                             GridConstraints.FILL_NONE,
                                                             GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                                             GridConstraints.SIZEPOLICY_FIXED,
                                                             null,
                                                             null,
                                                             null,
                                                             0,
                                                             false));
        a18RadioButton = new JRadioButton();
        a18RadioButton.setSelected(false);
        this.$$$loadButtonText$$$(a18RadioButton, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.objc2.radiobutton.rt_version_gcc"));
        a18RadioButton.setToolTipText(ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.objc2.radiobutton.rt_version_gcc.tooltip"));
        optionsPanel.add(a18RadioButton, new GridConstraints(14, 2,
                                                             1,
                                                             1,
                                                             GridConstraints.ANCHOR_WEST,
                                                             GridConstraints.FILL_NONE,
                                                             GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                                             GridConstraints.SIZEPOLICY_FIXED,
                                                             null,
                                                             null,
                                                             null,
                                                             0,
                                                             false));
        filesystemLayoutDropDown = new JComboBox();
        filesystemLayoutDropDown.setEditable(false);
        final DefaultComboBoxModel defaultComboBoxModel4 = new DefaultComboBoxModel();
        filesystemLayoutDropDown.setModel(defaultComboBoxModel4);
        optionsPanel.add(filesystemLayoutDropDown, new GridConstraints(23, 1,
                                                                       1,
                                                                       2,
                                                                       GridConstraints.ANCHOR_WEST,
                                                                       GridConstraints.FILL_HORIZONTAL,
                                                                       GridConstraints.SIZEPOLICY_CAN_GROW,
                                                                       GridConstraints.SIZEPOLICY_FIXED,
                                                                       null,
                                                                       null,
                                                                       null, 0, false));
        final JLabel label9 = new JLabel();
        this.$$$loadLabelText$$$(label9, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("working.directory.label"));
        optionsPanel.add(label9,
                         new GridConstraints(0,
                                             0,
                                             1,
                                             1,
                                             GridConstraints.ANCHOR_WEST,
                                             GridConstraints.FILL_NONE,
                                             GridConstraints.SIZEPOLICY_FIXED,
                                             GridConstraints.SIZEPOLICY_FIXED,
                                             null,
                                             null,
                                             null,
                                             0,
                                             false));
        workingPathTextField = new JTextField();
        workingPathTextField.setToolTipText(ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("working.directory.tooltip"));
        optionsPanel.add(workingPathTextField,
                         new GridConstraints(0,
                                             1,
                                             1,
                                             3,
                                             GridConstraints.ANCHOR_WEST,
                                             GridConstraints.FILL_HORIZONTAL,
                                             GridConstraints.SIZEPOLICY_WANT_GROW,
                                             GridConstraints.SIZEPOLICY_FIXED,
                                             null,
                                             new Dimension(150, -1),
                                             null,
                                             0,
                                             false));
        workingPathFindButton = new JButton();
        this.$$$loadButtonText$$$(workingPathFindButton, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("working.directory.button.find"));
        workingPathFindButton.setToolTipText(ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("working.directory.button.find.tooltip"));
        optionsPanel.add(workingPathFindButton,
                         new GridConstraints(0,
                                             4,
                                             1,
                                             1,
                                             GridConstraints.ANCHOR_CENTER,
                                             GridConstraints.FILL_HORIZONTAL,
                                             GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                             GridConstraints.SIZEPOLICY_FIXED,
                                             null,
                                             null,
                                             null,
                                             0,
                                             false));
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(a19RadioButton);
        buttonGroup.add(a20RadioButton);
        buttonGroup.add(a18RadioButton);
    }

    private void enableEverything() {
        for(Field c : this.getClass().getFields()) {
            if(c.getType() == Component.class) {
                try {
                    ((Component)c.get(this)).setEnabled(true);
                }
                catch(Exception e) {
                    System.err.printf("ERROR: %s\n", e.getLocalizedMessage());
                }
            }
        }
    }

}