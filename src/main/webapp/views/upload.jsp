<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Upload file bằng Multipart</title>
</head>
<body>
    <p><a href="<c:url value='/home'/>">Trang chủ</a></p>
    <h1>Upload file bằng Servlet Multipart</h1>
    <c:if test="${not empty error}"><p style="color: red"><c:out value="${error}"/></p></c:if>
    <form method="post" action="<c:url value='/multiPartServlet'/>" enctype="multipart/form-data">
        <label for="multiPartServlet">Chọn file (tối đa 5 MB):</label><br>
        <input id="multiPartServlet" type="file" name="multiPartServlet" required><br><br>
        <button type="submit">Upload</button>
    </form>
</body>
</html>
