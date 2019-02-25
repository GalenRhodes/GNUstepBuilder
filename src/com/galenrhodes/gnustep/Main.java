package com.galenrhodes.gnustep;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main implements Runnable, Callable<Integer> {

    private BuildOptions    buildOptions;
    private GNUstepOptions  options;
    private GNUstepBuilder  builder;
    private Future<Integer> ftask;
    private ExecutorService executor;
    private JFrame          builderFrame;
    private JFrame          optionsFrame;
    private String[]        args;

    private Main() {
        super();
        executor = Executors.newCachedThreadPool();
    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     *
     * @throws Exception if unable to compute a result
     */
    @Override
    public Integer call() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                openBuilderWindow();
            }
        });

        int b = waitForButton();
        builder.resetButtons();
        System.out.printf("\n%s Button Pressed.\n", ((b == 1) ? "OK" : ((b == 2) ? "Cancel" : "No")));
        System.exit(0);
        return 0;
    }

    public void openBuilderWindow() {
        builder = new GNUstepBuilder();
        builderFrame = new JFrame("GNUstep Builder");
        builderFrame.pack();
        builderFrame.setSize(800, 600);
        builderFrame.setResizable(false);
        builderFrame.getContentPane().add(builder.getBuilderStatus());
        builderFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        builderFrame.setLocationRelativeTo(null);
        builderFrame.setVisible(true);
    }

    @Override
    public void run() {
        optionsFrame = new JFrame("GNUstep Builder");
        buildOptions = new BuildOptions();
        options = new GNUstepOptions();

        options.setData(buildOptions);
        optionsFrame.getContentPane().add(options.getGNUstepOptionsPanel());
        optionsFrame.pack();
        optionsFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        optionsFrame.setResizable(false);
        optionsFrame.setLocationRelativeTo(null);
        optionsFrame.setVisible(true);

        options.getGNUstepStartButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                options.getData(buildOptions);
                ftask = executor.submit((Callable<Integer>)Main.this);
                optionsFrame.setVisible(false);
            }
        });
    }

    private int waitForButton() {
        int button = 0;

        synchronized(Main.getInstance()) {
            while(!(builder.isOkPushed() || builder.isCancelPushed())) {
                try { Main.getInstance().wait(); }
                catch(Exception ignored) {}
            }

            button = (builder.isOkPushed() ? 1 : (builder.isCancelPushed() ? 2 : 0));
        }

        return button;
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

        SwingUtilities.invokeLater(Main.getInstance(args));
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
