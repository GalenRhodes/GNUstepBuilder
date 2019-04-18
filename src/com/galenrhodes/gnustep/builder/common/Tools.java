package com.galenrhodes.gnustep.builder.common;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class Tools {

    public static final String USER_HOME_PROPKEY = "user.home";
    public static final String USER_HOME         = System.getProperty(USER_HOME_PROPKEY, "");

    private Tools() {}

    public static final boolean contains(String value, String... list) {
        for(String s : list) if(s.equals(value)) return true;
        return false;
    }

    public static final boolean containsAll(String master, String delimiterPattern, Object... values) {
        String[] arMaster = master.split(delimiterPattern);
        for(Object o : values) if((o != null) && !contains(o.toString(), arMaster)) return false;
        return true;
    }

    public static final void doFindDialog(JTextField textField, String defaultDir, Component parent) {
        textField.setText(doFindDialog(textField.getText(), defaultDir, parent));
    }

    public static final String doFindDialog(String dir, String defaultDir, Component parent) {
        return _doFindPathDialog(fixFilePath((z(dir) ? defaultDir : dir), true), parent);
    }

    public static final String fixFilePath(String filePath, boolean isDirectory) {
        filePath = filePath.trim().replace('\\', '/');
        if(filePath.equals("~")) filePath = USER_HOME;
        else if(filePath.startsWith("~/")) filePath = (USER_HOME + filePath.substring(1));
        boolean ews = filePath.endsWith("/");
        return ((isDirectory) ? (ews ? filePath : (filePath + "/")) : (ews ? filePath.substring(0, (filePath.length() - 1)) : filePath)).replace('/', File.separatorChar);
    }

    public static final JFrame getFrame(Component c) {
        if(c == null) return null;

        while(!(c instanceof JFrame)) {
            c = c.getParent();
            if(c == null) return null;
        }

        return (JFrame)c;
    }

    public static final String getSelected(JComboBox<String> cb) {
        return (cb.getSelectedIndex() == -1 ? null : Objects.requireNonNull(cb.getSelectedItem()).toString());
    }

    public static final <T> T ifNull(T obj, T defaultValue) { return ((obj == null) ? defaultValue : obj); }

    public static final <T> int indexOfDropDownItem(JComboBox<T> cb, T srch) {
        for(int x = 0, y = cb.getItemCount(); x < y; x++) if(cb.getItemAt(x).equals(srch)) return x;
        return -1;
    }

    public static final boolean isModified(JToggleButton cb, boolean fieldValue) {
        return (cb.isSelected() != fieldValue);
    }

    public static final boolean isModified(JTextComponent tc, String fieldValue) {
        return !Objects.equals(tc.getText(), fieldValue);
    }

    public static final boolean isModified(JComboBox<String> cb, String fieldValue) {
        if(cb.getSelectedIndex() == -1) return (fieldValue != null);
        return !Objects.equals(cb.getSelectedItem(), fieldValue);
    }

    public static final String makeRelToHomeDir(String defDir) {
        return (System.getProperty(USER_HOME_PROPKEY) + defDir).replace('/', File.separatorChar);
    }

    public static final void setSelected(JComboBox<String> cb, String fieldValue) {
        if(fieldValue == null) cb.setSelectedIndex(-1);
        else cb.setSelectedIndex(indexOfDropDownItem(cb, fieldValue.trim()));
    }

    public static final <T> java.util.List<Future<T>> submitAll(ExecutorService exsvc, Collection<Callable<T>> c) {
        List<Future<T>> f = new ArrayList<>();
        for(Callable<T> clb : c) f.add(exsvc.submit(clb));
        return f;
    }

    public static boolean z(String dir) {
        return ((dir == null) || (dir.trim().length() == 0));
    }

    private static final String _doFindPathDialog(String dir, Component parent) {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setSelectedFile(new File(dir));
        return ((fc.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) ? foo(fc.getSelectedFile(), dir) : dir);
    }

    private static final String foo(File f, String def) {
        return ((f == null) ? def : ((f.isDirectory() || !f.exists()) ? f.getAbsolutePath() : def));
    }

}
