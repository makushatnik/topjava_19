<%@ taglib uri="http://sargue.net/jsptags/time" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Meal Edit Form</title>
    <style type="text/css">
        .center { text-align: center; }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2 class="center">${meal.id != null && meal.id != '' ? 'Edit' : 'Insert'} Meal</h2>
<form method="POST" action='meals' name="frmAddMeal">
    <input type="hidden" name="mealId" value="${meal.id}" /> <br />
    Date (format - yyyy-MM-dd HH:mm): <input type="datetime" name="dateTime"
        value="<fmt:format value="${meal.dateTime}" pattern="yyyy-MM-dd HH:mm" />" /> <br />
    Description : <input type="text" name="description" value="${meal.description}" /> <br />
    Calories : <input type="text" name="calories" value="${meal.calories}" /> <br />
    <input type="submit" value="Submit" />
</form>
</body>
</html>
