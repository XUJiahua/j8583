package com.solab.iso8583.cardinfolink;

import com.solab.iso8583.CustomBinaryField;
import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.MessageFactory;
import com.solab.iso8583.codecs.CompositeField;
import com.solab.iso8583.util.Bcd;
import com.solab.iso8583.util.HexCodec;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;

public class TestRSA {
    private static String TPDU = "6004010000";
    private MessageFactory<IsoMessage> mf;

    @Before
    public void setup() throws IOException {
        mf = new MessageFactory<>();
        mf.setCharacterEncoding("UTF-8");
        mf.setConfigPath("config.xml");
        mf.setUseBinaryBitmap(true);//bitmap使用二进制形式
        mf.setUseBinaryMessages(true);
    }

    private byte[] getBytes() {
        IsoMessage req = mf.newMessage(0x800);
        // 2 bytes for storing length
        ByteBuffer byteBuffer = req.writeToBuffer(2);
        return byteBuffer.array();
    }

    @Test
    public void testRSA() throws IOException {
        Socket socket = null;
        DataOutputStream outputStream = null;
        DataInputStream inputStream = null;
        try {
            socket = new Socket("192.168.1.102", 5811);
            outputStream = new DataOutputStream(socket.getOutputStream());
            inputStream = new DataInputStream(socket.getInputStream());
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }

        if (socket != null && outputStream != null && inputStream != null) {
            try {
                byte[] bytes2 = getBytes();
                byte[] bytes = CilRateRecUtil.str2Bcd("003C600000973360220000000008000020000000C00012021806323031323132303653706563526F75746531323132303600110000000100100003303033");
                outputStream.write(bytes, 0, bytes.length);

                System.out.println(HexCodec.hexEncode(bytes2, 0, bytes2.length));
                System.out.println(HexCodec.hexEncode(bytes, 0, bytes.length));

                byte[] resBytes = new byte[2048];//2048可能不够
                int ret = inputStream.read(resBytes);
                if (ret == -1) {
                    System.out.println("No message returned");
                } else {
                    byte[] response = new byte[ret];
                    System.arraycopy(resBytes, 0, response, 0, ret);
                    String s = HexCodec.hexEncode(response, 0, response.length);
                    System.out.println(s);
                }
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            } finally {
                outputStream.close();
                inputStream.close();
                socket.close();
            }
        }
    }
}
