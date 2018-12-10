package service;

import exception.DAOException;
import exception.ServiceException;
import exception.ValidationException;
import validator.MeterReaderValidator;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import static util.Constants.METER_READERS_URL_LAST_STATE;
import static util.Constants.METER_READER_ID;
import static util.ServicesAllowedInputParametersLists.deleteMeterReaderServiceAllowedInputParameters;

public class DeleteMeterReaderService extends AbstractService {
    private static DeleteMeterReaderService instance;

    private DeleteMeterReaderService() throws DAOException {}

    public static synchronized DeleteMeterReaderService getInstance() throws DAOException {
        if (instance==null){
            instance = new DeleteMeterReaderService();
        }

        return instance;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, deleteMeterReaderServiceAllowedInputParameters);

            Long meterReaderId = getMeterReaderId(parameters);

            meterReaderDAO.deleteMeterReader(meterReaderId);

            response.sendRedirect(getLastServiceURL(METER_READERS_URL_LAST_STATE, request));
        } catch (IOException e) {
            throw new ServiceException(e);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } catch (ValidationException e) {
            throw new ServiceException(e);
        }
    }
}
