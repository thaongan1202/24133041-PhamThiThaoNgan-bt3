<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Video</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 1100px; margin: 24px auto; padding: 0 16px; }
        .videos { display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 18px; }
        .card { border: 1px solid #ddd; border-radius: 8px; overflow: hidden; }
        .card img { width: 100%; height: 145px; object-fit: cover; background: #eee; }
        .card div { padding: 12px; }
        .pages { display: flex; gap: 12px; margin: 24px 0; }
    </style>
</head>
<body>
    <p><a href="<c:url value='/home'/>">Trang chủ</a></p>
    <h1>Tất cả video</h1>
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
                    <small>Danh mục: <c:out value="${video.category.categoryname}"/> · Lượt xem: <c:out value="${video.views}"/></small>
                </div>
            </article>
        </c:forEach>
    </div>

    <nav class="pages" aria-label="Phân trang">
        <c:if test="${page > 1}">
            <a href="<c:url value='/video'><c:param name='page' value='${page - 1}'/></c:url>">← Trước</a>
        </c:if>
        <c:forEach begin="1" end="${totalPages}" var="p">
            <c:choose>
                <c:when test="${p == page}"><strong><c:out value="${p}"/></strong></c:when>
                <c:otherwise><a href="<c:url value='/video'><c:param name='page' value='${p}'/></c:url>"><c:out value="${p}"/></a></c:otherwise>
            </c:choose>
        </c:forEach>
        <c:if test="${page < totalPages}">
            <a href="<c:url value='/video'><c:param name='page' value='${page + 1}'/></c:url>">Sau →</a>
        </c:if>
    </nav>
</body>
</html>
