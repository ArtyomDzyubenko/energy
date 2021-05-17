package com.company.energy.exception;

public class ServiceException extends Exception {
    public ServiceException(Exception e) {super(e.getMessage());}
    public ServiceException(String msg) {super(msg);}

}
