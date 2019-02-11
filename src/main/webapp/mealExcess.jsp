<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://sargue.net/jsptags/time" prefix="fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Meals</title>
    <style type="text/css">
        .center { text-align: center; }
        .bold { font-weight: bold; }
        .green { color: green; }
        .red { color: red; }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2 class="center">Meals</h2>
<table>
    <tr class="bold center">
        <td width="150px">Date</td>
        <td width="200px">Description</td>
        <td width="50px">Calories</td>
    </tr>
    <c:forEach var="meal" items="${meals}">
        <fmt:format value="${meal.dateTime}" style="MS" var="parsedDate" />
        <tr class="center ${meal.excess ? 'red' : 'green' }">
            <td>${parsedDate}</td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
