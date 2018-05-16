<div class="row m-0">
    <div class="col-1"></div>
    <div class="col-10">
        <ul class="nav nav-tabs mt-3 mb-3 d-flex justify-content-center" id="myTab" role="tablist">
            <li class="nav-item col-6 pr-1">
                <a class="text-center nav-link active" id="profile-tab" data-toggle="tab"
                   href="#roles" role="tab" aria-controls="home" aria-selected="true">
                    <fmt:message key="title.view_settings.roles" bundle="${general}"/>
                </a>
            </li>
            <li class="nav-item col-6 pl-1">
                <a class="text-center nav-link" id="person-tab" data-toggle="tab"
                   href="#assignmentTypes" role="tab" aria-controls="profile" aria-selected="false">
                    <fmt:message key="title.view_settings.assignment_types" bundle="${general}"/>
                </a>
            </li>
        </ul>
        <div class="tab-pane fade show active" id="roles" role="tabpanel" aria-labelledby="profile-tab">
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
        <div class="tab-pane fade show active" id="assignmentTypes" role="tabpanel" aria-labelledby="profile-tab">
            <div class="row m-0">
                <table class="table table-sm table-bordered table-hover">
                    <thead class="bg-primary text-dark">
                    <tr class="d-flex ">
                        <th class="col-1 border-primary border" scope="col">#</th>
                        <th class="col-6 border-primary border" scope="col"><fmt:message key="assignment_type.name" bundle="${settings_bundle}"/></th>
                        <th class="col-3 border-primary border" scope="col"><fmt:message key="assignment_type.hospitalization" bundle="${settings_bundle}"/></th>
                        <th class="col-2 border-primary border" scope="col"><fmt:message key="form.actions" bundle="${general}"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${assignment_types}" begin="0" var="assignment_type" varStatus="number">
                        <tr class="d-flex">
                            <th class="col-1" scope="row">${number.index}</th>
                            <td class="col-6">${assignment_types.name}</td>
                            <td class="col-3 d-flex justify-content-center">
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
                                <a data-toggle="modal" href="#editviewAssignmentTypeModal"
                                   data-id="${role.id}" data-name="${role.name}"
                                   data-assignment-types="<ctg:roleAssignmentTypeIdsArray role="${role}"/>"/>
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
    <div class="col-1"></div>
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
                            <fmt:message key="assignmentType.id" bundle="${settings_bundle}"/>
                        </label>
                        <div class="col-9">
                            <input type="text" class="form-control-plaintext form-control-sm" id="editAssignmentTypeId" name="id">
                        </div>
                    </div>
                    <div class="form-group row mb-1 ml-4 mr-4">
                        <label for="editAssignmentTypeName" class="col-3 col-form-label">
                            <fmt:message key="role.name" bundle="${settings_bundle}"/>
                        </label>
                        <div class="col-9">
                            <input type="text"  class="form-control form-control-sm" id="editAssignmentTypeName" name="name">
                        </div>
                    </div>
                    <div class="form-group row mb-1 ml-4 mr-4">
                        <label for="editAssignmentTypeHospitalization" class="col-3 col-form-label">
                            <fmt:message key="role.assignment_types" bundle="${settings_bundle}"/>
                        </label>
                        <div class="col-9">
                            <div id="editAssignmentTypeHospitalization">
                                <div class="form-check checkbox checkbox-primary">
                                    <input  class="form-check-input styled" type="checkbox"
                                            name="hospitalization_required" value="${assignment_type.hospitalizationRequired}">
                                    <label class="form-check-label">
                                        <fmt:message key="role.assignment_types" bundle="${settings_bundle}"/>
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














<div class="tab-pane fade" id="assignmentTypes" role="tabpanel" aria-labelledby="profile-tab">

</div>