package org.fscanmqtt;

import java.util.HashMap;
import java.util.Map;

public class HandleSensors {
    Map<String, Integer> carStatus;

    public HandleSensors (){
        carStatus = new HashMap<>();
    }
}
