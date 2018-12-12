<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <jsp:include page="jspResources.jsp"/>
    <title></title>
</head>
<body>
    <form action="${contextPath}/addMeasurement" method="post">
        <input type="hidden" name="meterId" value="${meterId}" readonly/>
        <input type="hidden" name="measurementId" value="${measurement.id}" readonly/>

        <table class="form">
            <tr class="form">
                <td class="form">
                    <fmt:message key="addMeasurement"/>
                </td>
                <td class="form"></td>
            </tr>
            <tr class="form">
                <td class="form"><fmt:message key="date"/></td>
                <c:if test="${measurement.id != null}">
                    <td class="form">
                        <input type="datetime-local" name="dateTime" value="${measurement.localDateTimeString}" step="2" min="2000-01-01T00:00:01" max="2100-01-01T00:00:01"/>
                    </td>
                </c:if>
                <c:if test="${measurement.id == null}">
                    <td class="form">
                        <input type="datetime-local" name="dateTime" value="${measurement.dateTime}" step="2" min="2000-01-01T00:00:01" max="2100-01-01T00:00:01"/>
                    </td>
                </c:if>
            </tr>
            <tr class="form">
                <td class="form"><fmt:message key="value"/></td>
                <td class="form"><input type="number" step="0.01" name="value" value="${measurement.value}"/></td>
            </tr>
            <tr class="form">
                <td class="form"></td>
                <td class="form"><input type="submit" value="<fmt:message key="save"/>"></td>
            </tr>
        </table>
    </form>
</body>
</html>
