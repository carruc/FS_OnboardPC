package org.fscanmqtt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class Timer {
    private LocalDateTime startTime;
    private Duration accumulatedTime;
    private String logFileName;

    public Timer(String sensorName) {
        accumulatedTime = Duration.ZERO;
        this.logFileName = "C:\\Users\\pietr\\IdeaProjects\\FS_OnboardPC\\src\\main\\resources\\logs" + File.separator + sensorName + ".log";
        System.out.println("Creo file " + logFileName);
        createLogFile();
    }

    private void createLogFile() {
        try {
            File file = new File(logFileName);
            file.createNewFile();
            System.out.println("File: " + file);
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
        return startTime == null;
    }

    private void saveToLogFile(Duration elapsedTime) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.logFileName, true))) {
            writer.write(elapsedTime.toMillis() + "ms");
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

