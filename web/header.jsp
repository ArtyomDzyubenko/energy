<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title></title>
    <jsp:include page="jspResources.jsp"/>
</head>
<body>
    <table class="header">
        <tr class="header">
            <td class="header">
                <a href="${contextPath}/editUser?userId=${authUser.id}"><img src="${profileSettingsButton}" alt="<fmt:message key="profileSettings"/>" title="<fmt:message key="profileSettings"/>"></a>
            </td>
            <td class="header">
                <form action="${contextPath}/switchLanguage" method="post">
                    <select name="languageId">
                        <c:forEach var="language" items="${sessionScope.languages}">
                            <option value="${language.id}" ${language.code == pageContext.request.locale.language ? 'selected = "selected"' : ''}>${language.name}</option>
                        </c:forEach>
                    </select>
                    <input type="submit" value="<fmt:message key="okButton"/>">
                </form>
            </td>
            <td class="header">
                <a href="${contextPath}/logOut"><img src="${logOutButton}"  alt="<fmt:message key="logOut"/>" title="<fmt:message key="logOut"/>"></a>
            </td>
        </tr>
    </table>
</body>
</html>


