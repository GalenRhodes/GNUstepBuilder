package com.galenrhodes.gnustep.builder.common;

import com.galenrhodes.gnustep.builder.common.annotations.PublishJButtonActions;
import com.galenrhodes.gnustep.builder.common.events.EventEngine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ExecutorService;

public abstract class PanelBase {

    protected final Map<String, Map<String, Object>> cache = new TreeMap<>();

    private final String                     _lock_    = "";
    private final Set<ButtonPressedListener> btnLstnrs = new LinkedHashSet<>();
    private       StandardButtons            pressedButton;
    private       JFrame                     frame;

    protected PanelBase() {
        super();
    }

    public final void addButtonListener(ButtonPressedListener listener) {
        synchronized(btnLstnrs) {
            btnLstnrs.add(listener);
        }
    }

    public abstract String getFrameTitle();

    public abstract JPanel getRootPanel();

    public void hide() {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() { if(frame != null) frame.setVisible(false); }
            });
        }
        catch(Exception e) {
            throw new RuntimeException("Unable to hide panel.", e);
        }
    }

    public final ButtonPressedListener removeButtonListener(ButtonPressedListener listener) {
        synchronized(btnLstnrs) {
            return (btnLstnrs.remove(listener) ? listener : null);
        }
    }

    public final StandardButtons resetPressedButton() {
        StandardButtons button = null;
        synchronized(_lock_) {
            if((button = pressedButton) != null) {
                pressedButton = null;
                _lock_.notify();
            }
        }
        return button;
    }

    /**
     * Setup the panel. If you override this in the subclasses then you much call this method in the superclass.
     */
    public void setup() {
        for(Field f : getClass().getDeclaredFields()) {
            try {
                if(JButton.class.isAssignableFrom(f.getType())) {
                    PublishJButtonActions ann = f.getAnnotation(PublishJButtonActions.class);

                    if(ann != null) {
                        try {
                            boolean hidden = (!Modifier.isPublic(f.getModifiers()) || !Modifier.isPublic(f.getDeclaringClass().getModifiers()));
                            if(hidden) f.setAccessible(true);
                            ((JButton)f.get(this)).addActionListener(new ButtonActionListener(ann.buttonEnum()));
                        }
                        catch(IllegalAccessException e) {
                            System.err.printf("Cannot add listener for button %s: %s\n", f.getName(), e.getLocalizedMessage());
                        }
                    }
                }
            }
            catch(IllegalArgumentException e) {
                System.err.printf("Cannot access field: %s\n", e.getLocalizedMessage());
            }
        }
    }

    public void show() {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    frame = new JFrame(getFrameTitle());
                    frame.getContentPane().add(getRootPanel());
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.pack();
                    frame.setResizable(false);
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                }
            });
        }
        catch(Exception e) {
            throw new RuntimeException("Unable to create frame for panel.", e);
        }
    }

    public final StandardButtons waitForButton(StandardButtons... buttons) throws InterruptedException {
        synchronized(_lock_) {
            try {
                while(foo01(pressedButton, buttons)) _lock_.wait();
                return pressedButton;
            }
            finally {
                _lock_.notify();
            }
        }
    }

    public final void waitForButtonReset() throws InterruptedException {
        synchronized(_lock_) { try { while(pressedButton != null) _lock_.wait(); } finally { _lock_.notify(); }}
    }

    protected void enableEverything() {
        Class<JComponent> cc = JComponent.class;
        for(Field c : getClass().getDeclaredFields()) {
            if(cc.isAssignableFrom(c.getType())) {
                try { enableComp(c); } catch(Exception e) { printEx(e); }
            }
        }
    }

    protected void publishButtonEvent(StandardButtons button) {
        ExecutorService exec = EventEngine.getExecutor();
        exec.submit(new Runnable() {
            @Override
            public void run() {
                synchronized(_lock_) {
                    pressedButton = button;
                    _lock_.notify();
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

    protected void restoreFieldValue(Map<String, Object> map, JCheckBox checkBox, String fieldName) {
        checkBox.setEnabled(true);
        if(map != null) {
            Boolean storedValue  = (Boolean)map.get(fieldName);
            boolean defaultValue = checkBox.isSelected();
            checkBox.setSelected(Tools.ifNull(storedValue, defaultValue));
            map.remove(fieldName);
        }
    }

    @SuppressWarnings("SameParameterValue")
    protected void restoreFieldValue(Map<String, Object> map, JTextField textField, String fieldName, String defaultState) {
        textField.setEnabled(true);
        if(map != null) {
            textField.setText(Objects.toString(map.get(fieldName), defaultState));
            map.remove(fieldName);
        }
    }

    @SuppressWarnings("SameParameterValue")
    protected void saveFieldValue(Map<String, Object> map, JTextField textField, String fieldName, String disabledState) {
        String value = textField.getText();
        if(value != null) map.put(fieldName, value);
        textField.setText(disabledState);
        textField.setEnabled(false);
    }

    protected void saveFieldValue(Map<String, Object> map, JCheckBox checkBox, String fieldName, boolean disabledState) {
        map.put(fieldName, checkBox.isSelected());
        checkBox.setSelected(disabledState);
        checkBox.setEnabled(false);
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

    private void printEx(Exception e) {
        System.err.printf("ERROR: %s\n", e.getLocalizedMessage());
    }

    public static final <T extends PanelBase> T getInstance(Class<T> cls) {
        try {
            final T obj = cls.newInstance();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() { obj.setup(); }
            });
            return obj;
        }
        catch(InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Could not create instance of panel.", e);
        }
    }

    private final class ButtonActionListener implements ActionListener {

        private StandardButtons button;

        private ButtonActionListener(StandardButtons button) {
            this.button = button;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            publishButtonEvent(button);
        }

    }

}
