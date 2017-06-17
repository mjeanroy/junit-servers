<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="e" uri="https://www.owasp.org/index.php/OWASP_Java_Encoder_Project" %>
<html>
	<head>
		<meta charset="UTF-8" />
		<title>Embedded Jetty taglibs.</title>
	</head>
	<body>
		<div>Unsafe: <c:out value="${message}" /></div>
		<div>Safe: <e:forHtml value="${message}" /></div>
		
		<spring:url value="." var="url"/>
		The current URL is  <a href="${e:forHtmlAttribute(url)}">HERE</a>
	</body>
</html>
