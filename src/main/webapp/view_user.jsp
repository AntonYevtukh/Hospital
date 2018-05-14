<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ctg" uri="customtaglib" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<fmt:setLocale value="${current_user ne null ? current_user.language : 'ru'}"/>
<fmt:setBundle basename="gender" var="gender_bundle" />
<fmt:setBundle basename="validation.validation" var="validation" />
<fmt:setBundle basename="user" var="user_bundle"/>
<fmt:setBundle basename="general" var="general" />
<html>
<head>
    <jsp:include page="meta.jsp"/>
</head>
<body class="bg-light">
    <jsp:include page="header.jsp"/>
<div class="d-flex bg-light">
    <div class="col-12 align-self-center">
        <h1 class="text-center">${user.lastName}&nbsp;${user.firstName}&nbsp;${user.middleName}</h1>
        <div class="row">
            <div class="col-1"></div>
            <div class="col-10">
                <div class="row">
                    <div class="col-4">
                        <div class="img-square-container">
                            <ctg:img content="${user.photo.content}" cssClass="img-square" id="photo" alt="User Photo"/>
                        </div>
                    </div>
                    <div class="col-8">
                        <label><fmt:message key="user.last_name" bundle="${user_bundle}"/></label>: <label>${user.lastName}</label><br>
                        <label><fmt:message key="user.first_name" bundle="${user_bundle}"/></label>: <label>${user.firstName}</label><br>
                        <label><fmt:message key="user.middle_name" bundle="${user_bundle}"/></label>: <label>${user.middleName}</label><br>
                        <label><fmt:message key="user.passport_number" bundle="${user_bundle}"/></label>: <label>${user.passportNumber}</label><br>
                        <label><fmt:message key="user.phone" bundle="${user_bundle}"/></label>: <label>${user.phone}</label><br>
                        <label><fmt:message key="user.email" bundle="${user_bundle}"/></label>: <label>${user.email}</label><br>
                    </div>
                </div>
                <div class="row d-flex justify-content-center">
                    <a href="/serv?action=edit_user&id=${user.id}" class="btn btn-success col-3 ml-5">
                        <fmt:message key="button.edit" bundle="${general}"/>
                    </a>
                </div>
            </div>
            <div class="col-1"></div>
        </div>
    </div>
</div>
</body>
</html>
