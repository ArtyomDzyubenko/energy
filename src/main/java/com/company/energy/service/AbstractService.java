package com.company.energy.service;

import com.company.energy.exception.DAOException;
import com.company.energy.exception.ServiceException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.company.energy.util.Constants.LAST_URL;

public abstract class AbstractService {
    boolean allowEmpty = true;

    AbstractService() throws DAOException { }

    public abstract void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException;

    void saveLastServiceURL(String URL, HttpServletRequest request) {
        String queryString = request.getQueryString();
        String lastURL;

        if (queryString != null) {
            lastURL = request.getRequestURI() + "?" + queryString;
        } else {
            lastURL = request.getRequestURI();
        }

        request.getSession().setAttribute(LAST_URL, lastURL);
        request.getSession().setAttribute(URL, lastURL);
    }

    String getLastServiceURL(String URL, HttpServletRequest request) {
        return (String)request.getSession().getAttribute(URL);
    }
}
