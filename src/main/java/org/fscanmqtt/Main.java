package org.fscanmqtt;

public class Main {
    public static void main(String[] args) {
        System.out.println("PROGETTO CAN MQTT by" +
                "Carruccio Pietro   " +
                "Masetti Riccardo   " +
                "Paltrinieri Davide");

        SerialPortReader serialThread = new SerialPortReader("COM7", 115200, 8,1,0);
        ClientMQTT mqttThread = new ClientMQTT("tcp://94.23.68.97:1883", "deky", "Telemetry1!", "hivemq");
        mqttThread.setTopic("topic_name");

        serialThread.start();
        mqttThread.start();

    }
}