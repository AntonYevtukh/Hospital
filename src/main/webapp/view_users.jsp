<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
<body>
<jsp:include page="header.jsp"/>
<h1 class="text-center pt-3 pb-3"><fmt:message key="${title}" bundle="${general}"/></h1>
<div class="row m-0">
    <div class="col-1"></div>
    <div class="col-10">
        <table class="table table-sm table-bordered table-hover">
            <thead class="bg-primary text-dark">
                <tr class="d-flex">
                    <th class="col-1 border border-primary" scope="col">#</th>
                    <th class="col-2 border border-primary" scope="col"><fmt:message key="user.last_name" bundle="${user_bundle}"/></th>
                    <th class="col-3 border border-primary" scope="col"><fmt:message key="user.first_name" bundle="${user_bundle}"/></th>
                    <th class="col-3 border border-primary" scope="col"><fmt:message key="user.middle_name" bundle="${user_bundle}"/></th>
                    <th class="col-1 border border-primary" scope="col"><fmt:message key="user.passport_number" bundle="${user_bundle}"/></th>
                    <th class="col-2 border border-primary" scope="col"><fmt:message key="form.actions" bundle="${general}"/></th>
                </tr>
            </thead>
            <tbody>
            <c:forEach items="${page_content.content}" var="user" begin="0" varStatus="loop">
                <tr class="d-flex">
                    <th class="col-1" scope="row">${loop.index + 1}</th>
                    <th class="col-2" scope="col">${user.lastName}</th>
                    <th class="col-3" scope="col">${user.firstName}</th>
                    <th class="col-3" scope="col">${user.middleName}</th>
                    <th class="col-1" scope="col">
                        <c:choose>
                            <c:when test="${can_view_all}">
                                ${user.passportNumber}
                            </c:when>
                            <c:otherwise>
                                <fmt:message key="message.na" bundle="${general}"/>
                            </c:otherwise>
                        </c:choose>
                    </th>
                    <th class="col-2 d-flex justify-content-around pt-0 pb-0" scope="col">
                        <a href="/serv?action=view_user&id=${user.id}">
                            <i class="fa fa-eye fa-2x" aria-hidden="true"></i>
                        </a>
                        <c:choose>
                            <c:when test="${can_edit}">
                                <a href="/serv?action=edit_user&id=${user.id}">
                                    <i class="fa fa-edit fa-2x" aria-hidden="true"></i>
                                </a>
                            </c:when>
                            <c:otherwise>
                                <form class="m-0">
                                    <button class="link disabled" href="/serv?action=edit_user&id=${user.id}" disabled>
                                        <i class="fa fa-edit fa-2x" aria-hidden="true"></i>
                                    </button>
                                </form>
                            </c:otherwise>
                        </c:choose>
                        <form class="m-0">
                            <button class="d-flex align-top p-0 link ${can_edit ? '' : 'disabled'}"
                                ${can_edit ? '' : 'disabled'} onclick="deleteUser(${user.id}, window.location.href)">
                                <i class="fa fa-times fa-2x" aria-hidden="true"></i>
                            </button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <div class="row ml-0 mr-0 d-flex justify-content-between">
            <ctg:bootstrapPagination urlPattern="${url_pattern}" page="${page_content.page}"
                                     totalPages="${page_content.totalPages}"/>
        </div>
    </div>
    <div class="col-1"></div>
</div>
<jsp:include page="message_modal.jsp"/>
</body>
<script>


    function deleteUser(userId, redirectUrl) {
        event.preventDefault();
        $.ajax('/serv?action=delete_user&id=' + userId, {
            type: "POST",
            success: function(data){
                console.log(data);
                data = JSON.parse(data);
                onSuccess(data, redirectUrl, function(errors) {
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
                messageModalSuccessHeader.attr('hidden');
        }
        modal.modal('show');
    }

    function redirect(url) {
        window.location = url;
    }
</script>
</html>
