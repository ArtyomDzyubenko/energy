package com.epam.energy.exception;

import javax.servlet.ServletException;
import java.io.IOException;

public class ServiceException extends Exception {
    public ServiceException(DAOException e) {super(e.getMessage());}
    public ServiceException(IOException e) {super(e.getMessage());}
    public ServiceException(ValidationException e) {super(e.getMessage());}
    public ServiceException(ServletException e) {super(e);}
    public ServiceException(String message) {
        super(message);
    }
}
