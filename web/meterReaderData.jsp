<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<html>
<head>
    <jsp:include page="jspResources.jsp"/>
    <title></title>
</head>
<body>
    <jsp:include page="header.jsp"/>

    <div align="center">
        <a href="${sessionScope.get("meterReadersURL")}"><<< <fmt:message key="meterReaderDevices"/></a>
    </div>

    <table class="data">
        <tr>
            <td><fmt:message key="meterNumber"/></td>
            <td><fmt:message key="date"/></td>
            <td><fmt:message key="value"/></td>
        </tr>
        <c:forEach var="meter" items="${meters}">
            <tr>
                <td>${meter.number}</td>
                <td>${meter.measurement.dateTime}</td>
                <td>${meter.measurement.value}</td>
            </tr>
        </c:forEach>
    </table>

    <a href="#" title="<fmt:message key="upButton"/>" class="topButton"><fmt:message key="upButton"/></a>
</body>
</html>
