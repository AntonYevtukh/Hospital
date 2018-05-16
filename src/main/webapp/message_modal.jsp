<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${current_user ne null ? current_user.language : 'ru'}"/>
<fmt:setBundle basename="general" var="general" />
<div class="modal " id="messageModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 id="messageModalSuccessHeader" class="modal-title">
                    <fmt:message key="message.success" bundle="${general}"/>
                </h5>
                <h5 id="messageModalErrorHeader" class="modal-title">
                    <fmt:message key="message.error" bundle="${general}"/>
                </h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div id="messageModalText" class="alert text-center"></div>
            </div>
            <div class="modal-footer">
                <button id="messageModalButton" type="button" class="btn btn-sm" data-dismiss="modal">
                    <fmt:message key="button.close" bundle="${general}"/>
                </button>
            </div>
        </div>
    </div>
</div>
