<%-- 
    Author       : devstudy,
    Organization : </>DevStudy.net
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core"%>

<html>
    <head>
        <title>
            Available Web Services
        </title>
    </head>
    <body>
        <h1>Available Web Services</h1>
        <table width='100%' border='1'>
            <c:forEach var="ws" items="${webservices}">
            <tr>
                <td>
                    <table border="0">
                        <tr>
                            <td>Service Name:</td>
                            <td>{${ws.address}?wsdl}${ws.name}</td>
                        </tr>
                        <tr>
                            <td>Port Name:</td>
                            <td>{${ws.address}?wsdl}${ws.port}</td>
                        </tr>
                        <tr>
                            <td>Address:</td>
                            <td>${ws.address}</td>
                        </tr>
                        <tr>
                            <td>WSDL:</td>
                            <td><a href="${ws.address}?wsdl" target="_blank">${ws.address}?wsdl</a></td>
                        </tr>
                    </table>
                </td>
            </tr>
            </c:forEach>
        </table>
    </body>
</html>
