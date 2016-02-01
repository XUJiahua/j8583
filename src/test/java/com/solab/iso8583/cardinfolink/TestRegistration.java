package com.solab.iso8583.cardinfolink;

import com.solab.iso8583.*;
import com.solab.iso8583.parse.*;
import com.solab.iso8583.util.HexCodec;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.util.HashMap;

public class TestRegistration {
    private final static String TPDU = "6004010000";
    private final static String ISO_HEADER = "602200000000";
    private MessageFactory<IsoMessage> mf;

    @Before
    public void setup() throws IOException {
        mf = new MessageFactory<>();
        mf.setCharacterEncoding("UTF-8");
        mf.setIsoHeader(0x800, ISO_HEADER);

        // request template
        IsoMessage tmpl = new IsoMessage();
        tmpl.setType(0x800);
        tmpl.setValue(11, "021806", IsoType.NUMERIC,6); // Default to BCD
        tmpl.setValue(41,"20121206", IsoType.ALPHA, 8); // Default to ASCII
        tmpl.setValue(42,"SpecRoute121206", IsoType.ALPHA, 15); // Default to ASCII
        tmpl.setValue(60,"00000001001",IsoType.LLLVAR, 11, EncodingType.VAR_ENCODING_BCD); //BCD
        tmpl.setValue(63,"003",IsoType.LLLVAR,3, EncodingType.VAR_ENCODING_ASCII); //ASCII

        mf.addMessageTemplate(tmpl);

        // response parse guide
        HashMap<Integer, FieldParseInfo> pmap = new HashMap<>();
        pmap.put(11, new NumericParseInfo(6));
        pmap.put(12, new NumericParseInfo(6));
        pmap.put(13, new NumericParseInfo(4));
        pmap.put(32, new LlvarParseInfo()); // BCD
        pmap.put(37, new AlphaParseInfo(12));
        pmap.put(39, new AlphaParseInfo(2));
        pmap.put(41, new AlphaParseInfo(8));
        pmap.put(42, new AlphaParseInfo(15));
        pmap.put(60, new LllvarParseInfo()); // BCD
        pmap.put(62, new LllbinParseInfo());

        mf.setParseMap(0x810, pmap);
    }

    // contain 2 bytes length
    private byte[] getRequestPrefixWithLen() {
        IsoMessage req = mf.newMessage(0x800, TPDU);
        // 2 bytes for storing length
        ByteBuffer byteBuffer = req.writeToBuffer(2);

//        byte[] data = req.writeData();
//        System.out.println(HexCodec.hexEncode(data, 0, data.length));

        return byteBuffer.array();
    }

    @Test
    public void testRegistration() throws IOException, ParseException {
        byte[] request = getRequestPrefixWithLen();
        System.out.println(HexCodec.hexEncode(request, 0, request.length));
        byte[] response = NetUtil.callServer(request); // contains 5 bytes TPDU
        if (response != null) {
            System.out.println(HexCodec.hexEncode(response, 0, response.length));
        }
        // header长度: TPDU 5 bytes, isoHeader 6 bytes
        IsoMessage rep = mf.parseMessage(response);
    }
}
