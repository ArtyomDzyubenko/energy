package com.epam.energy.service;

import com.epam.energy.exception.DAOException;
import com.epam.energy.exception.ServiceException;
import org.apache.log4j.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import static com.epam.energy.util.Constants.*;

public class ServiceFactory {
    private Logger logger = Logger.getLogger(ServiceFactory.class);
    private Map<String, AbstractService> serviceMap = new HashMap<>();

    private static ServiceFactory instance;

    private ServiceFactory() throws DAOException {
        init();
    }

    public static synchronized ServiceFactory getInstance() throws DAOException {
        if (instance == null) {
            instance = new ServiceFactory();
        }

        return instance;
    }

    public void executeService(String serviceRequest, HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        AbstractService service = serviceMap.get(serviceRequest);

        if (service!=null) {
            service.execute(request, response);
        } else {
            logger.error(serviceRequest);

            throw new ServiceException("Incorrect request!");
        }
    }

    private void init() throws DAOException {
        serviceMap.put(LOGOUT, LogOutService.getInstance());

        serviceMap.put(SHOW_REGISTER_USER_FORM, ShowRegisterUserFormService.getInstance());
        serviceMap.put(SHOW_USERS, GetUsersService.getInstance());
        serviceMap.put(EDIT_USER, EditUserService.getInstance());
        serviceMap.put(DELETE_USER, DeleteUserService.getInstance());

        serviceMap.put(SHOW_ADDRESSES, GetAddressesService.getInstance());
        serviceMap.put(DELETE_ADDRESS, DeleteAddressService.getInstance());
        serviceMap.put(EDIT_ADDRESS, EditAddressService.getInstance());

        serviceMap.put(SHOW_METERS, GetMetersService.getInstance());
        serviceMap.put(EDIT_METER, EditMeterService.getInstance());
        serviceMap.put(DELETE_METER, DeleteMeterService.getInstance());

        serviceMap.put(SHOW_MEASUREMENTS, GetMeasurementsService.getInstance());
        serviceMap.put(EDIT_MEASUREMENT, EditMeasurementService.getInstance());
        serviceMap.put(DELETE_MEASUREMENT, DeleteMeasurementService.getInstance());

        serviceMap.put(SHOW_INVOICES, GetInvoicesService.getInstance());
        serviceMap.put(DELETE_INVOICE, DeleteInvoiceService.getInstance());
        serviceMap.put(PAY_INVOICE, PayInvoiceService.getInstance());

        serviceMap.put(SHOW_RESOURCES, GetResourcesService.getInstance());
        serviceMap.put(EDIT_RESOURCE, EditResourceService.getInstance());
        serviceMap.put(DELETE_RESOURCE, DeleteResourceService.getInstance());

        serviceMap.put(SHOW_STREETS, GetStreetsService.getInstance());
        serviceMap.put(EDIT_STREET, EditStreetService.getInstance());
        serviceMap.put(DELETE_STREET, DeleteStreetService.getInstance());

        serviceMap.put(SHOW_METER_READERS, GetMeterReadersService.getInstance());
        serviceMap.put(EDIT_METER_READER, EditMeterReaderService.getInstance());
        serviceMap.put(DELETE_METER_READER, DeleteMeterReaderService.getInstance());
        serviceMap.put(GET_DATA_FROM_METER_READER, GetDataFromMeterReaderService.getInstance());

        serviceMap.put(AUTH, AuthService.getInstance());
        serviceMap.put(SWITCH_LANGUAGE, LanguageService.getInstance());

        serviceMap.put(REGISTER_USER, RegisterUserService.getInstance());

        serviceMap.put(ADD_USER, AddUserService.getInstance());

        serviceMap.put(ADD_ADDRESS, AddAddressService.getInstance());

        serviceMap.put(ADD_METER, AddMeterService.getInstance());

        serviceMap.put(ADD_MEASUREMENT, AddMeasurementService.getInstance());

        serviceMap.put(ADD_INVOICE, AddInvoiceService.getInstance());

        serviceMap.put(ADD_RESOURCE, AddResourceService.getInstance());

        serviceMap.put(ADD_STREET, AddStreetService.getInstance());

        serviceMap.put(ADD_METER_READER, AddMeterReaderService.getInstance());
    }
}

