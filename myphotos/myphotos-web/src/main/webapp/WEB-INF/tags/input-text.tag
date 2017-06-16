<%-- 
    Author       : devstudy,
    Organization : </>DevStudy.net
--%>
<%@tag pageEncoding="UTF-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core"%>

<%@attribute name="name"        type="java.lang.String" required="true" %>
<%@attribute name="placeholder" type="java.lang.String" required="true" %>
<%@attribute name="value"       type="java.lang.Object" required="false" %>
<%@attribute name="bindId"      type="java.lang.String" required="false" %>

<c:forEach var="message" items="${violations[name]}">
<span class="error message">${message}</span>    
</c:forEach>
<c:if test="${bindId != null}">
    <c:set var="meta">data-bind-id="${bindId}"</c:set>
</c:if>
<input type="text" name="${name}" value="${value != null ? value : ''}" placeholder="${placeholder}" ${meta} />