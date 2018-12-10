package validator;

import exception.DAOException;
import exception.ValidationException;
import util.Localization;

public class InvoiceValidator extends AbstractValidator {
    private static InvoiceValidator instance;

    private InvoiceValidator() {}

    public static synchronized InvoiceValidator getInstance() {
        if(instance==null){
            instance = new InvoiceValidator();
        }

        return instance;
    }

    public Long validateId(String id, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = Localization.getLocalization().getString("invoiceId");
        return validateLongField(id, fieldName, allowEmpty);
    }

    public Double validateConsumption(double consumption) throws ValidationException, DAOException {
        if (consumption <= 0){
            concat.setLength(0);
            String errorMessage = concat.append(Localization.getLocalization().getString("negativeValueError")).append(" ")
                    .append(Localization.getLocalization().getString("consumption")).toString();
            throw new ValidationException(errorMessage);
        }

        return consumption;
    }

    public double validatePrice(double price) throws ValidationException, DAOException {
        if (price <= 0){
            concat.setLength(0);
            String errorMessage = concat.append(Localization.getLocalization().getString("negativeValueError")).append(" ")
                    .append(Localization.getLocalization().getString("price")).toString();
            throw new ValidationException(errorMessage);
        }

        return price;
    }
}
