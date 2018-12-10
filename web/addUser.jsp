<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <jsp:include page="jspResources.jsp"/>
    <title></title>
</head>
<body>
    <form action="${contextPath}/addUser" method="post">
        <input type="hidden" name="userId" value="${user.id}" readonly/>

        <table class="form">
            <tr class="form">
                <td class="form"><fmt:message key="addUser"/></td>
                <td class="form"></td>
            </tr>
            <tr class="form">
                <td class="form"><fmt:message key="login"/></td>
                <td class="form"><input type="text" name="login" value="${user.login}"/></td>
            </tr>
            <tr class="form">
                <td class="form"><fmt:message key="password"/></td>
                <td class="form"><input type="password" name="password" value="${user.password}"/></td>
            </tr>
            <tr class="form">
                <td class="form"><fmt:message key="firstName"/></td>
                <td class="form"><input type="text" name="firstName" value="${user.firstName}"/></td>
            </tr>
            <tr class="form">
                <td class="form"><fmt:message key="lastName"/></td>
                <td class="form"><input type="text" name="lastName" value="${user.lastName}"/></td>
            </tr>
            <tr class="form">
                <td class="form"><fmt:message key="phone"/></td>
                <td class="form"><input type="number" name="phone" value="${user.phone}"/></td>
            </tr>
            <tr class="form">
                <td class="form"><fmt:message key="email"/></td>
                <td class="form"><input type="email" name="email" value="${user.email}"/></td>
            </tr>
            <tr class="form">
                <td class="form"><fmt:message key="personalAccount"/></td>
                <td class="form"><input type="number" name="personalAccount" value="${user.personalAccount}"/></td>
            </tr>
            <tr class="form">
                <td class="form"></td>
                <td class="form"><input type="submit" value="<fmt:message key="save"/>"></td>
            </tr>
        </table>
    </form>
</body>
</html>
