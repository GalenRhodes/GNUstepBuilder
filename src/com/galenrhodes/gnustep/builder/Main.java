package com.galenrhodes.gnustep.builder;

import com.galenrhodes.gnustep.builder.common.*;
import com.galenrhodes.gnustep.builder.common.events.EventEngine;
import com.galenrhodes.gnustep.builder.options.GNUstepBuildOptions;
import com.galenrhodes.gnustep.builder.options.ui.GNUstepOptionsPanel;
import com.galenrhodes.gnustep.builder.progress.Builder;
import com.galenrhodes.gnustep.builder.progress.gui.GNUstepBuilderProgressPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.xml.bind.JAXBContext;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.Callable;

public class Main extends MainBase implements Runnable {

    private static final Logger log = LogManager.getLogger(Main.class);

    private final GNUstepBuildOptions options;
    private       String[]            args = {};

    private Main() {
        super();
        options = loadOptions();
    }

    @Override
    public void run() {
        try {
            if(openOptionsPanelAndWaitForButton() == StandardButtons.CANCEL) System.exit(0);
            if(openBuilderPanelAndWaitForButton() == StandardButtons.CANCEL) System.exit(0);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private GNUstepBuildOptions loadOptions() {
        try {
            JAXBContext ctx   = JAXBContext.newInstance(GNUstepBuildOptions.class);
            InputStream instr = Main.class.getResourceAsStream(getProperty("gnustep.bldr.default.options.file", "options/data/GNUstepOptionsDefaults.xml"));
            return (GNUstepBuildOptions)ctx.createUnmarshaller().unmarshal(instr);
        }
        catch(Exception e) {
            throw new RuntimeException(L.getString("error.cannot_load_options_file"), e);
        }
    }

    private GNUstepBuilderProgressPanel openBuilderPanel() throws Exception {
        GNUstepBuilderProgressPanel panel = SwingInvoker.invokeAndWait(new Callable<GNUstepBuilderProgressPanel>() {
            @Override
            public GNUstepBuilderProgressPanel call() throws Exception {
                GNUstepBuilderProgressPanel builderPanel = PanelBase.getInstance(GNUstepBuilderProgressPanel.class);
                builderPanel.show();
                return builderPanel;
            }
        });

        EventEngine.getExecutor().submit(new Builder(panel));
        return panel;
    }

    private StandardButtons openBuilderPanelAndWaitForButton() throws Exception {
        GNUstepBuilderProgressPanel builderPanel = openBuilderPanel();
        StandardButtons             button       = builderPanel.waitForButton();
        Tools.getFrame(builderPanel.getRootPanel()).setVisible(false);
        return button;
    }

    private GNUstepOptionsPanel openOptionsPanel() throws Exception {
        return SwingInvoker.invokeAndWait(new Callable<GNUstepOptionsPanel>() {
            @Override
            public GNUstepOptionsPanel call() throws Exception {
                GNUstepOptionsPanel optionsPanel = PanelBase.getInstance(GNUstepOptionsPanel.class);
                optionsPanel.show();
                optionsPanel.setData(options);
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

    public static final GNUstepBuildOptions getOptions() {
        return getInstance().options;
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

    private static final class SingHold {

        private static final Main INSTANCE = new Main();

    }

}
