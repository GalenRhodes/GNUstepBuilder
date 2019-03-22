package com.galenrhodes.gnustep;

import com.galenrhodes.gnustep.builder.Builder;
import com.galenrhodes.gnustep.builder.GNUstepBuilder;
import com.galenrhodes.gnustep.options.BuildOptions;
import com.galenrhodes.gnustep.options.GNUstepOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

    private static final Logger log = LogManager.getLogger(Main.class);

    private BuildOptions    buildOptions;
    private GNUstepOptions  optionsWindow;
    private GNUstepBuilder  builderWindow;
    private Future<Builder> ftask;
    private ExecutorService executor;
    private JFrame          builderFrame;
    private JFrame          optionsFrame;
    private String[]        args;

    private Main() {
        super();
        executor = Executors.newCachedThreadPool();
        args = new String[0];
    }

    public JFrame getBuilderFrame() {
        return builderFrame;
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public void openBuilderWindow() {
        builderWindow = new GNUstepBuilder();
        builderFrame = new JFrame("GNUstep Builder - Building...");
        builderFrame.pack();
        builderFrame.setSize(800, 600);
        builderFrame.setResizable(false);
        builderWindow.addToFrame(builderFrame);
        builderFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        builderFrame.setLocationRelativeTo(null);
        builderFrame.setVisible(true);
    }

    private void openOptionsWindow() {
        optionsFrame = new JFrame("GNUstep Builder - Options");
        buildOptions = new BuildOptions();
        optionsWindow = new GNUstepOptions();

        optionsWindow.setData(buildOptions);
        optionsFrame.getContentPane().add(optionsWindow.getGNUstepOptionsPanel());
        optionsFrame.pack();
        optionsFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        optionsFrame.setResizable(false);
        optionsFrame.setLocationRelativeTo(null);
        optionsFrame.setVisible(true);

        optionsWindow.getGNUstepStartButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                optionsWindow.getData(buildOptions);
                openBuilderWindow();
                optionsFrame.setVisible(false);
                ftask = executor.submit(new Builder(builderWindow, buildOptions));
            }
        });
    }

    public static Main getInstance() {
        return SingHold.INSTANCE;
    }

    public static void main(String[] args) {
        setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); // "javax.swing.plaf.nimbus.NimbusLookAndFeel"
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() { Main.getInstance(args).openOptionsWindow(); }
        });
    }

    private static void setLookAndFeel(String lookAndFeelClassName) {
        try { UIManager.setLookAndFeel(lookAndFeelClassName); }
        catch(Exception e) { try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch(Exception ignored) {} }
    }

    private static Main getInstance(String[] args) {
        Main m = getInstance();
        m.args = args;
        return m;
    }

    private static final class SingHold {

        private static final Main INSTANCE = new Main();

    }

}
