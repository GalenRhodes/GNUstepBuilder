package com.galenrhodes.gnustep;

import com.galenrhodes.gnustep.GNUstepBuilder.BuilderButton;
import com.galenrhodes.gnustep.processes.ProcessCmd;
import com.galenrhodes.gnustep.processes.ProcessEvent;
import com.galenrhodes.gnustep.processes.ProcessListener;

import javax.swing.*;
import java.io.File;
import java.util.concurrent.Callable;

public class Builder implements Callable<Integer>, ProcessListener {

    private GNUstepBuilder builderWindow;
    private BuildOptions   buildOptions;

    public Builder(GNUstepBuilder builderWindow, BuildOptions buildOptions) {
        super();
        this.builderWindow = builderWindow;
        this.buildOptions = buildOptions;
    }

    @Override
    public Integer call() throws Exception {
        File workingPath = new File(buildOptions.getWorkingPath());
        //noinspection ResultOfMethodCallIgnored
        workingPath.mkdirs();

        builderWindow.setProgress(0);
        ProcessCmd proc = new ProcessCmd("git", workingPath, "clone", "https://github.com/nickhutchinson/libdispatch.git", "libdispatch");
        proc.addProcessEventListener(this);
        /*@f:0*/
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() { builderWindow.getCancelButton().setEnabled(false); }
        }); /*@f:1*/
        proc.start();
        proc.waitFor();
        builderWindow.setProgress(10);

        BuilderButton button = builderWindow.waitForEitherButton();
        System.out.printf("\nButton Pressed: %s\n", button);
        System.exit(0);
        return 0;
    }

    @Override
    public void processCompleted(ProcessEvent event) {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() { builderWindow.getCancelButton().setEnabled(true); }
            });
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stderrLineReceived(ProcessEvent event) {
        builderWindow.addStatusText(event.getReceivedLine() + "\n");
    }

    @Override
    public void stdoutLineReceived(ProcessEvent event) {
        builderWindow.addStatusText(event.getReceivedLine() + "\n");
    }

}
