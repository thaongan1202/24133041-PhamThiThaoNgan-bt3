<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Đăng nhập</title>
</head>
<body>
    <form action="${pageContext.request.contextPath}/login" method="post">
        <h2>Đăng nhập vào hệ thống</h2>

        <c:if test="${alert != null}">
            <h3 style="color: red">${alert}</h3>
        </c:if>
        <c:if test="${verifyEmail != null}">
            <p>
                <a href="<c:url value='/verify'><c:param name='email' value='${verifyEmail}'/></c:url>">Nhập mã OTP để kích hoạt</a>
            </p>
        </c:if>
        <c:if test="${param.activated == 'true'}">
            <h3 style="color: green">Kích hoạt thành công, mời bạn đăng nhập</h3>
        </c:if>
        <c:if test="${param.reset == 'true'}">
            <h3 style="color: green">Đổi mật khẩu thành công, mời bạn đăng nhập</h3>
        </c:if>

        <p><input type="text" name="username" placeholder="Tài khoản"></p>
        <p><input type="password" name="password" placeholder="Mật khẩu"></p>
        <p><label><input type="checkbox" name="remember"> Nhớ tôi</label></p>
        <button type="submit">Đăng nhập</button>
    </form>
    <p><a href="${pageContext.request.contextPath}/forgot-password">Quên mật khẩu?</a></p>
    <p>Chưa có tài khoản? <a href="${pageContext.request.contextPath}/register">Đăng ký</a></p>
</body>
</html>