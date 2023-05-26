package org.fscanmqtt;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class Timer {
    private LocalDateTime startTime;
    private Duration accumulatedTime;
    private String logFileName;
    private String sensorName;

    public Timer(String sensorName) {
        accumulatedTime = Duration.ZERO;
        this.sensorName = sensorName;
        this.logFileName = LocalDateTime.now().toString()+ "_" + sensorName + ".log";
        createLogFile();
    }

    private void createLogFile() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(logFileName, false));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startTimer() {
        startTime = LocalDateTime.now();
    }

    public long getTimerMillis() {
        Duration elapsedTime = Duration.between(startTime, LocalDateTime.now());
        return elapsedTime.toMillis();
    }

    public void stopTimer() {
        if (startTime != null) {
            Duration elapsedTime = Duration.between(startTime, LocalDateTime.now());
            accumulatedTime = accumulatedTime.plus(elapsedTime);
            saveToLogFile(elapsedTime);
            startTime = null;
        }
    }

    public boolean isNotCounting() {
        /*Ritorna true se la variabile startTime non e' piena (non sta contando)*/
        return startTime.equals(null);
    }

    private void saveToLogFile(Duration elapsedTime) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.logFileName, true))) {
            writer.write(this.sensorName + ": " + elapsedTime.getSeconds() + "s");
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

