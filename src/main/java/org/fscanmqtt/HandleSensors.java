package org.fscanmqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.fscanmqtt.Sensor;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.sql.SQLOutput;
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

    public HandleSensors (){/*FILE json*/

        carStatus = new HashMap<>();
        initializeSensorList(); //inserisci file
        initializeCarStatus();
    }
    private void initializeCarStatus(){
        /*Crea mappa con ogni nome del sensore e valore da modificare.*/

        this.sensors.stream().forEach(sensor -> this.carStatus.put(sensor.getName(), Float.valueOf(0)));
    }

    public HashMap<String, Float> getCarStatus() {
        return carStatus;
    }

    public String getCANIdString(byte[] input){
        /*Traduce i 3 caratteri ascii che rappresentano il CANId*/

        byte[] tmp = new byte[CANID_DIM];
        System.arraycopy(input, CANID_OFF, tmp, 0,  CANID_DIM);
        String str = new String(tmp, StandardCharsets.US_ASCII);
        StringBuilder stringBuilder = new StringBuilder("0x");
        stringBuilder.append(str);
        return stringBuilder.toString();
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
        /*Aggiorna carStatus con nuovo messaggio, rispettivamente:
         * 1) Crea uno stream dalla lista di sensori con cui e' stato inizializzata la classe;
         * 2) Selezioniamo solo i sensor il cui attributo CANID matcha con la stringa CANId
         *   (potrebbe essere necessario fare il filtraggio dei byte all'interno del methodo ma vabbe');
         * 3) Per ognuno di questi sensori, prendiamo quelli presenti nel car status e ne sostituiamo il valore
         *   con il dato processato a partire dall'array di byte.;
         * */

        sensors.stream()
                .filter(sensor -> sensor.getCanID().equals(CANID))
                .forEach(sensor -> this.carStatus.replace(
                                sensor.getName(),
                                sensorValue(sensor, data)
                        )
                );
    }

    private Float sensorValue(Sensor sensor, byte[] data){
        /*Funzione che a partire dai dati grazzi, restituisce il dato in float, formattandolo secondo i suoi valori
         * di gain, offset, etc*/
        return sensor.applyFormat(
                (float) dataSubsetToInt(sensor.getByteInterval()[0], sensor.getByteInterval()[1], data));
    }

    private int dataSubsetToInt(int first, int last, byte[] data){
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
    private void initializeSensorList(){
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

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        this.carStatus.entrySet().stream().toList().forEach(stringFloatEntry -> str.append(stringFloatEntry.getKey() +
                ": "+ stringFloatEntry.getValue() + ";\n"));
        return str.toString();
    }

    public String toJSON() {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String json = objectMapper.writeValueAsString(getCarStatus());
            return json;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

}