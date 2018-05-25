<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${current_user ne null ? current_user.language : 'ru'}"/>
<fmt:setBundle basename="header"/>
<nav class="navbar navbar-expand-lg navbar-light bg-white border-primary border-bottom ${top_header ? 'fixed-top' : 'sticky-top'}">

    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav">
            <li class="nav-item">
                <a class="nav-link font-weight-bold" >
                    &nbsp;
                </a>
            </li>
        </ul>
        <c:if test="${current_user ne null}">
            <ul class="navbar-nav mr-auto ml-5">
                <li class="nav-item dropdown">
                    <c:set var="disabled" value="${current_user.roleMap.containsKey(1) || current_user.roleMap.containsKey(4) ? '' : 'disabled'}"/>
                    <a class="nav-link dropdown-toggle font-weight-bold ${disabled}"
                       href="#" id="patient_menu" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        <fmt:message key="patients"/>
                    </a>
                    <div class="dropdown-menu" aria-labelledby="patient_menu">
                        <a class="dropdown-item" href="/serv?action=view_nurses&page=1"><fmt:message key="patient.nurses"/></a>
                        <a class="dropdown-item" href="/serv?action=view_doctors&page=1"><fmt:message key="patient.doctors"/></a>
                        <div class="dropdown-divider"></div>
                        <a class="dropdown-item" href="/serv?action=view_patient_hospitalizations&page=1&patient_id=${current_user.id}">
                            <fmt:message key="patient.hospitalizations"/>
                        </a>
                        <a class="dropdown-item" href="/serv?action=view_patient_examinations&page=1&patient_id=${current_user.id}">
                            <fmt:message key="patient.examinations"/>
                        </a>
                        <a class="dropdown-item" href="/serv?action=view_patient_assignments&page=1&patient_id=${current_user.id}">
                            <fmt:message key="patient.assignments"/>
                        </a>
                    </div>
                </li>
                <li class="nav-item dropdown">
                    <c:set var="disabled" value="${current_user.roleMap.containsKey(2) || current_user.roleMap.containsKey(4) ? '' : 'disabled'}"/>
                    <a class="nav-link dropdown-toggle font-weight-bold ${disabled}"
                       href="#" id="nurse_menu" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        <fmt:message key="nurses"/>
                    </a>
                    <div class="dropdown-menu" aria-labelledby="nurse_menu">
                        <a class="dropdown-item" href="/serv?action=view_patients&page=1"><fmt:message key="nurse.patients"/></a>
                        <a class="dropdown-item" href="/serv?action=view_nurses&page=1"><fmt:message key="nurse.nurses"/></a>
                        <a class="dropdown-item" href="/serv?action=view_doctors&page=1"><fmt:message key="nurse.doctors"/></a>
                        <div class="dropdown-divider"></div>
                        <a class="dropdown-item" href="/serv?action=view_hospitalizations&page=1">
                            <fmt:message key="nurse.all_hospitalizations"/>
                        </a>
                        <a class="dropdown-item" href="/serv?action=view_examinations&page=1">
                            <fmt:message key="nurse.all_examinations"/>
                        </a>
                        <a class="dropdown-item" href="/serv?action=view_assignments&page=1">
                            <fmt:message key="nurse.all_assignments"/>
                        </a>
                        <div class="dropdown-divider"></div>
                        <a class="dropdown-item" href="/serv?action=view_executor_assignments&page=1&executor_id=${current_user.id}">
                            <fmt:message key="nurse.assignments_to_do"/>
                        </a>
                    </div>
                </li>
                <li class="nav-item dropdown">
                    <c:set var="disabled" value="${current_user.roleMap.containsKey(3) || current_user.roleMap.containsKey(4) ? '' : 'disabled'}"/>
                    <a class="nav-link dropdown-toggle font-weight-bold ${disabled}"
                       href="#" id="doctor_menu" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        <fmt:message key="doctors"/>
                    </a>
                    <div class="dropdown-menu" aria-labelledby="doctor_menu">
                        <a class="dropdown-item" href="/serv?action=view_patients&page=1"><fmt:message key="doctor.patients"/></a>
                        <a class="dropdown-item" href="/serv?action=view_nurses&page=1"><fmt:message key="doctor.nurses"/></a>
                        <a class="dropdown-item" href="/serv?action=view_doctors&page=1"><fmt:message key="doctor.doctors"/></a>
                        <div class="dropdown-divider"></div>
                        <a class="dropdown-item" href="/serv?action=view_diagnoses&page=1"><fmt:message key="doctor.diagnoses"/></a>
                        <div class="dropdown-divider"></div>
                        <a class="dropdown-item" href="/serv?action=view_hospitalizations&page=1">
                            <fmt:message key="doctor.all_hospitalizations"/>
                        </a>
                        <a class="dropdown-item" href="/serv?action=view_examinations&page=1">
                            <fmt:message key="doctor.all_examinations"/>
                        </a>
                        <a class="dropdown-item" href="/serv?action=view_assignments&page=1">
                            <fmt:message key="doctor.all_assignments"/>
                        </a>
                        <div class="dropdown-divider"></div>
                        <a class="dropdown-item" href="/serv?action=view_doctor_hospitalizations&page=1&accepted_doctor_id=${current_user.id}">
                            <fmt:message key="doctor.hospitalizations"/>
                        </a>
                        <a class="dropdown-item" href="/serv?action=view_doctor_discharges&page=1&discharged_doctor_id=${current_user.id}">
                            <fmt:message key="doctor.discharges"/>
                        </a>
                        <a class="dropdown-item" href="/serv?action=view_examinations&page=1&doctor_id=${current_user.id}">
                            <fmt:message key="doctor.examinations"/>
                        </a>
                        <a class="dropdown-item" href="/serv?action=view_doctor_assignments&page=1&doctor_id=${current_user.id}">
                            <fmt:message key="doctor.assignments_designated"/>
                        </a>
                        <a class="dropdown-item" href="/serv?action=view_executor_assignments&page=1&executor_id=${current_user.id}">
                            <fmt:message key="doctor.assignments_to_do"/>
                        </a>
                    </div>
                </li>
                <li class="nav-item dropdown">
                    <c:set var="disabled" value="${current_user.roleMap.containsKey(4) ? '' : 'disabled'}"/>
                    <a class="nav-link dropdown-toggle font-weight-bold ${disabled}"
                       href="#" id="admin_menu" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        <fmt:message key="admins"/>
                    </a>
                    <div class="dropdown-menu" aria-labelledby="doctor_menu">
                        <a class="dropdown-item" href="/serv?action=view_settings"><fmt:message key="admin.settings"/></a>
                    </div>
                </li>
            </ul>
            <ul class="navbar-nav mr-5">
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle font-weight-bold" href="#" id="user_profile" role="button"
                       data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        <fmt:message key="user"/> : ${current_user.login}
                    </a>
                    <div class="dropdown-menu" aria-labelledby="user_profile">
                        <a class="dropdown-item" href="/serv?action=view_user&id=${current_user.id}">
                            <fmt:message key="profile"/>
                        </a>
                        <a class="dropdown-item" href="/serv?action=sign_out">
                            <fmt:message key="sign_out"/>
                        </a>
                    </div>
                </li>
            </ul>
        </c:if>
    </div>
</nav>
