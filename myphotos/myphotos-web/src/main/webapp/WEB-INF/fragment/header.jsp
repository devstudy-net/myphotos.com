<%-- 
    Author       : devstudy,
    Organization : </>DevStudy.net
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core"%>

<c:if test="${currentRequestUrl != '/'}">
    <header class="menu">
        <a class="menu-btn"><i class="fa fa-bars" aria-hidden="true"></i></a>
        <div class="items">
            <a href="/" class="btn">Sign in</a>
        </div>
    </header>
</c:if>