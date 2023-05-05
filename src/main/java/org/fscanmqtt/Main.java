package org.fscanmqtt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {
    public static void main(String[] args) {
        System.out.println("PROGETTO CAN MQTT by " +
                "Carrucciu Pietro   " +
                "Masetti Riccardo   " +
                "Paltrinieri Davide");

        /*
        ora abbiamo una stinga nel main contenente tutto il file json
        nella prossima implementazione questa stinga si otterrà con una iscrizione ad un topic mqtt sul broker
         */
        String json = "{\"classname\":\"ThermalPressure\",\"name\":\"airT\",\"CANID\":\"0x208\",\"byte_interval\":[ 4, 5 ],\"gain\":0.1,\"offset\":0,\"min\":1.0,\"max\":1.0}";
        String jsonInput = "{ \"sensors\":" +
                "    [" +
                "    " +
                "      {" +
                "        \"classname\": \"ThermalPressure\"," +
                "        \"name\": \"waterT\"," +
                "        \"CANID\": \"0x200\"," +
                "        \"byte_interval\": [ 4, 5 ]," +
                "        \"gain\":0.1," +
                "        \"offset\":0," +
                "        \"min\":\"NULL\"," +
                "        \"max\":\"NULL\"" +
                "      }," +
                "  " +
                "      {" +
                "        \"classname\": \"ThermalPressure\"," +
                "        \"name\": \"airT\"," +
                "        \"CANID\": \"0x208\"," +
                "        \"byte_interval\": [ 4, 5 ]," +
                "        \"gain\":0.1," +
                "        \"offset\":0," +
                "        \"min\":\"NULL\"," +
                "        \"max\":\"NULL\"" +
                "      }," +
                "  " +
                "      {" +
                "        \"classname\": \"ThermalPressure\"," +
                "        \"name\": \"oilP\"," +
                "        \"CANID\": \"0x200\"," +
                "        \"byte_interval\": [ 6, 7 ]," +
                "        \"gain\":0.1," +
                "        \"offset\":0," +
                "        \"min\":\"NULL\"," +
                "        \"max\":\"NULL\"" +
                "      }," +
                "  " +
                "      {" +
                "        \"classname\": \"ThermalPressure\"," +
                "        \"name\": \"oilT\"," +
                "        \"CANID\": \"0x204\"," +
                "        \"byte_interval\": [ 0, 1 ]," +
                "        \"gain\":0.001," +
                "        \"offset\":0," +
                "        \"min\":\"NULL\"," +
                "        \"max\":\"NULL\"" +
                "      }," +
                "  " +
                "      {" +
                "        \"classname\": \"ThermalPressure\"," +
                "        \"name\": \"fuelP\"," +
                "        \"CANID\": \"0x208\"," +
                "        \"byte_interval\": [ 2, 3 ]," +
                "        \"gain\":0.001," +
                "        \"offset\":0," +
                "        \"min\":\"NULL\"," +
                "        \"max\":\"NULL\"" +
                "      }," +
                "  " +
                "      {" +
                "        \"classname\": \"ThermalPressure\"," +
                "        \"name\": \"fuelT\"," +
                "        \"CANID\": \"0x312\"," +
                "        \"byte_interval\": [ 0, 1 ]," +
                "        \"gain\":0.1," +
                "        \"offset\":0," +
                "        \"min\":\"NULL\"," +
                "        \"max\":\"NULL\"" +
                "      }," +
                "  " +
                "    " +
                "      {" +
                "        \"classname\": \"Mechanics\"," +
                "        \"name\": \"rpm\"," +
                "        \"CANID\": \"0x200\"," +
                "        \"byte_interval\": [ 0, 1 ]," +
                "        \"gain\":1.0," +
                "        \"offset\":0," +
                "        \"min\":\"NULL\"," +
                "        \"max\":\"NULL\"" +
                "      }," +
                "  " +
                "      {" +
                "        \"classname\": \"Mechanics\"," +
                "        \"name\": \"gear\"," +
                "        \"CANID\": \"0x204\"," +
                "        \"byte_interval\": [ 4, 4 ]," +
                "        \"gain\":1.0," +
                "        \"offset\":0," +
                "        \"min\":\"NULL\"," +
                "        \"max\":\"NULL\"" +
                "      }," +
                "  " +
                "      {" +
                "        \"classname\": \"Mechanics\"," +
                "        \"name\": \"slip\"," +
                "        \"CANID\": \"0x20C\"," +
                "        \"byte_interval\": [ 1, 1 ]," +
                "        \"gain\":1.0," +
                "        \"offset\":0," +
                "        \"min\":\"NULL\"," +
                "        \"max\":\"NULL\"" +
                "      }," +
                "  " +
                "      {" +
                "        \"classname\": \"Mechanics\"," +
                "        \"name\": \"speed\"," +
                "        \"CANID\": \"0x20C\"," +
                "        \"byte_interval\": [ 2, 3 ]," +
                "        \"gain\":0.1," +
                "        \"offset\":0," +
                "        \"min\":\"NULL\"," +
                "        \"max\":\"NULL\"" +
                "      },\n" +
                "  " +
                "      {" +
                "        \"classname\": \"Mechanics\"," +
                "        \"name\": \"tps\"," +
                "        \"CANID\": \"0x200\"," +
                "        \"byte_interval\": [ 2, 3 ]," +
                "        \"gain\":0.1," +
                "        \"offset\":0," +
                "        \"min\":\"NULL\"," +
                "        \"max\":\"NULL\"" +
                "      }," +
                "  " +
                "      {" +
                "        \"classname\": \"Controls\"," +
                "        \"name\": \"batteryV\"," +
                "        \"CANID\": \"0x204\"," +
                "        \"byte_interval\": [ 6, 7 ]," +
                "        \"gain\":0.001," +
                "        \"offset\":0," +
                "        \"min\":\"NULL\"," +
                "        \"max\":\"NULL\"" +
                "      }," +
                "  " +
                "      {" +
                "        \"classname\": \"Controls\"," +
                "        \"name\": \"launchControlStatus\"," +
                "        \"CANID\": \"0x220\"," +
                "        \"byte_interval\": [ 3, 3 ]," +
                "        \"gain\":1.0," +
                "        \"offset\":0," +
                "        \"min\":\"NULL\"," +
                "        \"max\":\"NULL\"" +
                "      }," +
                "  " +
                "      {" +
                "        \"classname\": \"Controls\"," +
                "        \"name\": \"pedal\"," +
                "        \"CANID\": \"0x20C\"," +
                "        \"byte_interval\": [ 0, 0 ]," +
                "        \"gain\":1.0," +
                "        \"offset\":0," +
                "        \"min\":\"NULL\"," +
                "        \"max\":\"NULL\"" +
                "      }," +
                "  " +
                "      {" +
                "        \"classname\": \"Controls\"," +
                "        \"name\": \"brake\"," +
                "        \"CANID\": \"0x20C\"," +
                "        \"byte_interval\": [ 4, 4 ]," +
                "        \"gain\":1.0," +
                "        \"offset\":0," +
                "        \"min\":\"NULL\"," +
                "        \"max\":\"NULL\"" +
                "      }," +
                "  " +
                "      {" +
                "        \"classname\": \"Controls\"," +
                "        \"name\": \"brakeRear\"," +
                "        \"CANID\": \"0x30C\"," +
                "        \"byte_interval\": [ 0, 1 ]," +
                "        \"gain\":1.0," +
                "        \"offset\":0," +
                "        \"min\":\"NULL\"," +
                "        \"max\":\"NULL\"" +
                "      }" +
                "  " +
                "  ]" +
                "}";

        ObjectMapper mapper = new ObjectMapper();
        try{
            //Sensor sensor = mapper.readValue(json, Sensor.class);
            //System.out.println(sensor);
            List<Sensor> sensors = mapper.readValue(json, new TypeReference<List<Sensor>>() { });
            sensors = Arrays.asList(mapper.readValue(json, Sensor[].class));
            Collection<COrder> readValues = new ObjectMapper().readValue(
                    jsonAsString, new TypeReference<Collection<COrder>>() { }
            );




            sensors.forEach(s -> System.out.println(s));
        }catch(JsonProcessingException e){
            e.printStackTrace();
        }


        /*
        SerialPortReader serialThread = new SerialPortReader("COM7", 115200, 8,1,0);
        ClientMQTT mqttThread = new ClientMQTT("tcp://94.23.68.97:1883", "deky", "Telemetry1!", "hivemq");
        mqttThread.setTopic("topic_name");

        serialThread.start();
        mqttThread.start();
         */
    }
}