package com.company.energy.filter;

import com.company.energy.model.User;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static com.company.energy.util.Constants.*;

public class AuthFilter implements Filter {
    private static List<String> servicesAllowedForGuest = new ArrayList<>();
    private static List<String> servicesAllowedForUser = new ArrayList<>();

    @Override
    public void init(FilterConfig filterConfig) {
        initAllowedServicesLists();
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException,
            ServletException, SecurityException {

        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;

        User authUser = (User)request.getSession().getAttribute(AUTHORIZED_USER);

        String serviceRequest = request.getServletPath();

        if (serviceRequest.startsWith(RESOURCES_DIR))
            filterChain.doFilter(servletRequest, servletResponse);
        else if (servicesAllowedForGuest.contains(serviceRequest)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else if (authUser!=null && authUser.isAdmin()) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else if (authUser!=null && !authUser.isAdmin()) {
            if (servicesAllowedForUser.contains(serviceRequest)) {
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                throw new SecurityException("Access denied!");
            }
        } else {
            request.getRequestDispatcher(INDEX_JSP).forward(request, response);
        }
    }

    private void initAllowedServicesLists(){
        servicesAllowedForGuest.addAll(Arrays.asList(AUTH, REGISTER_USER, SHOW_REGISTER_USER_FORM, INDEX_JSP));
        servicesAllowedForUser.addAll(Arrays.asList(AUTH, LOGOUT, SHOW_USERS, EDIT_USER, ADD_USER, SHOW_ADDRESSES,
                SHOW_METERS, SHOW_INVOICES, SHOW_MEASUREMENTS, PAY_INVOICE, SWITCH_LANGUAGE));
    }
}
