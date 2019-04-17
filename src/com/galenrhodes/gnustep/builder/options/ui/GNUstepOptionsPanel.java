package com.galenrhodes.gnustep.builder.options.ui;

import com.galenrhodes.gnustep.builder.common.ButtonPressedListener;
import com.galenrhodes.gnustep.builder.common.StandardButtons;
import com.galenrhodes.gnustep.builder.common.Tools;
import com.galenrhodes.gnustep.builder.common.events.EventEngine;
import com.galenrhodes.gnustep.builder.options.GNUstepBuildOptions;
import com.galenrhodes.gnustep.builder.options.data.FileSystemLayoutsEnum;
import com.galenrhodes.gnustep.builder.options.data.LibraryComboEnum;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ExecutorService;

public class GNUstepOptionsPanel {

    public static final String DEFAULT_INSTALL_DIR = "/gnustep";
    public static final String DEFAULT_WORKING_DIR = "/Downloads/gnustepwork";

    private final Map<String, Map<String, Object>> cache     = new TreeMap<>();
    private final Set<ButtonPressedListener>       btnLstnrs = new LinkedHashSet<>();

    private JPanel buildOptionsPanel;

    private JButton startButton;
    private JButton cancelButton;
    private JButton helpButton;
    private JButton installDirFindButton;
    private JButton workingDirFindButton;

    private JCheckBox archX32_64CheckBox;
    private JCheckBox archARM32CheckBox;
    private JCheckBox archARM64CheckBox;

    private JRadioButton rtVer1_8RadioButton;
    private JRadioButton rtVer1_9RadioButton;
    private JRadioButton rtVer2_0RadioButton;

    private JCheckBox installPrerequisiteSoftwareCheckBox;
    private JCheckBox installLibkqueueCheckBox;
    private JCheckBox createEntriesInLdSoConfDCheckBox;
    private JCheckBox buildClangCheckBox;
    private JCheckBox buildGoogleTestCheckBox;
    private JCheckBox useSwiftLibDispatchCheckBox;
    private JCheckBox buildLibDispatchFirstCheckBox;
    private JCheckBox buildMakeTwiceCheckBox;
    private JCheckBox noMixedABICheckBox;
    private JCheckBox nonFragileABICheckBox;
    private JCheckBox objectiveCArcCheckBox;
    private JCheckBox debugByDefaultCheckBox;
    private JCheckBox nativeObjectiveCExceptionsCheckBox;
    private JCheckBox oldABICompatCheckBox;
    private JCheckBox customLibNameCheckBox;

    private JComboBox<String> fileSystemLayoutDropDown;
    private JComboBox<String> libComboDropDown;

    private JTextField installDirField;
    private JTextField workingDirField;
    private JTextField customLibNameValue;

    private StandardButtons pressedButton;

    public GNUstepOptionsPanel() {
        super();
        ButtonHandler h = new ButtonHandler();
        startButton.addActionListener(h);
        cancelButton.addActionListener(h);
        helpButton.addActionListener(h);
        installDirFindButton.addActionListener(h);
        workingDirFindButton.addActionListener(h);

        for(FileSystemLayoutsEnum en : FileSystemLayoutsEnum.values()) fileSystemLayoutDropDown.addItem(en.value());
        for(LibraryComboEnum en : LibraryComboEnum.values()) libComboDropDown.addItem(en.value());
        fileSystemLayoutDropDown.setSelectedIndex(0);
        libComboDropDown.setSelectedIndex(0);

        ItemListener itemListener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                Object src = e.getSource();
                if((src == archARM32CheckBox) || (src == archARM64CheckBox)) handleArchARMxxCheckBoxStateChange();
                else if(src == installPrerequisiteSoftwareCheckBox) handleInstallPrerequisiteSoftwareCheckBoxStateChange();
                else if(src == rtVer1_8RadioButton) handleRtVer1_8RadioButtonStateChange();
                else if(src == buildClangCheckBox) handleBuildClangCheckBoxStateChange();
                else if(src == customLibNameCheckBox) handleCustomLibNameCheckBoxStateChange();
            }
        };

        archARM32CheckBox.addItemListener(itemListener);
        archARM64CheckBox.addItemListener(itemListener);
        installPrerequisiteSoftwareCheckBox.addItemListener(itemListener);
        rtVer1_8RadioButton.addItemListener(itemListener);
        buildClangCheckBox.addItemListener(itemListener);
        customLibNameCheckBox.addItemListener(itemListener);
    }

    public final void addButtonListener(ButtonPressedListener listener) { synchronized(btnLstnrs) { btnLstnrs.add(listener); }}

    public void getData(GNUstepBuildOptions data) {
        data.setArchX32_64(archX32_64CheckBox.isSelected());
        data.setArchARM32(archARM32CheckBox.isSelected());
        data.setArchARM64(archARM64CheckBox.isSelected());

        data.setRtVer1_8(rtVer1_8RadioButton.isSelected());
        data.setRtVer1_9(rtVer1_9RadioButton.isSelected());
        data.setRtVer2_0(rtVer2_0RadioButton.isSelected());

        data.setInstallPrerequisiteSoftware(installPrerequisiteSoftwareCheckBox.isSelected());
        data.setInstallLibkqueue(installLibkqueueCheckBox.isSelected());
        data.setCreateEntriesInLdSoConfD(createEntriesInLdSoConfDCheckBox.isSelected());
        data.setBuildClang(buildClangCheckBox.isSelected());
        data.setBuildGoogleTest(buildGoogleTestCheckBox.isSelected());
        data.setUseSwiftLibDispatch(useSwiftLibDispatchCheckBox.isSelected());
        data.setBuildLibDispatchFirst(buildLibDispatchFirstCheckBox.isSelected());
        data.setBuildMakeTwice(buildMakeTwiceCheckBox.isSelected());
        data.setNoMixedABI(noMixedABICheckBox.isSelected());
        data.setNonFragileABI(nonFragileABICheckBox.isSelected());
        data.setObjectiveCArc(objectiveCArcCheckBox.isSelected());
        data.setDebugByDefault(debugByDefaultCheckBox.isSelected());
        data.setNativeObjectiveCExceptions(nativeObjectiveCExceptionsCheckBox.isSelected());
        data.setOldABICompat(oldABICompatCheckBox.isSelected());
        data.setHasCustomLibName(customLibNameCheckBox.isSelected());

        data.setCustomLibNameValue(customLibNameValue.getText());
        data.setInstallDirectory(installDirField.getText());
        data.setWorkingDirectory(workingDirField.getText());
    }

    public final JPanel getRootPanel() { return buildOptionsPanel; }

    public boolean isModified(GNUstepBuildOptions data) {
        if(Tools.isModified(rtVer1_8RadioButton, data.isRtVer1_8())) return true;
        if(Tools.isModified(rtVer1_9RadioButton, data.isRtVer1_9())) return true;
        if(Tools.isModified(rtVer2_0RadioButton, data.isRtVer2_0())) return true;

        if(Tools.isModified(archX32_64CheckBox, data.isArchX32_64())) return true;
        if(Tools.isModified(archARM32CheckBox, data.isArchARM32())) return true;
        if(Tools.isModified(archARM64CheckBox, data.isArchARM64())) return true;

        if(Tools.isModified(installPrerequisiteSoftwareCheckBox, data.isInstallPrerequisiteSoftware())) return true;
        if(Tools.isModified(installLibkqueueCheckBox, data.isInstallLibkqueue())) return true;
        if(Tools.isModified(createEntriesInLdSoConfDCheckBox, data.isCreateEntriesInLdSoConfD())) return true;
        if(Tools.isModified(buildClangCheckBox, data.isBuildClang())) return true;
        if(Tools.isModified(buildGoogleTestCheckBox, data.isBuildGoogleTest())) return true;
        if(Tools.isModified(useSwiftLibDispatchCheckBox, data.isUseSwiftLibDispatch())) return true;
        if(Tools.isModified(buildLibDispatchFirstCheckBox, data.isBuildLibDispatchFirst())) return true;
        if(Tools.isModified(buildMakeTwiceCheckBox, data.isBuildMakeTwice())) return true;
        if(Tools.isModified(noMixedABICheckBox, data.isNoMixedABI())) return true;
        if(Tools.isModified(nonFragileABICheckBox, data.isNonFragileABI())) return true;
        if(Tools.isModified(objectiveCArcCheckBox, data.isObjectiveCArc())) return true;
        if(Tools.isModified(debugByDefaultCheckBox, data.isDebugByDefault())) return true;
        if(Tools.isModified(nativeObjectiveCExceptionsCheckBox, data.isNativeObjectiveCExceptions())) return true;
        if(Tools.isModified(oldABICompatCheckBox, data.isOldABICompat())) return true;
        if(Tools.isModified(customLibNameCheckBox, data.hasCustomLibName())) return true;

        if(Tools.isModified(fileSystemLayoutDropDown, data.getFileSystemLayoutValue())) return true;
        if(Tools.isModified(libComboDropDown, data.getLibraryComboValue())) return true;

        if(Tools.isModified(customLibNameValue, data.getCustomLibNameValue())) return true;
        if(Tools.isModified(installDirField, data.getInstallDirectory())) return true;
        return Tools.isModified(workingDirField, data.getWorkingDirectory());
    }

    public final ButtonPressedListener removeButtonListener(ButtonPressedListener listener) { synchronized(btnLstnrs) { return (btnLstnrs.remove(listener) ? listener : null); }}

    public final StandardButtons resetPressedButton() {
        StandardButtons button = null;
        synchronized(DEFAULT_INSTALL_DIR) {
            if((button = pressedButton) != null) {
                pressedButton = null;
                DEFAULT_INSTALL_DIR.notify();
            }
        }
        return button;
    }

    public void setData(GNUstepBuildOptions data) {
        cache.clear();
        enableEverything();

        archX32_64CheckBox.setSelected(data.isArchX32_64());
        archARM32CheckBox.setSelected(data.isArchARM32());
        archARM64CheckBox.setSelected(data.isArchARM64());

        rtVer1_8RadioButton.setSelected(data.isRtVer1_8());
        rtVer1_9RadioButton.setSelected(data.isRtVer1_9());
        rtVer2_0RadioButton.setSelected(data.isRtVer2_0());

        installPrerequisiteSoftwareCheckBox.setSelected(data.isInstallPrerequisiteSoftware());
        installLibkqueueCheckBox.setSelected(data.isInstallLibkqueue());
        createEntriesInLdSoConfDCheckBox.setSelected(data.isCreateEntriesInLdSoConfD());
        buildClangCheckBox.setSelected(data.isBuildClang());
        buildGoogleTestCheckBox.setSelected(data.isBuildGoogleTest());
        useSwiftLibDispatchCheckBox.setSelected(data.isUseSwiftLibDispatch());
        buildLibDispatchFirstCheckBox.setSelected(data.isBuildLibDispatchFirst());
        buildMakeTwiceCheckBox.setSelected(data.isBuildMakeTwice());
        noMixedABICheckBox.setSelected(data.isNoMixedABI());
        nonFragileABICheckBox.setSelected(data.isNonFragileABI());
        objectiveCArcCheckBox.setSelected(data.isObjectiveCArc());
        debugByDefaultCheckBox.setSelected(data.isDebugByDefault());
        nativeObjectiveCExceptionsCheckBox.setSelected(data.isNativeObjectiveCExceptions());
        oldABICompatCheckBox.setSelected(data.isOldABICompat());
        customLibNameCheckBox.setSelected(data.hasCustomLibName());

        Tools.setSelected(fileSystemLayoutDropDown, data.getFileSystemLayoutValue());
        Tools.setSelected(libComboDropDown, data.getLibraryComboValue());

        customLibNameValue.setText(Objects.toString(data.getCustomLibNameValue(), ""));
        installDirField.setText(Objects.toString(data.getInstallDirectory(), ""));
        workingDirField.setText(Objects.toString(data.getWorkingDirectory(), ""));

        handleArchARMxxCheckBoxStateChange();
        handleBuildClangCheckBoxStateChange();
        handleCustomLibNameCheckBoxStateChange();
        handleInstallPrerequisiteSoftwareCheckBoxStateChange();
        handleRtVer1_8RadioButtonStateChange();
    }

    public final StandardButtons waitForButton(StandardButtons... buttons) throws InterruptedException {
        synchronized(DEFAULT_INSTALL_DIR) {
            while(foo01(pressedButton, buttons)) try { DEFAULT_INSTALL_DIR.wait(); }
            catch(InterruptedException e) {
                DEFAULT_INSTALL_DIR.notify();
                throw e;
            }
            StandardButtons button = pressedButton;
            DEFAULT_INSTALL_DIR.notify();
            return button;
        }
    }

    public final void waitForButtonReset() throws InterruptedException {
        synchronized(DEFAULT_INSTALL_DIR) {
            while(pressedButton != null) {
                try { DEFAULT_INSTALL_DIR.wait(); }
                catch(InterruptedException e) {
                    DEFAULT_INSTALL_DIR.notify();
                    throw e;
                }
            }
            DEFAULT_INSTALL_DIR.notify();
        }
    }

    protected void publishButtonEvent(StandardButtons button) {
        ExecutorService exec = EventEngine.getExecutor();
        exec.submit(new Runnable() {
            @Override
            public void run() {
                synchronized(DEFAULT_INSTALL_DIR) {
                    pressedButton = button;
                    DEFAULT_INSTALL_DIR.notify();
                }
            }
        });
        synchronized(btnLstnrs) {
            for(final ButtonPressedListener l : btnLstnrs)
                exec.submit(new Runnable() {
                    @Override
                    public void run() { l.buttonPressed(button); }
                });
        }
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer >>> IMPORTANT!! <<< DO NOT edit this method OR call it in your code!
     */
    private void $$$setupUI$$$() {
        buildOptionsPanel = new JPanel();
        buildOptionsPanel.setLayout(new GridLayoutManager(4, 2, new Insets(3, 3, 3, 3), -1, -1, true, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        buildOptionsPanel.add(panel1,
                              new GridConstraints(1,
                                                  0,
                                                  1,
                                                  1,
                                                  GridConstraints.ANCHOR_CENTER,
                                                  GridConstraints.FILL_BOTH,
                                                  GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                                  GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                                  null,
                                                  null,
                                                  null,
                                                  0,
                                                  false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2,
                   new GridConstraints(0,
                                       0,
                                       1,
                                       1,
                                       GridConstraints.ANCHOR_CENTER,
                                       GridConstraints.FILL_BOTH,
                                       GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                       GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                       null,
                                       null,
                                       null,
                                       0,
                                       false));
        panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Miscellaneous Options"));
        installPrerequisiteSoftwareCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(installPrerequisiteSoftwareCheckBox,
                                  ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("install.checkbox.prereq_software"));
        installPrerequisiteSoftwareCheckBox.setToolTipText(ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("install.checkbox.prereq_software.tooltip"));
        panel2.add(installPrerequisiteSoftwareCheckBox,
                   new GridConstraints(0,
                                       0,
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
        installLibkqueueCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(installLibkqueueCheckBox, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("install.checkbox.libkqueue"));
        installLibkqueueCheckBox.setToolTipText(ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("install.checkbox.libkqueue.tooltip"));
        panel2.add(installLibkqueueCheckBox,
                   new GridConstraints(1,
                                       0,
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
        createEntriesInLdSoConfDCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(createEntriesInLdSoConfDCheckBox,
                                  ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("install.checkbox.etc_ld_so_conf_d"));
        createEntriesInLdSoConfDCheckBox.setToolTipText(ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("install.checkbox.etc_ld_so_conf_d.tooltip"));
        panel2.add(createEntriesInLdSoConfDCheckBox,
                   new GridConstraints(2,
                                       0,
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
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3,
                   new GridConstraints(1,
                                       0,
                                       1,
                                       1,
                                       GridConstraints.ANCHOR_CENTER,
                                       GridConstraints.FILL_BOTH,
                                       GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                       GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                       null,
                                       null,
                                       null,
                                       0,
                                       false));
        panel3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "CLANG/LLVM Options"));
        buildClangCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(buildClangCheckBox, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.clang.checkbox.latest"));
        buildClangCheckBox.setToolTipText(ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.clang.checkbox.latest.tooltip"));
        panel3.add(buildClangCheckBox,
                   new GridConstraints(0,
                                       0,
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
        buildGoogleTestCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(buildGoogleTestCheckBox, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.clang.checkbox.gtest"));
        buildGoogleTestCheckBox.setToolTipText(ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.clang.checkbox.gtest.tooltip"));
        panel3.add(buildGoogleTestCheckBox,
                   new GridConstraints(0,
                                       1,
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
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel4,
                   new GridConstraints(1,
                                       0,
                                       1,
                                       2,
                                       GridConstraints.ANCHOR_CENTER,
                                       GridConstraints.FILL_BOTH,
                                       GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                       GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                       null,
                                       null,
                                       null,
                                       0,
                                       false));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.clang.label.targets"));
        panel4.add(label1,
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
        archX32_64CheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(archX32_64CheckBox, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.clang.checkbox.arch_x86_64"));
        archX32_64CheckBox.setToolTipText(ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.clang.checkbox.arch_x64_64.tooltip"));
        panel4.add(archX32_64CheckBox,
                   new GridConstraints(0,
                                       1,
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
        panel4.add(archARM32CheckBox,
                   new GridConstraints(0,
                                       2,
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
        panel4.add(archARM64CheckBox,
                   new GridConstraints(0,
                                       3,
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
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel5,
                   new GridConstraints(2,
                                       0,
                                       1,
                                       1,
                                       GridConstraints.ANCHOR_CENTER,
                                       GridConstraints.FILL_BOTH,
                                       GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                       GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                       null,
                                       null,
                                       null,
                                       0,
                                       false));
        panel5.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Dispatch (libdispatch) Options"));
        useSwiftLibDispatchCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(useSwiftLibDispatchCheckBox,
                                  ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.dispatch.checkbox.dispatch_from_swift"));
        useSwiftLibDispatchCheckBox.setToolTipText(ResourceBundle.getBundle("com/galenrhodes/gnustep/localization")
                                                                 .getString("build.dispatch.checkbox.dispatch_from_swift.tooltip"));
        panel5.add(useSwiftLibDispatchCheckBox,
                   new GridConstraints(0,
                                       0,
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
        buildLibDispatchFirstCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(buildLibDispatchFirstCheckBox,
                                  ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.dispatch.checkbox.dispatch_first"));
        buildLibDispatchFirstCheckBox.setToolTipText(ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.dispatch.checkbox.dispatch_first.tooltip"));
        panel5.add(buildLibDispatchFirstCheckBox,
                   new GridConstraints(0,
                                       1,
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
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        buildOptionsPanel.add(panel6,
                              new GridConstraints(1,
                                                  1,
                                                  1,
                                                  1,
                                                  GridConstraints.ANCHOR_CENTER,
                                                  GridConstraints.FILL_BOTH,
                                                  GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                                  GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                                  null,
                                                  null,
                                                  null,
                                                  0,
                                                  false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel6.add(panel7,
                   new GridConstraints(1,
                                       0,
                                       1,
                                       1,
                                       GridConstraints.ANCHOR_CENTER,
                                       GridConstraints.FILL_BOTH,
                                       GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                       GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                       null,
                                       null,
                                       null,
                                       0,
                                       false));
        panel7.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "GNUstep Options"));
        buildMakeTwiceCheckBox = new JCheckBox();
        buildMakeTwiceCheckBox.setSelected(false);
        this.$$$loadButtonText$$$(buildMakeTwiceCheckBox, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.gnustep.checkbox.build_make_twice"));
        buildMakeTwiceCheckBox.setToolTipText(ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.gnustep.checkbox.build_make_twice.tooltip"));
        panel7.add(buildMakeTwiceCheckBox,
                   new GridConstraints(0,
                                       0,
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
        noMixedABICheckBox = new JCheckBox();
        noMixedABICheckBox.setSelected(false);
        this.$$$loadButtonText$$$(noMixedABICheckBox, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.gnustep.checkbox.no_mixed_abi"));
        noMixedABICheckBox.setToolTipText(ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.gnustep.checkbox.no_mixed_abi.tooltip"));
        panel7.add(noMixedABICheckBox,
                   new GridConstraints(1,
                                       0,
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
        nonFragileABICheckBox = new JCheckBox();
        nonFragileABICheckBox.setSelected(false);
        this.$$$loadButtonText$$$(nonFragileABICheckBox, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.gnustep.checkbox.use_nonfragile_abi"));
        nonFragileABICheckBox.setToolTipText(ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.gnustep.checkbox.use_nonfragile_abi.tooltip"));
        panel7.add(nonFragileABICheckBox,
                   new GridConstraints(2,
                                       0,
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
        nativeObjectiveCExceptionsCheckBox = new JCheckBox();
        nativeObjectiveCExceptionsCheckBox.setSelected(false);
        this.$$$loadButtonText$$$(nativeObjectiveCExceptionsCheckBox,
                                  ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.gnustep.checkbox.native_objc_exceptions"));
        nativeObjectiveCExceptionsCheckBox.setToolTipText(ResourceBundle.getBundle("com/galenrhodes/gnustep/localization")
                                                                        .getString("build.gnustep.checkbox.native_objc_exceptions.tooltip"));
        panel7.add(nativeObjectiveCExceptionsCheckBox,
                   new GridConstraints(2,
                                       1,
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
        debugByDefaultCheckBox = new JCheckBox();
        debugByDefaultCheckBox.setSelected(false);
        this.$$$loadButtonText$$$(debugByDefaultCheckBox,
                                  ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.gnustep.checkbox.enable_debug_by_default"));
        debugByDefaultCheckBox.setToolTipText(ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.gnustep.checkbox.enable_debug_by_default.tooltip"));
        panel7.add(debugByDefaultCheckBox,
                   new GridConstraints(1,
                                       1,
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
        objectiveCArcCheckBox = new JCheckBox();
        objectiveCArcCheckBox.setSelected(false);
        this.$$$loadButtonText$$$(objectiveCArcCheckBox, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.gnustep.checkbox.use_arc"));
        objectiveCArcCheckBox.setToolTipText(ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.gnustep.checkbox.use_arc.tooltip"));
        panel7.add(objectiveCArcCheckBox,
                   new GridConstraints(0,
                                       1,
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
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel7.add(panel8,
                   new GridConstraints(3,
                                       0,
                                       1,
                                       2,
                                       GridConstraints.ANCHOR_CENTER,
                                       GridConstraints.FILL_BOTH,
                                       GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                       GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                       null,
                                       null,
                                       null,
                                       0,
                                       false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.gnustep.label.lib_combo"));
        panel8.add(label2,
                   new GridConstraints(0,
                                       0,
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
        libComboDropDown = new JComboBox();
        panel8.add(libComboDropDown,
                   new GridConstraints(0,
                                       1,
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
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.gnustep.label.layout"));
        panel8.add(label3,
                   new GridConstraints(1,
                                       0,
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
        fileSystemLayoutDropDown = new JComboBox();
        panel8.add(fileSystemLayoutDropDown,
                   new GridConstraints(1,
                                       1,
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
        final Spacer spacer1 = new Spacer();
        panel8.add(spacer1,
                   new GridConstraints(0,
                                       2,
                                       2,
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
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridLayoutManager(3, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel6.add(panel9,
                   new GridConstraints(0,
                                       0,
                                       1,
                                       1,
                                       GridConstraints.ANCHOR_CENTER,
                                       GridConstraints.FILL_BOTH,
                                       GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                       GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                       null,
                                       null,
                                       null,
                                       0,
                                       false));
        panel9.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Objective-C Runtime Options"));
        oldABICompatCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(oldABICompatCheckBox, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.objc2.checkbox.old_api_compat"));
        panel9.add(oldABICompatCheckBox,
                   new GridConstraints(1,
                                       0,
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
        customLibNameCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(customLibNameCheckBox, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.objc2.label.custom_lib_name"));
        panel9.add(customLibNameCheckBox,
                   new GridConstraints(2,
                                       0,
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
        customLibNameValue = new JTextField();
        panel9.add(customLibNameValue,
                   new GridConstraints(2,
                                       1,
                                       1,
                                       1,
                                       GridConstraints.ANCHOR_WEST,
                                       GridConstraints.FILL_HORIZONTAL,
                                       GridConstraints.SIZEPOLICY_FIXED,
                                       GridConstraints.SIZEPOLICY_FIXED,
                                       new Dimension(150, -1),
                                       new Dimension(150, -1),
                                       null,
                                       0,
                                       false));
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel9.add(panel10,
                   new GridConstraints(0,
                                       0,
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
        final JLabel label4 = new JLabel();
        this.$$$loadLabelText$$$(label4, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.objc2.label.version"));
        panel10.add(label4,
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
        rtVer1_8RadioButton = new JRadioButton();
        this.$$$loadButtonText$$$(rtVer1_8RadioButton, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.objc2.radiobutton.rt_version_gcc"));
        rtVer1_8RadioButton.setToolTipText(ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.objc2.radiobutton.rt_version_gcc.tooltip"));
        panel10.add(rtVer1_8RadioButton,
                    new GridConstraints(0,
                                        1,
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
        rtVer1_9RadioButton = new JRadioButton();
        this.$$$loadButtonText$$$(rtVer1_9RadioButton, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.objc2.radiobutton.rt_version_1_9"));
        rtVer1_9RadioButton.setToolTipText(ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.objc2.radiobutton.rt_version_1_9.tooltip"));
        panel10.add(rtVer1_9RadioButton,
                    new GridConstraints(0,
                                        2,
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
        rtVer2_0RadioButton = new JRadioButton();
        this.$$$loadButtonText$$$(rtVer2_0RadioButton, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.objc2.radiobutton.rt_version_2_0"));
        rtVer2_0RadioButton.setToolTipText(ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("build.objc2.radiobutton.rt_version_2_0.tooltip"));
        panel10.add(rtVer2_0RadioButton,
                    new GridConstraints(0,
                                        3,
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
        panel9.add(spacer2,
                   new GridConstraints(2,
                                       2,
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
        final Spacer spacer3 = new Spacer();
        panel6.add(spacer3,
                   new GridConstraints(2,
                                       0,
                                       1,
                                       1,
                                       GridConstraints.ANCHOR_CENTER,
                                       GridConstraints.FILL_VERTICAL,
                                       1,
                                       GridConstraints.SIZEPOLICY_WANT_GROW,
                                       null,
                                       null,
                                       null,
                                       0,
                                       false));
        final JPanel panel11 = new JPanel();
        panel11.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        buildOptionsPanel.add(panel11,
                              new GridConstraints(3,
                                                  0,
                                                  1,
                                                  2,
                                                  GridConstraints.ANCHOR_CENTER,
                                                  GridConstraints.FILL_BOTH,
                                                  GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                                  GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                                  null,
                                                  null,
                                                  null,
                                                  0,
                                                  false));
        startButton = new JButton();
        startButton.setIcon(new ImageIcon(getClass().getResource("/images/accept.png")));
        this.$$$loadButtonText$$$(startButton, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("install.button.start"));
        panel11.add(startButton,
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
        cancelButton = new JButton();
        cancelButton.setIcon(new ImageIcon(getClass().getResource("/images/cross.png")));
        this.$$$loadButtonText$$$(cancelButton, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("install.button.cancel"));
        panel11.add(cancelButton,
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
        helpButton = new JButton();
        helpButton.setIcon(new ImageIcon(getClass().getResource("/images/help.png")));
        this.$$$loadButtonText$$$(helpButton, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("install.button.help"));
        panel11.add(helpButton,
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
        final Spacer spacer4 = new Spacer();
        panel11.add(spacer4,
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
        final Spacer spacer5 = new Spacer();
        buildOptionsPanel.add(spacer5,
                              new GridConstraints(2,
                                                  0,
                                                  1,
                                                  2,
                                                  GridConstraints.ANCHOR_CENTER,
                                                  GridConstraints.FILL_VERTICAL,
                                                  1,
                                                  GridConstraints.SIZEPOLICY_WANT_GROW,
                                                  null,
                                                  null,
                                                  null,
                                                  0,
                                                  false));
        final JPanel panel12 = new JPanel();
        panel12.setLayout(new GridLayoutManager(1, 2, new Insets(0, 3, 0, 3), -1, -1));
        buildOptionsPanel.add(panel12,
                              new GridConstraints(0,
                                                  0,
                                                  1,
                                                  2,
                                                  GridConstraints.ANCHOR_CENTER,
                                                  GridConstraints.FILL_BOTH,
                                                  GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                                  GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                                  null,
                                                  null,
                                                  null,
                                                  0,
                                                  true));
        panel12.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Directories"));
        final JPanel panel13 = new JPanel();
        panel13.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel12.add(panel13,
                    new GridConstraints(0,
                                        0,
                                        1,
                                        1,
                                        GridConstraints.ANCHOR_CENTER,
                                        GridConstraints.FILL_BOTH,
                                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                        null,
                                        null,
                                        null,
                                        0,
                                        false));
        final JLabel label5 = new JLabel();
        this.$$$loadLabelText$$$(label5, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("install.directory.label"));
        panel13.add(label5,
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
        installDirField = new JTextField();
        installDirField.setToolTipText(ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("install.directory.tooltip"));
        panel13.add(installDirField,
                    new GridConstraints(0,
                                        1,
                                        1,
                                        1,
                                        GridConstraints.ANCHOR_WEST,
                                        GridConstraints.FILL_HORIZONTAL,
                                        GridConstraints.SIZEPOLICY_WANT_GROW,
                                        GridConstraints.SIZEPOLICY_FIXED,
                                        null,
                                        new Dimension(150, -1),
                                        null,
                                        0,
                                        false));
        installDirFindButton = new JButton();
        installDirFindButton.setIcon(new ImageIcon(getClass().getResource("/images/find.png")));
        this.$$$loadButtonText$$$(installDirFindButton, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("install.directory.button.find"));
        installDirFindButton.setToolTipText(ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("install.directory.button.find.tooltip"));
        panel13.add(installDirFindButton,
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
        final JPanel panel14 = new JPanel();
        panel14.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel12.add(panel14,
                    new GridConstraints(0,
                                        1,
                                        1,
                                        1,
                                        GridConstraints.ANCHOR_CENTER,
                                        GridConstraints.FILL_BOTH,
                                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                        null,
                                        null,
                                        null,
                                        0,
                                        false));
        final JLabel label6 = new JLabel();
        this.$$$loadLabelText$$$(label6, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("working.directory.label"));
        panel14.add(label6,
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
        workingDirField = new JTextField();
        workingDirField.setToolTipText(ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("working.directory.tooltip"));
        panel14.add(workingDirField,
                    new GridConstraints(0,
                                        1,
                                        1,
                                        1,
                                        GridConstraints.ANCHOR_WEST,
                                        GridConstraints.FILL_HORIZONTAL,
                                        GridConstraints.SIZEPOLICY_WANT_GROW,
                                        GridConstraints.SIZEPOLICY_FIXED,
                                        null,
                                        new Dimension(150, -1),
                                        null,
                                        0,
                                        false));
        workingDirFindButton = new JButton();
        workingDirFindButton.setIcon(new ImageIcon(getClass().getResource("/images/find.png")));
        this.$$$loadButtonText$$$(workingDirFindButton, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("working.directory.button.find"));
        workingDirFindButton.setToolTipText(ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("working.directory.button.find.tooltip"));
        panel14.add(workingDirFindButton,
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
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(rtVer1_8RadioButton);
        buttonGroup.add(rtVer1_9RadioButton);
        buttonGroup.add(rtVer2_0RadioButton);
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
    public JComponent $$$getRootComponent$$$() { return buildOptionsPanel; }

    private void enableEverything() {
        Class<JComponent> cc = JComponent.class;
        for(Field c : getClass().getDeclaredFields()) if(cc.isAssignableFrom(c.getType())) try { enableComp(c); } catch(Exception e) { printEx(e); }
    }

    private void printEx(Exception e) {
        System.err.printf("ERROR: %s\n", e.getLocalizedMessage());
    }

    private void enableComp(Field c) throws IllegalAccessException {
        ((Component)c.get(this)).setEnabled(true);
    }

    private final boolean foo01(StandardButtons pressed, StandardButtons... interested) {
        if(pressed == null) return true;
        if(interested.length == 0) return false;
        for(StandardButtons b : interested) if(pressed == b) return false;
        return true;
    }

    private void handleArchARMxxCheckBoxStateChange() {
        if(archARM32CheckBox.isSelected() || archARM64CheckBox.isSelected()) {
            Map<String, Object> map = cache.get("archButtonGroup");
            restoreFieldValue(map, archX32_64CheckBox, "archX32_64CheckBox");
        }
        else {
            Map<String, Object> map = cache.computeIfAbsent("archButtonGroup", k -> new TreeMap<>());
            saveFieldValue(map, archX32_64CheckBox, "archX32_64CheckBox", true);
        }
    }

    private void handleBuildClangCheckBoxStateChange() {
        if(buildClangCheckBox.isSelected()) {
            Map<String, Object> map = cache.get("buildClangCheckBox");
            restoreFieldValue(map, buildGoogleTestCheckBox, "buildGoogleTestCheckBox");
            restoreFieldValue(map, archX32_64CheckBox, "archX32_64CheckBox");
            restoreFieldValue(map, archARM32CheckBox, "archARM32CheckBox");
            restoreFieldValue(map, archARM64CheckBox, "archARM64CheckBox");
            handleArchARMxxCheckBoxStateChange();
        }
        else {
            Map<String, Object> map = cache.computeIfAbsent("buildClangCheckBox", k -> new TreeMap<>());
            saveFieldValue(map, buildGoogleTestCheckBox, "buildGoogleTestCheckBox", false);
            saveFieldValue(map, archX32_64CheckBox, "archX32_64CheckBox", false);
            saveFieldValue(map, archARM32CheckBox, "archARM32CheckBox", false);
            saveFieldValue(map, archARM64CheckBox, "archARM64CheckBox", false);
        }
    }

    private void handleCustomLibNameCheckBoxStateChange() {
        if(customLibNameCheckBox.isSelected()) {
            Map<String, Object> map = cache.get("customLibNameCheckBox");
            restoreFieldValue(map, customLibNameValue, "customLibNameValue", "objc2");
        }
        else {
            Map<String, Object> map = cache.computeIfAbsent("customLibNameCheckBox", k -> new TreeMap<>());
            saveFieldValue(map, customLibNameValue, "customLibNameValue", "objc");
        }
    }

    private void handleInstallPrerequisiteSoftwareCheckBoxStateChange() {
        if(installPrerequisiteSoftwareCheckBox.isSelected()) {
            Map<String, Object> map = cache.get("installPrerequisiteSoftwareCheckBox");
            restoreFieldValue(map, installLibkqueueCheckBox, "installLibkqueueCheckBox");
        }
        else {
            Map<String, Object> map = cache.computeIfAbsent("installPrerequisiteSoftwareCheckBox", k -> new TreeMap<>());
            saveFieldValue(map, installLibkqueueCheckBox, "installLibkqueueCheckBox", false);
        }
    }

    private void handleRtVer1_8RadioButtonStateChange() {
        if(rtVer1_8RadioButton.isSelected()) {
            Map<String, Object> map = cache.computeIfAbsent("runtimeVersionButtonGroup", k -> new TreeMap<>());
            saveFieldValue(map, nonFragileABICheckBox, "nonFragileABICheckBox", false);
        }
        else {
            Map<String, Object> map = cache.get("runtimeVersionButtonGroup");
            restoreFieldValue(map, nonFragileABICheckBox, "nonFragileABICheckBox");
        }
    }

    private void restoreFieldValue(Map<String, Object> map, JCheckBox checkBox, String fieldName) {
        checkBox.setEnabled(true);
        if(map != null) {
            Boolean storedValue  = (Boolean)map.get(fieldName);
            boolean defaultValue = checkBox.isSelected();
            checkBox.setSelected(Tools.ifNull(storedValue, defaultValue));
            map.remove(fieldName);
        }
    }

    @SuppressWarnings("SameParameterValue")
    private void restoreFieldValue(Map<String, Object> map, JTextField textField, String fieldName, String defaultState) {
        textField.setEnabled(true);
        if(map != null) {
            textField.setText(Objects.toString(map.get(fieldName), defaultState));
            map.remove(fieldName);
        }
    }

    @SuppressWarnings("SameParameterValue")
    private void saveFieldValue(Map<String, Object> map, JTextField textField, String fieldName, String disabledState) {
        String value = textField.getText();
        if(value != null) map.put(fieldName, value);
        textField.setText(disabledState);
        textField.setEnabled(false);
    }

    private void saveFieldValue(Map<String, Object> map, JCheckBox checkBox, String fieldName, boolean disabledState) {
        map.put(fieldName, checkBox.isSelected());
        checkBox.setSelected(disabledState);
        checkBox.setEnabled(false);
    }

    public void showHelp() { /* TODO: Open up the help page. */ }

    private final class ButtonHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Object src = e.getSource();
            if(src == startButton) publishButtonEvent(StandardButtons.START);
            else if(src == cancelButton) publishButtonEvent(StandardButtons.CANCEL);
            else if(src == helpButton) showHelp();
            else if(src == installDirFindButton) Tools.doFindDialog(installDirField, Tools.makeRelToHomeDir(DEFAULT_INSTALL_DIR), getRootPanel());
            else if(src == workingDirFindButton) Tools.doFindDialog(workingDirField, Tools.makeRelToHomeDir(DEFAULT_WORKING_DIR), getRootPanel());
        }

    }

}
