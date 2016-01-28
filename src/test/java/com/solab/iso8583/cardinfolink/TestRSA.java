package com.solab.iso8583.cardinfolink;

import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.IsoValue;
import com.solab.iso8583.MessageFactory;
import com.solab.iso8583.NetUtil;
import com.solab.iso8583.util.HexCodec;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
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
        IsoValue<?> v = req.getField(60);
        if (v !=null){
            v.setNeedBcd(true);
        }

        // 2 bytes for storing length
        ByteBuffer byteBuffer = req.writeToBuffer(2);
        return byteBuffer.array();
    }

    @Test
    public void testRSA() throws IOException {
        byte[] request = getBytes();
        System.out.println(HexCodec.hexEncode(request, 0, request.length));
        byte[] response = NetUtil.callServer(request);
        System.out.println(HexCodec.hexEncode(response, 0, response.length));
    }
}
