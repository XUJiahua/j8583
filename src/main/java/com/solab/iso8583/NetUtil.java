package com.solab.iso8583;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class NetUtil {
    public static final String IP = "192.168.1.102";
    public static final int PORT = 5811;


    public static byte[] callServer(byte[] request) throws IOException {
        Socket socket = null;
        DataOutputStream outputStream = null;
        DataInputStream inputStream = null;
        try {
            socket = new Socket(IP, PORT);
            outputStream = new DataOutputStream(socket.getOutputStream());
            inputStream = new DataInputStream(socket.getInputStream());
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            return null;
        }

        if (socket != null && outputStream != null && inputStream != null) {
            try {
                outputStream.write(request, 0, request.length);

                byte[] resBytes = new byte[2048];//TODO: 缓冲区2048可能不够
                int ret = inputStream.read(resBytes);
                if (ret == -1) {
                    System.out.println("No message returned");
                    return new byte[0];
                } else {
                    byte[] response = new byte[ret];
                    System.arraycopy(resBytes, 0, response, 0, ret);
                    return response;
                }
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
                return null;
            } finally {
                outputStream.close();
                inputStream.close();
                socket.close();
            }
        }

        return null;
    }
}
