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
    </div>

    <c:if test="${authUserAdmin == true}">
        <jsp:include page="addAddress.jsp"/>
    </c:if>

    <table class="data">
        <tr>
            <td><fmt:message key="street"/></td>
            <td><fmt:message key="building"/></td>
            <td><fmt:message key="flat"/></td>
        </tr>
        <c:forEach var="address" items="${addresses}">
            <tr>
                <td>${address.street.name}</td>
                <td>${address.building}</td>
                <td>${address.flat}</td>
                <td>
                    <c:if test="${authUserAdmin == true}">
                        <a href="${pageContext.request.contextPath}/editAddress?addressId=${address.id}&userId=${address.userId}">
                            <img src="${editButton}" alt="<fmt:message key="edit"/>"  title="<fmt:message key="edit"/>"/></a>
                        <a href="${pageContext.request.contextPath}/deleteAddress?addressId=${address.id}">
                            <img src="${deleteButton}" alt="<fmt:message key="delete"/>"  title="<fmt:message key="delete"/>"/></a>
                    </c:if>
                    <a href="${pageContext.request.contextPath}/showMeters?addressId=${address.id}&userId=${userId}&sKey=${address.secretKey}">
                        <img src="${meterButton}" alt="<fmt:message key="meters"/>"  title="<fmt:message key="meters"/>"/></a>
                </td>
            </tr>
        </c:forEach>
    </table>

    <a href="#" title="<fmt:message key="upButton"/>" class="topButton"><fmt:message key="upButton"/></a>
</body>
</html>
