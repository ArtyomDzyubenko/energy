<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setBundle basename="Locale" scope="application"/>
<c:set value="${sessionScope.authUser}" var="authUser" scope="application"/>
<c:set value="${sessionScope.authUser.admin}" var="authUserAdmin" scope="application"/>
<c:set value="${sessionScope.languages}" var="languages" scope="application"/>
<c:set value="${pageContext.request.contextPath}" var="contextPath" scope="application"/>
<c:set value="res/profileSettings.png" var="userButton" scope="application"/>
<c:set value="res/back.png" var="backButton" scope="application"/>
<c:set value="res/edit.png" var="editButton" scope="application"/>
<c:set value="res/delete.png" var="deleteButton" scope="application"/>
<c:set value="res/address.png" var="addressButton" scope="application"/>
<c:set value="res/invoice.png" var="invoiceButton" scope="application"/>
<c:set value="res/meter.png" var="meterButton" scope="application"/>
<c:set value="res/measurement.png" var="measurementButton" scope="application"/>
<c:set value="res/getData.png" var="getDataButton" scope="application"/>
<c:set value="res/profileSettings.png" var="profileSettingsButton" scope="application"/>
<c:set value="res/logOut.png" var="logOutButton" scope="application"/>
<c:set value="res/pay.png" var="payButton" scope="application"/>

<link rel="stylesheet" href="${contextPath}/res/style.css" type="text/css" media="screen">

<html>
<head>
    <title></title>
</head>
<body>

</body>
</html>
