<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <jsp:include page="jspResources.jsp"/>
    <title></title>
</head>
<body>
    <form action="${contextPath}/addResource" method="post">
        <input type="hidden" name="resourceId" value="${resource.id}" readonly/>

        <table class="form">
            <tr class="form">
                <td class="form"><fmt:message key="addResource"/></td>
                <td class="form"></td>
            </tr>
            <tr class="form">
                <td class="form"><fmt:message key="bundleKey"/></td>
                <td class="form"><input type="text" name="name" value="${resource.name}"/></td>
            </tr>
            <tr class="form">
                <td class="form"><fmt:message key="resourceCost"/></td>
                <td class="form"><input type="number" name="cost" step="0.01" value="${resource.cost}"/></td>
            </tr>
            <tr class="form">
                <td class="form"></td>
                <td class="form"><input type="submit" value="<fmt:message key="save"/>"></td>
            </tr>
        </table>
    </form>
</body>
</html>
