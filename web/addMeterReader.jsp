<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <jsp:include page="jspResources.jsp"/>
    <title></title>
</head>
<body>
    <form action="${contextPath}/addMeterReader" method="post">
        <input type="hidden" name="meterReaderId" value="${meterReader.id}" readonly/>

        <table class="form">
            <tr class="form">
                <td class="form"><fmt:message key="addMeterReader"/></td>
                <td class="form"></td>
            </tr>
            <tr class="form">
                <td class="form"><fmt:message key="meterReaderNumber"/></td>
                <td class="form"><input type="number" name="readerNumber" value="${meterReader.number}"/></td>
            </tr>
            <tr class="form">
                <td class="form"><fmt:message key="IPAddress"/></td>
                <td class="form"><input type="text" name="IPAddress" value="${meterReader.IPAddress}"/></td>
            </tr>
            <tr class="form">
                <td class="form"><fmt:message key="port"/></td>
                <td class="form"><input type="number" name="port" value="${meterReader.port}"/></td>
            </tr>
            <tr class="form">
                <td class="form"></td>
                <td class="form"><input type="submit" value="<fmt:message key="save"/>"></td>
            </tr>
        </table>
    </form>
</body>
</html>
