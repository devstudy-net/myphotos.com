<%-- 
    Author       : devstudy,
    Organization : </>DevStudy.net
--%>
<%@tag pageEncoding="UTF-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt"    uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags"   tagdir="/WEB-INF/tags"%>

<%@attribute name="header"                      type="java.lang.String"  required="true" %>
<%@attribute name="saveAction"                  type="java.lang.String"  required="true" %>
<%@attribute name="saveCaption"                 type="java.lang.String"  required="true" %>
<%@attribute name="gotoProfileAvailable"        type="java.lang.Boolean" required="true" %>
<%@attribute name="isCancelBtnAvailable"        type="java.lang.Boolean" required="true" %>
<%@attribute name="isAgreeCheckBoxAvailable"    type="java.lang.Boolean" required="true" %>
<%@attribute name="isUploadAvatarAvailable"     type="java.lang.Boolean" required="true" %>

<header id="header">
    <div class="inner">
        <a id="${isUploadAvatarAvailable ? 'avatar-uploader' : 'sign-up-avatar'} " class="image avatar">
            <img src="${profile.avatarUrl}" alt="${profile.fullName}" />
        </a>
        <h1><strong id="firstName">${profile.firstName}</strong> <strong id="lastName">${profile.lastName}</strong></h1>
        <h4><span id="jobTitle">${profile.jobTitle}</span> <span id="in">in</span> <span id="location">${profile.location}</span></h4>
        <c:if test="${gotoProfileAvailable}">
        <br>
        <ul class="actions fit small">
            <li><a href="/${profile.uid}" class="button special small">Go to my profile</a></li>
        </ul>
        </c:if>
    </div>
</header>
<div id="main">
    <section>
        <h4>${pageScope.header}</h4>
        <form method="post" action="${saveAction}">
            <input type="hidden" name="avatarUrl" value="${profile.avatarUrl}" />
            <div class="row uniform 50%">
                <div class="${'6u 12u$(xsmall)'}">
                    <input type="text" name="firstName" value="${profile.firstName}" placeholder="Your first name" />
                </div>
                <div class="${'6u$ 12u$(xsmall)'}">
                    <input type="text" name="lastName"  value="${profile.lastName}" placeholder="Your last name"  />
                </div>
                <div class="${'6u 12u$(xsmall)'}">
                    <input type="text" name="jobTitle"  value="${profile.jobTitle}" placeholder="Your job title" />
                </div>
                <div class="${'6u$ 12u$(xsmall)'}">
                    <input type="text" name="location"  value="${profile.location}" placeholder="Where are you from" />
                </div>
                <c:if test="${isAgreeCheckBoxAvailable}">
                    <div class="${'12u$'}">
                        <c:set var="label">I agree to the <a href="/terms">terms of service</a></c:set>
                        <input type="checkbox" name="agree" id="agree" ${value ? 'checked' : ''}>
                        <label for="agree">${label}</label>
                    </div>
                </c:if>
                <div class="${'12u$'}">
                    <ul class="actions">
                        <li><input type="submit" value="${saveCaption}" class="special small" /></li>
                        <c:if test="${isCancelBtnAvailable}">
                        <li><a href="/${profile.uid}" class="button small">Cancel</a></li>
                        </c:if>
                    </ul>
                </div>
            </div>
        </form>
    </section>
</div>