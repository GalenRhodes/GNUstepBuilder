package com.galenrhodes.gnustep;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProcessCmd implements Caliphate {

    private static final ExecutorService tp = Executors.newCachedThreadPool();

    private final StringBuilder  sbOut;
    private final StringBuilder  sbErr;
    private final Process        process;
    private final ProcessBuilder processBuilder;
    private       boolean        wasInterrupted;
    private       Caliphate      caliphate;

    public ProcessCmd(String exec, File workingDir, String... args) throws Exception {
        this(exec, workingDir, Collections.emptyMap(), null, args);
    }

    public ProcessCmd(String exec, File workingDir, Caliphate caliphate, String... args) throws Exception {
        this(exec, workingDir, Collections.emptyMap(), caliphate, args);
    }

    public ProcessCmd(String exec, String... args) throws Exception {
        this(exec, null, Collections.emptyMap(), null, args);
    }

    public ProcessCmd(String exec, Caliphate caliphate, String... args) throws Exception {
        this(exec, null, Collections.emptyMap(), caliphate, args);
    }

    public ProcessCmd(String exec, File workingDir, Map<String, String> env, String... args) throws Exception {
        this(exec, workingDir, env, null, args);
    }

    public ProcessCmd(String exec, File workingDir, Map<String, String> env, Caliphate c, String... args) throws Exception {
        super();
        wasInterrupted = false;
        sbOut = new StringBuilder();
        sbErr = new StringBuilder();
        caliphate = ((c == null) ? this : c);

        processBuilder = new ProcessBuilder(getArgsList(exec, args));
        if(workingDir != null) processBuilder.directory(workingDir);
        if((env != null) && (env.size() > 0)) processBuilder.environment().putAll(env);
        process = processBuilder.start();

        startIOUp();
    }

    public ProcessCmd(ProcessBuilder pb) throws IOException {
        super();
        wasInterrupted = false;
        sbOut = new StringBuilder();
        sbErr = new StringBuilder();
        processBuilder = pb;
        process = processBuilder.start();

        startIOUp();
    }

    public String getError()  { synchronized(sbErr) { return sbErr.toString(); } }

    public String getOutput() { synchronized(sbOut) { return sbOut.toString(); } }

    public ProcessBuilder getProcessBuilder() {
        return processBuilder;
    }

    public OutputStream getStdInput() { return process.getOutputStream(); }

    public void handleSingleLine(final StringBuilder sb, String line, boolean isErr) {
        sb.append(line).append("\n");
    }

    public int waitFor() {
        try {
            return process.waitFor();
        }
        catch(InterruptedException e) {
            wasInterrupted = true;
            return -1;
        }
    }

    public boolean wasInterrupted() {
        return wasInterrupted;
    }

    private List<String> getArgsList(String exec, String... args) {
        List<String> zzTop = new ArrayList<>();
        zzTop.add(exec);
        if(args.length > 0) zzTop.addAll(Arrays.asList(args));
        return zzTop;
    }

    private boolean readStream(InputStream instr, final StringBuilder sb, boolean isErr) {
        try {
            try(BufferedReader br = new BufferedReader(new InputStreamReader(instr, StandardCharsets.UTF_8))) {
                String line = br.readLine();

                while(line != null) {
                    //noinspection SynchronizationOnLocalVariableOrMethodParameter
                    synchronized(sb) { caliphate.handleSingleLine(sb, line, isErr); }
                    line = br.readLine();
                }
            }

            return true;
        }
        catch(IOException e) {
            return false;
        }
    }

    private void startIOUp() throws IOException {
        tp.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() {
                return readStream(process.getInputStream(), sbOut, false);
            }
        });

        tp.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() {
                return readStream(process.getErrorStream(), sbErr, true);
            }
        });
    }

}
