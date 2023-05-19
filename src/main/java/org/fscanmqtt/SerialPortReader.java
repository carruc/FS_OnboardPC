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
    private final HandleSensors sharedHandleSensors;


    public SerialPortReader(String portName, int baudRate, int dataBits, int stopBits, int parity, HandleSensors sharedHandleSensors) {
        this.serialPort = new SerialPort(portName);
        this.baudRate = baudRate;
        this.dataBits = dataBits;
        this.stopBits = stopBits;
        this.parity = parity;
        this.sharedHandleSensors = sharedHandleSensors;
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
        while (!isInterrupted()){
            try {
                byte[] format = readData(1);
                if (format[0] == 't'){
                    byte[] data = readData(20);
                    for(byte b : data) {System.out.print((char) b);}
                    sharedHandleSensors.updateCarStatus(sharedHandleSensors.getCANIdString(data), sharedHandleSensors.getActualDataArray(data));
                    //sharedHandleSensors.updateCarStatus("0x20C", new byte[] { 0,1,2,3,4,5,6,7 });
                }
                else{
                    System.out.println("ERRORE: format invalid (not t)");
                }
                System.out.println("\n");
            } catch (SerialPortException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }
}