package org.fscanmqtt;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

public class Sensor {
    private String classname;
    private String name;
    private String canID; //2 byte
    private Integer[] byteInterval;
    private Float gain;
    private Float offset;
    private Float min;
    private Float max;

    /*  Jackson utilizza either il constructor vuoto (),
    * oppure e' richiesto specificare il field name
    * per ogni attributo.*/
    public Sensor(@JsonProperty("classname") String classname,
                  @JsonProperty("name") String name,
                  @JsonProperty("CANID") String canID,
                  @JsonProperty("byteInterval") String byteInterval,
                  @JsonProperty("gain") String gain,
                  @JsonProperty("offset") String offset,
                  @JsonProperty("min") String min,
                  @JsonProperty("max") String max) {
        this.classname = classname;
        this.name = name;
        this.canID = canID;
        this.byteInterval = toIntArray(byteInterval);
        this.gain = Float.parseFloat(gain);
        this.offset = Float.parseFloat(offset);
        if(min.equals("NULL")){
            this.min = Float.MIN_VALUE;
        }else{
            this.min = Float.parseFloat(min);
        }
        if(min.equals("NULL")){
            this.min = Float.MAX_VALUE;
        }else{
            this.max = Float.parseFloat(max);
        }
    }

    private Integer[] toIntArray(String byteInterval){
            String[] strings = byteInterval.replaceAll("\\[", "")
                    .replaceAll("]", "")
                    .replaceAll(" ", "")
                    .split(",");


        Integer[] array = new Integer[strings.length];

        for (int i = 0; i < strings.length; ++i) {
            try {
                array[i] = Integer.parseInt(strings[i]);
            } catch (NumberFormatException nfe) {
            }
        }

        return array;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCanID() {
        return canID;
    }

    public void setCanID(String canID) {
        this.canID = canID;
    }

    public Integer[] getByteInterval() {
        return byteInterval;
    }

    public void setByteInterval(Integer[] byteInterval) {
        this.byteInterval = byteInterval;
    }

    public Float getGain() {
        return gain;
    }

    public void setGain(Float gain) {
        this.gain = gain;
    }

    public Float getOffset() {
        return offset;
    }

    public void setOffset(Float offset) {
        this.offset = offset;
    }

    public Float getMin() {
        return min;
    }

    public void setMin(Float min) {
        this.min = min;
    }

    public Float getMax() {
        return max;
    }

    public void setMax(Float max) {
        this.max = max;
    }
    @Override
    public String toString() {
        return "Sensor{" +
                "classname='" + classname + '\'' +
                ", name='" + name + '\'' +
                ", canID='" + canID + '\'' +
                ", byteInterval=" + Arrays.toString(byteInterval) +
                ", gain=" + gain +
                ", offset=" + offset +
                ", min=" + min +
                ", max=" + max +
                '}';
    }
}
