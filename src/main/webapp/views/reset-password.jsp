<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Đặt lại mật khẩu</title>
</head>
<body>
    <form action="${pageContext.request.contextPath}/reset-password" method="post">
        <h2>Đặt lại mật khẩu</h2>
        <p>Mã OTP đã được gửi tới email: <b><c:out value="${email}"/></b></p>

        <c:if test="${alert != null}">
            <h3 style="color: red">${alert}</h3>
        </c:if>

        <input type="hidden" name="email" value="<c:out value='${email}'/>">
        <p><input type="text" name="otp" placeholder="Mã OTP 6 số" maxlength="6"></p>
        <p><input type="password" name="password" placeholder="Mật khẩu mới"></p>
        <p><input type="password" name="repassword" placeholder="Nhập lại mật khẩu mới"></p>
        <button type="submit">Đổi mật khẩu</button>
    </form>
</body>
</html>