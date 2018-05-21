<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ctg" uri="customtaglib" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<fmt:setLocale value="${current_user ne null ? current_user.language : 'ru'}"/>
<fmt:setBundle basename="validation.validation" var="validation" />
<fmt:setBundle basename="user" var="user_bundle"/>
<fmt:setBundle basename="examination" var="examination_bundle"/>
<fmt:setBundle basename="assignment" var="assignment_bundle"/>
<fmt:setBundle basename="general" var="general" />
<fmt:setBundle basename="formats" var="formats" />
<c:set var="format_string"><fmt:message key="date.regular" bundle="${formats}"/></c:set>
<html>
<head>
    <jsp:include page="meta.jsp"/>
</head>
<body>
    <jsp:include page="header.jsp"/>
    <h1 class="text-center pt-3"><fmt:message key="${title}" bundle="${general}"/></h1>
    <div class="row m-0">
        <div class="col-1"></div>
        <div class="col-10">
            <form class="form-group pr-1 pl-1" method="POST" action="/serv">
                <input type="hidden" name="action" value="update_examination">
                <div class="row m-0">
                    <div class="col-5 pl-0">
                        <div class="row input-group-text bg-white border-0 pt-0 pb-0">
                            <label class="mb-1"><fmt:message key="examination.doctor" bundle="${examination_bundle}"/></label>
                            <select name="patient" class="form-control form-control-sm disabled" readonly disabled>
                                <c:set var="doctor" value="${examination.doctor}"/>
                                <option value="${doctor.id}" selected>
                                    <ctg:userShortInfo user="${doctor}" showPassportNumber="true"/>
                                </option>
                            </select>
                        </div>
                    </div>
                    <div class="col-5">
                        <div class="row input-group-text bg-white border-0 pt-0 pb-0">
                            <label class="mb-1"><fmt:message key="examination.patient" bundle="${examination_bundle}"/></label>
                            <select name="patient_id" class="form-control form-control-sm disabled" readonly disabled>
                                <c:set var="patient" value="${examination.patient}"/>
                                <option value="${patient.id}" selected>
                                    <ctg:userShortInfo user="${patient}" showPassportNumber="true"/>
                                </option>
                            </select>
                        </div>
                    </div>
                    <div class="col-2 pr-0">
                        <label class="mb-1"><fmt:message key="examination.date" bundle="${examination_bundle}"/></label>
                        <input type="text" class="date form-control form-control-sm" name="date"
                               value="<fmt:formatDate value="${examination.date}" pattern="${format_string}"/>"
                               id="assignmentEndDate" onClick="removeInvalidClass(this)"
                               data-toggle="tooltip" data-placement="bottom" readonly disabled
                               title="<fmt:message key = 'validation.assignment.end_date' bundle='${validation}'/>">
                    </div>
                </div>

                <div class="row input-group-text bg-white border-0 pt-0 pb-0">
                    <label class="mb-1"><fmt:message key="examination.diagnoses" bundle="${examination_bundle}"/></label>
                    <select id="diagnoses" name="diagnose" class="form-control form-control-sm" multiple readonly disabled>
                        <c:forEach items="${examination.diagnoses}" var="diagnose">
                            <option class="m-0 p-0" value="${diagnose.id}" selected>
                                    ${diagnose.code.concat(' ').concat(diagnose.name)}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="row input-group-text bg-white border-0 pt-0 pb-0">
                    <label class="mb-1"><fmt:message key="examination.comment" bundle="${examination_bundle}"/></label>
                    <textarea type="text" class="form-control form-control-sm"
                              name="comment" value="" rows="4" readonly disabled>${comment}</textarea>
                </div>
                <div class="row ml-0 mr-0 mt-2 d-flex justify-content-end">
                    <a href="/serv?action=edit_examination&id=${examination.id}" class="btn btn-sm col-1 btn-primary">
                        <fmt:message key="button.edit" bundle="${general}"/>
                    </a>
                </div>
            </form>

            <h2 class="text-center pt-0"><fmt:message key="assignments.assignments" bundle="${assignment_bundle}"/></h2>

            <c:if test="${fn:length(examination.assignments) gt 0}">
                <table class="table table-sm table-bordered table-hover mb-2">
                    <thead class="bg-primary text-dark">
                    <tr class="d-flex">
                        <th class="col-2 border border-primary" scope="col"><fmt:message key="assignment.doctor" bundle="${assignment_bundle}"/></th>
                        <th class="col-2 border border-primary" scope="col"><fmt:message key="assignment.patient" bundle="${assignment_bundle}"/></th>
                        <th class="col-2 border border-primary" scope="col"><fmt:message key="assignment.executor" bundle="${assignment_bundle}"/></th>
                        <th class="col-1 border border-primary" scope="col"><fmt:message key="assignment.start_date" bundle="${assignment_bundle}"/></th>
                        <th class="col-1 border border-primary" scope="col"><fmt:message key="assignment.end_date" bundle="${assignment_bundle}"/></th>
                        <th class="col-2 border border-primary" scope="col"><fmt:message key="assignment.description" bundle="${assignment_bundle}"/></th>
                        <th class="col-2 border border-primary" scope="col"><fmt:message key="form.actions" bundle="${general}"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${examination.assignments}" var="assignment">
                        <tr class="d-flex">
                            <td class="col-2"><ctg:userShortInfo user="${assignment.doctor}" showPassportNumber="${can_view_all}"/></td>
                            <td class="col-2"><ctg:userShortInfo user="${assignment.patient}" showPassportNumber="${can_view_all}"/></td>
                            <td class="col-2"><ctg:userShortInfo user="${assignment.executor}" showPassportNumber="${can_view_all}"/></td>
                            <td class="col-1"><fmt:formatDate value="${assignment.startDate}" pattern="${format_string}"/></td>
                            <td class="col-1"><fmt:formatDate value="${assignment.endDate}" pattern="${format_string}"/></td>
                            <td class="col-2">${assignment.description}</td>
                            <td class="col-2 d-flex flex-column justify-content-center pt-0 pb-0">
                                <div class="d-flex justify-content-around">
                                    <a data-toggle="modal" href="#assignmentModal"
                                       data-id="${assignment.id}" data-description="${assignment.description}" data-mode="view" data-examination-id="${assignment.examination.id}"
                                       data-assignment-type-id="${assignment.assignmentType.id}" data-assignment-type="${assignment.assignmentType.name}"
                                       data-executor-id="${assignment.executor.id}" data-executor-info="<ctg:userShortInfo user="${assignment.executor}" showPassportNumber="${can_view_all}"/>"
                                       data-doctor-id="${assignment.doctor.id}" data-doctor-info="<ctg:userShortInfo user="${assignment.doctor}" showPassportNumber="${can_view_all}"/>"
                                       data-patient-id="${assignment.patient.id}" data-patient-info="<ctg:userShortInfo user="${assignment.patient}" showPassportNumber="${can_view_all}"/>"
                                       data-start-date="<fmt:formatDate value="${assignment.startDate}" pattern="${format_string}"/>"
                                       data-end-date="<fmt:formatDate value="${assignment.endDate}" pattern="${format_string}"/>"
                                    >
                                        <i class="fa fa-eye fa-2x" aria-hidden="true"></i>
                                    </a>
                                    <a class="disabled" readonly disabled>
                                        <i class="fa fa-edit fa-2x" aria-hidden="true"></i>
                                    </a>
                                    <form class="m-0">
                                        <button class="d-flex align-top p-0 link disabled" readonly disabled>
                                            <i class="fa fa-times fa-2x" aria-hidden="true"></i>
                                        </button>
                                    </form>
                                </div>

                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:if>
        </div>
        <div class="col-1"></div>
    </div>
    <jsp:include page="footer.jsp"/>
    <jsp:include page="assignment_modal.jsp"/>
</body>
<script>

    /*$.fn.select2.amd.require([
        'select2/selection/multiple',
        'select2/selection/search',
        'select2/dropdown',
        'select2/dropdown/attachBody',
        'select2/dropdown/closeOnSelect',
        'select2/compat/containerCss',
        'select2/utils'
    ], function (MultipleSelection, Search, Dropdown, AttachBody, CloseOnSelect, ContainerCss, Utils) {
        var SelectionAdapter = Utils.Decorate(
            MultipleSelection,
            Search
        );

        var DropdownAdapter = Utils.Decorate(
            Utils.Decorate(
                Dropdown,
                CloseOnSelect
            ),
            AttachBody
        );

        $('#patients').select2({
            dropdownAdapter: DropdownAdapter,
            selectionAdapter: SelectionAdapter
        });
    })*/

    /*$('#assignmentModal').on('shown.bs.modal', function (event) {
        console.log($('#assignmentModal'))
        var link = $(event.relatedTarget);
        var mode = link.data('mode');
        var doctorId = link.data('doctor-id');
        var patientId = link.data('patient-id');
        var assignmentTypeId = link.data('assignment-type-id');
        var executorId = link.data('executor-id');
        var executorRoleId = detectExecutorRoleId(maps.roleIdsToExecutorMap, executorId);
        var startDate = link.data('start-date');
        var endDate = link.data('end-date');
        var description = link.data('description');
        var modal = $(this);

        modal.find('#assignmentDoctor').val(doctorId);
        modal.find('#assignmentPatient').val(patientId);
        modal.find('#assignmentType').val(assignmentTypeId);
        modal.find('#executorRole').val(executorRoleId);
        modal.find('#executor').val(executorId);
        modal.find('#assignmentStartDate').val(startDate);
        modal.find('#assignmentEndDate').val(endDate);
        modal.find('#assignmentDescription').text(description);
    })*/

    $(document).ready(function() {
        $('#diagnoses').select2();
        /*$('#patients').select2({
            maximumSelectionLength: 1,
            multiple: true
        });*/
    });

</script>
</html>


