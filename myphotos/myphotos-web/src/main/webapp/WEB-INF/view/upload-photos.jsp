<%-- 
    Author       : devstudy,
    Organization : </>DevStudy.net
--%>
<%@page import="net.devstudy.myphotos.web.Constants"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt"    uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro"  uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="tags"   tagdir="/WEB-INF/tags"%>

<c:set var="photoLimit" value="<%=Constants.PHOTO_LIMIT%>"/>

<header id="header">
    <div class="inner">
        <a href="javascript:void(0);" class="image avatar">
            <img src="${profile.avatarUrl}" alt="${profile.fullName}" />
        </a>
        <h1><strong>${profile.fullName}</strong></h1>
        <br>
        <ul class="actions fit small">
            <li><a href="/${profile.uid}" class="button special small">Go to my profile</a></li>
        </ul>
    </div>
</header>
<div id="main">
    <section>
        <header class="major">
            <h3>Upload new photo</h3>
        </header>
        <div id="photo-container" class="row" data-photo-limit="${photoLimit}"
             data-more-url="/photos/profile/more?profileId=${profile.id}&">
            <c:set var="classes" value="${'6u 12u$(xsmall) photo-item' }"/>
            <article id="upload-photo" class="${classes }">
                <div class="upload-container image fit thumb">
                    <img src="/static/images/upload-bg.jpg" alt="Upload background" />
                    <div class="center">
                        <a class="button special small">Upload a file</a>
                    </div>
                </div>
            </article>
            <c:set var="firstExist" value="${true}" scope="request" />
            <jsp:include page="../fragment/more-photos.jsp" />
        </div>
    </section>
</div>

<script type="text/template" id="upload-photo-result-template">
    <article id="current-photo" class="6u 12u$(xsmall) photo-item not-init">
        <figure>
            <a href="#" class="image fit thumb large-photo">
                <img src="data:image/gif;base64,R0lGODlhAQABAAD/ACwAAAAAAQABAAACADs=" class="small-photo" alt="Photo" />
            </a>
            <figcaption>
                <p class="author">
                    <span class="uploaded-date"><i class="fa fa-calendar" aria-hidden="true"></i> Uploaded: <span class="created"></span></span>
                </p>
                <p class="stat">
                    <span class="delim">|</span>
                    <span><i class="fa fa-eye" aria-hidden="true"></i> <span class="views"></span> <span class="txt">Views</span> |</span>  
                    <span><i class="fa fa-download" aria-hidden="true"></i> <span class="downloads"></span> <span class="txt">Downloads</span></span>
                </p>
                <p class="actions">
                    <a class="button special small fit original-photo" href="#">Download</a>
                </p>
            </figcaption>
        </figure>
    </article>
</script>
