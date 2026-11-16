<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quản lý video</title>
</head>
<body>
    <h1>Quản lý video</h1>
    <p><a href="<c:url value='/home'/>">Trang chủ</a> | <a href="<c:url value='/admin/video/add'/>">Thêm video</a></p>
    <table border="1" cellpadding="8" cellspacing="0" width="100%">
        <thead>
            <tr><th>Poster</th><th>Tiêu đề</th><th>ID YouTube</th><th>Danh mục</th><th>Người đăng</th><th>Trạng thái</th><th>Thao tác</th></tr>
        </thead>
        <tbody>
            <c:forEach items="${videos}" var="video">
                <tr>
                    <td>
                        <c:choose>
                            <c:when test="${fn:startsWith(video.poster, 'http://') or fn:startsWith(video.poster, 'https://')}"><img src="<c:out value='${video.poster}'/>" width="120" alt="Poster"></c:when>
                            <c:when test="${not empty video.poster}"><c:url var="posterUrl" value="/image"><c:param name="fname" value="${video.poster}"/></c:url><img src="${posterUrl}" width="120" alt="Poster"></c:when>
                            <c:otherwise>—</c:otherwise>
                        </c:choose>
                    </td>
                    <td><c:out value="${video.title}"/></td>
                    <td><c:out value="${video.videoId}"/></td>
                    <td><c:out value="${video.category.categoryname}"/></td>
                    <td><c:out value="${video.user.fullname}"/></td>
                    <td><c:choose><c:when test="${video.active == 1}">Đang bật</c:when><c:otherwise>Đang ẩn</c:otherwise></c:choose></td>
                    <td>
                        <a href="<c:url value='/video'><c:param name='id' value='${video.videoId}'/></c:url>">Xem</a> |
                        <a href="<c:url value='/admin/video/edit'><c:param name='id' value='${video.videoId}'/></c:url>">Sửa</a>
                        <form method="post" action="<c:url value='/admin/video/delete'/>">
                            <input type="hidden" name="videoId" value="<c:out value='${video.videoId}'/>">
                            <button type="submit" onclick="return confirm('Xóa video này?')">Xóa</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty videos}"><tr><td colspan="7">Chưa có video.</td></tr></c:if>
        </tbody>
    </table>
</body>
</html>
