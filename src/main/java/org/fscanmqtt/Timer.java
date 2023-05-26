package org.fscanmqtt;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class Timer {
    private LocalDateTime startTime;
    private Duration accumulatedTime;

    public Timer() {
        accumulatedTime = Duration.ZERO;
    }

    public void startTimer() {
        startTime = LocalDateTime.now();
    }

    public void getTimerMillis(){
        
    }
    public void stopTimer(String sensorName) {
        if (startTime != null) {
            Duration elapsedTime = Duration.between(startTime, LocalDateTime.now());
            accumulatedTime = accumulatedTime.plus(elapsedTime);
            saveToLogFile(sensorName, elapsedTime);
            startTime = null;
        }
    }

    public boolean isNotCounting() {
        /*Ritorna true se la variabile startTime non e' piena (non sta contando)*/
        return startTime.equals(null);
    }

    private void saveToLogFile(String sensorName, Duration elapsedTime) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("timer.log", true))) {
            writer.write(sensorName + ": " + elapsedTime.getSeconds() + "s"); writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

