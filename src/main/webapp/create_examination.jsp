<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ctg" uri="customtaglib" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<fmt:setLocale value="${current_user ne null ? current_user.language : 'ru'}"/>
<fmt:setBundle basename="validation.validation" var="validation" />
<fmt:setBundle basename="user" var="user_bundle"/>
<fmt:setBundle basename="examination" var="examination_bundle"/>
<fmt:setBundle basename="general" var="general" />
<fmt:setBundle basename="formats" var="formats" />
<fmt:setBundle basename="messages" var="messages" />
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
            <form id="form" class="form-group" method="POST" action="/serv">
                <div class="row m-0">
                    <div class="col-5 pl-0">
                        <div class="row input-group-text bg-white border-0 pt-0 pb-0">
                            <input type="hidden" name="action" value="add_examination">
                            <c:set var="doctor" value="${current_user}"/>
                            <input type="hidden" name="doctor" value="${doctor.id}">
                            <label class="mb-1"><fmt:message key="examination.doctor" bundle="${examination_bundle}"/></label>
                            <select name="patient" class="form-control form-control-sm disabled" readonly disabled>
                                <option value="${doctor.id}" selected>
                                    <ctg:userShortInfo user="${doctor}" showPassportNumber="true"/>
                                </option>
                            </select>
                        </div>
                    </div>
                    <div class="col-5">
                        <div class="row input-group-text bg-white border-0 pt-0 pb-0">
                            <label class="mb-1"><fmt:message key="examination.patient" bundle="${examination_bundle}"/></label>
                            <c:choose>
                                <c:when test="${patient ne null}">
                                    <input type="hidden" name="patient" value="${patient.id}">
                                    <input id="hospitalized" type="hidden" name="hospitalized" value="${patient.hospitalized ? 'true' : ''}">
                                    <select id="patient" name="patient" class="form-control form-control-sm disabled" readonly disabled>
                                        <option value="${patient.id}" data-hospitalized="${patient.hospitalized}" selected>
                                            <ctg:userShortInfo user="${patient}" showPassportNumber="true"/>
                                        </option>
                                    </select>
                                </c:when>
                                <c:otherwise>
                                    <input id="hospitalized" type="hidden" name="hospitalized" value="">
                                    <c:set var="status" scope="page"
                                           value="${validationFails.contains('validation.examination.patient') ? 'is-invalid' : ''}"/>
                                    <select id="patient" name="patient" class="form-control form-control-sm" onclick="updateHospitalized()">
                                        <c:forEach items="${patients}" var="patient">
                                            <c:set var="selected" value="${patient_id eq patient.id ? 'selected' : ''}"/>
                                            <option value="${patient.id}" ${selected} data-hospitalized="${patient.hospitalized}">
                                                <ctg:userShortInfo user="${patient}" showPassportNumber="true"/>
                                            </option>
                                        </c:forEach>
                                    </select>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                    <div class="col-2 pr-0">
                        <label class="mb-1"><fmt:message key="examination.date" bundle="${examination_bundle}"/></label>
                        <jsp:useBean id="today" class="java.util.Date" />
                        <input type="text" class="date form-control form-control-sm" name="date"
                               value="<fmt:formatDate value="${today}" pattern="${format_string}"/>"
                               id="assignmentEndDate" onClick="removeInvalidClass(this)"
                               data-toggle="tooltip" data-placement="bottom" readonly disabled
                               title="<fmt:message key = 'validation.assignment.end_date' bundle='${validation}'/>">
                    </div>
                </div>


                <div class="row input-group-text bg-white border-0 pt-0 pb-0">
                    <c:set var="status" scope="page"
                           value="${validationFails.contains('validation.examination.diagnoses') ? 'is-invalid' : ''}"/>
                    <label class="mb-1"><fmt:message key="examination.diagnoses" bundle="${examination_bundle}"/></label>
                    <select id="diagnoses" name="diagnose" class="form-control form-control-sm ${status}" multiple>
                        <c:forEach items="${diagnoses}" var="diagnose">
                            <c:set var="selected" value="${examination.diagnoses.contains(diagnose) ? 'selected' : ''}"/>
                            <option class="m-0 p-0" value="${diagnose.id}" ${selected}>
                                    ${diagnose.code.concat(' ').concat(diagnose.name)}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="row input-group-text bg-white border-0 pt-0 pb-0">
                    <label class="mb-1"><fmt:message key="examination.comment" bundle="${examination_bundle}"/></label>
                    <textarea type="text" class="form-control form-control-sm"
                              name="comment" value="" rows="4">${comment}</textarea>
                </div>
                <div class="row ml-0 mr-0 mt-2 d-flex justify-content-between">
                    <c:set var="checked_disabled" value="${hospitalize ? 'checked disabled' : ''}"/>
                    <c:set var="display" value="${hospitalize_show ? '' : 'd-none'}"/>
                    <div id="hospitalizeDiv" class="form-check checkbox checkbox-primary ${display}">
                        <input id="hospitalizeCheck" class="form-check-input styled" type="checkbox" name="hospitalize" ${checked_disabled}>
                        <label class="form-check-label">
                            <fmt:message key="examination.hospitalize" bundle="${examination_bundle}"/>
                        </label>
                    </div>
                    <c:set var="checked_disabled" value="${discharge ? 'checked disabled' : ''}"/>
                    <c:set var="display" value="${discharge_show ? '' : 'd-none'}"/>
                    <div id="dischargeDiv" class="form-check checkbox checkbox-primary ${display}">
                        <input id="dischargeCheck" class="form-check-input styled" type="checkbox" name="discharge" ${checked_disabled}>
                        <label class="form-check-label">
                            <fmt:message key="examination.discharge" bundle="${examination_bundle}"/>
                        </label>
                    </div>
                    <button class="btn btn-sm col-1 btn-primary" onclick="showForm()">
                        <fmt:message key="button.create" bundle="${general}"/>
                    </button>
                </div>
            </form>
        </div>
        <div class="col-1"></div>
    </div>
</body>
<script>

    $('.date').datepicker({
        uiLibrary: 'bootstrap4',
        format: "dd.mm.yyyy",
        weekStart: 1,
        language: "${current_user ne null ? current_user.language : 'ru'}",
        calendarWeeks: true
    });

    function showForm() {
        event.preventDefault();
        var form = $('#form');
        $('#hospitalizeCheck').removeAttr('disabled');
        $('#dischargeCheck').removeAttr('disabled');
        console.log(form.serialize());
        form.submit();
    }



    function updateHospitalized() {
        var hospitalized = $('#patient').find(':selected').data('hospitalized');
        var hospitalizedInput = $('#hospitalized');
        hospitalizedInput.val(hospitalized ? 'true' : '');
        var hospitalizeDiv = $('#hospitalizeDiv');
        var dischargeDiv = $('#dischargeDiv');
        var hospitalizeInput = $(hospitalizeDiv.find('input')[0]);
        var dischargeInput = $(dischargeDiv.find('input')[0]);
        console.log(hospitalizeInput)
        if (hospitalizeInput.attr('disabled') == undefined || hospitalizeInput.attr('disabled') == false) {
            hospitalizeInput.removeAttr('checked');
        }
        if (dischargeInput.attr('disabled') == undefined || dischargeInput.attr('disabled') == false) {
            dischargeInput.removeAttr('checked');
        }
        if (!hospitalized) {
            hospitalizeDiv.removeClass('d-none');
            dischargeDiv.addClass('d-none');
        } else {
            hospitalizeDiv.addClass('d-none');
            dischargeDiv.removeClass('d-none');
        }
    }

    $(document).ready(function() {
        $('#diagnoses').select2();
        updateHospitalized();
    });
</script>
</html>
