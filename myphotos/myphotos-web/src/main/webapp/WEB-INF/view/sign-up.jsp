<%-- 
    Author       : devstudy,
    Organization : </>DevStudy.net
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags"   tagdir="/WEB-INF/tags"%>

<tags:edit-form gotoProfileAvailable="false" 
                header="To complete registration please fill the following fields" 
                isAgreeCheckBoxAvailable="true" 
                isCancelBtnAvailable="false" 
                isUploadAvatarAvailable="false"
                saveAction="/sign-up/complete" 
                saveCaption="Complete registration" />
