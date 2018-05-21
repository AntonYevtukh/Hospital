<%@ taglib prefix="ctg" uri="customtaglib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<table class="table table-sm table-bordered table-hover">
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
            <td class="col-2 d-flex justify-content-around pt-0 pb-0">
                <a data-toggle="modal" href="#assignmentModal"
                   data-id="${assignment.id}" data-description="${diagnose.description}" data-mode="view"
                   data-doctor-id="${assignment.doctor.id}" data-doctor="<ctg:userShortInfo user="${assignment.doctor}" showPassportNumber="${can_view_all}"/>"
                   data-patient-id="${assignment.patient.id}" data-patient="<ctg:userShortInfo user="${assignment.patient}" showPassportNumber="${can_view_all}"/>"
                   data-executor-id="${assignment.executor.id}" data-start-date="<fmt:formatDate value="${assignment.startDate}" pattern="${format_string}"/>"
                   data-end-date="<fmt:formatDate value="${assignment.endDate}" pattern="${format_string}"/>"
                >
                    <i class="fa fa-eye fa-2x" aria-hidden="true"></i>
                </a>
                <a data-toggle="modal" href="#assignmentModal"
                   data-id="${assignment.id}" data-description="${diagnose.description}" data-mode="edit"
                   data-doctor-id="${assignment.doctor.id}" data-doctor="<ctg:userShortInfo user="${assignment.doctor}" showPassportNumber="${can_view_all}"/>"
                   data-patient-id="${assignment.patient.id}" data-patient="<ctg:userShortInfo user="${assignment.patient}" showPassportNumber="${can_view_all}"/>"
                   data-executor-id="${assignment.executor.id}" data-start-date="<fmt:formatDate value="${assignment.startDate}" pattern="${format_string}"/>"
                   data-end-date="<fmt:formatDate value="${assignment.endDate}" pattern="${format_string}"/>"
                >
                    <i class="fa fa-edit fa-2x" aria-hidden="true"></i>
                </a>
                <form class="m-0">
                    <button class="d-flex align-top p-0 link" onclick="deleteAssignment(${assignment.id})">
                        <i class="fa fa-times fa-2x" aria-hidden="true"></i>
                    </button>
                </form>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>