package org.fscanmqtt;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import org.json.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {
    public static void main(String[] args) {

        /*
        SerialPortReader serialThread = new SerialPortReader("COM7", 115200, 8,1,0);
        ClientMQTT mqttThread = new ClientMQTT("tcp://94.23.68.97:1883", "deky", "Telemetry1!", "hivemq");
        mqttThread.setTopic("topic_name");

        serialThread.start();
        mqttThread.start();
         */

        String CANID = "0x20C";
        byte[] data = new byte[16];
        for (int i = 0; i < data.length; ++i) {
            data[i] = (byte) 255;
        }

        HandleSensors conca = new HandleSensors();
        conca.initializeSensorList();
        conca.initializeCarStatus();
        conca.updateCarStatus(CANID, data);
        System.out.println(conca.toString());
    }
}