package com.galenrhodes.gnustep.builder.progress;

import com.galenrhodes.gnustep.builder.options.GNUstepBuildOptions;
import com.galenrhodes.gnustep.builder.progress.gui.GNUstepBuilderProgressPanel;

import java.util.concurrent.Callable;

public class Builder implements Callable<Builder> {

    private final GNUstepBuilderProgressPanel builderPanel;
    private final GNUstepBuildOptions         options;

    public Builder(GNUstepBuilderProgressPanel bp) {
        super();
        builderPanel = bp;
        options = ((builderPanel == null) ? null : builderPanel.getOptions());
    }

    @Override
    public Builder call() throws Exception {
        return this;
    }

    public GNUstepBuilderProgressPanel getBuilderPanel() { return builderPanel; }

    public GNUstepBuildOptions getOptions()              { return options; }

}
