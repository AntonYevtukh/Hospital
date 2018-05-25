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
        <h1 class="text-center mt-4 mb-4"><ctg:userShortInfo user="${user}" showPassportNumber="false"/></h1>
        <div class="row">
            <div class="col-1"></div>
            <div class="col-10">
                <div class="row">
                    <div class="col-3">
                        <div class="img-square-container">
                            <ctg:photo photo="${user.photo}" cssClass="img-square" htmlId="photo"/>
                        </div>
                        <a href="/serv?action=edit_user&id=${user.id}" class="btn btn-success mt-2 col-12">
                            <fmt:message key="button.edit" bundle="${general}"/>
                        </a>
                        <button class="btn btn-danger mt-2 col-12" onclick="deleteUser(${user.id})">
                            <fmt:message key="button.delete" bundle="${general}"/>
                        </button>
                    </div>
                    <div class="col-3">
                        <label><fmt:message key="user.last_name" bundle="${user_bundle}"/></label>: <label>${user.lastName}</label><br>
                        <label><fmt:message key="user.first_name" bundle="${user_bundle}"/></label>: <label>${user.firstName}</label><br>
                        <label><fmt:message key="user.middle_name" bundle="${user_bundle}"/></label>: <label>${user.middleName}</label><br>
                        <label><fmt:message key="user.passport_number" bundle="${user_bundle}"/></label>: <label>${user.passportNumber}</label><br>
                        <label><fmt:message key="user.phone" bundle="${user_bundle}"/></label>: <label>${user.phone}</label><br>
                        <label><fmt:message key="user.email" bundle="${user_bundle}"/></label>: <label>${user.email}</label><br>
                    </div>
                    <div class="col-3">
                        <label><fmt:message key="user.last_name" bundle="${user_bundle}"/></label>: <label>${user.lastName}</label><br>
                        <label><fmt:message key="user.first_name" bundle="${user_bundle}"/></label>: <label>${user.firstName}</label><br>
                        <label><fmt:message key="user.middle_name" bundle="${user_bundle}"/></label>: <label>${user.middleName}</label><br>
                        <label><fmt:message key="user.passport_number" bundle="${user_bundle}"/></label>: <label>${user.passportNumber}</label><br>
                        <label><fmt:message key="user.phone" bundle="${user_bundle}"/></label>: <label>${user.phone}</label><br>
                        <label><fmt:message key="user.email" bundle="${user_bundle}"/></label>: <label>${user.email}</label><br>
                    </div>
                    <div class="col-3">
                        <c:choose>
                            <c:when test="${user.roleMap.containsKey(1) and current_user.roleMap.containsKey(3) and (user.id != current_user.id)}">
                                <div class="btn-group col-12">
                                    <button type="button" class="col-12 btn btn-primary dropdown-toggle dropdown" data-toggle="dropdown"
                                            aria-haspopup="true" aria-expanded="false">
                                        <fmt:message key="user.actions" bundle="${user_bundle}"/>
                                    </button>
                                    <div class="dropdown-menu dropdown-menu-right">
                                        <a class="dropdown-item" href="/serv?action=new_examination&patient_id=${user.id}">
                                            <fmt:message key="user.new_examination" bundle="${user_bundle}"/>
                                        </a>
                                        <c:set var="disabled" value="${!user.hospitalized ? '' : 'disabled'}"/>
                                        <c:set var="href" value="${!user.hospitalized ? '/serv?action=new_hospitalization&patient_id='.concat(user.id) : '#'}"/>
                                        <a class="dropdown-item ${disabled}" href="${href}">
                                            <fmt:message key="user.new_hospitalization" bundle="${user_bundle}"/>
                                        </a>
                                        <c:set var="disabled" value="${user.hospitalized ? '' : 'disabled'}"/>
                                        <c:set var="href" value="${user.hospitalized ? '/serv?action=new_discharge&patient_id='.concat(user.id) : '#'}"/>
                                        <a class="dropdown-item ${disabled}" href="${href}">
                                            <fmt:message key="user.new_discharge" bundle="${user_bundle}"/>
                                        </a>
                                        <div class="dropdown-divider"></div>
                                        <a class="dropdown-item" href="#">Separated link</a>
                                    </div>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="btn-group col-12">
                                    <button type="button" class="col-12 btn btn-secondary dropdown-toggle dropdown disabled" data-toggle="dropdown"
                                            aria-haspopup="true" aria-expanded="false">
                                        <fmt:message key="user.actions" bundle="${user_bundle}"/>
                                    </button>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
            <div class="col-1"></div>
        </div>
    </div>
</div>
<jsp:include page="message_modal.jsp"/>
</body>
<script>
    function deleteUser(userId) {
        event.preventDefault();
        $.ajax('/serv?action=delete_user&id=' + userId, {
            type: "POST",
            success: function(data){
                data = JSON.parse(data);
                onSuccess(data, window.location.href, function(errors) {
                })
            }
        })
    }

    function onSuccess(data, url, showErrors) {
        if (data.success != undefined) {
            console.log(data.success);
            showMessageModal(data.success, 'success', url);
        }
        if (data.error != undefined) {
            showMessageModal(data.error, 'error', window.location.href)
        }
        else if (data.errors != undefined) {
            showErrors(data.errors);
        }
    }

    function showMessageModal(message, style, url) {
        var modals = $('.modal');
        for (var i = 0; i < modals.length; i++) {
            $(modals[i]).modal('hide');
        }
        var modal = $('#messageModal');
        var messageText = $('#messageModalText');
        var messageModalButton = $('#messageModalButton');
        var messageModalSuccessHeader = $('#messageModalSuccessHeader');
        var messageModalErrorHeader = $('#messageModalErrorHeader');
        messageText.text(message);
        messageModalButton.click(function(){ return redirect(url);})
        switch (style) {
            case 'success':
                messageModalButton.removeClass('btn-danger');
                messageModalButton.addClass('btn-success');
                messageText.removeClass('alert-danger');
                messageText.addClass('alert-success');
                messageModalSuccessHeader.removeAttr('hidden');
                messageModalErrorHeader.attr('hidden', '');
                break;
            case 'error':
                messageModalButton.removeClass('btn-success');
                messageModalButton.addClass('btn-danger');
                messageText.removeClass('alert-success');
                messageText.addClass('alert-danger');
                messageModalErrorHeader.removeAttr('hidden');
                messageModalSuccessHeader.attr('hidden', '');
        }
        modal.modal('show');
    }

    function redirect(url) {
        window.location = url;
    }
</script>
</html>
