package org.fscanmqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.fscanmqtt.Sensor;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class HandleSensors {
    private final int CANID_OFF = 0;
    private final int CANID_DIM = 3;
    private final int DATA_OFF = 4;
    private final int DATA_DIM = 16;
    private HashMap<String, Float> carStatus;
    public List<Sensor> sensors;

    public HandleSensors (){
        carStatus = new HashMap<>();
    }
    public void initializeCarStatus(){
        /*Crea mappa con ogni nome del sensore e valore da modificare.*/
        this.sensors.stream().forEach(sensor -> this.carStatus.put(sensor.getName(), Float.valueOf(0)));
    }

    public String getCANIdString(byte[] input){
        /*Traduce i 3 caratteri ascii che rappresentano il CANId*/
        byte[] tmp = new byte[CANID_DIM];
        System.arraycopy(input, CANID_OFF, tmp, 0,  CANID_DIM);
        return new String(tmp, StandardCharsets.US_ASCII);
    }

    public byte[] getActualDataArray(byte[] input){
        /*Traduce i 16 caratteri ascii che rappresentano il dato grezzo*/
        byte[] tmp = new byte[DATA_DIM];
        System.arraycopy(input, DATA_OFF, tmp, 0,  DATA_DIM);
        ByteBuffer buffer = ByteBuffer.allocate(8);
        for (int i = 0; i < 16; i += 2) {
            buffer.put((byte) ((Character.digit(tmp[i], 16) << 4)
                    + Character.digit(tmp[i+1], 16)));
        }
        return buffer.array();
    }

    public void updateCarStatus(String CANID, byte[] data){
        /*Aggiorna carStatus con nuovo messaggio.*/
        sensors.stream()
                .filter(sensor -> sensor.getCanID().equals(CANID))
                .forEach(sensor -> this.carStatus.replace(
                                sensor.getName(), sensor.applyFormat(
                                    (float) dataSubsetToInt(sensor.getByteInterval()[0], sensor.getByteInterval()[1], data))
                                                                    )
                                                        );
    }

    public int dataSubsetToInt(int first, int last, byte[] data){
        /* Converte un byte[] nell'int che rappresenta */
        if(     first < 0
                || first > data.length
                || last > data.length
                || last < 0
                || first > last){
            return Integer.MIN_VALUE;
        }

        int ret = 0;

        for(int i = last, k = 0; i >= first; i = i - 1, ++k){
            ret += ((int)Math.pow((double)256, (double)k))*(int)data[i];
        }

        return ret;
    }
    public void initializeSensorList(){
        /*Scrivere funzione di download e lettura del file json dal sito.*/
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
            this.sensors = Arrays.asList(mapper.readValue(jsonInput, Sensor[].class));
        }catch(JsonProcessingException e){
            e.printStackTrace();
        }
    }
}
