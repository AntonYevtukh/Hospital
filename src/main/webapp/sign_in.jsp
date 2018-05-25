<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<fmt:setLocale value="${current_user ne null ? current_user.language : 'ru'}"/>
<fmt:setBundle basename="general" var="general" />
<fmt:setBundle basename="user" var="user_bundle" />
<fmt:setBundle basename="errors" var="errors"/>
<html>
<head>
    <jsp:include page="meta.jsp"/>
</head>
<body>
<div class="d-flex bg-light" style="min-height:100%">
    <c:set scope="request" var="top_header" value="true"/>
    <jsp:include page="header.jsp"/>
    <div class="col-12 align-self-center">
        <h1 class="text-center pb-3"><fmt:message key="title.sign_in" bundle="${general}"/> </h1>
        <div class="row">
            <div class="col-4"></div>
            <div class="col-4">
                <form class="form-group" method="POST" action="/serv">
                    <input type="hidden" name="action" value="sign_in">
                    <c:set var = "status" scope = "page" value = "${sign_in_fails.contains('error.login_not_found') ? 'is-invalid' : ''}"/>
                    <div class="row input-group-text bg-light border-0 pt-0 pb-0">
                        <label class="mb-1"><fmt:message key="user.login" bundle="${user_bundle}"/></label>
                        <input type="text" class="form-control form-control-sm ${status}" name="login" value="${login}"
                               <c:if test="${status eq 'is-invalid'}">
                                   data-toggle="tooltip" data-placement="bottom"
                                   title="<fmt:message key = "error.login_not_found" bundle="${errors}"/>"
                               </c:if>
                               onClick="removeInvalidClassAndTooltip(this)"/>
                    </div>
                    <c:set var = "status" scope = "page" value = "${sign_in_fails.contains('error.password_wrong') ? 'is-invalid' : ''}"/>
                    <div class="row input-group-text bg-light border-0 pt-0 pb-0">
                        <label class="mb-1"><fmt:message key="user.password" bundle="${user_bundle}"/></label>
                        <input type="password" class="form-control form-control-sm ${status}" name="password" value=""
                                <c:if test="${status eq 'is-invalid'}">
                                    data-toggle="tooltip" data-placement="bottom"
                                    title="<fmt:message key = "error.password_wrong" bundle="${errors}"/>"
                                </c:if>
                               onClick="removeInvalidClassAndTooltip(this)"/>
                    </div>
                    <div class="d-flex pt-4 p-2 justify-content-around">
                        <input type="submit" class="btn btn-success col-4" value=
                                "<fmt:message key="button.sign_in" bundle="${general}"/>">
                        <a href="/serv?action=new_user" class="btn btn-danger col-4">
                            <fmt:message key="button.sign_up" bundle="${general}"/>
                        </a>
                    </div>
                </form>
            </div>
            <div class="col-4"></div>
        </div>
    </div>
</div>
<script charset="UTF-8">
    $('#date_of_birth').datepicker({
        uiLibrary: 'bootstrap4',
        format: "dd.mm.yyyy",
        weekStart: 1,
        language: "ru",
        calendarWeeks: true
    });
</script>
<script>
    $(function () {
        $('[data-toggle="tooltip"]').tooltip({
                trigger: 'hover'
        });
        $('.is-invalid[data-toggle="tooltip"]').tooltip('dispose');
        $('.is-invalid[data-toggle="tooltip"]').tooltip({
                trigger: 'hover focus',
                template:
                '<div class="tooltip danger" role="tooltip">' +
                '<div class="arrow danger"></div>' +
                '<div class="tooltip-inner danger"></div>' +
                '</div>'
        });
    });

    function removeInvalidClass(sourceElement){
        var element = $(sourceElement);
        element.removeClass("is-invalid");
        element.tooltip('dispose');
        element.tooltip({
            trigger: 'hover',
        });
        element.tooltip('show');
    }

    function removeInvalidClassAndTooltip(sourceElement){
        var element = $(sourceElement);
        element.removeClass("is-invalid");
        element.tooltip('dispose');
    }
</script>
</body>