<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ctg" uri="customtaglib" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<fmt:setLocale value="${current_user ne null ? current_user.language : 'ru'}"/>
<fmt:setBundle basename="gender" var="gender_bundle" />
<fmt:setBundle basename="validation.validation" var="validation" />
<fmt:setBundle basename="settings" var="settings_bundle"/>
<fmt:setBundle basename="general" var="general" />
<html>
<head>
    <jsp:include page="meta.jsp"/>
</head>
<body>
<jsp:include page="header.jsp"/>
<h1 class="text-center pt-3 pb-0"><fmt:message key="title.view_settings" bundle="${general}"/></h1>
<div class="row m-0">
    <div class="col-1"></div>
    <div class="col-10">
        <ul class="nav nav-tabs mt-3 mb-3 d-flex justify-content-center" id="myTab" role="tablist">
            <li class="nav-item col-6 pr-1">
                <a class="text-center nav-link active" id="roles-tab" data-toggle="tab"
                   href="#roles" role="tab" aria-controls="home" aria-selected="true">
                    <fmt:message key="title.view_settings.roles" bundle="${general}"/>
                </a>
            </li>
            <li class="nav-item col-6 pl-1">
                <a class="text-center nav-link" id="assignment-types-tab" data-toggle="tab"
                   href="#assignmentTypes" role="tab" aria-controls="profile" aria-selected="false">
                    <fmt:message key="title.view_settings.assignment_types" bundle="${general}"/>
                </a>
            </li>
        </ul>
        <div class="form-group tab-content mr-5 ml-5" id="myTabContent">
            <div class="tab-pane fade show active" id="roles" role="tabpanel" >
                <div class="row m-0">
                    <table class="table table-sm table-bordered table-hover">
                        <thead class="bg-primary text-dark">
                        <tr class="d-flex ">
                            <th class="col-1 border-primary border" scope="col"><fmt:message key="role.id" bundle="${settings_bundle}"/></th>
                            <th class="col-2 border-primary border" scope="col"><fmt:message key="role.name" bundle="${settings_bundle}"/></th>
                            <th class="col-7 border-primary border" scope="col"><fmt:message key="role.assignment_types" bundle="${settings_bundle}"/></th>
                            <th class="col-2 border-primary border" scope="col"><fmt:message key="form.actions" bundle="${general}"/></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${roles}" var="role">
                            <tr class="d-flex">
                                <th class="col-1" scope="row">${role.id}</th>
                                <td class="col-2">${role.name}</td>
                                <td class="col-7"><ctg:roleAssignmentTypeNames role="${role}"/></td>
                                <td class="col-2 d-flex justify-content-around pt-0 pb-0">
                                    <a data-toggle="modal" href="#viewRoleModal"
                                       data-id="${role.id}" data-name="${role.name}"
                                       data-assignment-types="<ctg:roleAssignmentTypeIdsArray role="${role}"/>"/>
                                    <i class="fa fa-eye fa-2x" aria-hidden="true"></i>
                                    </a>
                                    <a data-toggle="modal" href="#editRoleModal"
                                       data-id="${role.id}" data-name="${role.name}"
                                       data-assignment-types="<ctg:roleAssignmentTypeIdsArray role="${role}"/>"/>
                                    <i class="fa fa-edit fa-2x" aria-hidden="true"></i>
                                    </a>

                                    <form class="m-0">
                                        <button class="d-flex align-top p-0 link" onclick="deleteRole(${role.id})">
                                            <i class="fa fa-times fa-2x" aria-hidden="true"></i>
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="row ml-0 mr-0 d-flex justify-content-end">
                    <a data-toggle="modal" href="#addRoleModal" class="btn btn-sm col-1 btn-primary">
                        <fmt:message key="button.add" bundle="${general}"/>
                    </a>
                </div>
            </div>
            <div class="tab-pane fade" id="assignmentTypes" role="tabpanel" >
                <div class="row m-0">
                    <table class="table table-sm table-bordered table-hover">
                        <thead class="bg-primary text-dark">
                        <tr class="d-flex ">
                            <th class="col-1 border-primary border" scope="col">#</th>
                            <th class="col-6 border-primary border" scope="col">
                                <fmt:message key="assignment_type.name" bundle="${settings_bundle}"/>
                            </th>
                            <th class="col-3 border-primary border" scope="col">
                                <fmt:message key="assignment_type.hospitalization_required" bundle="${settings_bundle}"/>
                            </th>
                            <th class="col-2 border-primary border" scope="col">
                                <fmt:message key="form.actions" bundle="${general}"/>
                            </th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${assignment_types}" begin="0" var="assignment_type" varStatus="number">
                            <tr class="d-flex">
                                <th class="col-1" scope="row">${number.index}</th>
                                <td class="col-6">${assignment_type.name}</td>
                                <td class="col-3 d-flex justify-content-center pt-0 pb-0">
                                    <c:if test="${assignment_type.hospitalizationRequired}">
                                        <i class="fa fa-check fa-2x" aria-hidden="true"></i>
                                    </c:if>
                                </td>
                                <td class="col-2 d-flex justify-content-around pt-0 pb-0">
                                    <a data-toggle="modal" href="#viewAssignmentTypeModal"
                                       data-id="${assignment_type.id}" data-name="${assignment_type.name}"
                                       data-hospitalization-required="${assignment_type.hospitalizationRequired}"/>
                                    <i class="fa fa-eye fa-2x" aria-hidden="true"></i>
                                    </a>
                                    <a data-toggle="modal" href="#editAssignmentTypeModal"
                                       data-id="${assignment_type.id}" data-name="${assignment_type.name}"
                                       data-hospitalization-required="${assignment_type.hospitalizationRequired}"/>
                                    <i class="fa fa-edit fa-2x" aria-hidden="true"></i>
                                    </a>
                                    <form class="m-0">
                                        <button class="d-flex align-top p-0 link" onclick="deleteAssignmentType(${assignment_type.id})">
                                            <i class="fa fa-times fa-2x" aria-hidden="true"></i>
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="row ml-0 mr-0 d-flex justify-content-end">
                    <a data-toggle="modal" href="#addAssignmentTypeModal" class="btn btn-sm col-1 btn-primary">
                        <fmt:message key="button.add" bundle="${general}"/>
                    </a>
                </div>
            </div>
        </div>
    </div>
    <div class="col-1"></div>
</div>

<div class="modal " id="viewRoleModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg  " role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form id="viewRoleForm">
                    <input type="hidden" name="id">
                    <div class="form-group row mb-1 ml-4 mr-4">
                        <label for="viewRoleId" class="col-3 col-form-label">
                            <fmt:message key="role.id" bundle="${settings_bundle}"/>
                        </label>
                        <div class="col-9">
                            <input type="text" readonly class="form-control-plaintext form-control-sm" id="viewRoleId" name="code">
                        </div>
                    </div>
                    <div class="form-group row mb-1 ml-4 mr-4">
                        <label for="viewRoleName" class="col-3 col-form-label">
                            <fmt:message key="role.name" bundle="${settings_bundle}"/>
                        </label>
                        <div class="col-9">
                            <input type="text" readonly class="form-control-plaintext form-control-sm" id="viewRoleName" name="name">
                        </div>
                    </div>
                    <div class="form-group row mb-1 ml-4 mr-4">
                        <label for="viewRoleAssignmentTypes" class="col-3 col-form-label">
                            <fmt:message key="role.assignment_types" bundle="${settings_bundle}"/>
                        </label>
                        <div class="col-9">
                            <div id="viewRoleAssignmentTypes">
                                <c:forEach items="${assignment_types}" var="assignment_type">
                                    <div class="form-check checkbox checkbox-primary">
                                        <input readonly disabled class="form-check-input styled" type="checkbox" name="assignment_type_id" value="${assignment_type.id}">
                                        <label class="form-check-label">${assignment_type.name}</label>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-sm btn-danger" data-dismiss="modal">
                    <fmt:message key="button.close" bundle="${general}"/>
                </button>
            </div>
        </div>
    </div>
</div>

<div class="modal " id="editRoleModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg  " role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form id="editRoleForm">
                    <div class="form-group row mb-1 ml-4 mr-4">
                        <label for="editRoleId" class="col-3 col-form-label">
                            <fmt:message key="role.id" bundle="${settings_bundle}"/>
                        </label>
                        <div class="col-9">
                            <input type="text" class="form-control-plaintext form-control-sm" id="editRoleId" name="id">
                        </div>
                    </div>
                    <div class="form-group row mb-1 ml-4 mr-4">
                        <label for="editRoleName" class="col-3 col-form-label">
                            <fmt:message key="role.name" bundle="${settings_bundle}"/>
                        </label>
                        <div class="col-9">
                            <input type="text"  class="form-control form-control-sm" id="editRoleName" name="name">
                        </div>
                    </div>
                    <div class="form-group row mb-1 ml-4 mr-4">
                        <label for="editRoleAssignmentTypes" class="col-3 col-form-label">
                            <fmt:message key="role.assignment_types" bundle="${settings_bundle}"/>
                        </label>
                        <div class="col-9">
                            <div id="editRoleAssignmentTypes">
                                <c:forEach items="${assignment_types}" var="assignment_type">
                                    <div class="form-check checkbox checkbox-primary">
                                        <input  class="form-check-input styled" type="checkbox" name="assignment_type_id" value="${assignment_type.id}">
                                        <label class="form-check-label">${assignment_type.name}</label>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-sm btn-primary" onclick="updateRole()">
                    <fmt:message key="button.update" bundle="${general}"/>
                </button>
                <button type="button" class="btn btn-sm btn-danger" data-dismiss="modal">
                    <fmt:message key="button.close" bundle="${general}"/>
                </button>
            </div>
        </div>
    </div>
</div>

<div class="modal " id="addRoleModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg  " role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form id="addRoleForm">
                    <div class="form-group row mb-1 ml-4 mr-4">
                        <label for="addRoleId" class="col-3 col-form-label">
                            <fmt:message key="role.id" bundle="${settings_bundle}"/>
                        </label>
                        <div class="col-9">
                            <input type="text"  class="form-control-plaintext form-control-sm" id="addRoleId" name="code">
                        </div>
                    </div>
                    <div class="form-group row mb-1 ml-4 mr-4">
                        <label for="addRoleName" class="col-3 col-form-label">
                            <fmt:message key="role.name" bundle="${settings_bundle}"/>
                        </label>
                        <div class="col-9">
                            <input type="text"  class="form-control form-control-sm" id="addRoleName" name="name">
                        </div>
                    </div>
                    <div class="form-group row mb-1 ml-4 mr-4">
                        <label for="addRoleAssignmentTypes" class="col-3 col-form-label">
                            <fmt:message key="role.assignment_types" bundle="${settings_bundle}"/>
                        </label>
                        <div class="col-9">
                            <div id="addRoleAssignmentTypes">
                                <c:forEach items="${assignment_types}" var="assignment_type">
                                    <div class="form-check checkbox checkbox-primary">
                                        <input  class="form-check-input styled" type="checkbox" name="assignment_type_id" value="${assignment_type.id}">
                                        <label class="form-check-label">${assignment_type.name}</label>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-sm btn-primary" onclick="addRole()">
                    <fmt:message key="button.update" bundle="${general}"/>
                </button>
                <button type="button" class="btn btn-sm btn-danger" data-dismiss="modal">
                    <fmt:message key="button.close" bundle="${general}"/>
                </button>
            </div>
        </div>
    </div>
</div>

<div class="modal " id="viewAssignmentTypeModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg  " role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form id="viewAssignmentTypeForm">
                    <div class="form-group row mb-1 ml-4 mr-4">
                        <label for="viewAssignmentTypeId" class="col-3 col-form-label">
                            <fmt:message key="assignment_type.id" bundle="${settings_bundle}"/>
                        </label>
                        <div class="col-9">
                            <input type="text" class="form-control-plaintext form-control-sm" id="viewAssignmentTypeId" name="id">
                        </div>
                    </div>
                    <div class="form-group row mb-1 ml-4 mr-4">
                        <label for="viewAssignmentTypeName" class="col-3 col-form-label">
                            <fmt:message key="assignment_type.name" bundle="${settings_bundle}"/>
                        </label>
                        <div class="col-9">
                            <input type="text" class="form-control-plaintext form-control-sm" id="viewAssignmentTypeName" name="name">
                        </div>
                    </div>
                    <div class="form-group row mb-1 ml-4 mr-4">
                        <label for="viewAssignmentCheckBox" class="col-3 col-form-label">
                            <fmt:message key="assignment_type.hospitalization_required" bundle="${settings_bundle}"/>
                        </label>
                        <div class="col-9 d-flex m-auto">
                            <div >
                                <div class="form-check checkbox checkbox-primary" id="viewAssignmentCheckBox">
                                    <input id="viewAssignmentTypeHospitalization" disabled
                                           class="form-check-input styled form-control-plaintext" type="checkbox"
                                           name="hospitalization_required" value="${assignment_type.hospitalizationRequired}">
                                    <label class="form-check-label">
                                        <fmt:message key="assignment_type.hospitalization_required" bundle="${settings_bundle}"/>
                                    </label>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-sm btn-danger" data-dismiss="modal">
                    <fmt:message key="button.close" bundle="${general}"/>
                </button>
            </div>
        </div>
    </div>
</div>

<div class="modal " id="editAssignmentTypeModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg  " role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form id="editAssignmentTypeForm">
                    <div class="form-group row mb-1 ml-4 mr-4">
                        <label for="editAssignmentTypeId" class="col-3 col-form-label">
                            <fmt:message key="assignment_type.id" bundle="${settings_bundle}"/>
                        </label>
                        <div class="col-9">
                            <input type="text" class="form-control-plaintext form-control-sm" id="editAssignmentTypeId" name="id">
                        </div>
                    </div>
                    <div class="form-group row mb-1 ml-4 mr-4">
                        <label for="editAssignmentTypeName" class="col-3 col-form-label">
                            <fmt:message key="assignment_type.name" bundle="${settings_bundle}"/>
                        </label>
                        <div class="col-9">
                            <input type="text" class="form-control form-control-sm" id="editAssignmentTypeName" name="name">
                        </div>
                    </div>
                    <div class="form-group row mb-1 ml-4 mr-4">
                        <label for="editAssignmentCheckBox" class="col-3 col-form-label">
                            <fmt:message key="assignment_type.hospitalization_required" bundle="${settings_bundle}"/>
                        </label>
                        <div class="col-9 d-flex m-auto">
                            <div >
                                <div class="form-check checkbox checkbox-primary" id="editAssignmentCheckBox">
                                    <input id="editAssignmentTypeHospitalization"
                                            class="form-check-input styled" type="checkbox"
                                            name="hospitalization_required" value="${assignment_type.hospitalizationRequired}">
                                    <label class="form-check-label">
                                        <fmt:message key="assignment_type.hospitalization_required" bundle="${settings_bundle}"/>
                                    </label>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-sm btn-primary" onclick="updateAssignmentType()">
                    <fmt:message key="button.update" bundle="${general}"/>
                </button>
                <button type="button" class="btn btn-sm btn-danger" data-dismiss="modal">
                    <fmt:message key="button.close" bundle="${general}"/>
                </button>
            </div>
        </div>
    </div>
</div>

<div class="modal " id="addAssignmentTypeModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg  " role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form id="addAssignmentTypeForm">
                    <div class="form-group row mb-1 ml-4 mr-4">
                        <label for="addAssignmentTypeId" class="col-3 col-form-label">
                            <fmt:message key="assignment_type.id" bundle="${settings_bundle}"/>
                        </label>
                        <div class="col-9">
                            <input type="text" class="form-control-plaintext form-control-sm" id="addAssignmentTypeId" name="id">
                        </div>
                    </div>
                    <div class="form-group row mb-1 ml-4 mr-4">
                        <label for="addAssignmentTypeName" class="col-3 col-form-label">
                            <fmt:message key="assignment_type.name" bundle="${settings_bundle}"/>
                        </label>
                        <div class="col-9">
                            <input type="text" class="form-control form-control-sm" id="addAssignmentTypeName" name="name">
                        </div>
                    </div>
                    <div class="form-group row mb-1 ml-4 mr-4">
                        <label for="addAssignmentCheckBox" class="col-3 col-form-label">
                            <fmt:message key="assignment_type.hospitalization_required" bundle="${settings_bundle}"/>
                        </label>
                        <div class="col-9 d-flex m-auto">
                            <div >
                                <div class="form-check checkbox checkbox-primary" id="addAssignmentCheckBox">
                                    <input id="addAssignmentTypeHospitalization"
                                           class="form-check-input styled" type="checkbox"
                                           name="hospitalization_required" value="${assignment_type.hospitalizationRequired}">
                                    <label class="form-check-label">
                                        <fmt:message key="assignment_type.hospitalization_required" bundle="${settings_bundle}"/>
                                    </label>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-sm btn-primary" onclick="addAssignmentType()">
                    <fmt:message key="button.update" bundle="${general}"/>
                </button>
                <button type="button" class="btn btn-sm btn-danger" data-dismiss="modal">
                    <fmt:message key="button.close" bundle="${general}"/>
                </button>
            </div>
        </div>
    </div>
</div>

<jsp:include page="message_modal.jsp"/>
</body>
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

    $('#viewRoleModal').on('show.bs.modal', function (event) {
        var link = $(event.relatedTarget);
        var id = link.data('id');
        var name = link.data('name');
        var assignmentTypeIdsArray = link.data('assignment-types');
        markRoleAssignmentTypeCheckboxes('#viewRoleAssignmentTypes', assignmentTypeIdsArray);
        var modal = $(this);
        modal.find('.modal-title').text(name);
        modal.find('#viewRoleId').val(id);
        modal.find('#viewRoleName').val(name);
    })
    
    $('#editRoleModal').on('show.bs.modal', function (event) {
        var link = $(event.relatedTarget);
        var id = link.data('id');
        var name = link.data('name');
        var assignmentTypeIdsArray = link.data('assignment-types');
        markRoleAssignmentTypeCheckboxes('#editRoleAssignmentTypes', assignmentTypeIdsArray);
        var modal = $(this);
        modal.find('.modal-title').text(name);
        modal.find('#editRoleId').val(id);
        modal.find('#editRoleName').val(name);
    })

    $('#addRoleModal').on('show.bs.modal', function (event) {
        var link = $(event.relatedTarget);
        var name = link.data('name');
        var assignmentTypeIdsArray = link.data('assignment-types');
        var modal = $(this);
        modal.find('.modal-title').text(name);
    })

    $('#viewAssignmentTypeModal').on('show.bs.modal', function (event) {
        var link = $(event.relatedTarget);
        var id = link.data('id');
        var name = link.data('name');
        var hospitalizationRequired = link.data('hospitalization-required');
        var modal = $(this);
        modal.find('.modal-title').text(name);
        modal.find('#viewAssignmentTypeId').val(id);
        modal.find('#viewAssignmentTypeName').val(name);
        if (hospitalizationRequired == true) {
            modal.find('#viewAssignmentTypeHospitalization').prop('checked', true);
        } else {
            modal.find('#viewAssignmentTypeHospitalization').prop('checked', false);
        }
    })

    $('#editAssignmentTypeModal').on('show.bs.modal', function (event) {
        var link = $(event.relatedTarget);
        var id = link.data('id');
        var name = link.data('name');
        var hospitalizationRequired = link.data('hospitalization-required');
        var modal = $(this);
        modal.find('.modal-title').text(name);
        modal.find('#editAssignmentTypeId').val(id);
        modal.find('#editAssignmentTypeName').val(name);
        if (hospitalizationRequired == true) {
            modal.find('#editAssignmentTypeHospitalization').prop('checked', true);
        } else {
            modal.find('#editAssignmentTypeHospitalization').prop('checked', false);
        }
    })

    function markRoleAssignmentTypeCheckboxes(modalId, assignmentTypeIdsArray) {
        var checkboxes = $(modalId).find('input');
        for (var i = 0; i < checkboxes.length; i++) {
            var checkbox = $(checkboxes[i]);
            checkbox.prop('checked', false);
            if (assignmentTypeIdsArray.includes(parseInt(checkbox.attr('value')))) {
                checkbox.prop('checked', true);
            }
        }
    }

    function updateRole() {
        $.ajax('/serv?action=update_role', {
            type: "POST",
            data: $('#editRoleForm').serialize(),
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            success: function(data){
                data = JSON.parse(data);
                onSuccess(data, window.location.href, function(errors) {
                    if (errors.validation_role_name != undefined)
                        $('#editRoleName').addClass('is-invalid');
                    recolorTooltips();
                });
            }
        })
    }

    function addRole() {
        $.ajax('/serv?action=add_role', {
            type: "POST",
            data: $('#addRoleForm').serialize(),
            success: function(data){
                data = JSON.parse(data);
                onSuccess(data, window.location.href, function(errors) {
                    if (errors.validation_role_name != undefined)
                        $('#addRoleName').addClass('is-invalid');
                    recolorTooltips();
                });
            }
        })
    }

    function deleteRole(roleId) {
        event.preventDefault();
        $.ajax('/serv?action=delete_role&id=' + roleId, {
            type: "POST",
            success: function(data){
                data = JSON.parse(data);
                onSuccess(data, window.location.href, function(errors) {
                })
            }
        })
    }

    function updateAssignmentType() {
        $.ajax('/serv?action=update_assignment_type', {
            type: "POST",
            data: $('#editAssignmentTypeForm').serialize(),
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            success: function(data){
                data = JSON.parse(data);
                onSuccess(data, window.location.href, function(errors) {
                    if (errors.validation_assignment_type_name != undefined)
                        $('#editAssignmentTypeName').addClass('is-invalid');
                    recolorTooltips();
                });
            }
        })
    }

    function addAssignmentType() {
        $.ajax('/serv?action=add_assignment_type', {
            type: "POST",
            data: $('#addAssignmentTypeForm').serialize(),
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            success: function(data){
                data = JSON.parse(data);
                onSuccess(data, window.location.href, function(errors) {
                    if (errors.validation_assignment_type_name != undefined)
                        $('#addAssignmentTypeName').addClass('is-invalid');
                    recolorTooltips();
                });
            }
        })
    }

    function deleteAssignmentType(assignmentTypeId) {
        event.preventDefault();
        $.ajax('/serv?action=delete_assignment_type&id=' + assignmentTypeId, {
            type: "POST",
            success: function(data){
                data = JSON.parse(data);
                onSuccess(data, window.location.href, function(errors) {
                })
            }
        })
    }

    function objectifyForm(formArray) {

        var returnArray = {};
        for (var i = 0; i < formArray.length; i++){
            returnArray[formArray[i]['name']] = formArray[i]['value'];
            if (formArray[i]['name'] == 'id')
                returnArray[formArray[i]['name']] = parseInt(formArray[i]['value']);
        }
        return returnArray;
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

    function recolorTooltips() {
        $('.is-invalid[data-toggle="tooltip"]').tooltip('dispose');
        $('.is-invalid[data-toggle="tooltip"]').tooltip({
            trigger: 'hover focus',
            template:
            '<div class="tooltip danger" role="tooltip">' +
            '<div class="arrow danger"></div>' +
            '<div class="tooltip-inner danger"></div>' +
            '</div>'
        });
    }
</script>
</html>
