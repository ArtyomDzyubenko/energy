<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <jsp:include page="jspResources.jsp"/>
    <title></title>
</head>
<body>
    <form action="${contextPath}/addMeter" method="post">
        <input type="hidden" name="addressId" value="${addressId}" readonly/>
        <input type="hidden" name="meterId" value="${meter.id}" readonly/>

        <table class="form">
            <tr class="form">
                <td class="form"><fmt:message key="addMeter"/></td>
                <td class="form"></td>
            </tr>
            <tr class="form">
                <td class="form"><fmt:message key="attachToAnotherAddress" /></td>
                <td class="form">
                    <select name="transferAddressId">
                        <c:if test="${meter.id == null}">
                            <option value="${addressId}"></option>
                        </c:if>
                        <c:forEach var="address" items="${addresses}">
                            <option value="${address.id}" ${addressId == address.id ? 'selected = "selected"' : ''}>
                                <fmt:message key="street"/>: ${address.street.name}:
                                <fmt:message key="building"/>: ${address.building}:
                                <fmt:message key="flat"/>: ${address.flat}
                            </option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr class="form">
                <td class="form"><fmt:message key="meterNumber"/></td>
                <td class="form"><input type="number" name="number" value="${meter.number}"/></td>
            </tr>
            <tr class="form">
                <td class="form"><fmt:message key="resource"/></td>
                <td class="form">
                    <select name="resourceId">
                        <c:forEach var="resource" items="${resources}">
                            <option value="${resource.id}" ${resource.id == meter.resource.id ? 'selected = "selected"' : ''}>
                                <fmt:message key="${resource.name}"/>
                            </option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr class="form">
                <td class="form"><fmt:message key="meterReader"/></td>
                <td class="form">
                    <select name="meterReaderId">
                        <c:forEach var="meterReader" items="${meterReaders}">
                            <option value="${meterReader.id}" ${meterReader.id == meter.meterReaderId ? 'selected = "selected"' : ''}>
                                ${meterReader.number}
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
