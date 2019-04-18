package com.galenrhodes.gnustep.builder.progress.gui;

import com.galenrhodes.gnustep.builder.Main;
import com.galenrhodes.gnustep.builder.common.L;
import com.galenrhodes.gnustep.builder.common.PanelBase;
import com.galenrhodes.gnustep.builder.common.StandardButtons;
import com.galenrhodes.gnustep.builder.common.annotations.PublishJButtonActions;
import com.galenrhodes.gnustep.builder.options.GNUstepBuildOptions;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public class GNUstepBuilderProgressPanel extends PanelBase {

    private final GNUstepBuildOptions options;

    private JPanel       builderProgress;
    @PublishJButtonActions(buttonEnum = StandardButtons.OK)
    private JButton      okButton;
    @PublishJButtonActions(buttonEnum = StandardButtons.CANCEL)
    private JButton      cancelButton;
    private JProgressBar progressBar;
    private JTextArea    textArea;
    private JLabel       statusText;

    private GNUstepBuilderProgressPanel(GNUstepBuildOptions options) {
        super();
        this.options = options;
    }

    private GNUstepBuilderProgressPanel() {
        this(Main.getOptions());
    }

    @Override
    public String getFrameTitle() {
        return L.getString("window.title.builder");
    }

    public GNUstepBuildOptions getOptions() { return options; }

    @Override
    public JPanel getRootPanel() {
        return builderProgress;
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
        builderProgress = new JPanel();
        builderProgress.setLayout(new GridLayoutManager(4, 1, new Insets(5, 5, 5, 5), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        builderProgress.add(panel1,
                            new GridConstraints(3,
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
        okButton = new JButton();
        Font okButtonFont = this.$$$getFont$$$(null, Font.BOLD, -1, okButton.getFont());
        if(okButtonFont != null) okButton.setFont(okButtonFont);
        okButton.setIcon(new ImageIcon(getClass().getResource("/images/accept.png")));
        this.$$$loadButtonText$$$(okButton, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("builder.button.ok.label"));
        okButton.setToolTipText(ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("builder.button.ok.tooltip"));
        panel1.add(okButton,
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
        this.$$$loadButtonText$$$(cancelButton, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("builder.button.cancel.label"));
        cancelButton.setToolTipText(ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("builder.button.cancel.tooltip"));
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
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1,
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
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        builderProgress.add(panel2,
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
        progressBar = new JProgressBar();
        Font progressBarFont = this.$$$getFont$$$(null, -1, 8, progressBar.getFont());
        if(progressBarFont != null) progressBar.setFont(progressBarFont);
        progressBar.setStringPainted(true);
        progressBar.setValue(0);
        panel2.add(progressBar,
                   new GridConstraints(0,
                                       1,
                                       1,
                                       1,
                                       GridConstraints.ANCHOR_CENTER,
                                       GridConstraints.FILL_HORIZONTAL,
                                       GridConstraints.SIZEPOLICY_WANT_GROW,
                                       GridConstraints.SIZEPOLICY_FIXED,
                                       null,
                                       null,
                                       null,
                                       0,
                                       false));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("builder.label.progress"));
        panel2.add(label1,
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
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        builderProgress.add(panel3,
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
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setRows(0);
        textArea.setTabSize(4);
        textArea.setText("");
        panel3.add(textArea,
                   new GridConstraints(0,
                                       0,
                                       1,
                                       1,
                                       GridConstraints.ANCHOR_CENTER,
                                       GridConstraints.FILL_BOTH,
                                       GridConstraints.SIZEPOLICY_WANT_GROW,
                                       GridConstraints.SIZEPOLICY_WANT_GROW,
                                       new Dimension(800, 600),
                                       new Dimension(800, 300),
                                       null,
                                       0,
                                       false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        builderProgress.add(panel4,
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
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("com/galenrhodes/gnustep/localization").getString("builder.label.status"));
        panel4.add(label2,
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
        statusText = new JLabel();
        statusText.setText("");
        panel4.add(statusText,
                   new GridConstraints(0,
                                       1,
                                       1,
                                       1,
                                       GridConstraints.ANCHOR_WEST,
                                       GridConstraints.FILL_NONE,
                                       GridConstraints.SIZEPOLICY_WANT_GROW,
                                       GridConstraints.SIZEPOLICY_FIXED,
                                       new Dimension(650, -1),
                                       new Dimension(650, -1),
                                       null,
                                       0,
                                       false));
        final Spacer spacer2 = new Spacer();
        panel4.add(spacer2,
                   new GridConstraints(0,
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
    }

    /**
     *
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if(currentFont == null) return null;
        String resultName;
        if(fontName == null) {resultName = currentFont.getName();}
        else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if(testFont.canDisplay('a') && testFont.canDisplay('1')) {resultName = fontName;}
            else {resultName = currentFont.getName();}
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
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
     *
     */
    public JComponent $$$getRootComponent$$$() { return builderProgress; }

}
