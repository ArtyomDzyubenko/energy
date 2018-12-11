package com.epam.energy.filter;

import com.epam.energy.model.User;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static com.epam.energy.util.Constants.*;

public class AuthFilter implements Filter {
    private static List<String> servicesAllowedForAllUsers = new ArrayList<>();
    private static List<String> servicesAllowedForUser = new ArrayList<>();

    static {
        servicesAllowedForAllUsers.add(AUTH);
        servicesAllowedForAllUsers.add(REGISTER_USER);
        servicesAllowedForAllUsers.add(SHOW_REGISTER_USER_FORM);
        servicesAllowedForAllUsers.add(INDEX_JSP);

        servicesAllowedForUser.add(AUTH);
        servicesAllowedForUser.add(LOGOUT);
        servicesAllowedForUser.add(SHOW_USERS);
        servicesAllowedForUser.add(EDIT_USER);
        servicesAllowedForUser.add(ADD_USER);
        servicesAllowedForUser.add(SHOW_ADDRESSES);
        servicesAllowedForUser.add(SHOW_METERS);
        servicesAllowedForUser.add(SHOW_INVOICES);
        servicesAllowedForUser.add(SHOW_MEASUREMENTS);
        servicesAllowedForUser.add(PAY_INVOICE);
        servicesAllowedForUser.add(SWITCH_LANGUAGE);
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException, SecurityException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;

        User authUser = (User)request.getSession().getAttribute(AUTH_USER);

        String action = request.getServletPath();

        if (action.startsWith(RESOURCES_DIR))
            filterChain.doFilter(servletRequest, servletResponse);
        else if (servicesAllowedForAllUsers.contains(action)){
            filterChain.doFilter(servletRequest, servletResponse);
        } else if (authUser!=null && authUser.isAdmin()) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else if (authUser!=null && !authUser.isAdmin()) {
            if (servicesAllowedForUser.contains(action)) {
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                throw new SecurityException("Access denied!");
            }
        } else {
            request.getRequestDispatcher(INDEX_JSP).forward(request, response);
        }
    }
}
