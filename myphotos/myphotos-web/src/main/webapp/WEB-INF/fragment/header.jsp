<%-- 
    Author       : devstudy,
    Organization : </>DevStudy.net
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro"  uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="tags"   tagdir="/WEB-INF/tags"%>

<tags:if-not-error>
    <jsp:attribute name="body">
        <shiro:authenticated>
            <header class="menu">
                <a class="menu-btn"><i class="fa fa-bars" aria-hidden="true"></i></a>
                <div class="items">
                    <shiro:hasRole name="PROFILE">
                        <c:if test="${currentRequestUrl != '/'}">
                            <a href="/" class="btn">All photos</a>
                        </c:if>
                        <c:if test="${currentRequestUrl != '/upload-photos'}">
                            <a href="/upload-photos" class="btn">Upload photos</a>
                        </c:if>
                        <c:if test="${currentRequestUrl != '/edit'}">
                            <a href="/edit" class="btn">Edit profile</a>
                        </c:if>
                    </shiro:hasRole>
                    <a href="/sign-out" class="btn">Sign Out</a>
                </div>
            </header>
        </shiro:authenticated>
        <shiro:notAuthenticated>
            <c:if test="${currentRequestUrl != '/'}">
                <header class="menu">
                    <a href="/" class="btn">Sign in</a>
                </header>
            </c:if>
        </shiro:notAuthenticated>
    </jsp:attribute>
</tags:if-not-error>