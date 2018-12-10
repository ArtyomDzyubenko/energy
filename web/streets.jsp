<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

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

    <jsp:include page="addStreet.jsp"/>

    <table class="data">
        <tr>
            <td><fmt:message key="street"/></td>
        </tr>
        <c:forEach var="street" items="${streets}">
            <tr>
                <td>${street.name}</td>
                <td>
                    <c:if test="${authUserAdmin == true}">
                        <a href="${pageContext.request.contextPath}/editStreet?streetId=${street.id}">
                            <img src="${editButton}" alt="<fmt:message key="edit"/>"  title="<fmt:message key="edit"/>"/></a>
                        <a href="${pageContext.request.contextPath}/deleteStreet?streetId=${street.id}">
                            <img src="${deleteButton}" alt="<fmt:message key="delete"/>"  title="<fmt:message key="delete"/>"/></a>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
    </table>

    <a href="#" title="<fmt:message key="upButton"/>" class="topButton"><fmt:message key="upButton"/></a>
</body>
</html>
