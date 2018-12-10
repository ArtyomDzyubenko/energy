<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <jsp:include page="jspResources.jsp"/>
    <title></title>
</head>
<body>
    <form action="${contextPath}/addAddress" method="post">
        <input type="hidden" name="userId" value="${userId}" readonly/>
        <input type="hidden" name="addressId" value="${address.id}" readonly/>

        <table class="form">
            <tr class="form">
                <td class="form"><fmt:message key="addAddress"/></td>
                <td class="form"></td>
            </tr>
            <tr class="form">
                <td class="form"><fmt:message key="attachToAnotherAccount" /></td>
                <td class="form">
                    <select name="transferUserId">
                        <c:if test="${address.id == null}">
                            <option value="${userId}"></option>
                        </c:if>
                        <c:forEach var="user" items="${users}">
                            <option value="${user.id}" ${userId == user.id ? 'selected = "selected"' : ''}>${user.personalAccount}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr class="form">
                <td class="form"><fmt:message key="building"/></td>
                <td class="form"><input type="text" name="building" value="${address.building}"/></td>
            </tr>
            <tr class="form">
                <td class="form"><fmt:message key="flat"/></td>
                <td class="form"><input type="text" name="flat" value="${address.flat}"/></td>
            </tr>
            <tr class="form">
                <td class="form"><fmt:message key="street"/></td>
                <td class="form">
                    <select name="streetId">
                        <c:forEach var="street" items="${streets}">
                            <option value="${street.id}" ${street.id == address.street.id ? 'selected = "selected"' : ''}>${street.name}</option>
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
