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

    <table class="data">
        <tr>
            <td><fmt:message key="date"/></td>
            <td><fmt:message key="meter"/></td>
            <td><fmt:message key="resource"/></td>
            <td><fmt:message key="resourceCost"/></td>
            <td><fmt:message key="startDate"/></td>
            <td><fmt:message key="startValue"/></td>
            <td><fmt:message key="endDate"/></td>
            <td><fmt:message key="endValue"/></td>
            <td><fmt:message key="consumption"/></td>
            <td><fmt:message key="price"/></td>
            <td><fmt:message key="isPaid"/></td>
        </tr>
        <c:forEach var="invoice" items="${invoices}">
            <tr>
                <td>${invoice.date}</td>
                <td>${invoice.meter.number}</td>
                <td><fmt:message key="${invoice.meter.resource.name}"/></td>
                <td>${invoice.meter.resource.cost}</td>
                <td>${invoice.startValue.dateTime}</td>
                <td>${invoice.startValue.value}</td>
                <td>${invoice.endValue.dateTime}</td>
                <td>${invoice.endValue.value}</td>
                <td>${invoice.consumption}</td>
                <td>${invoice.price}</td>
                <td>
                    <c:if test="${invoice.paid == true}">
                        <input type="checkbox" name="paid" disabled="disabled" checked>
                    </c:if>
                    <c:if test="${invoice.paid == false}">
                        <input type="checkbox" name="paid" disabled="disabled">
                    </c:if>
                </td>
                <c:if test="${authUserAdmin == true}">
                    <td>
                        <a href="${pageContext.request.contextPath}/deleteInvoice?invoiceId=${invoice.id}&userId=${userId}&sKey=${invoice.secretKey}">
                            <img src="${deleteButton}" alt="<fmt:message key="delete"/>"  title="<fmt:message key="delete"/>"/></a>
                    </td>
                </c:if>
                <c:if test="${authUserAdmin == false}">
                    <c:if test="${invoice.paid == false}">
                        <td>
                            <a href="${pageContext.request.contextPath}/payInvoice?invoiceId=${invoice.id}&userId=${userId}&sKey=${invoice.secretKey}">
                                <img src="${payButton}" alt="<fmt:message key="pay"/>"  title="<fmt:message key="pay"/>"/></a>
                        </td>
                    </c:if>
                </c:if>
            </tr>
        </c:forEach>
    </table>

    <a href="#" title="<fmt:message key="upButton"/>" class="topButton"><fmt:message key="upButton"/></a>
</body>
</html>
