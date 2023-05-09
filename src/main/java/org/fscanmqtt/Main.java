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
        System.out.println("PROGETTO CAN MQTT by " +
                "Carrucciu Pietro   " +
                "Masetti Riccardo   " +
                "Paltrinieri Davide");

        String json = "[{\"classname\":\"ThermalPressure\",\"name\":\"waterT\",\"CANID\":\"0x200\"," +
                "\"byteInterval\":\"[ 4, 5 ]\",\"gain\":0.1,\"offset\":0,\"min\":\"NULL\",\"max\":\"NULL\"}," +
                "{\"classname\": \"ThermalPressure\",\"name\": \"airT\",\"CANID\":\"0x208\"," +
                "\"byteInterval\":\"[ 4, 5 ]\",\"gain\":0.1,\"offset\":0,\"min\":\"NULL\",\"max\":\"NULL\"}]";

        /*
        ora abbiamo una stinga nel main contenente tutto il file json
        nella prossima implementazione questa stinga si otterrà con una iscrizione ad un topic mqtt sul broker
        */
        String jsonInput =
                "    [" +
                "      {" +
                "        \"classname\": \"ThermalPressure\"," +
                "        \"name\": \"waterT\"," +
                "        \"CANID\": \"0x200\"," +
                "        \"byteInterval\": \"[ 4, 5 ]\"," +
                "        \"gain\":0.1," +
                "        \"offset\":0," +
                "        \"min\":\"NULL\"," +
                "        \"max\":\"NULL\"" +
                "      }," +
                "      {" +
                "        \"classname\": \"ThermalPressure\"," +
                "        \"name\": \"airT\"," +
                "        \"CANID\":\"0x208\"," +
                "        \"byteInterval\":\"[ 4, 5 ]\"," +
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
                "        \"byteInterval\":\"[ 6, 7 ]\"," +
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
                "        \"byteInterval\":\"[ 0, 1 ]\"," +
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
                "        \"byteInterval\":\"[ 2, 3 ]\"," +
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
                "        \"byteInterval\":\"[ 0, 1 ]\"," +
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
                "        \"byteInterval\":\"[ 0, 1 ]\"," +
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
                "        \"byteInterval\":\"[ 4, 4 ]\"," +
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
                "        \"byteInterval\":\"[ 1, 1 ]\"," +
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
                "        \"byteInterval\":\"[ 2, 3 ]\"," +
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
                "        \"byteInterval\": \"[ 2, 3 ]\"," +
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
                "        \"byteInterval\": \"[ 6, 7 ]\"," +
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
                "        \"byteInterval\": \"[ 3, 3 ]\"," +
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
                "        \"byteInterval\": \"[ 0, 0 ]\"," +
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
                "        \"byteInterval\": \"[ 4, 4 ]\"," +
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
                "        \"byteInterval\": \"[ 0, 1 ]\"," +
                "        \"gain\":1.0," +
                "        \"offset\":0," +
                "        \"min\":\"NULL\"," +
                "        \"max\":\"NULL\"" +
                "      }" +
                "  " +
                "  ]";

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        try{
            List<Sensor> sensors = Arrays.asList(mapper.readValue(jsonInput, Sensor[].class));
            sensors.forEach(s -> System.out.println(s.toString()));
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