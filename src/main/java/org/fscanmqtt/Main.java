package org.fscanmqtt;

import java.io.Serial;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import jssc.SerialPortException;
import org.json.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {
    public static void main(String[] args) {

        try{
            if(args[0].length() < 4 || args[0].charAt(0) != 'C' || args[0].charAt(1) != 'O' || args[0].charAt(2) != 'M') {
                System.out.println("ERR1: " + args[0] + " is not a valid portname.");
                return;
            }
        }catch(ArrayIndexOutOfBoundsException e){
            System.out.println(e);
        }

        HandleSensors handleSensors = new HandleSensors(); // shared object
        SerialPortReader serialPortReader = new SerialPortReader(args[0], 115200, 8,1,0, handleSensors);
        ClientMQTT clientMQTT = new ClientMQTT("tcp://94.23.68.97:1883", "deky", "Telemetry1!", "hivemq", "topic_name",handleSensors);

        clientMQTT.mqttConnect();
        serialPortReader.connect();

        ExecutorService executorService = Executors.newFixedThreadPool(2); //ricordiamoci di definire correttamente il numero di thread
        executorService.submit(serialPortReader);
        executorService.submit(clientMQTT);

    }
}