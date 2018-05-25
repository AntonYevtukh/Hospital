<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ctg" uri="customtaglib" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<fmt:setLocale value="${current_user ne null ? current_user.language : 'ru'}"/>
<fmt:setBundle basename="gender" var="gender_bundle" />
<fmt:setBundle basename="validation.validation" var="validation" />
<fmt:setBundle basename="user" var="user_bundle"/>
<fmt:setBundle basename="general" var="general" />
<fmt:setBundle basename="formats" var="formats" />
<c:set var="format_string"><fmt:message key="date.regular" bundle="${formats}"/></c:set>
<html>
<head>
    <jsp:include page="meta.jsp"/>
</head>
<body>
<div class="bg-light" style="min-height:100%">
    <c:set scope="request" var="top_header" value="false"/>
    <jsp:include page="header.jsp"/>
    <h1 class="text-center pt-3"><fmt:message key="title.sign_up" bundle="${general}"/></h1>
    <div class="row m-0">
        <div class="col-1"></div>
        <div class="col-10">
            <ul class="nav nav-tabs mr-5 ml-5 mt-3 mb-3 d-flex justify-content-center" id="myTab" role="tablist">
                <li class="nav-item col-6 pr-1">
                    <a class="text-center nav-link active" id="profile-tab" data-toggle="tab"
                       href="#profile" role="tab" aria-controls="home" aria-selected="true">
                        <fmt:message key="tab.profile" bundle="${user_bundle}"/>
                    </a>
                </li>
                <li class="nav-item col-6 pl-1">
                    <a class="text-center nav-link" id="person-tab" data-toggle="tab"
                       href="#person" role="tab" aria-controls="profile" aria-selected="false">
                        <fmt:message key="tab.person" bundle="${user_bundle}"/>
                    </a>
                </li>
            </ul>
            <form class="form-group tab-content mr-5 ml-5" id="myTabContent" method="POST" action="/serv" enctype="multipart/form-data">
                <input type="hidden" name="action" value="sign_up">
                <div class="tab-pane fade show active" id="profile" role="tabpanel" aria-labelledby="profile-tab">
                    <div class="row m-0">
                        <div class="col-8">
                            <div class="row m-0">
                                <div class="col-6 pl-0">
                                    <c:set var = "status" scope = "page"
                                           value = "${validationFails.contains('validation.user.login') ? 'is-invalid' : ''}"/>
                                    <div class="row input-group-text bg-light border-0 pt-0 pb-0">
                                        <label class="mb-1"><fmt:message key="user.login" bundle="${user_bundle}"/></label>
                                        <input type="text" class="form-control form-control-sm ${status}" name="login"
                                               value="${user.login}" onClick="removeInvalidClass(this)"
                                               data-toggle="tooltip" data-placement="bottom"
                                               title="<fmt:message key = 'validation.user.login' bundle='${validation}'/>"/>
                                    </div>

                                    <c:set var = "status" scope = "page"
                                           value = "${validationFails.contains('validation.user.password') ? 'is-invalid' : ''}"/>
                                    <div class="row input-group-text bg-light border-0 pt-0 pb-0">
                                        <label class="mb-1"><fmt:message key="user.password" bundle="${user_bundle}"/></label>
                                        <input type="password" class="form-control form-control-sm ${status}" name="password"
                                               value="" onClick="removeInvalidClass(this)"
                                               data-toggle="tooltip" data-placement="bottom"
                                               title="<fmt:message key = 'validation.user.password' bundle='${validation}'/>"/>
                                    </div>

                                    <c:set var = "status" scope = "page"
                                           value = "${validationFails.contains('validation.user.password_retype') ? 'is-invalid' : ''}"/>
                                    <div class="row input-group-text bg-light border-0 pt-0 pb-0">
                                        <label class="mb-1"><fmt:message key="user.password_retype" bundle="${user_bundle}"/></label>
                                        <input type="password" class="form-control form-control-sm ${status}" name="password_retype"
                                               value="" onClick="removeInvalidClass(this)"
                                               data-toggle="tooltip" data-placement="bottom"
                                               title="<fmt:message key = 'validation.user.password_retype' bundle='${validation}'/>"/>
                                    </div>

                                    <div class="row input-group-text bg-light border-0 pt-0 pb-0">
                                        <label class="mb-1"><fmt:message key="user.language" bundle="${user_bundle}"/></label>
                                        <select name="language" class="form-control form-control-sm">
                                            <option value="ru" selected ><fmt:message key="language.ru" bundle="${user_bundle}"/></option>
                                            <option value="en"> <fmt:message key="language.en" bundle="${user_bundle}"/></option>
                                        </select>
                                    </div>
                                </div>
                                <div class="col-6 pr-0 d-flex justify-content-start">
                                    <div <c:if test="${validationFails.contains('validation.user.roles')}">
                                        onClick="removeInvalidClassAndTooltip(this)" data-toggle="tooltip" data-placement="bottom"
                                        title="<fmt:message key = 'validation.user.roles' bundle='${validation}'/>"
                                    </c:if>>
                                        <label class="mb-1"><fmt:message key="user.roles" bundle="${user_bundle}"/></label>
                                        <c:forEach items="${all_roles}" var="role">
                                            <c:set var="checked" scope="page" value="${user.roleMap.containsKey(role.id) ? 'checked' : ''}"/>
                                            <div class="form-check checkbox checkbox-primary">
                                                <input class="form-check-input styled" type="checkbox" name="role" value="${role.id}" ${checked}>
                                                <label class="form-check-label">${role.name}</label>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </div>
                            </div>
                            <div class="row m-0">
                                <div class="col-6 pl-0">
                                    <div class="row input-group-text bg-light border-0 pt-0 pb-0">
                                        <c:set var="status" scope="page" value=""/>
                                        <label class="mb-1"><fmt:message key="user.items" bundle="${user_bundle}"/></label>
                                        <input type="number" min="1" max="20" step="1" class="form-control form-control-sm ${status}" name="items"
                                               value="${user ne null ? user.itemsPerPage : 10}" onClick="removeInvalidClass(this)"
                                               data-toggle="tooltip" data-placement="bottom"
                                               title="<fmt:message key = 'validation.user.items' bundle='${validation}'/>"/>
                                    </div>
                                </div>
                                <div class="col-6 pr-0">
                                    <c:set var="status" scope="page" value="${validationFails.contains('validation.user.photo') ? 'is-invalid' : ''}"/>
                                    <div class="row input-group-text bg-light border-0 pt-0 pb-0">
                                        <label class="mb-1"><fmt:message key="user.photo_upload" bundle="${user_bundle}"/></label>
                                        <input type="file" class="form-control form-control-sm file ${status}" name="photo"
                                               id="fileinput" value="" onClick="removeInvalidClass(this)"
                                               data-toggle="tooltip" data-placement="bottom" onchange="readURL(this);"
                                               title="<fmt:message key = 'validation.user.photo' bundle='${validation}'/>"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-4 pt-0 mt-0">
                            <div class="row">
                                <div class="col-1"></div>
                                <div class="col-10 pl-2 pr-2">
                                    <label class="mb-1 mt-0"><fmt:message key="user.photo" bundle="${user_bundle}"/></label>
                                    <div class="img-square-container">
                                        <ctg:photo photo="${user.photo}" cssClass="img-square" htmlId="photo"/>
                                    </div>
                                </div>
                                <div class="col-1"></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="tab-pane fade" id="person" role="tabpanel" aria-labelledby="person-tab">
                    <div class="row pr-3 pl-3">
                        <div class="col-4 pb-1">
                            <div class="row input-group-text bg-light border-0 pt-0 pb-0">
                                <c:set var="status" scope="page"
                                       value="${validationFails.contains('validation.user.last_name') ? 'is-invalid' : ''}"/>
                                <label class="mb-1"><fmt:message key="user.last_name" bundle="${user_bundle}"/></label>
                                <input type="text" class="form-control form-control-sm ${status}" name="last_name"
                                       value="${user.lastName}" onClick="removeInvalidClass(this)"
                                       data-toggle="tooltip" data-placement="bottom"
                                       title="<fmt:message key = 'validation.user.last_name' bundle='${validation}'/>"/>
                            </div>
                        </div>
                        <div class="col-4 pb-1">
                            <div class="row input-group-text bg-light border-0 pt-0 pb-0">
                                <c:set var="status" scope="page"
                                       value="${validationFails.contains('validation.user.first_name') ? 'is-invalid' : ''}"/>
                                <label class="mb-1"><fmt:message key="user.first_name" bundle="${user_bundle}"/></label>
                                <input type="text" class="form-control form-control-sm ${status}" name="first_name"
                                       value="${user.firstName}" onClick="removeInvalidClass(this)"
                                       data-toggle="tooltip" data-placement="bottom"
                                       title="<fmt:message key = 'validation.user.first_name' bundle='${validation}'/>"/>
                            </div>
                        </div>
                        <div class="col-4 pb-1">
                            <div class="row input-group-text bg-light border-0 pt-0 pb-0">
                                <c:set var="status" scope="page"
                                       value="${validationFails.contains('validation.user.middle_name') ? 'is-invalid' : ''}"/>
                                <label class="mb-1"><fmt:message key="user.middle_name" bundle="${user_bundle}"/></label>
                                <input type="text" class="form-control form-control-sm ${status}" name="middle_name"
                                       value="${user.middleName}" onClick="removeInvalidClass(this)"
                                       data-toggle="tooltip" data-placement="bottom"
                                       title="<fmt:message key = 'validation.user.middle_name' bundle='${validation}'/>"/>
                            </div>
                        </div>
                    </div>

                    <div class="row pr-3 pl-3">
                        <div class="col-4 pb-1">
                            <div class="row input-group-text bg-light border-0 pt-0 pb-0">
                                <c:set var="status" scope="page"
                                       value="${validationFails.contains('validation.user.date_of_birth') ? 'is-invalid' : ''}"/>
                                <label class="mb-1"><fmt:message key="user.date_of_birth" bundle="${user_bundle}"/></label>
                                <input type="text" class="form-control form-control-sm ${status}" name="date_of_birth"
                                       value="<fmt:formatDate value="${user.dateOfBirth}" pattern="${format_string}"/>"
                                       id="date_of_birth" onClick="removeInvalidClass(this)"
                                       data-toggle="tooltip" data-placement="bottom"
                                       title="<fmt:message key = 'validation.user.date_of_birth' bundle='${validation}'/>"/>
                            </div>
                        </div>
                        <div class="col-4 pb-1">
                            <div class="row input-group-text bg-light border-0 pt-0 pb-0">
                                <label class="mb-1"><fmt:message key="user.gender" bundle="${user_bundle}"/></label>
                                <select name="gender" class="form-control form-control-sm">
                                    <c:forEach items="${genders_global}" var="gender">
                                        <option value="${gender.code}" ${gender.code == 0 ? 'selected' : ''}>
                                            <fmt:message key="${gender.code}" bundle="${gender_bundle}"/>
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="col-4 pb-1">
                            <div class="row input-group-text bg-light border-0 pt-0 pb-0">
                                <c:set var="status" scope="page"
                                       value="${validationFails.contains('validation.user.passport_number') ? 'is-invalid' : ''}"/>
                                <label class="mb-1"><fmt:message key="user.passport_number" bundle="${user_bundle}"/></label>
                                <input type="text" class="form-control form-control-sm ${status}" name="passport_number"
                                       value="${user.passportNumber}" onClick="removeInvalidClass(this)" placeholder="AAXXXXX"
                                       data-toggle="tooltip" data-placement="bottom"
                                       title="<fmt:message key = 'validation.user.passport_number' bundle='${validation}'/>"/>
                            </div>
                        </div>
                    </div>

                    <div class="row pr-3 pl-3">
                        <div class="col-4">
                            <div class="row input-group-text bg-light border-0 pt-0 pb-0">
                                <c:set var="status" scope="page"
                                       value="${validationFails.contains('validation.user.phone') ? 'is-invalid' : ''}"/>
                                <label class="mb-1"><fmt:message key="user.phone" bundle="${user_bundle}"/></label>
                                <input type="text" class="form-control form-control-sm ${status}" name="phone"
                                       value="${user.phone}" onClick="removeInvalidClass(this)"
                                       placeholder="(XXX)-XXX-XX-XX" data-toggle="tooltip" data-placement="bottom"
                                       title="<fmt:message key = 'validation.user.phone' bundle='${validation}'/>"/>
                            </div>
                            <div class="row input-group-text bg-light border-0 pt-0 pb-0">
                                <c:set var="status" scope="page"
                                       value="${validationFails.contains('validation.user.email') ? 'is-invalid' : ''}"/>
                                <label class="mb-1"><fmt:message key="user.email" bundle="${user_bundle}"/></label>
                                <input type="text" class="form-control form-control-sm ${status}" name="email"
                                       value="${user.email}" onClick="removeInvalidClass(this)"
                                       placeholder="username@example.com" data-toggle="tooltip" data-placement="bottom"
                                       title="<fmt:message key = 'validation.user.email' bundle='${validation}'/>"/>
                            </div>
                        </div>
                        <div class="col-8">
                            <div class="row input-group-text bg-light border-0 pt-0 pb-0">
                                <label class="mb-1"><fmt:message key="user.address" bundle="${user_bundle}"/></label>
                                <textarea type="text" class="form-control form-control-sm" placeholder="(XXX)-XXX-XX-XX"
                                          name="address" value="" rows="4">
                                    ${user.address}
                                </textarea>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="d-flex pt-5 p-2 justify-content-center">
                    <input type="submit" class="btn btn-success col-3 mr-5" value=
                            "<fmt:message key="button.sign_up" bundle="${general}"/>">
                    <a href="sign_up.jsp" class="btn btn-danger col-3 ml-5">
                        <fmt:message key="button.sign_in" bundle="${general}"/>
                    </a>
                </div>
            </form>
        </div>
        <div class="col-1"></div>
    </div>
</div>
<script charset="UTF-8">
    $('#date_of_birth').datepicker({
        uiLibrary: 'bootstrap4',
        format: "dd.mm.yyyy",
        weekStart: 1,
        language: "${current_user ne null ? current_user.language : 'ru'}",
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

    $("#fileinput").fileinput({
        mainClass: "file-caption-main w-100",
        captionClass: "p-0",
        browseClass: "btn btn-primary btn-sm",
        showPreview: false,
        showRemove: false,
        showCancel: false,
        showUpload: false,
        language: "${current_user ne null ? current_user.language : 'ru'}",
        allowedFileExtensions: ["jpg", "png", "gif"]
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

    function readURL(input) {
        if (input.files && input.files[0]) {
            var reader = new FileReader();

            reader.onload = function (e) {
                $('#photo')
                    .attr('src', e.target.result)
                    .width(150)
                    .height(200);
            };

            reader.readAsDataURL(input.files[0]);
        }
    }
</script>
</body>