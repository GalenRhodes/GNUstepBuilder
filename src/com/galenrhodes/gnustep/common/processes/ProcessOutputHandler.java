package com.galenrhodes.gnustep.common.processes;

public interface ProcessOutputHandler {

    void handleSingleLine(final StringBuilder sb, String line, boolean isErr);

}
