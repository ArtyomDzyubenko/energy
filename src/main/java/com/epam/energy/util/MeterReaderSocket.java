package com.epam.energy.util;

import com.epam.energy.exception.DAOException;
import com.epam.energy.model.Meter;
import com.epam.energy.service.LanguageService;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import static com.epam.energy.util.Constants.EMPTY_STRING;
import static com.epam.energy.util.Constants.INT_ZERO;
import static com.epam.energy.util.Constants.SPACE;

public final class MeterReaderSocket {
    private static MeterReaderSocket instance;
    private static final int MAX_STRINGS_FOR_ITERATION = 30;
    private static final int SOCKET_TIMEOUT = 5000; //milliseconds
    private static final int READ_STRINGS_NUMBER_BUFFER_SIZE = 16;
    private static final int READ_BUFFER_SIZE = 42;
    private static final int DATA_STRING_LENGTH = 38;
    private static final int LAST_MEMORY_ADDRESS = 104;
    private static final int FIRST_TARIFF_NUMBER = 1;
    private static final String DATA_HEADER = "F5";
    private static final String READ_DATA_COMMAND_ID = "5040";
    private static final String READ_STRINGS_NUMBER_COMMAND = "850030402177\n";
    private static final String GET_METER_READER_NUMBER_COMMAND = "2410201558\n";
    private static final String BYTE_NUMBER_STRING_FORMAT = "%1$04X"; //"%1$02X", "%1$04X" save significant zeros
    private static final String MEMORY_ADDRESS_STRING_FORMAT = "%1$02X";
    private static final int START_RECORDS_NUMBER_STRING_POSITION = 8;
    private static final int END_RECORDS_NUMBER_STRING_POSITION = 12;
    private StringBuilder stringBuilder = new StringBuilder();
    private Checksum checksum = Checksum.getInstance();
    private MeterReaderParser parser = MeterReaderParser.getInstance();

    private MeterReaderSocket() {}

    public static synchronized MeterReaderSocket getInstance() {
        if (instance == null) {
            instance = new MeterReaderSocket();
        }

        return instance;
    }

    public List<Meter> getMetersFromMeterReader(String IPAddress, int port) throws IOException, DAOException {
        List<String> data = getDataFromMeterReader(IPAddress, port);
        List<Meter> meters = new ArrayList<>();

        for (String dataString: data) {
            if (isDataStringValid(dataString) && (parser.getTariffNumber(dataString) == FIRST_TARIFF_NUMBER)) {
                Meter meter = new Meter();
                meter.setNumber(parser.getMeterNumber(dataString));
                meter.getMeasurement().setDateTime(parser.getDate(dataString));
                meter.getMeasurement().setValue((double)parser.getMeasurementValue(dataString));
                meters.add(meter);
            }
        }

        return meters;
    }

    private List<String> getDataFromMeterReader(String IPAddress, int port) throws IOException, DAOException {
        List<String> out = new ArrayList<>();

        try(Socket socket = new Socket(IPAddress, port)) {
            socket.setSoTimeout(SOCKET_TIMEOUT);
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();

            checkConnectionBusy(outputStream, inputStream);

            getDataFromInputStream(out, outputStream, inputStream);
        } catch (ConnectException e) {
            throw new IOException(getErrorLocalization("connectionFailed"));
        } catch (SocketTimeoutException e) {
            return out;
        }

        return out;
    }

    private void getDataFromInputStream(List<String> out, OutputStream outputStream, InputStream inputStream) throws IOException {
        List<String> commandsList = getReadDataCommandsList(getRecordsNumber(outputStream, inputStream));
        int commandCounter = 0;

        for (String command : commandsList) {
            write(outputStream, command.getBytes());
            commandCounter++;

            if ((commandCounter % MAX_STRINGS_FOR_ITERATION == 0) || (commandCounter == commandsList.size())) {
                for (int i = 0; i < MAX_STRINGS_FOR_ITERATION; i++) {
                    out.add(read(inputStream));
                }
            }
        }
    }

    private void checkConnectionBusy(OutputStream outputStream, InputStream inputStream) throws IOException, DAOException {
        byte[] buffer = new byte[READ_BUFFER_SIZE];
        outputStream.write(GET_METER_READER_NUMBER_COMMAND.getBytes());
        int bytesCount = inputStream.read(buffer);

        if (bytesCount < 0){
            throw new IOException(getErrorLocalization("connectionBusy"));
        }
    }

    private void write(OutputStream stream, byte[] data) throws IOException {
        stream.write(data);
    }

    private String read(InputStream stream) throws IOException {
        String out = EMPTY_STRING;
        byte[] buffer = new byte[READ_BUFFER_SIZE];

        int readByteCount = stream.read(buffer);

        if (readByteCount > 0) {
            out = new String(buffer, 0, readByteCount).trim().replace(SPACE, EMPTY_STRING);
        }

        return out;
    }

    private List<String> getReadDataCommandsList(int recordsNumbers) {
        MeterReaderParser parser = MeterReaderParser.getInstance();
        StringBuilder out = new StringBuilder();
        StringBuilder checksum = new StringBuilder();
        List<String> commands = new ArrayList<>();

        int memoryAddress = 0;

        for (int recordNumber = 0; recordNumber < recordsNumbers; recordNumber++) {
            out.setLength(0);
            String byteNumberString = out.append(String.format(BYTE_NUMBER_STRING_FORMAT, recordNumber)).reverse().toString();
            out.setLength(0);
            String memoryAddressString = out.append(String.format(MEMORY_ADDRESS_STRING_FORMAT, memoryAddress)).reverse().toString();

            memoryAddress++;

            if (memoryAddress == LAST_MEMORY_ADDRESS) {
                memoryAddress = 0;
            }

            out.setLength(0);
            String dataString = out.append(DATA_HEADER).append(memoryAddressString).append(READ_DATA_COMMAND_ID).append(byteNumberString).toString();
            String swappedDataString = parser.swapStringSymbols(dataString);

            checksum.setLength(0);
            String checksumString = getChecksumString(parser.stringToByteArray(swappedDataString));
            checksum.append(checksumString).reverse().append("\n");

            out.setLength(0);
            out.append(dataString).append(checksum.toString());
            commands.add(out.toString());
        }

        return commands;
    }

    private String getChecksumString(byte[] bytes) {
        for (int position : bytes) {
            checksum.update(position);
        }

        int result = (int)checksum.getValue();
        checksum.reset();

        return Integer.toHexString(result).toUpperCase();
    }

    private int getRecordsNumber(OutputStream outputStream, InputStream inputStream) throws IOException {
        outputStream.write(READ_STRINGS_NUMBER_COMMAND.getBytes());
        byte[] buffer = new byte[READ_STRINGS_NUMBER_BUFFER_SIZE];
        int bytesCount = inputStream.read(buffer);

        if (bytesCount > 0) {
            String input = new String(buffer, 0, bytesCount).trim()
                    .substring(START_RECORDS_NUMBER_STRING_POSITION, END_RECORDS_NUMBER_STRING_POSITION);
            stringBuilder.setLength(0);
            stringBuilder.append(input).reverse();

            return Integer.parseInt(stringBuilder.toString(), 16);
        }

        return INT_ZERO;
    }

    private boolean isDataStringValid(String inputString) {
        if ((inputString.length() != DATA_STRING_LENGTH) || (!inputString.startsWith(DATA_HEADER))) {
            return false;
        }

        String dataString = parser.getDataString(inputString);
        String swappedDataString = parser.swapStringSymbols(dataString);

        byte[] data = parser.stringToByteArray(swappedDataString);
        stringBuilder.setLength(0);
        String calculatedChecksum = stringBuilder.append(getChecksumString(data)).reverse().toString().toUpperCase();
        String originalChecksum = parser.getChecksum(inputString);

        return calculatedChecksum.equals(originalChecksum);
    }

    private String getErrorLocalization(String key) throws DAOException {
        return LanguageService.getInstance().getLocalization().getString(key);
    }
}
