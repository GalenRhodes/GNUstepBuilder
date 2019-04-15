package com.galenrhodes.gnustep.builder;

import com.galenrhodes.gnustep.builder.common.StandardButtons;
import com.galenrhodes.gnustep.builder.common.SwingInvoker;
import com.galenrhodes.gnustep.builder.common.Tools;
import com.galenrhodes.gnustep.builder.options.GNUstepBuildOptions;
import com.galenrhodes.gnustep.builder.options.ui.GNUstepOptionsPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.xml.bind.JAXBContext;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private static final Logger log = LogManager.getLogger(Main.class);

    private ExecutorService     executor = Executors.newCachedThreadPool();
    private String[]            args     = {};
    private GNUstepBuildOptions options  = null;

    private Main() {
        super();
        try {
            JAXBContext ctx = JAXBContext.newInstance(GNUstepBuildOptions.class);
            options = (GNUstepBuildOptions)ctx.createUnmarshaller().unmarshal(this.getClass().getResourceAsStream("GNUstepOptionsDefaults.xml"));
        }
        catch(Exception e) {
            throw new RuntimeException("Cannot load options file.", e);
        }
    }

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
                JFrame              frame        = new JFrame("GNUstep Builder");

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
