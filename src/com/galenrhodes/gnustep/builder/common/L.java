package com.galenrhodes.gnustep.builder.common;

import java.util.Objects;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class L {

    private static final ResourceBundle bundle  = ResourceBundle.getBundle("com/galenrhodes/gnustep/localization");
    private static final Pattern        pattern = Pattern.compile("\\$\\{([^}]+)}");

    private L() {}

    public static final String getFormatted(final String fmtKey, final Object... args) {
        final String fmt = _getString(fmtKey);
        return ((fmt == null) ? fmt : _getFormatted(fmt, args));
    }

    public static final String getString(final String key) {
        return _getString(key);
    }

    public static final int getInteger(final String key) { return getInteger(key, 0); }

    public static final int getInteger(final String key, int defaultValue) {
        String str = getString(key);
        if(str == null || str.trim().length() == 0) return defaultValue;
        try { return Integer.parseInt(str); } catch(Exception e) { return defaultValue; }
    }

    public static final Object lit(String str) {
        return new Literal(str);
    }

    private static final String _getFormatted(String fmt, Object... args) {
        final Object[] _args = new Object[args.length];

        for(int i = 0; i < args.length; i++) {
            Object arg = args[i];
            _args[i] = ((arg == null) ? null : ((arg instanceof CharSequence) ? _getString((CharSequence)arg) : arg.toString()));
        }

        return String.format(fmt, _args);
    }

    private static final String _getString(final CharSequence key) {
        final String str = (bundle.containsKey(key.toString()) ? bundle.getString(key.toString()) : null);

        if(str != null) {
            Matcher m = getMatcher(str);

            if(m.find()) {
                StringBuffer sb = new StringBuffer();

                do {
                    String skey = m.group(1);
                    String sval = _getString(skey);
                    m.appendReplacement(sb, ((sval == null) ? Matcher.quoteReplacement("${" + skey + "}") : sval));
                }
                while(m.find());

                m.appendTail(sb);
                return sb.toString();
            }
        }

        return str;
    }

    private static final Matcher getMatcher(String str) {
        synchronized(pattern) {
            return pattern.matcher(str);
        }
    }

    private static final class Literal implements Comparable<Literal> {

        private final String lit;

        private Literal(String str) { lit = str; }

        @Override
        public int compareTo(Literal o) {
            //noinspection StringEquality
            return ((o == null) ? 1 : ((lit == o.lit) ? 0 : ((lit == null) ? -1 : ((o.lit == null) ? 1 : lit.compareTo(o.lit)))));
        }

        @Override
        public boolean equals(Object obj) { return ((obj != null) && ((this == obj) || ((obj instanceof Literal) && (Objects.equals(lit, ((Literal)obj).lit))))); }

        @Override
        public int hashCode() { return ((lit == null) ? 0 : lit.hashCode()); }

        public String toString() { return lit; }

    }

}
