package org.fscanmqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.HashMap;
import java.util.Map;

public class ClientMQTT extends Thread{

    private final String broker;
    private final String username;
    private final String psw;
    private final String clientID;
    private MqttClient client;



    private String topic;
    private final HandleSensors sharedHandleSensors;

    public ClientMQTT(String broker, String username, String psw, String clientID, String topic, HandleSensors sharedHandleSensors) {
        this.broker = broker;
        this.username = username;
        this.psw = psw;
        this.clientID = clientID;
        this.topic = topic;
        this.sharedHandleSensors = sharedHandleSensors;
    }

    public void mqttConnect(){
        try {
            this.client = new MqttClient(broker, clientID, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(username);
            options.setPassword(psw.toCharArray());
            options.setConnectionTimeout(60);
            options.setKeepAliveInterval(60);
            // connect
            client.connect(options);

        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(String msg){
        try{
            // create message and setup QoS
            MqttMessage message = new MqttMessage(msg.getBytes());
            message.setQos(0);
            // publish message
            client.publish(this.topic, message);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void disconnect() {
        try{
            // disconnect
            client.disconnect();
            // close client
            client.close();
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            sendMessage(sharedHandleSensors.toJSON());
            System.out.println(sharedHandleSensors.toJSON());
            //System.out.println("messaggio inviato");
            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }
}