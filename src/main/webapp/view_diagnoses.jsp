<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ctg" uri="customtaglib" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<fmt:setLocale value="${current_user ne null ? current_user.language : 'ru'}"/>
<fmt:setBundle basename="gender" var="gender_bundle" />
<fmt:setBundle basename="validation.validation" var="validation" />
<fmt:setBundle basename="diagnose" var="diagnose_bundle"/>
<fmt:setBundle basename="general" var="general" />
<html>
<head>
    <jsp:include page="meta.jsp"/>
</head>
<body>
<jsp:include page="header.jsp"/>
<h1 class="text-center pt-3 pb-3"><fmt:message key="title.view_diagnoses" bundle="${general}"/></h1>
<div class="row m-0">
    <div class="col-1"></div>
    <div class="col-10">
        <table class="table table-sm table-bordered table-hover">
            <thead class="bg-primary text-dark">
                <tr class="d-flex">
                    <th class="col-1 border border-primary" scope="col">#</th>
                    <th class="col-1 border border-primary" scope="col"><fmt:message key="diagnose.code" bundle="${diagnose_bundle}"/></th>
                    <th class="col-8 border border-primary" scope="col"><fmt:message key="diagnose.name" bundle="${diagnose_bundle}"/></th>
                    <th class="col-2 border border-primary" scope="col"><fmt:message key="form.actions" bundle="${general}"/></th>
                </tr>
            </thead>
            <tbody>
            <c:forEach items="${page_content.content}" var="diagnose" begin="0" varStatus="loop">
                <tr class="d-flex">
                    <th class="col-1" scope="row">${loop.index + 1}</th>
                    <td class="col-1">${diagnose.code}</td>
                    <td class="col-8">${diagnose.name}</td>
                    <td class="col-2 d-flex justify-content-around pt-0 pb-0">
                        <a data-toggle="modal" href="#viewModal"
                           data-code="${diagnose.code}" data-name="${diagnose.name}" data-description="${diagnose.description}">
                            <i class="fa fa-eye fa-2x" aria-hidden="true"></i>
                        </a>
                        <a data-toggle="modal" href="#editModal" data-id="${diagnose.id}"
                           data-code="${diagnose.code}" data-name="${diagnose.name}" data-description="${diagnose.description}">
                            <i class="fa fa-edit fa-2x" aria-hidden="true"></i>
                        </a>

                        <form class="m-0">
                            <button class="d-flex align-top p-0 link" onclick="deleteDiagnose(${diagnose.id})">
                                <i class="fa fa-times fa-2x" aria-hidden="true"></i>
                            </button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <div class="row ml-0 mr-0 d-flex justify-content-between">
            <ctg:bootstrapPagination urlPattern="/serv?action=view_diagnoses&page=" page="${page_content.page}"
                                     totalPages="${page_content.totalPages}"/>
            <a data-toggle="modal" href="#addModal" class="btn btn-sm col-1 btn-primary">
                <fmt:message key="button.add" bundle="${general}"/>
            </a>
        </div>
    </div>
    <div class="col-1"></div>
</div>

<div class="modal " id="viewModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg  " role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form>
                    <input type="hidden" name="id">
                    <div class="form-group row mb-1 ml-4 mr-4">
                        <label for="viewCode" class="col-3 col-form-label">
                            <fmt:message key="diagnose.code" bundle="${diagnose_bundle}"/>
                        </label>
                        <div class="col-9">
                            <input type="text" readonly class="form-control-plaintext form-control-sm" id="viewCode" name="code">
                        </div>
                    </div>
                    <div class="form-group row mb-1 ml-4 mr-4">
                        <label for="viewName" class="col-3 col-form-label">
                            <fmt:message key="diagnose.name" bundle="${diagnose_bundle}"/>
                        </label>
                        <div class="col-9">
                            <input type="text" readonly class="form-control-plaintext form-control-sm" id="viewName" name="name">
                        </div>
                    </div>
                    <div class="form-group row mb-1 ml-4 mr-4">
                        <label for="viewDescription" class="col-3 col-form-label">
                            <fmt:message key="diagnose.description" bundle="${diagnose_bundle}"/>
                        </label>
                        <div class="col-9">
                            <textarea type="text" rows="4" readonly class="form-control-plaintext form-control-sm" id="viewDescription" name="description"></textarea>
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

<div class="modal " id="editModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg  " role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title"><fmt:message key="diagnose.edit" bundle="${diagnose_bundle}"/></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form id="editForm">
                    <input id="editId" type="hidden" name="id">
                    <div class="form-group row mb-1 ml-4 mr-4">
                        <label for="editCode" class="col-3 col-form-label">
                            <fmt:message key="diagnose.code" bundle="${diagnose_bundle}"/>
                        </label>
                        <div class="col-9">
                            <input type="text" class="form-control form-control-sm" id="editCode" name="code" placeholder="AXX.X"
                                   data-toggle="tooltip" data-placement="bottom" data-container="#editModal" onclick="removeInvalidClass(this)"
                                   title="<fmt:message key = 'validation.diagnose.code' bundle='${validation}'/>">
                        </div>
                    </div>
                    <div class="form-group row mb-1 ml-4 mr-4">
                        <label for="editName" class="col-3 col-form-label">
                            <fmt:message key="diagnose.name" bundle="${diagnose_bundle}"/>
                        </label>
                        <div class="col-9">
                            <input type="text" class="form-control form-control-sm" id="editName" name="name"
                                   data-toggle="tooltip" data-placement="bottom" data-container="#editModal" onclick="removeInvalidClass(this)"
                                   title="<fmt:message key = 'validation.diagnose.name' bundle='${validation}'/>">
                        </div>
                    </div>
                    <div class="form-group row mb-1 ml-4 mr-4">
                        <label for="editDescription" class="col-3 col-form-label">
                            <fmt:message key="diagnose.description" bundle="${diagnose_bundle}"/>
                        </label>
                        <div class="col-9">
                            <textarea type="text" rows="4" class="form-control form-control-sm" id="editDescription" name="description"></textarea>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-sm btn-primary" onclick="updateDiagnose()">
                    <fmt:message key="button.update" bundle="${general}"/>
                </button>
                <button type="button" class="btn btn-sm btn-danger" data-dismiss="modal">
                    <fmt:message key="button.close" bundle="${general}"/>
                </button>
            </div>
        </div>
    </div>
</div>

<div class="modal " id="addModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg  " role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title"><fmt:message key="diagnose.add" bundle="${diagnose_bundle}"/></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form id="addForm">
                    <input id="addId" type="hidden" name="id" value="0">
                    <div class="form-group row mb-1 ml-4 mr-4">
                        <label for="addCode" class="col-3 col-form-label">
                            <fmt:message key="diagnose.code" bundle="${diagnose_bundle}"/>
                        </label>
                        <div class="col-9">
                            <input type="text" class="form-control form-control-sm" id="addCode" name="code" placeholder="AXX.X"
                                   data-toggle="tooltip" data-placement="bottom" onclick="removeInvalidClass(this)"
                                   title="<fmt:message key = 'validation.diagnose.code' bundle='${validation}'/>">
                        </div>
                    </div>
                    <div class="form-group row mb-1 ml-4 mr-4">
                        <label for="addName" class="col-3 col-form-label">
                            <fmt:message key="diagnose.name" bundle="${diagnose_bundle}"/>
                        </label>
                        <div class="col-9">
                            <input type="text" class="form-control form-control-sm" id="addName" name="name"
                                   data-toggle="tooltip" data-placement="bottom" data-container="#addModal" onclick="removeInvalidClass(this)"
                                   title="<fmt:message key = 'validation.diagnose.name' bundle='${validation}'/>">
                        </div>
                    </div>
                    <div class="form-group row mb-1 ml-4 mr-4">
                        <label for="addDescription" class="col-3 col-form-label">
                            <fmt:message key="diagnose.description" bundle="${diagnose_bundle}"/>
                        </label>
                        <div class="col-9">
                            <textarea type="text" rows="4" class="form-control form-control-sm" id="addDescription" name="description"></textarea>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-sm btn-primary" onclick="addDiagnose()">
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

    $('#viewModal').on('show.bs.modal', function (event) {
        var link = $(event.relatedTarget);
        var code = link.data('code');
        var name = link.data('name');
        var description = link.data('description');
        var modal = $(this);
        modal.find('.modal-title').text(code);
        modal.find('#viewCode').val(code);
        modal.find('#viewName').val(name);
        modal.find('#viewDescription').text(description);
    })
    
    $('#editModal').on('show.bs.modal', function (event) {
        var link = $(event.relatedTarget);
        var id = link.data('id');
        var code = link.data('code');
        var name = link.data('name');
        var description = link.data('description');
        var modal = $(this);
        modal.find('.modal-title').text(code);
        modal.find('#editId').val(id);
        modal.find('#editCode').val(code);
        modal.find('#editName').val(name);
        modal.find('#editDescription').text(description);
    })

    function updateDiagnose() {
        var formArray = $('#editForm').serializeArray();
        var diagnose = objectifyForm(formArray);
        $.ajax('/serv?action=update_diagnose', {
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(diagnose),
            dataType: "json",
            success: function(data){
                console.log(data);
                onSuccess(data, window.location.href, function(errors) {
                    if (errors.validation_diagnose_code != undefined)
                        $('#editCode').addClass('is-invalid');
                    if (errors.validation_diagnose_name != undefined)
                        $('#editName').addClass('is-invalid');
                    recolorTooltips();
                });
            }
        })
    }

    function addDiagnose() {
        var formArray = $('#addForm').serializeArray();
        var diagnose = objectifyForm(formArray);
        $.ajax('/serv?action=add_diagnose', {
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(diagnose),
            dataType: "json",
            success: function(data){
                onSuccess(data, "/serv?view_diagnoses&page=1", function(errors) {
                    if (errors.validation_diagnose_code != undefined)
                        $('#addCode').addClass('is-invalid');
                    if (errors.validation_diagnose_name != undefined)
                        $('#addName').addClass('is-invalid');
                    recolorTooltips();
                })
            }
        })
    }

    function deleteDiagnose(diagnoseId) {
        event.preventDefault();
        $.ajax('/serv?action=delete_diagnose&id=' + diagnoseId, {
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
</script>
</html>
