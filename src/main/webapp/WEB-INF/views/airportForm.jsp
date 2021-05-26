<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Home</title>
</head>
<body>
<form action="/airport" method="post">
	출발 : <input type="text" name="depAirportNm"><br>
	도착 : <input type="text" name="arrAirportNm"><br>
	가는날 : <input type="text" name="depPlandTime"><br>
	오는날 : <input type="text" name="arrPlandTime"><br>
	<input type="submit" value="전송">
</form>
</body>
</html>