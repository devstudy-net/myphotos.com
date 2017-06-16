<%-- 
    Author       : devstudy,
    Organization : </>DevStudy.net
--%>

<%@tag pageEncoding="UTF-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core"%>

<%@ attribute name="body" fragment="true" %>

<c:if test="${errorModel == null}">
    <jsp:invoke fragment="body"/>
</c:if>