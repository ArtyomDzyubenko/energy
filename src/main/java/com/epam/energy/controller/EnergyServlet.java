package com.epam.energy.controller;

import com.epam.energy.service.ServiceFactory;
import org.apache.log4j.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import static com.epam.energy.util.Constants.*;

public class EnergyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(EnergyServlet.class);

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getMethod().equals(GET_METHOD)) {
            doGet(req, resp);
        } else if (req.getMethod().equals(POST_METHOD)) {
            doPost(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        serveRequest(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        serveRequest(request, response);
    }

    private void serveRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            ServiceFactory factory = ServiceFactory.getInstance();

            String serviceRequest = request.getServletPath();
            factory.executeService(serviceRequest, request, response);
        }
        catch (Exception e) {
            String errorMessage = e.getMessage();
            logger.error(errorMessage);

            request.setAttribute(ERROR_MESSAGE_ATTRIBUTE, errorMessage);
            request.getRequestDispatcher(ERROR_JSP).forward(request, response);
        }
    }
}
