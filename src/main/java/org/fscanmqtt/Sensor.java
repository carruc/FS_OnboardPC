package org.fscanmqtt;

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

    public Sensor(String classname, String name, String canID, Integer[] byteInterval, Float gain, Float offset, Float min, Float max) {
        this.classname = classname;
        this.name = name;
        this.canID = canID;
        this.byteInterval = byteInterval;
        this.gain = gain;
        this.offset = offset;
        this.min = min;
        this.max = max;
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
