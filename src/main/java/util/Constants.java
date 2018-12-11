package util;

public final class Constants {
    //RESOURCE BUNDLES NAMES
    public static final String localizationBundleName = "Locale";
    public static final String errorBundleName = "DAOError";

    //COMMON
    public static final String EMPTY_STRING = "";
    public static final String DATE_TIME_DELIMITER = "T";
    public static final String SPACE = " ";
    public static final Long LONG_ZERO = 0L;
    public static final Integer INT_ZERO = 0;
    public static final Double DOUBLE_ZERO = 0.0;
    public static final String AUTH_USER = "authUser";
    public static final String SECRET_KEY = "sKey";
    public static final String GET_METHOD = "GET";
    public static final String POST_METHOD = "POST";

    //VALIDATION
    public static final int STRING_MAX_LENGTH = 32;
    public static final int BUILDING_STRING_MAX_LENGTH = 4;
    public static final int FLAT_STRING_MAX_LENGTH = 4;
    public static final int IP_ADDRESS_STRING_MAX_LENGTH = 15;
    public static final int MIN_PORT_NUMBER = 1;
    public static final int MAX_PORT_NUMBER = 65535;
    public static final Long MIN_PHONE_NUMBER = 7000000000L;
    public static final Long MAX_PHONE_NUMBER = 7999999999L;
    public static final String EMAIL_REGEX = "([.[^@\\s]]+)@([.[^@\\s]]+)\\.([a-z]+)";
    public static final String IP_ADDRESS_REGEX = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
    public static final String USER_FIRST_LAST_NAME_REGEX = "[a-zA-Zа-яА-ЯЁё]+";
    public static final String USER_LOGIN_REGEX = "[a-zA-Z0-9]+";

    //JSP
    public static final String RESOURCES_DIR = "/res";
    public static final String INDEX_JSP = "/index.jsp";
    public static final String ADDRESSES_JSP = "/addresses.jsp";
    public static final String USERS_JSP = "/users.jsp";
    public static final String INVOICES_JSP = "/invoices.jsp";
    public static final String MEASUREMENTS_JSP = "/measurements.jsp";
    public static final String METERS_JSP = "/meters.jsp";
    public static final String METER_READERS_JSP = "/meterReaders.jsp";
    public static final String METER_READER_DATA = "/meterReaderData.jsp";
    public static final String RESOURCES_JSP =  "/resources.jsp";
    public static final String STREETS_JSP = "/streets.jsp";
    public static final String REGISTER_USER_JSP = "/registerUser.jsp";
    public static final String ERROR_JSP = "/error.jsp";

    //ENTITIES
    public static final String ID = "id";

    public static final String USER_ID = "userId";
    public static final String TRANSFER_USER_ID = "transferUserId";
    public static final String USER_LOGIN = "login";
    public static final String USER_PASSWORD = "password";
    public static final String USER_FIRST_NAME = "firstName";
    public static final String USER_LAST_NAME = "lastName";
    public static final String USER_PHONE = "phone";
    public static final String USER_EMAIL = "email";
    public static final String USER_PERSONAL_ACCOUNT = "personalAccount";
    public static final String USER_IS_ADMIN = "isAdmin";

    public static final String ADDRESS_ID = "addressId";
    public static final String TRANSFER_ADDRESS_ID = "transferAddressId";
    public static final String ADDRESS_BUILDING = "building";
    public static final String ADDRESS_FLAT = "flat";

    public static final String STREET_ID ="streetId";
    public static final String STREET_NAME = "name";

    public static final String INVOICE_ID ="invoiceId";
    public static final String INVOICE_DATE = "date";
    public static final String INVOICE_METER_ID = "meterId";
    public static final String INVOICE_START_MEASUREMENT_ID = "startPeriodId";
    public static final String INVOICE_END_MEASUREMENT_ID = "endPeriodId";
    public static final String INVOICE_CONSUMPTION = "consumption";
    public static final String INVOICE_PRICE = "price";
    public static final String INVOICE_USER_ID = "userId";
    public static final String INVOICE_IS_PAID = "isPaid";

    public static final String METER_ID = "meterId";
    public static final String METER_NUMBER = "number";

    public static final String METER_READER_ID = "meterReaderId";
    public static final String METER_READER_NUMBER = "readerNumber";
    public static final String METER_READER_IP_ADDRESS = "IPAddress";
    public static final String METER_READER_PORT = "port";

    public static final String MEASUREMENT_ID = "measurementId";
    public static final String MEASUREMENT_DATE_TIME = "dateTime";
    public static final String MEASUREMENT_LOCAL_DATE_TIME_STRING = "localDateTimeString";
    public static final String MEASUREMENT_VALUE =  "value";
    public static final String MEASUREMENT_START_VALUE = "startValue";
    public static final String MEASUREMENT_END_VALUE = "endValue";

    public static final String RESOURCE_ID = "resourceId";
    public static final String RESOURCE_NAME = "name";
    public static final String RESOURCE_COST = "cost";

    public static final String LANGUAGE_ID = "languageId";
    public static final String LANGUAGE_NAME = "name";
    public static final String LANGUAGE_CODE = "code";
    public static final String LANGUAGE_COUNTRY = "country";

    //REQUEST ATTRIBUTES NAMES
    public static final String USER_ATTRIBUTE_NAME = "user";
    public static final String USERS_ATTRIBUTE_NAME = "users";

    public static final String ADDRESS_ATTRIBUTE_NAME = "address";
    public static final String ADDRESSES_ATTRIBUTE_NAME = "addresses";

    public static final String STREET_ATTRIBUTE_NAME = "street";
    public static final String STREETS_ATTRIBUTE_NAME = "streets";

    public static final String INVOICES_ATTRIBUTE_NAME = "invoices";

    public static final String METER_ATTRIBUTE_NAME = "meter";
    public static final String METERS_ATTRIBUTE_NAME = "meters";

    public static final String METER_READER_ATTRIBUTE_NAME = "meterReader";
    public static final String METER_READERS_ATTRIBUTE_NAME = "meterReaders";

    public static final String MEASUREMENT_ATTRIBUTE_NAME = "measurement";
    public static final String MEASUREMENTS_ATTRIBUTE_NAME = "measurements";

    public static final String RESOURCES_ATTRIBUTE_NAME = "resources";
    public static final String RESOURCE_ATTRIBUTE_NAME = "resource";

    public static final String LANGUAGES_ATTRIBUTE_NAME = "languages";

    public static final String ERROR_MESSAGE_ATTRIBUTE_NAME = "errorMessage";

    //SERVICES
    public static final String AUTH = "/auth";
    public static final String LOGOUT = "/logOut";
    public static final String SWITCH_LANGUAGE = "/switchLanguage";

    public static final String SHOW_REGISTER_USER_FORM = "/showRegisterUserForm";
    public static final String REGISTER_USER = "/registerUser";
    public static final String SHOW_USERS = "/showUsers";
    public static final String ADD_USER = "/addUser";
    public static final String EDIT_USER = "/editUser";
    public static final String DELETE_USER = "/deleteUser";

    public static final String SHOW_ADDRESSES = "/showAddresses";
    public static final String ADD_ADDRESS = "/addAddress";
    public static final String EDIT_ADDRESS = "/editAddress";
    public static final String DELETE_ADDRESS = "/deleteAddress";

    public static final String SHOW_STREETS = "/showStreets";
    public static final String ADD_STREET = "/addStreet";
    public static final String EDIT_STREET = "/editStreet";
    public static final String DELETE_STREET = "/deleteStreet";

    public static final String SHOW_INVOICES = "/showInvoices";
    public static final String ADD_INVOICE = "/addInvoice";
    public static final String DELETE_INVOICE = "/deleteInvoice";
    public static final String PAY_INVOICE = "/payInvoice";

    public static final String SHOW_METERS = "/showMeters";
    public static final String ADD_METER = "/addMeter";
    public static final String EDIT_METER = "/editMeter";
    public static final String DELETE_METER = "/deleteMeter";

    public static final String SHOW_MEASUREMENTS = "/showMeasurements";
    public static final String ADD_MEASUREMENT =  "/addMeasurement";
    public static final String EDIT_MEASUREMENT = "/editMeasurement";
    public static final String DELETE_MEASUREMENT = "/deleteMeasurement";

    public static final String SHOW_RESOURCES = "/showResources";
    public static final String ADD_RESOURCE = "/addResource";
    public static final String EDIT_RESOURCE = "/editResource";
    public static final String DELETE_RESOURCE = "/deleteResource";

    public static final String SHOW_METER_READERS = "/showMeterReaders";
    public static final String ADD_METER_READER = "/addMeterReader";
    public static final String EDIT_METER_READER = "/editMeterReader";
    public static final String DELETE_METER_READER = "/deleteMeterReader";
    public static final String GET_DATA_FROM_METER_READER =  "/getDataFromMeterReader";

    //URL LAST STATES
    public static final String LAST_URL = "lastURL";
    public static final String USERS_URL_LAST_STATE = "usersURL";
    public static final String ADDRESSES_URL_LAST_STATE = "addressesURL";
    public static final String METERS_URL_LAST_STATE = "metersURL";
    public static final String MEASUREMENTS_URL_LAST_STATE = "measurementsURL";
    public static final String INVOICES_URL_LAST_STATE = "invoicesURL";
    public static final String RESOURCES_URL_LAST_STATE = "resourcesURL";
    public static final String STREETS_URL_LAST_STATE = "streetsURL";
    public static final String METER_READERS_URL_LAST_STATE = "meterReadersURL";

    private Constants(){}
}
