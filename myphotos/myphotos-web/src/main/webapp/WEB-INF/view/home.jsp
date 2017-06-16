<%-- 
    Author       : devstudy,
    Organization : </>DevStudy.net
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tags"   tagdir="/WEB-INF/tags"%>

<header id="header">
    <div class="inner">
        <h1><strong>To get personal page please signup with</strong></h1>
        <br><br>
        <ul class="actions fit small">
            <li><a href="/sign-up/facebook" class="button facebook fit small icon fa-facebook">Facebook</a></li>
            <li><a href="#" class="button google-plus fit small icon fa-google-plus">Google+</a></li>
        </ul> 
    </div>
</header>
<div id="main">
    <section>
        <header class="major">
            <h1>Welcome, to MyPhotos.com</h1>
        </header>
        <div class="row">
            <div class="${'8u 12u$(xsmall)'}">
                <p>service, which allow you to share high-resolution photos for <a href="https://creativecommons.org/publicdomain/zero/1.0/" target="_blank">free</a></p>
            </div>
            <div class="${'4u$ 12u$(xsmall)'}">
                <tags:sort-mode-selector/>
            </div>
        </div>
        <div id="photo-container" class="row" data-page="1" data-total-count="${totalCount}" data-more-url="/photos/popular/more?sort=${sortMode}&">
            <jsp:include page="../fragment/more-photos.jsp" />
        </div>
        <c:if test="${totalCount > fn:length(photos)}">
            <div id="load-more-container">
                <hr>
                <div class="text-center">
                    <a id="load-more-button" class="button small">Load more photos</a>
                </div>
            </div>
        </c:if>
    </section>
</div>