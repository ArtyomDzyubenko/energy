<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
<head>
    <jsp:include page="jspResources.jsp"/>
    <title></title>
</head>
<body>
    <jsp:include page="header.jsp"/>

    <c:if test="${authUserAdmin == true}">
        <div align="center">
            <a href="${contextPath}/showMeterReaders"><fmt:message key="meterReaderDevices"/></a>
            <a href="${contextPath}/showResources"><fmt:message key="resources"/></a>
            <a href="${contextPath}/showStreets"><fmt:message key="streets"/></a>
        </div>
    </c:if>

    <c:if test="${authUserAdmin == true}">
        <jsp:include page="addUser.jsp"/>
    </c:if>
    <c:if test="${authUserAdmin == false}">
        <jsp:include page="registerUser.jsp"/>
    </c:if>

    <table class="data">
        <tr>
            <td><fmt:message key="login"/></td>
            <td><fmt:message key="password"/></td>
            <td><fmt:message key="firstName"/></td>
            <td><fmt:message key="lastName"/></td>
            <td><fmt:message key="phone"/></td>
            <td><fmt:message key="email"/></td>
            <td><fmt:message key="personalAccount"/></td>
        </tr>

        <c:forEach var="user" items="${users}">
            <tr>
                <td>${user.login}</td>
                <td>${user.password}</td>
                <td>${user.firstName}</td>
                <td>${user.lastName}</td>
                <td>${user.phone}</td>
                <td>${user.email}</td>
                <td>${user.personalAccount}</td>
                <td>
                    <c:if test="${authUserAdmin == true}">
                        <a href="${contextPath}/editUser?userId=${user.id}">
                            <img src="${editButton}" alt="<fmt:message key="edit"/>" title="<fmt:message key="edit"/>"></a>
                        <a href="${contextPath}/deleteUser?userId=${user.id}">
                            <img src="${deleteButton}" alt="<fmt:message key="delete"/>" title="<fmt:message key="delete"/>"></a>
                    </c:if>
                    <a href="${contextPath}/showAddresses?userId=${user.id}&sKey=${user.secretKey}">
                        <img src="${addressButton}" alt="<fmt:message key="addresses"/>" title="<fmt:message key="addresses"/>"></a>

                    <a href="${contextPath}/showInvoices?userId=${user.id}&sKey=${user.secretKey}">
                        <img src="${invoiceButton}" alt="<fmt:message key="invoices"/>" title="<fmt:message key="invoices"/>"></a>
                </td>
            </tr>
        </c:forEach>
    </table>

    <a href="#" title="<fmt:message key="upButton"/>" class="topButton"><fmt:message key="upButton"/></a>
</body>
</html>
