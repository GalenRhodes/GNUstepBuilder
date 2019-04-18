package com.galenrhodes.gnustep.builder.common;

import javax.swing.*;
import java.util.Properties;

public class MainBase {

    protected final Properties properties;

    public MainBase() {
        super();
        try {
            properties = new Properties();
            properties.loadFromXML(this.getClass().getResourceAsStream("/com/galenrhodes/gnustep/GNUstepBuilder.xml"));
        }
        catch(Exception e) {
            throw new RuntimeException(L.getString("error.cannot_load_properties_file"), e);
        }
    }

    public boolean getBooleanProperty(String key, boolean defaultValue) {
        try { return Boolean.parseBoolean(getProperty(key, (defaultValue ? "true" : "false"))); } catch(Exception e) { return defaultValue; }
    }

    public boolean getBooleanProperty(String key) { return getBooleanProperty(key, false); }

    public byte getByteProperty(String key, byte defaultValue) {
        try { return Byte.parseByte(getProperty(key, String.valueOf(defaultValue))); } catch(Exception e) { return defaultValue; }
    }

    public byte getByteProperty(String key) { return getByteProperty(key, (byte)0); }

    public double getDoubleProperty(String key, double defaultValue) {
        try { return Double.parseDouble(getProperty(key, String.valueOf(defaultValue))); } catch(Exception e) { return defaultValue; }
    }

    public double getDoubleProperty(String key) { return getDoubleProperty(key, 0.0); }

    public float getFloatProperty(String key, float defaultValue) {
        try { return Float.parseFloat(getProperty(key, String.valueOf(defaultValue))); } catch(Exception e) { return defaultValue; }
    }

    public float getFloatProperty(String key) { return getFloatProperty(key, 0.0f); }

    public int getIntProperty(String key, int defaultValue) {
        try { return Integer.parseInt(getProperty(key, String.valueOf(defaultValue))); } catch(Exception e) { return defaultValue; }
    }

    public int getIntProperty(String key) { return getIntProperty(key, 0); }

    public long getLongProperty(String key, long defaultValue) {
        try { return Long.parseLong(getProperty(key, String.valueOf(defaultValue))); } catch(Exception e) { return defaultValue; }
    }

    public long getLongProperty(String key) { return getLongProperty(key, 0L); }

    public Properties getProperties()       { return properties; }

    public String getProperty(String key, String defaultValue) {
        return getProperties().getProperty(key, defaultValue);
    }

    public String getProperty(String key) {
        return getProperties().getProperty(key);
    }

    public short getShortProperty(String key, short defaultValue) {
        try { return Short.parseShort(getProperty(key, String.valueOf(defaultValue))); } catch(Exception e) { return defaultValue; }
    }

    public short getShortProperty(String key) { return getShortProperty(key, (short)0); }

    protected static void setLookAndFeel(String lookAndFeelClassName) {
        try { UIManager.setLookAndFeel(lookAndFeelClassName); }
        catch(Exception e) { try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch(Exception ignored) {}}
    }

}
