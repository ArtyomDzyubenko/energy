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
        <a href="${sessionScope.get("usersURL")}"><<< <fmt:message key="users"/></a>
        <a href="${sessionScope.get("addressesURL")}"><<< <fmt:message key="addresses"/></a>
    </div>

    <c:if test="${authUserAdmin == true}">
        <jsp:include page="addMeter.jsp"/>
    </c:if>

    <table class="data">
        <tr>
            <td><fmt:message key="meterNumber"/></td>
            <c:if test="${authUserAdmin == true}">
                <td>
                    <fmt:message key="meterReader"/>
                </td>
            </c:if>
            <td><fmt:message key="resource"/></td>
        </tr>
        <c:forEach var="meter" items="${meters}">
            <tr>
                <td>${meter.number}</td>
                <c:if test="${authUserAdmin == true}">
                    <td>
                            ${meter.meterReaderNumber}
                    </td>
                </c:if>
                <td><fmt:message key="${meter.resource.name}"/></td>
                <td>
                    <c:if test="${authUserAdmin == true}">
                        <a href="${contextPath}/editMeter?meterId=${meter.id}&addressId=${meter.addressId}">
                            <img src="${editButton}" alt="<fmt:message key="edit"/>"  title="<fmt:message key="edit"/>"/></a>
                        <a href="${contextPath}/deleteMeter?meterId=${meter.id}">
                            <img src="${deleteButton}" alt="<fmt:message key="delete"/>"  title="<fmt:message key="delete"/>"/></a>
                    </c:if>
                    <a href="${contextPath}/showMeasurements?userId=${userId}&meterId=${meter.id}&addressId=${addressId}&sKey=${meter.secretKey}">
                        <img src="${measurementButton}" alt="<fmt:message key="measurements"/>"  title="<fmt:message key="measurements"/>"/></a>
                </td>
            </tr>
        </c:forEach>
    </table>

    <a href="#" title="<fmt:message key="upButton"/>" class="topButton"><fmt:message key="upButton"/></a>
</body>
</html>
