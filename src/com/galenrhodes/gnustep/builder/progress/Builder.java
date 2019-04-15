package com.galenrhodes.gnustep.builder.progress;

import com.galenrhodes.gnustep.builder.common.ButtonPressedListener;
import com.galenrhodes.gnustep.builder.common.L;
import com.galenrhodes.gnustep.builder.common.StandardButtons;
import com.galenrhodes.gnustep.builder.common.Tools;
import com.galenrhodes.gnustep.builder.common.processes.ProcessCmd;
import com.galenrhodes.gnustep.builder.options.GNUstepBuildOptions;

import javax.swing.*;
import java.io.File;
import java.util.concurrent.Callable;

public class Builder implements Callable<Builder>, ButtonPressedListener {

    private final String llvmHost     = "http://llvm.org/svn/llvm-project";
    private final String lllvmVersion = "trunk";

    private GNUstepBuildOptions   buildOptions;
    private ProcessCmd     currentProc;

    public Builder(JWindow builderWindow, GNUstepBuildOptions buildOptions) {
        super();
        this.buildOptions = buildOptions;
    }

    @Override
    public void buttonPressed(StandardButtons button) {

    }

    @Override
    public Builder call() throws Exception {
        File workingPath = new File(buildOptions.getWorkingDirectory());
        //noinspection ResultOfMethodCallIgnored
        workingPath.mkdirs();
        int progress  = 0;
        int increment = 10;

//        builderWindow.setProgress(progress);
        int res = downloadLibDispatch(workingPath);
//        if(res == 0) builderWindow.setProgress(progress += increment);
//        else return handleError();

        res = downloadLibObjc(workingPath);
//        if(res == 0) builderWindow.setProgress(progress += increment);
//        else return handleError();

        res = downloadGNUstepMake(workingPath, progress += increment, increment);
        if(res != 0) return handleError();

        res = downloadGNUstepCCommandLineFixer(workingPath);
//        if(res == 0) builderWindow.setProgress(progress += increment);
//        else return handleError();

//        StandardButtons button = builderWindow.waitForAnyButton();
//        System.out.printf("\nButton Pressed: %s\n", button);
//        System.exit(0);
        return this;
    }

    protected int downloadCLang(File workingPath) throws Exception {
        String url = L.getString("gnustep.llvm.host");
        int    cc  = L.getInteger("gnustep.llvm.project.count");

        for(int i = 1; i <= cc; i++) {
            String[] s2 = L.getString(String.format("gnustep.llvm.project.%02d", i)).split("\\|");
            int      r  = fetchFromSVN(workingPath, String.format(url, s2[0]), s2[1].replace('/', File.separatorChar));
            if(r != 0) return r;
        }

        return 0;
    }

    protected int downloadGNUstepCCommandLineFixer(File workingPath) throws Exception {
        return fetchFromGIT(workingPath, L.getFormatted("gnustep.github.url", "gnustep.cmdlinefixer"), "GNUstepCCommandLineFixer");
    }

    protected int downloadGNUstepMake(File workingPath, int progStart, int progInc) throws Exception {
        String url = L.getString("gnustep.gnustep.libs.url");
        int    cc  = L.getInteger("gnustep.gnustep.libs.count");

        for(int i = 1; i <= cc; i++) {
            String[] _str = L.getString(String.format("gnustep.gnustep.libs.%2d", i)).split("\\|");
            int      r    = fetchFromGIT(workingPath, String.format(url, _str[0]), String.format("core/%s", _str[1]).replace('/', File.separatorChar));

            if(r != 0) return r;
//            builderWindow.setProgress(progStart);
            progStart += progInc;
        }

        return 0;
    }

    protected int downloadLibDispatch(File workingPath) throws Exception {
        return fetchFromGIT(workingPath,
                            L.getFormatted("gnustep.github.url", (buildOptions.isUseSwiftLibDispatch() ? "gnustep.libdispatch.swift" : "gnustep.libdispatch.nick")),
                            "libdispatch");
    }

    protected int downloadLibObjc(File workingPath) throws Exception {
        return fetchFromGIT(workingPath, L.getFormatted("gnustep.github.url", "gnustep.libobjc"), "libobjc");
    }

    protected int fetchFromGIT(File workingPath, String url, String savePath) throws Exception {
        return launchProc(workingPath, "git", "clone", url, savePath);
    }

    protected int fetchFromSVN(File workingPath, String url, String savePath) throws Exception {
        int r = launchProc(workingPath, "svn", "co", url, savePath);
        if(r == 0) launchProc(workingPath, "svn", "upgrade", savePath);
        return r;
    }

    protected Builder handleError() {
        return this;
    }

    protected int launchProc(File workingPath, Object... cmdline) throws Exception {
        if(cmdline.length == 0) throw new IllegalArgumentException("No command-line to execute!");
        String[] args = new String[cmdline.length - 1];

        for(int i = 1; i < cmdline.length; i++) args[i - 1] = Tools.ifNull(cmdline[i], "").toString();

        currentProc = new ProcessCmd(cmdline[0].toString(), workingPath, args);
//        currentProc.addProcessEventListener(new ProcessListener() {
//            @Override
//            public void processCompleted(ProcessEvent event) { builderWindow.setCancelButtonEnabledState(true); }
//
//            @Override
//            public void stderrLineReceived(ProcessEvent event) { builderWindow.addStatusText(event.getReceivedLine() + System.lineSeparator()); }
//
//            @Override
//            public void stdoutLineReceived(ProcessEvent event) { builderWindow.addStatusText(event.getReceivedLine() + System.lineSeparator()); }
//        });
//
//        builderWindow.setCancelButtonEnabledState(false);
        currentProc.start();
        return currentProc.waitFor();
    }

}
