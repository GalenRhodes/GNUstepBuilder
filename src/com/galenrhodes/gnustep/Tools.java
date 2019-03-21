package com.galenrhodes.gnustep;

import com.galenrhodes.gnustep.processes.ProcessCmd;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Tools {

    private Tools() {}

    public static final boolean contains(String value, String... list) {
        for(String s : list) {
            if(s.equals(value)) {
                return true;
            }
        }

        return false;
    }

    public static final boolean containsAll(String master, String delimiterPattern, Object... values) {
        String[] arMaster = master.split(delimiterPattern);

        for(Object o : values) {
            if((o != null) && !contains(o.toString(), arMaster)) return false;
        }

        return true;
    }

    public static final void doFindDialog(JTextField textField, String defaultDir, Component parent) {
        textField.setText(doFindDialog(textField.getText(), defaultDir, parent));
    }

    public static final String doFindDialog(String dir, String defaultDir, Component parent) {
        if(dir == null || dir.trim().length() == 0) dir = defaultDir;

        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setSelectedFile(new File(dir.trim()));

        return ((fc.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) ? foo(fc.getSelectedFile(), dir) : dir);
    }

    public static final void runFind() {
        try {
            ProcessCmd pb = new ProcessCmd("find", "/usr", "-name", "*.h");
            int        rc = pb.waitFor();

            System.out.println(pb.getOutput());
            System.err.println(pb.getError());

            System.out.printf("\nProgram exited with return code: %d\n", rc);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static final <T> int indexOfDropDownItem(JComboBox<T> cb, T srch) {
        for(int x = 0, y = cb.getItemCount(); x < y; x++) {
            if(cb.getItemAt(x).equals(srch)) {
                return x;
            }
        }

        return -1;
    }

    private static final String foo(File f, String def) {
        return ((f == null) ? def : ((f.isDirectory() || !f.exists()) ? f.getAbsolutePath() : def));
    }

}
