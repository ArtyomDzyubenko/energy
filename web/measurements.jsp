<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <jsp:include page="jspResources.jsp"/>
    <title></title>
</head>
<body>
    <jsp:include page="/header.jsp"/>

    <div align="center">
        <a href="${sessionScope.get("usersURL")}"><<< <fmt:message key="users"/></a>
        <a href="${sessionScope.get("addressesURL")}"><<< <fmt:message key="addresses"/></a>
        <a href="${sessionScope.get("metersURL")}"><<< <fmt:message key="meters"/></a>
    </div>

    <c:if test="${authUserAdmin == true}">
        <table class="header">
            <tr class="header">
                <td class="header"><jsp:include page="addMeasurement.jsp"/></td>
                <td class="header"><jsp:include page="addInvoice.jsp"/></td>
            </tr>
        </table>
    </c:if>

    <table class="data">
        <tr>
            <td><fmt:message key="date"/></td>
            <td><fmt:message key="value"/></td>
        </tr>
        <c:forEach var="measurement" items="${measurements}">
            <tr>
                <td><c:out value="${measurement.dateTime}"/></td>
                <td><c:out value="${measurement.value}"/></td>
                <c:if test="${authUserAdmin == true}">
                    <td>
                        <a href="${pageContext.request.contextPath}/editMeasurement?measurementId=${measurement.id}&meterId=${meterId}">
                            <img src="${editButton}" alt="<fmt:message key="edit"/>"  title="<fmt:message key="edit"/>"/></a>
                        <a href="${pageContext.request.contextPath}/deleteMeasurement?measurementId=${measurement.id}">
                            <img src="${deleteButton}" alt="<fmt:message key="delete"/>"  title="<fmt:message key="delete"/>"/></a>
                    </td>
                </c:if>
            </tr>
        </c:forEach>
    </table>

    <a href="#" title="<fmt:message key="upButton"/>" class="topButton"><fmt:message key="upButton"/></a>
</body>
</html>
