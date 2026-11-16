<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Kích hoạt tài khoản</title>
</head>
<body>
    <form action="${pageContext.request.contextPath}/verify" method="post">
        <h2>Kích hoạt tài khoản</h2>
        <p>Mã OTP đã được gửi tới email: <b><c:out value="${email}"/></b></p>

        <c:if test="${alert != null}">
            <h3 style="color: red">${alert}</h3>
        </c:if>
        <c:if test="${param.sent == 'false'}">
            <h3 style="color: red">Chưa gửi được email, hãy bấm "Gửi lại mã"</h3>
        </c:if>
        <c:if test="${param.resent == 'true'}">
            <h3 style="color: green">Đã gửi lại mã mới</h3>
        </c:if>

        <input type="hidden" name="email" value="<c:out value='${email}'/>">
        <p><input type="text" name="otp" placeholder="Nhập mã OTP 6 số" maxlength="6" required></p>
        <button type="submit">Kích hoạt</button>
    </form>
    <p>
        <a href="<c:url value='/resend-otp'><c:param name='email' value='${email}'/></c:url>">Gửi lại mã</a>
    </p>
</body>
</html>