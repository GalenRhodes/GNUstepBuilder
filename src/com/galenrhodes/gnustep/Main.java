package com.galenrhodes.gnustep;

import javax.swing.*;

public class Main implements Runnable {

    private BuildOptions buildOptions;

    public Main(String[] args) {
        super();
        buildOptions = new BuildOptions();
    }

    @Override
    public void run() {
        JFrame         mainFrame = new JFrame("GNUstep Builder");
        GNUstepOptions options   = new GNUstepOptions();

        options.setData(buildOptions);

        mainFrame.getContentPane().add(options.getGNUstepOptionsPanel());
        mainFrame.pack();
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setResizable(false);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        }
        catch(Exception e) {
            e.printStackTrace();
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch(Exception ignored) {}
        }
        SwingUtilities.invokeLater(new Main(args));
    }

}
