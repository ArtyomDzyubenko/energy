<%@ page import="com.epam.energy.util.Encryption" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <fmt:setLocale value="${pageContext.request.locale.language}" scope="application"/>
    <fmt:setBundle basename="Locale" scope="page"/>
    <jsp:include page="jspResources.jsp"/>
    <title></title>
</head>
<body>
    <h3 align="center"><fmt:message key="appName"/></h3>

    <form action="${contextPath}/auth" method="post">
        <table class="form">
            <tr class="form">
                <td class="form"><fmt:message key="login"/></td>
                <td class="form"><input type="text" name="login"></td>
            </tr>
            <tr class="form">
                <td class="form"><fmt:message key="password"/></td>
                <td class="form"><input type="password" name="password"></td>
            </tr>
            <tr class="form">
                <td class="form"></td>
                <td class="form"><a href="${contextPath}/showRegisterUserForm"><fmt:message key="registration"/></a></td>
            </tr>
            <tr class="form">
                <td class="form"></td>
                <td class="form"><input type="submit" value="<fmt:message key="enter"/>"></td>
            </tr>
        </table>
    </form>
</body>
</html>
