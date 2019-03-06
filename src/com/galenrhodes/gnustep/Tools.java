package com.galenrhodes.gnustep;

import com.galenrhodes.gnustep.processes.ProcessCmd;

import javax.swing.*;

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

    public static final <T> int indexOfDropDownItem(JComboBox<T> cb, T srch) {
        for(int x = 0, y = cb.getItemCount(); x < y; x++) {
            if(cb.getItemAt(x).equals(srch)) {
                return x;
            }
        }

        return -1;
    }

    public static void runFind() {
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

}
