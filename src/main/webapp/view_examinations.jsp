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
    <h1 class="text-center pt-3">
        <fmt:message key="${title}" bundle="${assignment_bundle}"/>
        <c:if test="${user ne null}">
            <br>
            <ctg:userShortInfo user="${user}" showPassportNumber="${false}"/>
        </c:if>
    </h1>
    <div class="row m-0">
        <div class="col-1"></div>
        <div class="col-10">
            <table class="table table-sm table-bordered table-hover mb-2">
                <thead class="bg-primary text-dark">
                <tr class="d-flex">
                    <th class="col-1 border border-primary" scope="col">#</th>
                    <th class="col-2 border border-primary" scope="col"><fmt:message key="examination.doctor" bundle="${examination_bundle}"/></th>
                    <th class="col-2 border border-primary" scope="col"><fmt:message key="examination.patient" bundle="${examination_bundle}"/></th>
                    <th class="col-1 border border-primary" scope="col"><fmt:message key="examination.date" bundle="${examination_bundle}"/></th>
                    <th class="col-4 border border-primary" scope="col"><fmt:message key="examination.diagnoses" bundle="${examination_bundle}"/></th>
                    <th class="col-2 border border-primary" scope="col"><fmt:message key="form.actions" bundle="${general}"/></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${page_content.content}" var="examination" varStatus="loop">
                    <tr class="d-flex">
                        <td class="col-1">${loop.index}</td>
                        <td class="col-2"><ctg:userShortInfo user="${examination.doctor}" showPassportNumber="${can_view_all}"/></td>
                        <td class="col-2"><ctg:userShortInfo user="${examination.patient}" showPassportNumber="${can_view_all}"/></td>
                        <td class="col-1"><fmt:formatDate value="${examination.date}" pattern="${format_string}"/></td>
                        <td class="col-4"><ctg:examinationDiagnoses examination="${examination}"/></td>
                        <td class="col-2 d-flex flex-column justify-content-center pt-0 pb-0">
                            <div class="d-flex justify-content-around">
                                <a href="/serv?action=view_examination&id=${examination.id}">
                                    <i class="fa fa-eye fa-2x" aria-hidden="true"></i>
                                </a>
                                <a href="/serv?action=edit_examination&id=${examination.id}">
                                    <i class="fa fa-edit fa-2x" aria-hidden="true"></i>
                                </a>
                                <form class="m-0">
                                    <button class="d-flex align-top p-0 link" onclick="deleteExamination(${examination.id})">
                                        <i class="fa fa-times fa-2x" aria-hidden="true"></i>
                                    </button>
                                </form>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

            <div class="row pr-3 pl-3">
                <ctg:bootstrapPagination urlPattern="${url_pattern}" page="${page_content.page}" totalPages="${page_content.totalPages}"/>
            </div>
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


