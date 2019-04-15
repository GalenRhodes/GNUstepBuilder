package com.galenrhodes.gnustep.builder.common.processes;

public interface ProcessOutputHandler {

    void handleSingleLine(final StringBuilder sb, String line, boolean isErr);

}
