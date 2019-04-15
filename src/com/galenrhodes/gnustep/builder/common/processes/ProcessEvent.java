package com.galenrhodes.gnustep.builder.common.processes;

import java.util.EventObject;

public class ProcessEvent extends EventObject {

    private ProcessEventType eventType;
    private String           receivedLine;
    private int              exitCode;

    /**
     * Constructs a prototypical Event.
     *
     * @param source    The object on which the Event initially occurred.
     * @param eventType The type of event.
     *
     * @throws IllegalArgumentException if source or eventType is not {@link ProcessEventType#PROCESS_END}.
     */
    public ProcessEvent(Object source, ProcessEventType eventType, int exitCode) {
        super(source);
        if(eventType != ProcessEventType.PROCESS_END) throw new IllegalArgumentException("Incorrect Event Type: " + eventType);
        this.eventType = eventType;
        this.exitCode = exitCode;
    }

    /**
     * Constructs a prototypical Event.
     *
     * @param source       The object on which the Event initially occurred.
     * @param eventType    The type of event.
     * @param receivedLine The received line of text.
     *
     * @throws IllegalArgumentException if source or receivedLine is null or the eventType is not {@link ProcessEventType#STDOUT_LINE_RCVD} or {@link
     *                                  ProcessEventType#STDERR_LINE_RCVD}.
     */
    public ProcessEvent(Object source, ProcessEventType eventType, String receivedLine) {
        super(source);
        if(receivedLine == null) throw new IllegalArgumentException("Received line is null.");
        if(eventType == ProcessEventType.STDERR_LINE_RCVD || eventType == ProcessEventType.STDOUT_LINE_RCVD) {
            this.eventType = eventType;
            this.receivedLine = receivedLine;
        }
        else {
            throw new IllegalArgumentException("Incorrect Event Type: " + eventType);
        }
    }

    public ProcessEventType getEventType() { return eventType; }

    public int getExitCode()               { return exitCode; }

    public String getReceivedLine()        { return receivedLine; }

    public enum ProcessEventType {
        PROCESS_END, STDOUT_LINE_RCVD, STDERR_LINE_RCVD
    }

}
