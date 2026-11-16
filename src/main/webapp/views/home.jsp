<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Trang chủ - ShoppingJPA</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 1100px; margin: 24px auto; padding: 0 16px; }
        nav { display: flex; gap: 16px; padding: 12px 0; border-bottom: 1px solid #ddd; }
        .videos { display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 18px; }
        .card { border: 1px solid #ddd; border-radius: 8px; overflow: hidden; }
        .card img { width: 100%; height: 145px; object-fit: cover; background: #eee; }
        .card div { padding: 12px; }
    </style>
</head>
<body>
    <h1>Xin chào <c:out value="${account.fullname}"/></h1>
    <nav>
        <a href="<c:url value='/home'/>">Trang chủ</a>
        <a href="<c:url value='/video'/>">Tất cả video</a>
        <a href="<c:url value='/admin/videos'/>">Quản lý video</a>
        <a href="<c:url value='/admin/categories'/>">Quản lý danh mục</a>
        <a href="<c:url value='/multiPartServlet'/>">Upload file (Multipart)</a>
        <a href="<c:url value='/logout'/>">Đăng xuất</a>
    </nav>

    <h2>10 video mới nhất</h2>
    <c:if test="${empty videos}"><p>Chưa có video đang hoạt động.</p></c:if>
    <div class="videos">
        <c:forEach items="${videos}" var="video">
            <article class="card">
                <c:choose>
                    <c:when test="${fn:startsWith(video.poster, 'http://') or fn:startsWith(video.poster, 'https://')}">
                        <img src="<c:out value='${video.poster}'/>" alt="Poster video">
                    </c:when>
                    <c:when test="${not empty video.poster}">
                        <c:url var="posterUrl" value="/image"><c:param name="fname" value="${video.poster}"/></c:url>
                        <img src="${posterUrl}" alt="Poster video">
                    </c:when>
                    <c:otherwise><img src="" alt="Chưa có poster"></c:otherwise>
                </c:choose>
                <div>
                    <h3><a href="<c:url value='/video'><c:param name='id' value='${video.videoId}'/></c:url>"><c:out value="${video.title}"/></a></h3>
                    <p><c:out value="${video.description}"/></p>
                    <small>Lượt xem: <c:out value="${video.views}"/></small>
                </div>
            </article>
        </c:forEach>
    </div>
    <p><a href="<c:url value='/video'/>">Xem tất cả video →</a></p>
</body>
</html>
