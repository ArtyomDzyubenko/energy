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

    <div align="center">
        <a href="${sessionScope.get("usersURL")}"><<< <fmt:message key="users"/></a>
    </div>

    <jsp:include page="addMeterReader.jsp"/>

    <table class="data">
        <tr>
            <td><fmt:message key="meterReaderNumber"/></td>
            <td><fmt:message key="IPAddress"/></td>
            <td><fmt:message key="port"/></td>
        </tr>
        <c:forEach var="meterReader" items="${meterReaders}">
            <tr>
                <td>${meterReader.number}</td>
                <td>${meterReader.IPAddress}</td>
                <td>${meterReader.port}</td>
                <td>
                    <a href="${contextPath}/editMeterReader?meterReaderId=${meterReader.id}">
                        <img src="${editButton}" alt="<fmt:message key="edit"/>" title="<fmt:message key="edit"/>"></a>
                    <a href="${contextPath}/deleteMeterReader?meterReaderId=${meterReader.id}">
                        <img src="${deleteButton}" alt="<fmt:message key="delete"/>" title="<fmt:message key="delete"/>"></a>
                    <a href="${contextPath}/getDataFromMeterReader?meterReaderId=${meterReader.id}">
                        <img src="${getDataButton}" alt="<fmt:message key="getData"/>" title="<fmt:message key="getData"/>"></a>
                </td>
            </tr>
        </c:forEach>
    </table>

    <a href="#" title="<fmt:message key="upButton"/>" class="topButton"><fmt:message key="upButton"/></a>
</body>
</html>
