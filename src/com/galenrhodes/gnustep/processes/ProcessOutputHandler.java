package com.galenrhodes.gnustep.processes;

public interface ProcessOutputHandler {

    void handleSingleLine(final StringBuilder sb, String line, boolean isErr);

}
