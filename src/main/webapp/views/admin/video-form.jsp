<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><c:choose><c:when test="${editing}">Sửa video</c:when><c:otherwise>Thêm video</c:otherwise></c:choose></title>
</head>
<body>
    <h1><c:choose><c:when test="${editing}">Sửa video</c:when><c:otherwise>Thêm video</c:otherwise></c:choose></h1>
    <p><a href="<c:url value='/admin/videos'/>">← Danh sách video</a></p>
    <c:if test="${not empty error}"><p style="color: red"><c:out value="${error}"/></p></c:if>
    <c:if test="${empty categories}"><p style="color: #a60">Hãy tạo ít nhất một danh mục trước khi thêm video.</p></c:if>

    <c:choose>
        <c:when test="${editing}"><c:url var="formAction" value="/admin/video/update"/></c:when>
        <c:otherwise><c:url var="formAction" value="/admin/video/insert"/></c:otherwise>
    </c:choose>
    <c:set var="posterLink" value=""/>
    <c:if test="${fn:startsWith(video.poster, 'http://') or fn:startsWith(video.poster, 'https://')}">
        <c:set var="posterLink" value="${video.poster}"/>
    </c:if>
    <form method="post" action="${formAction}" enctype="multipart/form-data">
        <label for="videoId">ID hoặc URL YouTube:</label><br>
        <input id="videoId" name="videoId" type="text" required maxlength="100"
               value="<c:out value='${video.videoId}'/>" placeholder="VD: M7lc1UVf-VE hoặc URL watch?v=..."
               <c:if test="${editing}">readonly</c:if>><br><br>

        <label for="title">Tiêu đề:</label><br>
        <input id="title" name="title" type="text" required maxlength="500" value="<c:out value='${video.title}'/>"><br><br>

        <label for="description">Mô tả (tối đa 500 ký tự):</label><br>
        <textarea id="description" name="description" rows="5" cols="60" maxlength="500"><c:out value="${video.description}"/></textarea><br><br>

        <label for="categoryId">Danh mục:</label><br>
        <select id="categoryId" name="categoryId" required>
            <option value="">-- Chọn danh mục --</option>
            <c:forEach items="${categories}" var="category">
                <option value="${category.categoryid}" <c:if test="${video.category.categoryid == category.categoryid}">selected</c:if>><c:out value="${category.categoryname}"/></option>
            </c:forEach>
        </select><br><br>

        <label for="posterUrl">Link poster (có thể bỏ trống nếu tải ảnh lên):</label><br>
        <input id="posterUrl" name="posterUrl" type="url" size="70"
               value="<c:out value='${posterLink}'/>"><br><br>
        <label for="posterFile">Hoặc tải poster lên (JPG/PNG/GIF/WEBP, tối đa 5 MB):</label><br>
        <input id="posterFile" name="posterFile" type="file" accept="image/jpeg,image/png,image/gif,image/webp"><br>
        <c:if test="${not empty video.poster}">
            <p>Poster hiện tại:<br>
                <c:choose>
                    <c:when test="${fn:startsWith(video.poster, 'http://') or fn:startsWith(video.poster, 'https://')}"><img src="<c:out value='${video.poster}'/>" width="240" alt="Poster hiện tại"></c:when>
                    <c:otherwise><c:url var="currentPoster" value="/image"><c:param name="fname" value="${video.poster}"/></c:url><img src="${currentPoster}" width="240" alt="Poster hiện tại"></c:otherwise>
                </c:choose>
            </p>
        </c:if>
        <br>

        <label>Trạng thái:</label><br>
        <label><input type="radio" name="active" value="1" <c:if test="${video.active == 1}">checked</c:if>> Đang bật</label>
        <label><input type="radio" name="active" value="0" <c:if test="${video.active == 0}">checked</c:if>> Đang ẩn</label><br><br>

        <button type="submit"><c:choose><c:when test="${editing}">Cập nhật</c:when><c:otherwise>Thêm video</c:otherwise></c:choose></button>
        <a href="<c:url value='/admin/videos'/>">Hủy</a>
    </form>
</body>
</html>
