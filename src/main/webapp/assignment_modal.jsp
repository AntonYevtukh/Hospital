<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ctg" uri="customtaglib" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<fmt:setLocale value="${current_user ne null ? current_user.language : 'ru'}"/>
<fmt:setBundle basename="assignment" var="assignment_bundle"/>
<fmt:setBundle basename="formats" var="formats" />
<fmt:setBundle basename="validation.validation" var="validation" />
<fmt:setBundle basename="general" var="general"/>
<c:set var="format_string"><fmt:message key="date.regular" bundle="${formats}"/></c:set>
<html>
<head>
</head>
<body>
<div class="modal " id="assignmentModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg " role="document">
        <div class="modal-content">
            <div class="modal-header">

                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form id="assignmentForm">
                    <input id="assignmentId" type="hidden" name="id">
                    <input id="assignmentExamination" type="hidden" name="examination">
                    <div class="row m-0">
                        <div class="col-6">
                            <c:set var="doctor" value="${assignment eq null ? examination.doctor : assignment.doctor}"/>
                            <input type="hidden" name="doctor" value="${doctor.id}">
                            <label class="mb-1"><fmt:message key="assignment.doctor" bundle="${assignment_bundle}"/></label>
                            <select id="assignmentDoctor" name="doctor" class="form-control form-control-sm disabled" readonly disabled>
                                <option value="${doctor.id}" selected>
                                    ${doctor.lastName.concat(' ').concat(doctor.firstName).concat(' ').
                                            concat(doctor.middleName).concat(' / ').concat(doctor.passportNumber)}
                                </option>
                            </select>
                        </div>
                        <div class="col-6">
                            <label class="mb-1"><fmt:message key="assignment.patient" bundle="${assignment_bundle}"/></label>
                            <c:set var="patient" value="${assignment eq null ? examination.patient : assignment.patient}"/>
                            <input type="hidden" name="patient" value="${patient.id}">
                            <select id="assignmentPatient" name="patient" class="form-control form-control-sm disabled" readonly disabled>
                                <option value="${patient.id}" selected>
                                    ${patient.lastName.concat(' ').concat(patient.firstName).concat(' ').
                                            concat(patient.middleName).concat(' / ').concat(patient.passportNumber)}
                                </option>
                            </select>
                        </div>
                    </div>
                    <div class="row m-0">
                        <div class="col-3">
                            <label class="mb-1"><fmt:message key="assignment.assignment_type" bundle="${assignment_bundle}"/></label>
                            <select class="form-control form-control-sm"
                                    id="assignmentType" name="assignment_type" onclick="fillExecutorRole(this, maps)">

                            </select>
                        </div>
                        <div class="col-3">
                            <label class="mb-1"><fmt:message key="assignment.executor_role" bundle="${assignment_bundle}"/></label>
                            <select class="form-control form-control-sm"
                                    id="executorRole" name="executor_role" onclick="fillExecutor(this, maps)">

                            </select>
                        </div>
                        <div class="col-6">
                            <label class="mb-1"><fmt:message key="assignment.executor" bundle="${assignment_bundle}"/></label>
                            <select class="form-control form-control-sm"
                                    id="executor" name="executor">

                            </select>
                        </div>
                    </div>
                    <div class="row m-0">
                        <div class="col-6">
                            <c:set var="status" scope="page"
                                   value="${validationFails.contains('validation.assignment.start_date') ? 'is-invalid' : ''}"/>
                            <label class="mb-1"><fmt:message key="assignment.start_date" bundle="${assignment_bundle}"/></label>
                            <input type="text" class="date form-control form-control-sm ${status}" name="start_date"
                                   value="<fmt:formatDate value="${assignment.date}" pattern="${format_string}"/>"
                                   id="assignmentStartDate" onClick="removeInvalidClass(this)"
                                   data-toggle="tooltip" data-placement="bottom"
                                   title="<fmt:message key = 'validation.assignment.start_date' bundle='${validation}'/>">
                        </div>
                        <div class="col-6">
                            <c:set var="status" scope="page"
                                   value="${validationFails.contains('validation.assignment.start_date') ? 'is-invalid' : ''}"/>
                            <label class="mb-1"><fmt:message key="assignment.end_date" bundle="${assignment_bundle}"/></label>
                            <input type="text" class="date form-control form-control-sm ${status}" name="end_date"
                                   value="<fmt:formatDate value="${assignment.date}" pattern="${format_string}"/>"
                                   id="assignmentEndDate" onClick="removeInvalidClass(this)"
                                   data-toggle="tooltip" data-placement="bottom"
                                   title="<fmt:message key = 'validation.assignment.end_date' bundle='${validation}'/>">
                        </div>
                    </div>
                    <div class="row m-0">
                        <div class="col-12">
                            <label class="mb-1"><fmt:message key="assignment.description" bundle="${assignment_bundle}"/></label>
                            <textarea type="text" class="form-control form-control-sm" id="assignmentDescription"
                                      name="description" rows="4">${assignment.description}</textarea>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button id="assignmentUpdateButton" type="button" class="btn btn-sm btn-primary" onclick="updateAssignment()">
                    <fmt:message key="button.update" bundle="${general}"/>
                </button>
                <button id="assignmentAddButton" type="button" class="btn btn-sm btn-primary" onclick="addAssignment()">
                    <fmt:message key="button.create" bundle="${general}"/>
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

    $('.date').datepicker({
        uiLibrary: 'bootstrap4',
        format: "dd.mm.yyyy",
        weekStart: 1,
        language: "${current_user ne null ? current_user.language : 'ru'}",
        calendarWeeks: true
    });

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

    $('#assignmentModal').on('shown.bs.modal', function (event) {
        var link = $(event.relatedTarget);
        var mode = link.data('mode');
        var examinationId = link.data('examination-id');
        var id = link.data('id');
        var doctorId = link.data('doctor-id');
        var patientId = link.data('patient-id');
        var doctorInfo = link.data('doctor-info');
        var patientInfo = link.data('patient-info');
        var assignmentTypeInfo = link.data('assignment-type-info');
        var executorInfo = link.data('executor-info');
        var assignmentTypeId = link.data('assignment-type-id');
        var executorId = link.data('executor-id');
        var executorRoleId = detectExecutorRoleId(maps.roleIdsToExecutorMap, executorId);
        var startDate = link.data('start-date');
        var endDate = link.data('end-date');
        var description = link.data('description');
        var modal = $(this);

        var assignmentId = modal.find('#assignmentId');
        var assignmentExamination = modal.find('#assignmentExamination');
        var assignmentDoctor = modal.find('#assignmentDoctor');
        var assignmentPatient = modal.find('#assignmentPatient');
        var assignmentType = modal.find('#assignmentType');
        var executorRole = modal.find('#executorRole');
        var executor = modal.find('#executor');
        var assignmentStartDate = modal.find('#assignmentStartDate');
        var assignmentEndDate = modal.find('#assignmentEndDate');
        var assignmentDescription = modal.find('#assignmentDescription');
        var assignmentUpdateButton = modal.find('#assignmentUpdateButton');
        var assignmentAddButton = modal.find('#assignmentAddButton');

        switch (mode) {
            case 'view':
                assignmentDoctor.addClass('form-control-plaintext disabled');
                assignmentPatient.addClass('form-control-plaintext disabled');
                assignmentType.addClass('form-control-plaintext disabled');
                executorRole.addClass('form-control-plaintext disabled');
                executor.addClass('form-control-plaintext disabled');
                assignmentStartDate.addClass('form-control-plaintext disabled');
                assignmentEndDate.addClass('form-control-plaintext disabled');
                assignmentDescription.addClass('form-control-plaintext disabled');
                assignmentUpdateButton.addClass('d-none');
                assignmentAddButton.addClass('d-none');

                assignmentType.attr('disabled', 'disabled');
                executorRole.attr('disabled', 'disabled');
                executor.attr('disabled', 'disabled');
                assignmentStartDate.attr('disabled', 'disabled');
                assignmentEndDate.attr('disabled', 'disabled');
                assignmentDescription.attr('disabled', 'disabled');
                /*var opt = document.createElement('option');
                opt.name = 'assignment_type';
                opt.value = assignmentTypeId;
                opt.innerHTML = assignmentTypeInfo;
                assignmentType.get(0).appendChild(opt);*/

                opt = document.createElement('option');
                opt.name = 'executor';
                opt.value = executorId;
                opt.innerHTML = executorInfo;
                executor.get(0).appendChild(opt);

                fillExecutorRoleById(executorRoleId, maps);

                break;
            case 'edit':
                assignmentDoctor.removeClass('form-control-plaintext disabled');
                assignmentPatient.removeClass('form-control-plaintext disabled');
                assignmentType.removeClass('form-control-plaintext disabled');
                executorRole.removeClass('form-control-plaintext disabled');
                executor.removeClass('form-control-plaintext disabled');
                assignmentStartDate.removeClass('form-control-plaintext disabled');
                assignmentEndDate.removeClass('form-control-plaintext disabled');
                assignmentDescription.removeClass('form-control-plaintext disabled');
                assignmentUpdateButton.removeClass('d-none');
                assignmentAddButton.addClass('d-none');

                assignmentType.removeAttr('disabled', 'disabled');
                executorRole.removeAttr('disabled', 'disabled');
                executor.removeAttr('disabled', 'disabled');
                assignmentStartDate.removeAttr('disabled', 'disabled');
                assignmentEndDate.removeAttr('disabled', 'disabled');
                assignmentDescription.removeAttr('disabled', 'disabled');

                /*var opt = document.createElement('option');
                opt.name = 'assignment_type';
                opt.value = assignmentTypeId;
                opt.innerHTML = assignmentTypeInfo;
                assignmentType.get(0).appendChild(opt);*/

                opt = document.createElement('option');
                opt.name = 'executor';
                opt.value = executorId;
                opt.innerHTML = executorInfo;
                executor.get(0).appendChild(opt);

                fillExecutorRoleById(executorRoleId, maps);

                break;
            case 'add':
                assignmentDoctor.removeClass('form-control-plaintext disabled');
                assignmentPatient.removeClass('form-control-plaintext disabled');
                assignmentType.removeClass('form-control-plaintext disabled');
                executorRole.removeClass('form-control-plaintext disabled');
                executor.removeClass('form-control-plaintext disabled');
                assignmentStartDate.removeClass('form-control-plaintext disabled');
                assignmentEndDate.removeClass('form-control-plaintext disabled');
                assignmentDescription.removeClass('form-control-plaintext disabled');
                assignmentUpdateButton.addClass('d-none');
                assignmentAddButton.removeClass('d-none');

                assignmentType.removeAttr('disabled', 'disabled');
                executorRole.removeAttr('disabled', 'disabled');
                executor.removeAttr('disabled', 'disabled');
                assignmentStartDate.removeAttr('disabled', 'disabled');
                assignmentEndDate.removeAttr('disabled', 'disabled');
                assignmentDescription.removeAttr('disabled', 'disabled');

                break;
        }

        assignmentId.val(id);
        assignmentDoctor.val();
        assignmentPatient.val();
        assignmentExamination.val(examinationId);
        assignmentType.val(assignmentTypeId);
        executorRole.val(executorRoleId);
        executor.val(executorId);
        assignmentStartDate.val(startDate);
        assignmentEndDate.val(endDate);
        assignmentDescription.text(description);
    })

    function updateAssignment() {
        $.ajax('/serv?action=update_assignment', {
            type: "POST",
            data: $('#assignmentForm').serialize(),
            success: function(data){
                data = JSON.parse(data);
                onSuccess(data, window.location.href, function(errors) {
                    if (errors.validation_assignment_code != undefined)
                        $('#editCode').addClass('is-invalid');
                    if (errors.validation_assignment_name != undefined)
                        $('#editName').addClass('is-invalid');
                    recolorTooltips();
                });
            }
        })
    }

    function addAssignment() {
        $.ajax('/serv?action=add_assignment', {
            type: "POST",
            data: $('#assignmentForm').serialize(),
            success: function(data){
                data = JSON.parse(data);
                onSuccess(data, window.location.href, function(errors) {
                    if (errors.validation_assignment_code != undefined)
                        $('#addCode').addClass('is-invalid');
                    if (errors.validation_assignment_name != undefined)
                        $('#addName').addClass('is-invalid');
                    recolorTooltips();
                })
            }
        })
    }

    function deleteAssignment(assignmentId) {
        event.preventDefault();
        $.ajax('/serv?action=delete_assignment&id=' + assignmentId, {
            type: "POST",
            success: function(data){
                data = JSON.parse(data);
                onSuccess(data, window.location.href, function(errors) {
                })
            }
        })
    }

    var maps;
    $(document).ready(function() {
        loadMaps();
    })

    function detectExecutorRoleId(roleIdsToExecutorMap, executorId) {
        for (var key in roleIdsToExecutorMap) {
            if (roleIdsToExecutorMap.hasOwnProperty(key)) {
                var executors = roleIdsToExecutorMap[key];
                for (var i = 0; i < executors.length; i++) {
                    if (executors[i].id == executorId) {
                        return parseInt(key);
                    }
                }
            }
        }
    }

    function loadMaps() {
        $.ajax('/serv?action=load_drop_downs_data', {
            type: "GET",
            success: function(data){
                maps = JSON.parse(data);
                console.log(maps);
                fillAssignmentTypes(maps);
            }
        })
    }

    function fillAssignmentTypes(maps) {
        var list1 = document.getElementById('assignmentType');
        while (list1.firstChild) {
            list1.removeChild(list1.firstChild);
        }
        var assignmentTypes = maps.assignmentTypes;
        for (var i = 0; i < assignmentTypes.length; i++) {
            var opt = document.createElement('option');
            opt.name = 'assignment_type';
            opt.value = assignmentTypes[i].id;
            opt.innerHTML = assignmentTypes[i].name;
            list1.appendChild(opt);
        }
    }

    function fillExecutorRole(source, maps) {
        var list2 = document.getElementById('executorRole');
        var list3 = document.getElementById('executor');
        while (list2.firstChild) {
            list2.removeChild(list2.firstChild);
        }
        while (list3.firstChild) {
            list3.removeChild(list3.firstChild);
        }
        list2.appendChild(document.createElement('option'));
        list3.appendChild(document.createElement('option'));
        var executorRoles = maps.assignmentTypeIdsToRoleMap[source.value];
        for (var i = 0; i < executorRoles.length; i++) {
            var opt = document.createElement('option');
            opt.name = 'executor_role';
            opt.value = executorRoles[i].id;
            opt.innerHTML = executorRoles[i].name;
            list2.appendChild(opt);
        }
    }

    function fillExecutorRoleById(executorRoleId, maps) {
        var list2 = document.getElementById('executorRole');
        while (list2.firstChild) {
            list2.removeChild(list2.firstChild);
        }
        var executorRoles = maps.roles;
        for (var i = 0; i < executorRoles.length; i++) {
            var opt = document.createElement('option');
            opt.name = 'executor_role';
            opt.value = executorRoles[i].id;
            opt.innerHTML = executorRoles[i].name;
            list2.appendChild(opt);
        }
        list2.value = executorRoleId;
    }

    function fillExecutor(source, maps) {
        var list3 = document.getElementById('executor');
        while (list3.firstChild) {
            list3.removeChild(list3.firstChild);
        }
        list3.appendChild(document.createElement('option'));
        var executors = maps.roleIdsToExecutorMap[source.value];
        for (var i = 0; i < executors.length; i++) {
            var executor = executors[i];
            var opt = document.createElement('option');
            opt.name = 'executor';
            opt.value = executors[i].id;
            opt.innerHTML = executor.lastName + ' ' + executor.firstName + ' ' +
                executor.middleName + ' / ' + executor.passportNumber;
            list3.appendChild(opt);
        }
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
        console.log(data);
        console.log(data.success != undefined)
        if (data.success != undefined) {
            console.log("in onsuccess != undef: data" + data)
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
        console.log(message);
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

    function checkForm() {
        var form = $('#assignmentForm');
        console.log(form.serialize());
    }
</script>
</html>
