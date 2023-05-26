package org.fscanmqtt;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static boolean checkArgs(String[] args){
        if (args[0].matches("COM*")) {
            System.out.println("ERR1: " + args[0] + " is not a valid portname. Correct usage is: programName " +
                    "<usbport> <masterformat.json abs pathname>");
            return false;
        }

        File controlFile = new File(args[1]);
        if (!controlFile.canRead() || !controlFile.exists()) {
            System.out.println("ERR2: " + args[1] + " is not a valid filename, or it isn't readable.");
            return false;
        }

        return true;
    }
    public static void main(String[] args) {

        if(!checkArgs(args)){
            return;
        }

        HandleSensors handleSensors = new HandleSensors(args[1]); // shared object

        SerialPortReader serialPortReader = new SerialPortReader(args[0], 115200, 8,1,0, handleSensors);
        ClientMQTT clientMQTT = new ClientMQTT("tcp://94.23.68.97:1883", "deky", "Telemetry1!", "hivemq", "topic_name",handleSensors);

        clientMQTT.mqttConnect();
        serialPortReader.connect();

        ExecutorService executorService = Executors.newFixedThreadPool(2); //ricordiamoci di definire correttamente il numero di thread
        executorService.submit(serialPortReader);
        executorService.submit(clientMQTT);

    }
}