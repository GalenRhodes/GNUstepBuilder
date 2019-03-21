package com.galenrhodes.gnustep;

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
    private Future<Integer> ftask;
    private ExecutorService executor;
    private JFrame          builderFrame;
    private JFrame          optionsFrame;
    private String[]        args;

    private Main() {
        super();
        executor = Executors.newCachedThreadPool();
        log.debug("Hello World!");
    }

    public void openBuilderWindow() {
        builderWindow = new GNUstepBuilder();
        builderFrame = new JFrame("GNUstep Builder");
        builderFrame.pack();
        builderFrame.setSize(800, 600);
        builderFrame.setResizable(false);
        builderFrame.getContentPane().add(builderWindow.getBuilderStatus());
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
        try {
            /* UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel"); */
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception e) {
            e.printStackTrace();
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch(Exception ignored) {}
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() { Main.getInstance(args).openOptionsWindow();}
        });
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
