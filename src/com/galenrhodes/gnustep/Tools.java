package com.galenrhodes.gnustep;

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

}
