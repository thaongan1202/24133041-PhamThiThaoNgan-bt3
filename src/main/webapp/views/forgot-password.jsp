<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Quên mật khẩu</title>
</head>
<body>
    <form action="${pageContext.request.contextPath}/forgot-password" method="post">
        <h2>Quên mật khẩu</h2>

        <c:if test="${alert != null}">
            <h3 style="color: red">${alert}</h3>
        </c:if>

        <p><input type="email" name="email" placeholder="Email đã đăng ký" required></p>
        <button type="submit">Gửi mã xác nhận</button>
    </form>
    <p><a href="${pageContext.request.contextPath}/login">Quay lại đăng nhập</a></p>
</body>
</html>