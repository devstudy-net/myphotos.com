<%-- 
    Author       : devstudy,
    Organization : </>DevStudy.net
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<header id="header">
    <div class="inner">
        <div class="inner">
            <h1><strong>Unfortunately, <br />we can't complete your request</strong></h1>
        </div>
    </div>
</header>
<div id="main">
    <section>
        <header class="major">
            <h1>Status: ${errorModel.status}</h1>
        </header>
        <p style="color:#DC143C;">${errorModel.message}</p>
    </section>
</div>
