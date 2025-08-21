package com.branchlift.backend.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ShellCommandExecutor {
    private static final Logger logger = LoggerFactory.getLogger(ShellCommandExecutor.class);

    public static CommandResult executeCommand(List<String> command, File workingDirectory, long timeoutSeconds) {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.directory(workingDirectory);
        processBuilder.redirectErrorStream(true);

        StringBuilder output = new StringBuilder();
        int exitCode = -1;

        try {
            Process process = processBuilder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            boolean finished = process.waitFor(timeoutSeconds, TimeUnit.SECONDS);

            if (finished) {
                exitCode = process.exitValue();
            } else {
                process.destroyForcibly();
                output.append("\n[ERROR] Command timed out.");
            }
        } catch (Exception e) {
            logger.error("Error executing command: {}", String.join(" ", command), e);
            output.append("\n[ERROR] Exception while executing command: ").append(e.getMessage());
        }

        return new CommandResult(exitCode, output.toString());
    }

    public static class CommandResult {
        private final int exitCode;
        private final String output;

        public CommandResult(int exitCode, String output) {
            this.exitCode = exitCode;
            this.output = output;
        }

        public int getExitCode() { return exitCode; }
        public String getOutput() { return output; }
        public boolean isSuccess() { return exitCode == 0; }
    }
}