package com.galenrhodes.gnustep.builder.common;

import javax.swing.*;
import java.util.concurrent.Callable;

public class SwingInvoker<T> {

    private T         _returnValue;
    private Exception _exception;

    private SwingInvoker() {
        _returnValue = null;
        _exception = null;
    }

    public static final <X> X invokeAndWait(Callable<X> callable) throws Exception {
        SwingInvoker<X> i = new SwingInvoker<X>();
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() { try { i._returnValue = callable.call(); } catch(Exception e) { i._exception = e; }}
        });
        if(i._exception != null) throw i._exception;
        return i._returnValue;
    }

}
