package com.galenrhodes.gnustep.builder;

import com.galenrhodes.gnustep.builder.common.L;
import com.galenrhodes.gnustep.builder.common.StandardButtons;
import com.galenrhodes.gnustep.builder.common.SwingInvoker;
import com.galenrhodes.gnustep.builder.common.Tools;
import com.galenrhodes.gnustep.builder.options.GNUstepBuildOptions;
import com.galenrhodes.gnustep.builder.options.ui.GNUstepOptionsPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.xml.bind.JAXBContext;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private static final Logger log = LogManager.getLogger(Main.class);

    private final GNUstepBuildOptions options;
    private final Properties          properties;
    private       ExecutorService     executor = Executors.newCachedThreadPool();
    private       String[]            args     = {};

    private Main() {
        super();
        properties = new Properties();
        try {
            properties.loadFromXML(this.getClass().getResourceAsStream("/com/galenrhodes/gnustep/GNUstepBuilder.xml"));

            JAXBContext ctx = JAXBContext.newInstance(GNUstepBuildOptions.class);
            InputStream instr = this.getClass().getResourceAsStream(getProperty("gnustep.bldr.default.options.file", "options/data/GNUstepOptionsDefaults.xml"));
            options = (GNUstepBuildOptions)ctx.createUnmarshaller().unmarshal(instr);
        }
        catch(Exception e) {
            throw new RuntimeException(L.getString("error.cannot_load_options_file"), e);
        }
    }

    public Properties getProperties() { return properties; }

    public String getProperty(String key, String defaultValue) {
        return getProperties().getProperty(key, defaultValue);
    }

    public String getProperty(String key) {
        return getProperties().getProperty(key);
    }

    public byte getByteProperty(String key, byte defaultValue) {
        try { return Byte.parseByte(getProperty(key, String.valueOf(defaultValue))); } catch(Exception e) { return defaultValue; }
    }

    public byte getByteProperty(String key) { return getByteProperty(key, (byte)0); }

    public short getShortProperty(String key, short defaultValue) {
        try { return Short.parseShort(getProperty(key, String.valueOf(defaultValue))); } catch(Exception e) { return defaultValue; }
    }

    public short getShortProperty(String key) { return getShortProperty(key, (short)0); }

    public int getIntProperty(String key, int defaultValue) {
        try { return Integer.parseInt(getProperty(key, String.valueOf(defaultValue))); } catch(Exception e) { return defaultValue; }
    }

    public int getIntProperty(String key) { return getIntProperty(key, 0); }

    public long getLongProperty(String key, long defaultValue) {
        try { return Long.parseLong(getProperty(key, String.valueOf(defaultValue))); } catch(Exception e) { return defaultValue; }
    }

    public long getLongProperty(String key) { return getLongProperty(key, 0L); }

    public float getFloatProperty(String key, float defaultValue) {
        try { return Float.parseFloat(getProperty(key, String.valueOf(defaultValue))); } catch(Exception e) { return defaultValue; }
    }

    public float getFloatProperty(String key) { return getFloatProperty(key, 0.0f); }

    public double getDoubleProperty(String key, double defaultValue) {
        try { return Double.parseDouble(getProperty(key, String.valueOf(defaultValue))); } catch(Exception e) { return defaultValue; }
    }

    public double getDoubleProperty(String key) { return getDoubleProperty(key, 0.0); }

    public boolean getBooleanProperty(String key, boolean defaultValue) {
        try { return Boolean.parseBoolean(getProperty(key, (defaultValue ? "true" : "false"))); } catch(Exception e) { return defaultValue; }
    }

    public boolean getBooleanProperty(String key) { return getBooleanProperty(key, false); }

    public void run() {
        try {
            if(openOptionsPanelAndWaitForButton() == StandardButtons.CANCEL) System.exit(0);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private GNUstepOptionsPanel openOptionsPanel() throws Exception {
        return SwingInvoker.invokeAndWait(new Callable<GNUstepOptionsPanel>() {
            @Override
            public GNUstepOptionsPanel call() throws Exception {
                GNUstepOptionsPanel optionsPanel = new GNUstepOptionsPanel();
                JFrame              frame        = new JFrame(L.getString("window.title.builder"));

                frame.getContentPane().add(optionsPanel.getRootPanel());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setResizable(false);
                frame.setLocationRelativeTo(null);
                optionsPanel.setData(options);
                frame.setVisible(true);
                return optionsPanel;
            }
        });
    }

    private StandardButtons openOptionsPanelAndWaitForButton() throws Exception {
        GNUstepOptionsPanel optionsPanel = openOptionsPanel();
        StandardButtons     button       = optionsPanel.waitForButton(StandardButtons.START, StandardButtons.CANCEL);

        Tools.getFrame(optionsPanel.getRootPanel()).setVisible(false);
        if(button == StandardButtons.START) SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() { optionsPanel.getData(options); }
        });
        return button;
    }

    public static Main getInstance() {
        return SingHold.INSTANCE;
    }

    public static void main(String[] args) {
        setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); // "javax.swing.plaf.nimbus.NimbusLookAndFeel"
        Main.getInstance(args).run();
    }

    private static Main getInstance(String[] args) {
        Main m = getInstance();
        m.args = Arrays.copyOf(args, args.length);
        return m;
    }

    private static void setLookAndFeel(String lookAndFeelClassName) {
        try { UIManager.setLookAndFeel(lookAndFeelClassName); }
        catch(Exception e) { try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch(Exception ignored) {}}
    }

    private static final class SingHold {

        private static final Main INSTANCE = new Main();

    }

}
