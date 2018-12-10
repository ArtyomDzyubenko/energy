<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <jsp:include page="jspResources.jsp"/>
    <title></title>
</head>
<body>
    <div align="center">
        <input type="image" src="${backButton}" onclick="history.back()"/>
        <br>
        ${errorMessage}
        <br>
        ${pageContext.exception.message}
    </div>
</body>
</html>
