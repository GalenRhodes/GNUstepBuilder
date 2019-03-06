package com.galenrhodes.gnustep.processes;

public interface StandardOutputHandler {

    void handleSingleLine(final StringBuilder sb, String line, boolean isErr);

}
