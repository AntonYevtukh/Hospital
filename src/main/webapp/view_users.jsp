<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${current_user ne null ? current_user.language : 'ru'}"/>
<html>
<head>
    <jsp:include page="meta.jsp"/>
</head>
<body>
    <jsp:include page="header.jsp"/>
</body>
</html>
