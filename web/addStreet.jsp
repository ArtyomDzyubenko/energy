<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <jsp:include page="jspResources.jsp"/>
    <title></title>
</head>
<body>
    <form action="${contextPath}/addStreet" method="post">
        <input type="hidden" name="streetId" value="${street.id}" readonly/>

        <table class="form">
            <tr class="form">
                <td class="form"><fmt:message key="addStreet"/></td>
                <td class="form"></td>
            </tr>
            <tr class="form">
                <td class="form"><fmt:message key="street"/></td>
                <td class="form"><input type="text" name="name" value="${street.name}"/></td>
            </tr>
            <tr class="form">
                <td class="form"></td>
                <td class="form"><input type="submit" value="<fmt:message key="save"/>"></td>
            </tr>
        </table>
    </form>
</body>
</html>
