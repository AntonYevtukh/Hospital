<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${current_user ne null}">
    <c:redirect url="/serv?action=view_user&id=${current_user.id}"/>
</c:if>
<c:if test="${current_user eq null}">
    <c:redirect url="/sign_in.jsp"/>
</c:if>



