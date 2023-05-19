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

    public ClientMQTT(String broker, String username, String psw, String clientID) {
        this.broker = broker;
        this.username = username;
        this.psw = psw;
        this.clientID = clientID;
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

    public void sendMessage(String topic, String msg){
        try{
            // create message and setup QoS
            MqttMessage message = new MqttMessage(msg.getBytes());
            message.setQos(0);
            // publish message
            client.publish(topic, message);
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

    }
}