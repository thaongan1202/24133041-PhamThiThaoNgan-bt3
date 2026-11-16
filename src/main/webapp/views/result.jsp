<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Kết quả upload</title>
</head>
<body>
    <h1>Upload thành công</h1>
    <p>Tên file: <c:out value="${originalName}"/></p>
    <p>Tên lưu trên máy chủ: <c:out value="${savedName}"/></p>
    <p>Kích thước: <c:out value="${fileSize}"/> byte</p>
    <p><a href="<c:url value='/multiPartServlet'/>">Upload file khác</a></p>
    <p><a href="<c:url value='/home'/>">Về trang chủ</a></p>
</body>
</html>
