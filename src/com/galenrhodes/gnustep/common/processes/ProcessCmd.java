package com.galenrhodes.gnustep.common.processes;

import com.galenrhodes.gnustep.common.events.EventEngine;
import com.galenrhodes.gnustep.common.processes.ProcessEvent.ProcessEventType;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProcessCmd implements ProcessOutputHandler {

    private static final ExecutorService tp = Executors.newCachedThreadPool();

    private final StringBuilder  sbOut;
    private final StringBuilder  sbErr;
    private final ProcessBuilder processBuilder;

    private EventEngine<ProcessEvent, ProcessListener> eventEngine;
    private ProcessOutputHandler                       outputHandler;
    private boolean                                    wasInterrupted;
    private Process                                    process;

    public ProcessCmd(String exec, File workingDir, String... args) throws Exception {
        this(exec, workingDir, Collections.emptyMap(), null, args);
    }

    public ProcessCmd(String exec, File workingDir, ProcessOutputHandler outputHandler, String... args) throws Exception {
        this(exec, workingDir, Collections.emptyMap(), outputHandler, args);
    }

    public ProcessCmd(String exec, String... args) throws Exception {
        this(exec, null, Collections.emptyMap(), null, args);
    }

    public ProcessCmd(String exec, ProcessOutputHandler outputHandler, String... args) throws Exception {
        this(exec, null, Collections.emptyMap(), outputHandler, args);
    }

    public ProcessCmd(String exec, File workingDir, Map<String, String> env, String... args) throws Exception {
        this(exec, workingDir, env, null, args);
    }

    public ProcessCmd(String exec, File workingDir, Map<String, String> env, ProcessOutputHandler outputHandler, String... args) throws Exception {
        super();
        this.wasInterrupted = false;
        this.sbOut = new StringBuilder();
        this.sbErr = new StringBuilder();
        this.outputHandler = ((outputHandler == null) ? this : outputHandler);

        this.processBuilder = new ProcessBuilder(getArgsList(exec, args));
        if(workingDir != null) this.processBuilder.directory(workingDir);
        if((env != null) && (env.size() > 0)) this.processBuilder.environment().putAll(env);
        init();
    }

    public ProcessCmd(ProcessBuilder processBuilder) throws Exception {
        super();
        this.processBuilder = processBuilder;
        this.sbOut = new StringBuilder();
        this.sbErr = new StringBuilder();
        this.outputHandler = this;
        this.wasInterrupted = false;
        init();
    }

    public void addProcessEventListener(ProcessListener listener) {
        eventEngine.addEventListener(listener);
    }

    public String getError() { synchronized(sbErr) { return sbErr.toString(); } }

    public int getExitCode() {
        return process.exitValue();
    }

    public String getOutput() { synchronized(sbOut) { return sbOut.toString(); } }

    public ProcessBuilder getProcessBuilder() {
        return processBuilder;
    }

    public OutputStream getStdInput() { return process.getOutputStream(); }

    public void handleSingleLine(final StringBuilder sb, String line, boolean isErr) {
        sb.append(line).append("\n");
    }

    public boolean isAlive() { return process.isAlive(); }

    public void removeAllProcessEventListeners() {
        eventEngine.removeAllEventListeners();
    }

    public void removeProcessEventListener(ProcessListener listener) {
        eventEngine.removeEventListener(listener);
    }

    public void start() throws IOException {
        process = processBuilder.start();

        tp.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                int exitCode = 0;
                wasInterrupted = false;
                try { exitCode = process.waitFor(); } catch(InterruptedException ignored) { wasInterrupted = true; }
                eventEngine.asyncPublishEvent(new ProcessEvent(ProcessCmd.this, ProcessEventType.PROCESS_END, wasInterrupted ? -1 : exitCode));
                return true;
            }
        });
        tp.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() { return readStream(process.getInputStream(), sbOut, false); }
        });
        tp.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() { return readStream(process.getErrorStream(), sbErr, true); }
        });
    }

    public int waitFor() {
        try {
            return process.waitFor();
        }
        catch(InterruptedException e) {
            return -1;
        }
    }

    public boolean wasInterrupted() {
        return wasInterrupted;
    }

    private List<String> getArgsList(String exec, String... args) {
        List<String> list = new ArrayList<>();
        list.add(exec);
        if(args.length > 0) list.addAll(Arrays.asList(args));
        return list;
    }

    private void init() throws Exception {
        eventEngine = new EventEngine<ProcessEvent, ProcessListener>() {
            @Override
            public void dispatchEvent(ProcessEvent event, ProcessListener listener) {
                switch(event.getEventType()) {
                    case PROCESS_END:
                        listener.processCompleted(event);
                        break;
                    case STDOUT_LINE_RCVD:
                        listener.stdoutLineReceived(event);
                        break;
                    case STDERR_LINE_RCVD:
                        listener.stderrLineReceived(event);
                        break;
                }
            }
        };
    }

    private boolean readStream(InputStream instr, final StringBuilder sb, boolean isErr) {
        try {
            try(BufferedReader br = new BufferedReader(new InputStreamReader(instr, StandardCharsets.UTF_8))) {
                String line = br.readLine();

                while(line != null) {
                    ProcessEvent event = new ProcessEvent(this, isErr ? ProcessEventType.STDERR_LINE_RCVD : ProcessEventType.STDOUT_LINE_RCVD, line);
                    eventEngine.asyncPublishEvent(event);

                    //noinspection SynchronizationOnLocalVariableOrMethodParameter
                    synchronized(sb) { outputHandler.handleSingleLine(sb, line, isErr); }
                    line = br.readLine();
                }
            }

            return true;
        }
        catch(IOException e) {
            return false;
        }
    }

}
