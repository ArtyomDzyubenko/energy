package util;

import exception.DAOException;
import model.MeterEntity;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public final class MeterReaderSocket {
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
    private static final Checksum checksum = new Checksum();
    private static final StringBuilder stringBuilder = new StringBuilder();
    private static final MeterReaderParser parser = MeterReaderParser.getInstance();
    private static MeterReaderSocket socket;

    private MeterReaderSocket(){}

    public static synchronized MeterReaderSocket getInstance() {
        if(socket ==null){
            socket = new MeterReaderSocket();
        }

        return socket;
    }

    public List<MeterEntity> getMetersFromMeterReader(String IPAddress, int port) throws IOException, DAOException {
        List<String> data = getDataFromMeterReader(IPAddress, port);
        List<MeterEntity> meters = new ArrayList<>();

        for (String dataString: data) {
            if (isDataStringValid(dataString) && parser.getTariffNumber(dataString) == FIRST_TARIFF_NUMBER) {
                MeterEntity meter = new MeterEntity();
                meter.setNumber(parser.getMeterNumber(dataString));
                meter.getMeasurement().setDateTime(parser.getDate(dataString));
                meter.getMeasurement().setValue((double)parser.getMeasurementValue(dataString));
                meters.add(meter);
            }
        }

        return meters;
    }

    private List<String> getDataFromMeterReader(String IPAddress, int port) throws IOException, DAOException {
        List<String> outputData = new ArrayList<>();

        try(Socket socket = new Socket(IPAddress, port)) {
            socket.setSoTimeout(SOCKET_TIMEOUT);
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();

            if (!checkConnectionBusy(outputStream, inputStream)){
                throw new IOException(Localization.getLocalization().getString("connectionBusy"));
            }

            List<String> commandsList = makeReadDataCommandsList(getRecordsNumber(outputStream, inputStream));
            int commandCounter = 0;

            for (String command : commandsList) {
                write(outputStream, command.getBytes());
                commandCounter++;

                if ((commandCounter % MAX_STRINGS_FOR_ITERATION == 0) || commandCounter == commandsList.size()) {
                    for (int i = 0; i < MAX_STRINGS_FOR_ITERATION; i++) {
                        outputData.add(read(inputStream));
                    }
                }
            }
        } catch (ConnectException e){
            throw new IOException(Localization.getLocalization().getString("connectionFailed"));
        } catch (SocketTimeoutException e){
            return outputData;
        }

        return outputData;
    }

    private static boolean checkConnectionBusy(OutputStream outputStream, InputStream inputStream) throws IOException {
        byte[] buffer = new byte[READ_BUFFER_SIZE];
        outputStream.write(GET_METER_READER_NUMBER_COMMAND.getBytes());
        int bytesCount = inputStream.read(buffer);

        return bytesCount != -1;
    }

    private void write(OutputStream stream, byte[] data) throws IOException {
        stream.write(data);
    }

    private String read(InputStream stream) throws IOException {
        String out = "";
        byte[] buffer = new byte[READ_BUFFER_SIZE];

        int readByteCount = stream.read(buffer);

        if(readByteCount > 0){
            out = new String(buffer, 0, readByteCount).trim().replace(" ", "");
        }

        return out;
    }

    private List<String> makeReadDataCommandsList(int recordsNumbers){
        int memoryAddress = 0;

        MeterReaderParser parser = MeterReaderParser.getInstance();
        StringBuilder out = new StringBuilder();
        StringBuilder checksum = new StringBuilder();
        List<String> commands = new ArrayList<>();

        for (int recordNumber = 0; recordNumber < recordsNumbers; recordNumber++){
            out.setLength(0);
            String byteNumberString = out.append(String.format("%1$04X", recordNumber)).reverse().toString();
            out.setLength(0);
            String memoryAddressString = out.append(String.format("%1$02X", memoryAddress)).reverse().toString(); //"%1$02X", "%1$04X" save significant zeros

            memoryAddress++;

            if (memoryAddress == LAST_MEMORY_ADDRESS) {
                memoryAddress=0;
            }

            out.setLength(0);
            out.append(DATA_HEADER).append(memoryAddressString).append(READ_DATA_COMMAND_ID).append(byteNumberString);
            String outString = out.toString();

            String forCalculateChecksum = parser.swapStringSymbols(outString);

            checksum.setLength(0);
            checksum.append(Integer.toHexString(calculateChecksum(parser.stringToByteArray(forCalculateChecksum))).toUpperCase()).reverse().append("\n");

            out.setLength(0);
            out.append(outString).append(checksum.toString());
            commands.add(out.toString());
        }

        return commands;
    }

    private int calculateChecksum(byte[] bytes){
        for (int position : bytes) {
            checksum.update(position);
        }

        int result = (int)checksum.getValue();
        checksum.reset();

        return result;
    }

    private int getRecordsNumber(OutputStream outputStream, InputStream inputStream) throws IOException {
        outputStream.write(READ_STRINGS_NUMBER_COMMAND.getBytes());
        byte[] buffer = new byte[READ_STRINGS_NUMBER_BUFFER_SIZE];
        int bytesCount = inputStream.read(buffer);

        if (bytesCount > 0) {
            String input = new String(buffer, 0, bytesCount).trim().substring(8, 12);
            stringBuilder.setLength(0);
            stringBuilder.append(input).reverse();
            return Integer.parseInt(stringBuilder.toString(), 16);
        }

        return 0;
    }

    private boolean isDataStringValid(String dataString){
        if ((dataString.length() != DATA_STRING_LENGTH) || (!dataString.startsWith(DATA_HEADER))){
            return false;
        }

        byte[] data = parser.stringToByteArray(parser.swapStringSymbols(parser.getDataString(dataString)));
        stringBuilder.setLength(0);
        String calculatedChecksum = stringBuilder.append(Integer.toHexString(calculateChecksum(data))).reverse().toString().toUpperCase();
        String originalChecksum = parser.getChecksum(dataString);

        return calculatedChecksum.equals(originalChecksum);
    }
}
