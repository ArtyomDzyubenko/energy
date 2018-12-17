package com.epam.energy.util;

import javax.xml.bind.DatatypeConverter;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public final class MeterReaderParser {
    private static final int DATA_STRING_START_POSITION = 0;
    private static final int DATA_STRING_END_POSITION = 34;
    private static final int CHECKSUM_STRING_START_POSITION = 34;
    private static final int CHECKSUM_STRING_END_POSITION = 38;
    private static final int START_METER_NUMBER_STRING_POSITION = 8;
    private static final int END_METER_NUMBER_STRING_POSITION = 14;
    private static final int START_TARIFF_NUMBER_STRING_POSITION = 16;
    private static final int END_TARIFF_NUMBER_STRING_POSITION = 18;
    private static final int START_SECONDS_STRING_POSITION = 18;
    private static final int END_SECONDS_STRING_POSITION = 26;
    private static final int START_MEASUREMENT_STRING_POSITION = 26;
    private static final int END_MEASUREMENT_STRING_POSITION = 34;
    private static final int OFFSET = 946684800;        //in input stream, time starts from 2000-01-01, but EPOCH time starts from 1970-01-01
    private static final StringBuilder stringBuilder = new StringBuilder();
    private static MeterReaderParser instance;

    private MeterReaderParser() {}

    public static synchronized MeterReaderParser getInstance() {
        if (instance == null) {
            instance = new MeterReaderParser();
        }

        return instance;
    }

    int getMeterNumber(String input) {
        String meterNumberString = input.substring(START_METER_NUMBER_STRING_POSITION, END_METER_NUMBER_STRING_POSITION);
        stringBuilder.setLength(0);
        String out = stringBuilder.append(meterNumberString).reverse().toString();

        return Integer.parseInt(out, 16);
    }

    int getTariffNumber(String input) {
        String tariffNumberString = input.substring(START_TARIFF_NUMBER_STRING_POSITION, END_TARIFF_NUMBER_STRING_POSITION);
        stringBuilder.setLength(0);
        String out = stringBuilder.append(tariffNumberString).reverse().toString();

        return Integer.parseInt(out, 16) + 1;   //tariff number starts from 0
    }

    Timestamp getDate (String input) {
        String secondsString = input.substring(START_SECONDS_STRING_POSITION, END_SECONDS_STRING_POSITION);
        stringBuilder.setLength(0);
        long UNIXSeconds =  Long.parseLong(stringBuilder.append(secondsString).reverse().toString(), 16) + OFFSET;

        return Timestamp.valueOf(LocalDateTime.ofEpochSecond(UNIXSeconds, 0, ZoneOffset.UTC));
    }

    Float getMeasurementValue(String input) {
        String measurementString = input.substring(START_MEASUREMENT_STRING_POSITION, END_MEASUREMENT_STRING_POSITION);
        stringBuilder.setLength(0);
        byte[] bytes = stringToByteArray(stringBuilder.append(measurementString).reverse().toString());

        if ((bytes[0] & (byte)0b00000001)==1) {          //bin -> float IEEE754 standard
            bytes[1] = (byte)(bytes[1] + 0b10000000);
        }

        bytes[0] = (byte)((bytes[0] >>> 1) & 0b01111111);

        stringBuilder.setLength(0);
        String result = stringBuilder.append(byteToBinaryString(bytes[0]))
                .append(byteToBinaryString(bytes[1]))
                .append(byteToBinaryString(bytes[2]))
                .append(byteToBinaryString(bytes[3])).toString();

        return binaryStringToFloat32(result);
    }

    String getDataString(String input) {
        return input.substring(DATA_STRING_START_POSITION, DATA_STRING_END_POSITION);
    }

    String getChecksum(String input) {
        return input.substring(CHECKSUM_STRING_START_POSITION, CHECKSUM_STRING_END_POSITION);
    }

    byte[] stringToByteArray(String s) {
        return DatatypeConverter.parseHexBinary(s);
    }

    private String byteToBinaryString(byte input) {
        return Integer.toBinaryString((input & 0xFF) + 0x100).substring(1);
    }

    String swapStringSymbols(String input) {
        char[] out = input.toCharArray();

        for (int i = 0; i < out.length-1; i += 2) {
            char tmp = out[i];
            out[i] = out[i+1];
            out[i+1] = tmp;
        }

        return String.valueOf(out);
    }

    private Float binaryStringToFloat32(String Binary ) {
        return Float.intBitsToFloat(Integer.parseInt(Binary, 2));
    }
}
