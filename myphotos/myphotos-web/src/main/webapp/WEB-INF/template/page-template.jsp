<%-- 
    Author       : devstudy,
    Organization : </>DevStudy.net
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<!--
Strata by HTML5 UP
html5up.net | @ajlkn
Free for personal and commercial use under the CCA 3.0 license (html5up.net/license)
-->
<html>
    <head>
        <title>myphotos.com</title>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <jsp:include page="../fragment/styles.jsp" />
    </head>
    <body id="top">
        <jsp:include page="../fragment/header.jsp" />
        <jsp:include page="${currentPage}" />
        <jsp:include page="../fragment/footer.jsp" />
        <jsp:include page="../fragment/scripts.jsp" />
    </body>
</html>
