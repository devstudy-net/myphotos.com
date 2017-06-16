<%-- 
    Author       : devstudy,
    Organization : </>DevStudy.net
--%>
<%@tag pageEncoding="UTF-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core"%>

<%@attribute name="name"        type="java.lang.String" required="true" %>
<%@attribute name="value"       type="java.lang.Boolean" required="true" %>
<%@attribute name="label"       type="java.lang.String" required="true" %>

<c:forEach var="message" items="${violations[name]}">
<span class="error message">${message}</span>    
</c:forEach>
<input type="checkbox" name="${name}" id="${name}" ${value ? 'checked' : ''}>
<label for="${name}">${label}</label>