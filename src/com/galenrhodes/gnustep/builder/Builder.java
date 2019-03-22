package com.galenrhodes.gnustep.builder;

import com.galenrhodes.gnustep.common.L;
import com.galenrhodes.gnustep.common.processes.ProcessCmd;
import com.galenrhodes.gnustep.common.processes.ProcessEvent;
import com.galenrhodes.gnustep.common.processes.ProcessListener;
import com.galenrhodes.gnustep.options.BuildOptions;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;

public class Builder implements Callable<Builder>, ButtonPressedListener {

    private GNUstepBuilder builderWindow;
    private BuildOptions   buildOptions;
    private ProcessCmd     currentProc;

    public Builder(GNUstepBuilder builderWindow, BuildOptions buildOptions) {
        super();
        this.builderWindow = builderWindow;
        this.buildOptions = buildOptions;
    }

    @Override
    public void buttonPressed(BuilderButton button) {

    }

    @Override
    public Builder call() throws Exception {
        File workingPath = new File(buildOptions.getWorkingPath());
        //noinspection ResultOfMethodCallIgnored
        workingPath.mkdirs();
        int progress  = 0;
        int increment = 10;

        builderWindow.setProgress(progress);
        int res = downloadLibDispatch(workingPath);
        if(res == 0) builderWindow.setProgress(progress += increment);
        else return handleError();

        res = downloadLibObjc(workingPath);
        if(res == 0) builderWindow.setProgress(progress += increment);
        else return handleError();

        res = downloadGNUstepMake(workingPath, progress += increment, increment);
        if(res != 0) return handleError();

        res = downloadGNUstepCCommandLineFixer(workingPath);
        if(res == 0) builderWindow.setProgress(progress += increment);
        else return handleError();

        BuilderButton button = builderWindow.waitForAnyButton();
        System.out.printf("\nButton Pressed: %s\n", button);
        System.exit(0);
        return this;
    }

    protected int downloadGNUstepCCommandLineFixer(File workingPath) throws Exception {
        return launchProc(new ProcessCmd("git", workingPath, "clone", L.getFormatted("gnustep.github.url", "gnustep.cmdlinefixer"), "GNUstepCCommandLineFixer"));
    }

    protected int downloadGNUstepMake(File workingPath, int progStart, int progInc) throws Exception {
        String[] libs = {"make", "base", "gui", "back"};

        for(String lib : libs) {
            int res = launchProc(new ProcessCmd("git", workingPath, "clone", L.getFormatted("gnustep.github.url", "gnustep.gnustep." + lib), "core" + File.separatorChar + lib));
            if(res != 0) return res;
            builderWindow.setProgress(progStart);
            progStart += progInc;
        }

        return 0;
    }

    protected int downloadLibDispatch(File workingPath) throws Exception {
        String site = L.getFormatted("gnustep.github.url", (buildOptions.useSwiftLibDispatch() ? "gnustep.libdispatch.swift" : "gnustep.libdispatch.nick"));
        return launchProc(new ProcessCmd("git", workingPath, "clone", site, "libdispatch"));
    }

    protected int downloadLibObjc(File workingPath) throws Exception {
        return launchProc(new ProcessCmd("git", workingPath, "clone", L.getFormatted("gnustep.github.url", "gnustep.libobjc"), "libobjc"));
    }

    protected Builder handleError() {
        return this;
    }

    protected int launchProc(ProcessCmd proc) throws InterruptedException, InvocationTargetException, IOException {
        currentProc = proc;
        currentProc.addProcessEventListener(new ProcessListener() {
            @Override
            public void processCompleted(ProcessEvent event) { builderWindow.setCancelButtonEnabledState(true); }

            @Override
            public void stderrLineReceived(ProcessEvent event) {
                builderWindow.addStatusText(event.getReceivedLine() + System.lineSeparator());
            }

            @Override
            public void stdoutLineReceived(ProcessEvent event) {
                builderWindow.addStatusText(event.getReceivedLine() + System.lineSeparator());
            }
        });

        builderWindow.setCancelButtonEnabledState(false);
        currentProc.start();
        return currentProc.waitFor();
    }

}
