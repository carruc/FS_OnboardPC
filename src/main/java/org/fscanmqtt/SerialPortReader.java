package org.fscanmqtt;


import jssc.SerialPort;
import jssc.SerialPortException;

import java.util.HashMap;
import java.util.Map;

public class SerialPortReader extends Thread{
    private final SerialPort serialPort;
    private final int baudRate;
    private final int dataBits;
    private final int stopBits;
    private final int parity;
    private final Map<String, Integer> sharedStatusCar;


    public SerialPortReader(String portName, int baudRate, int dataBits, int stopBits, int parity) {
        this.serialPort = new SerialPort(portName);
        this.baudRate = baudRate;
        this.dataBits = dataBits;
        this.stopBits = stopBits;
        this.parity = parity;
        sharedStatusCar = new HashMap<>();
    }

    public void connect() {
        try {
            serialPort.openPort();
            serialPort.setParams(baudRate, dataBits, stopBits, parity);
        } catch (SerialPortException ex) {
            System.out.println("Error: " + ex);
        }
    }

    public byte[] readData(int byteCount) throws SerialPortException {
        byte[] buffer;
        buffer = serialPort.readBytes(byteCount);
            /*
            semplice ciclo per stampare tutti i byte letti
            for(byte b : buffer) {
                System.out.print((char) b);
            }
             */
        return buffer;

    }

    public void disconnect() {
        try {
            serialPort.closePort();
        } catch (SerialPortException ex) {
            System.out.println("Error: " + ex);
        }
    }

    @Override
    public void run() {
        synchronized(sharedStatusCar) {
            /* dentro questo metodo inseriamo ciò che il thread esegue alla chiamata della start() */
            connect();

            while(true){
                try {
                    readData(21);
                    sharedStatusCar.put("sensore di prova", 42);
                    System.out.println(sharedStatusCar.toString());
                } catch (SerialPortException e) {
                    disconnect();
                    throw new RuntimeException("ERROR: error in with readData function\n" + e);
                }
            }
        }
    }
}