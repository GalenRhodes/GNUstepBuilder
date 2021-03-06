package com.galenrhodes.gnustep.builder.common.processes;

import java.util.EventListener;

public interface ProcessListener extends EventListener {

    void processCompleted(ProcessEvent event);

    void stderrLineReceived(ProcessEvent event);

    void stdoutLineReceived(ProcessEvent event);

}
