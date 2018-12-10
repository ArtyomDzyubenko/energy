<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <jsp:include page="jspResources.jsp"/>
    <title></title>
</head>
<body>
    <form action="${contextPath}/addInvoice" method="post">
        <input type="hidden" name="meterId" value="${meterId}" readonly/>
        <input type="hidden" name="userId" value="${userId}" readonly/>
        <input type="hidden" name="addressId" value="${addressId}" readonly/>
        <input type="hidden" name="invoiceId" value="${invoice.id}" readonly/>

        <table class="form">
            <tr class="form">
                <td class="form" colspan>
                    <fmt:message key="addInvoice"/>
                </td>
                <td class="form"></td>
            </tr>
            <tr class="form">
                <td class="form"><fmt:message key="startValue"/></td>
                <td class="form">
                    <select name="startValue">
                        <c:forEach var="measurement" items="${measurements}">
                            <option selected="${measurement.id}" value="${measurement.id}"}>
                                    <fmt:message key="date"/>: ${measurement.dateTime} :
                                    <fmt:message key="value"/>: ${measurement.value}
                            </option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr class="form">
                <td class="form"><fmt:message key="endValue"/></td>
                <td class="form">
                    <select name="endValue">
                        <c:forEach var="measurement" items="${measurements}">
                            <option selected="${measurement.id}" value="${measurement.id}"}>
                                <fmt:message key="date"/>: ${measurement.dateTime} :
                                <fmt:message key="value"/>: ${measurement.value}
                            </option>
                        </c:forEach>
                    </select>
                </td>
            </tr>

            <tr class="form">
                <td class="form"></td>
                <td class="form"><input type="submit" value="<fmt:message key="save"/>"></td>
            </tr>
        </table>
    </form>
</body>
</html>
