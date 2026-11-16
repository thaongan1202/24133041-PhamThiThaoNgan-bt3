<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><c:out value="${video.title}"/> - Video</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 1000px; margin: 24px auto; padding: 0 16px; }
        .player { position: relative; width: 100%; aspect-ratio: 16 / 9; background: #111; }
        .player iframe { width: 100%; height: 100%; border: 0; }
    </style>
</head>
<body>
    <p><a href="<c:url value='/video'/>">← Danh sách video</a> · <a href="<c:url value='/home'/>">Trang chủ</a></p>
    <h1><c:out value="${video.title}"/></h1>
    <div class="player">
        <iframe src="https://www.youtube-nocookie.com/embed/${video.videoId}"
                title="<c:out value='${video.title}'/>"
                allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
                referrerpolicy="strict-origin-when-cross-origin" allowfullscreen></iframe>
    </div>
    <p>Danh mục: <c:out value="${video.category.categoryname}"/></p>
    <p>Lượt xem: <c:out value="${video.views}"/></p>
    <h2>Mô tả</h2>
    <p><c:out value="${video.description}"/></p>
</body>
</html>
