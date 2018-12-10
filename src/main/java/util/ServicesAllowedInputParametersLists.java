package util;

import java.util.ArrayList;
import java.util.List;
import static util.Constants.*;
import static util.Constants.USER_ID;

public final class ServicesAllowedInputParametersLists {
    public static final List<String> addMeterServiceAllowedInputParameters = new ArrayList<>();
    public static final List<String> getMetersServiceAllowedInputParameters = new ArrayList<>();
    public static final List<String> deleteMeterServiceAllowedInputParameters = new ArrayList<>();
    public static final List<String> editMeterServiceAllowedInputParameters = new ArrayList<>();
    public static final List<String> authServiceAllowedInputParameters = new ArrayList<>();
    public static final List<String> getInvoicesServiceAllowedInputParameters = new ArrayList<>();
    public static final List<String> addInvoiceServiceAllowedInputParameters = new ArrayList<>();
    public static final List<String> deleteInvoiceServiceAllowedInputParameters = new ArrayList<>();
    public static final List<String> payInvoiceServiceAllowedInputParameters = new ArrayList<>();
    public static final List<String> switchLanguageServiceAllowedInputParameters = new ArrayList<>();
    public static final List<String> getAddressesServiceAllowedInputParameters = new ArrayList<>();
    public static final List<String> addAddressServiceAllowedInputParameters = new ArrayList<>();
    public static final List<String> deleteAddressServiceAllowedInputParameters = new ArrayList<>();
    public static final List<String> editAddressServiceAllowedInputParameters = new ArrayList<>();
    public static final List<String> getMeasurementsServiceAllowedInputParameters = new ArrayList<>();
    public static final List<String> addMeasurementServiceAllowedInputParameters = new ArrayList<>();
    public static final List<String> deleteMeasurementServiceAllowedInputParameters = new ArrayList<>();
    public static final List<String> editMeasurementServiceAllowedInputParameters = new ArrayList<>();
    public static final List<String> addMeterReaderServiceAllowedInputParameters = new ArrayList<>();
    public static final List<String> deleteMeterReaderServiceAllowedInputParameters = new ArrayList<>();
    public static final List<String> editMeterReaderServiceAllowedInputParameters = new ArrayList<>();
    public static final List<String> getDataFromMeterReaderServiceAllowedInputParameters = new ArrayList<>();
    public static final List<String> addResourceServiceAllowedInputParameters = new ArrayList<>();
    public static final List<String> editResourceServiceAllowedInputParameters = new ArrayList<>();
    public static final List<String> deleteResourceServiceAllowedInputParameters = new ArrayList<>();
    public static final List<String> addStreetServiceAllowedInputParameters = new ArrayList<>();
    public static final List<String> editStreetServiceAllowedInputParameters = new ArrayList<>();
    public static final List<String> deleteStreetServiceAllowedInputParameters = new ArrayList<>();
    public static final List<String> addUserServiceAllowedInputParameters = new ArrayList<>();
    public static final List<String> registerUserServiceAllowedInputParameters = new ArrayList<>();
    public static final List<String> editUserServiceAllowedInputParameters = new ArrayList<>();
    public static final List<String> deleteUserServiceAllowedInputParameters = new ArrayList<>();

    static {
        addMeterServiceAllowedInputParameters.add(METER_ID);
        addMeterServiceAllowedInputParameters.add(METER_NUMBER);
        addMeterServiceAllowedInputParameters.add(METER_READER_ID);
        addMeterServiceAllowedInputParameters.add(RESOURCE_ID);
        addMeterServiceAllowedInputParameters.add(ADDRESS_ID);
        addMeterServiceAllowedInputParameters.add(TRANSFER_ADDRESS_ID);

        getMetersServiceAllowedInputParameters.add(ADDRESS_ID);
        getMetersServiceAllowedInputParameters.add(USER_ID);
        getMetersServiceAllowedInputParameters.add(SECRET_KEY);

        deleteMeterServiceAllowedInputParameters.add(METER_ID);

        editMeterServiceAllowedInputParameters.add(METER_ID);
        editMeterServiceAllowedInputParameters.add(ADDRESS_ID);

        authServiceAllowedInputParameters.add(USER_LOGIN);
        authServiceAllowedInputParameters.add(USER_PASSWORD);

        getInvoicesServiceAllowedInputParameters.add(USER_ID);
        getInvoicesServiceAllowedInputParameters.add(SECRET_KEY);

        addInvoiceServiceAllowedInputParameters.add(MEASUREMENT_START_VALUE);
        addInvoiceServiceAllowedInputParameters.add(MEASUREMENT_END_VALUE);
        addInvoiceServiceAllowedInputParameters.add(USER_ID);
        addInvoiceServiceAllowedInputParameters.add(MEASUREMENT_START_VALUE);
        addInvoiceServiceAllowedInputParameters.add(METER_ID);
        addInvoiceServiceAllowedInputParameters.add(INVOICE_ID);
        addInvoiceServiceAllowedInputParameters.add(ADDRESS_ID);

        deleteInvoiceServiceAllowedInputParameters.add(INVOICE_ID);

        payInvoiceServiceAllowedInputParameters.add(INVOICE_ID);

        switchLanguageServiceAllowedInputParameters.add(LANGUAGE_ID);

        getAddressesServiceAllowedInputParameters.add(USER_ID);
        getAddressesServiceAllowedInputParameters.add(SECRET_KEY);

        addAddressServiceAllowedInputParameters.add(ADDRESS_ID);
        addAddressServiceAllowedInputParameters.add(ADDRESS_BUILDING);
        addAddressServiceAllowedInputParameters.add(ADDRESS_FLAT);
        addAddressServiceAllowedInputParameters.add(USER_ID);
        addAddressServiceAllowedInputParameters.add(STREET_ID);
        addAddressServiceAllowedInputParameters.add(TRANSFER_USER_ID);

        deleteAddressServiceAllowedInputParameters.add(ADDRESS_ID);

        editAddressServiceAllowedInputParameters.add(ADDRESS_ID);
        editAddressServiceAllowedInputParameters.add(USER_ID);

        getMeasurementsServiceAllowedInputParameters.add(METER_ID);
        getMeasurementsServiceAllowedInputParameters.add(ADDRESS_ID);
        getMeasurementsServiceAllowedInputParameters.add(USER_ID);
        getMeasurementsServiceAllowedInputParameters.add(SECRET_KEY);

        addMeasurementServiceAllowedInputParameters.add(MEASUREMENT_ID);
        addMeasurementServiceAllowedInputParameters.add(MEASUREMENT_DATE_TIME);
        addMeasurementServiceAllowedInputParameters.add(MEASUREMENT_VALUE);
        addMeasurementServiceAllowedInputParameters.add(METER_ID);

        deleteMeasurementServiceAllowedInputParameters.add(MEASUREMENT_ID);

        editMeasurementServiceAllowedInputParameters.add(MEASUREMENT_ID);
        editMeasurementServiceAllowedInputParameters.add(METER_ID);

        addMeterReaderServiceAllowedInputParameters.add(METER_READER_ID);
        addMeterReaderServiceAllowedInputParameters.add(METER_READER_NUMBER);
        addMeterReaderServiceAllowedInputParameters.add(METER_READER_IP_ADDRESS);
        addMeterReaderServiceAllowedInputParameters.add(METER_READER_PORT);

        deleteMeterReaderServiceAllowedInputParameters.add(METER_READER_ID);

        editMeterReaderServiceAllowedInputParameters.add(METER_READER_ID);

        getDataFromMeterReaderServiceAllowedInputParameters.add(METER_READER_ID);

        addResourceServiceAllowedInputParameters.add(RESOURCE_ID);
        addResourceServiceAllowedInputParameters.add(RESOURCE_NAME);
        addResourceServiceAllowedInputParameters.add(RESOURCE_COST);

        editResourceServiceAllowedInputParameters.add(RESOURCE_ID);

        deleteResourceServiceAllowedInputParameters.add(RESOURCE_ID);

        addStreetServiceAllowedInputParameters.add(STREET_ID);
        addStreetServiceAllowedInputParameters.add(STREET_NAME);

        editStreetServiceAllowedInputParameters.add(STREET_ID);

        deleteStreetServiceAllowedInputParameters.add(STREET_ID);

        addUserServiceAllowedInputParameters.add(USER_ID);
        addUserServiceAllowedInputParameters.add(USER_LOGIN);
        addUserServiceAllowedInputParameters.add(USER_PASSWORD);
        addUserServiceAllowedInputParameters.add(USER_FIRST_NAME);
        addUserServiceAllowedInputParameters.add(USER_LAST_NAME);
        addUserServiceAllowedInputParameters.add(USER_PHONE);
        addUserServiceAllowedInputParameters.add(USER_EMAIL);
        addUserServiceAllowedInputParameters.add(USER_PERSONAL_ACCOUNT);

        registerUserServiceAllowedInputParameters.add(USER_ID);
        registerUserServiceAllowedInputParameters.add(USER_LOGIN);
        registerUserServiceAllowedInputParameters.add(USER_PASSWORD);
        registerUserServiceAllowedInputParameters.add(USER_PHONE);
        registerUserServiceAllowedInputParameters.add(USER_EMAIL);

        editUserServiceAllowedInputParameters.add(USER_ID);

        deleteUserServiceAllowedInputParameters.add(USER_ID);
    }

    private ServicesAllowedInputParametersLists(){}
}
