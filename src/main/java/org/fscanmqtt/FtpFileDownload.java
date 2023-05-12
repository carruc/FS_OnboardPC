package org.fscanmqtt;
import org.apache.commons.net.ftp.FTPClient;

import java.io.FileOutputStream;
import java.io.IOException;

public class FtpFileDownload {
    public static void main(String[] args) {
        FTPClient client = new FTPClient();

        FileOutputStream fos = null;
        try {
            client.connect("ftp.javacodegeeks.com");
            client.login("username", "password");

// Create an OutputStream for the file
            String filename = "test.txt";
            fos = new FileOutputStream(filename);

// Fetch file from server

            client.retrieveFile("/" + filename, fos);

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            try {
                if (fos != null) {
                    fos.close();

                }
                client.disconnect();

            } catch (IOException e) {
                e.printStackTrace();

            }

        }

    }
}
