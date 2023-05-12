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


    public SerialPortReader(String portName, int baudRate, int dataBits, int stopBits, int parity) {
        this.serialPort = new SerialPort(portName);
        this.baudRate = baudRate;
        this.dataBits = dataBits;
        this.stopBits = stopBits;
        this.parity = parity;
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

    }
}