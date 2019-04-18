package com.galenrhodes.gnustep.builder.progress;

import com.galenrhodes.gnustep.builder.options.GNUstepBuildOptions;
import com.galenrhodes.gnustep.builder.progress.gui.GNUstepBuilderProgressPanel;

import java.util.UUID;
import java.util.concurrent.Callable;

public class Builder implements Callable<Builder> {

    private final GNUstepBuilderProgressPanel builderPanel;
    private final GNUstepBuildOptions         options;
    private final String                      _lock_ = UUID.randomUUID().toString();

    private boolean stopBuild = false;
    private boolean isDone    = false;
    private boolean hasError  = false;

    public Builder(GNUstepBuilderProgressPanel bp) {
        super();
        builderPanel = bp;
        options = ((builderPanel == null) ? null : builderPanel.getOptions());
    }

    @Override
    public Builder call() throws Exception {

        return this;
    }

    public GNUstepBuilderProgressPanel getBuilderPanel() {
        return builderPanel;
    }

    public GNUstepBuildOptions getOptions() {
        return options;
    }

    public boolean hasError() {
        return hasError;
    }

    public boolean isDone() {
        synchronized(_lock_) {
            return isDone;
        }
    }

    protected void setDone(boolean done) {
        synchronized(_lock_) {
            this.isDone = done;
            _lock_.notify();
        }
    }

    public boolean isStopBuild() {
        synchronized(_lock_) {
            return stopBuild;
        }
    }

    public void stopBuild() {
        synchronized(_lock_) {
            if(!stopBuild) {
                stopBuild = true;
                _lock_.notify();
            }
        }
    }

    public void waitForDone() throws InterruptedException {
        synchronized(_lock_) {
            try { while(!isDone) _lock_.wait(); } finally { _lock_.notify(); }
        }
    }

    public void waitForStop() throws InterruptedException {
        synchronized(_lock_) {
            try { while(!stopBuild) _lock_.wait(); } finally { _lock_.notify(); }
        }
    }

    protected void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

}
