package com.solab.iso8583;

import com.solab.iso8583.util.HexCodec;

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

                byte[] resBytes = new byte[2];
                int ret = inputStream.read(resBytes);
                if (ret == -1) {
                    System.out.println("No message returned");
                    return null;
                } else {
                    int length = Integer.parseInt(HexCodec.hexEncode(resBytes, 0, resBytes.length), 16);
                    resBytes = new byte[length];
                    ret = inputStream.read(resBytes);
                    if (ret == length){
                        return resBytes;
                    }
                    return null;
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
