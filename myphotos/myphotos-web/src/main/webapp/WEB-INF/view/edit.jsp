<%-- 
    Author       : devstudy,
    Organization : </>DevStudy.net
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags"   tagdir="/WEB-INF/tags"%>

<tags:edit-form gotoProfileAvailable="false" 
                header="Edit my profile" 
                isUploadAvatarAvailable="true"
                isAgreeCheckBoxAvailable="false" 
                isCancelBtnAvailable="true" 
                saveAction="/save" 
                saveCaption="Save changes" />
