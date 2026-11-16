package vn.iotstar.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import vn.iotstar.entity.Category;
import vn.iotstar.entity.User;
import vn.iotstar.entity.Video;
import vn.iotstar.service.ICategoryService;
import vn.iotstar.service.IVideoService;
import vn.iotstar.service.impl.CategoryServiceImpl;
import vn.iotstar.service.impl.VideoServiceImpl;
import vn.iotstar.util.Constant;

@MultipartConfig(fileSizeThreshold = 1024 * 1024,
        maxFileSize = 5 * 1024 * 1024,
        maxRequestSize = 6 * 1024 * 1024)
@WebServlet(urlPatterns = {
        "/video",
        "/admin/videos",
        "/admin/video/add",
        "/admin/video/insert",
        "/admin/video/edit",
        "/admin/video/update",
        "/admin/video/delete"
})
public class VideoController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final int PAGE_SIZE = 6;
    private static final Set<String> IMAGE_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif", "webp");

    private final IVideoService videoService = new VideoServiceImpl();
    private final ICategoryService categoryService = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        if ("/video".equals(path)) {
            String videoId = req.getParameter("id");
            if (videoId != null && !videoId.isBlank()) {
                showVideo(req, resp, videoId.trim());
            } else {
                showVideoPage(req, resp);
            }
            return;
        }

        if (!requireLogin(req, resp)) {
            return;
        }

        if ("/admin/videos".equals(path)) {
            req.setAttribute("videos", videoService.findAll(1, Math.max(1, videoService.count())));
            req.getRequestDispatcher("/views/admin/video-list.jsp").forward(req, resp);
        } else if ("/admin/video/add".equals(path)) {
            Video video = new Video();
            video.setActive(1);
            showForm(req, resp, video, false);
        } else if ("/admin/video/edit".equals(path)) {
            String id = req.getParameter("id");
            if (id == null || id.isBlank()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            Video video = videoService.findById(id.trim());
            if (video == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            showForm(req, resp, video, true);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String path = req.getServletPath();
        if (!requireLogin(req, resp)) {
            return;
        }

        if ("/admin/video/delete".equals(path)) {
            deleteVideo(req, resp);
        } else if ("/admin/video/insert".equals(path)) {
            saveVideo(req, resp, false);
        } else if ("/admin/video/update".equals(path)) {
            saveVideo(req, resp, true);
        }
    }

    private void showVideoPage(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int totalVideos = videoService.countActive();
        int totalPages = Math.max(1, (int) Math.ceil(totalVideos / (double) PAGE_SIZE));
        int page = parsePage(req.getParameter("page"));
        page = Math.min(page, totalPages);

        req.setAttribute("videos", videoService.findActive(page, PAGE_SIZE));
        req.setAttribute("page", page);
        req.setAttribute("totalPages", totalPages);
        req.getRequestDispatcher("/views/video-list.jsp").forward(req, resp);
    }

    private void showVideo(HttpServletRequest req, HttpServletResponse resp, String videoId)
            throws ServletException, IOException {
        Video video = videoService.findById(videoId);
        if (video == null || video.getActive() != 1) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        videoService.incrementViews(videoId);
        video.setViews(video.getViews() + 1);
        req.setAttribute("video", video);
        req.getRequestDispatcher("/views/video-detail.jsp").forward(req, resp);
    }

    private void showForm(HttpServletRequest req, HttpServletResponse resp, Video video, boolean editing)
            throws ServletException, IOException {
        req.setAttribute("video", video);
        req.setAttribute("categories", categoryService.findAll());
        req.setAttribute("editing", editing);
        req.getRequestDispatcher("/views/admin/video-form.jsp").forward(req, resp);
    }

    private void saveVideo(HttpServletRequest req, HttpServletResponse resp, boolean editing)
            throws ServletException, IOException {
        String videoId = req.getParameter("videoId");
        if (editing && (videoId == null || videoId.isBlank())) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Video video = editing ? videoService.findById(videoId) : new Video();
        if (editing && video == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String oldPoster = video.getPoster();
        try {
            String normalizedId = normalizeVideoId(videoId);
            if (!normalizedId.matches("[A-Za-z0-9_-]{6,20}")) {
                throw new IllegalArgumentException("Hãy nhập ID hoặc URL YouTube hợp lệ.");
            }
            if (editing && !normalizedId.equals(video.getVideoId())) {
                throw new IllegalArgumentException("Không thể đổi Video ID sau khi đã tạo.");
            }
            if (!editing && videoService.findById(normalizedId) != null) {
                throw new IllegalArgumentException("Video ID này đã có trong danh sách.");
            }

            String title = trim(req.getParameter("title"));
            if (title.isEmpty()) {
                throw new IllegalArgumentException("Tiêu đề video không được để trống.");
            }
            String description = trim(req.getParameter("description"));
            if (description.length() > 500) {
                throw new IllegalArgumentException("Mô tả tối đa 500 ký tự.");
            }

            int categoryId = parsePositiveInt(req.getParameter("categoryId"), "Danh mục");
            Category category = categoryService.findById(categoryId);
            if (category == null) {
                throw new IllegalArgumentException("Danh mục đã chọn không tồn tại.");
            }

            int active = parseActive(req.getParameter("active"));
            String posterUrl = trim(req.getParameter("posterUrl"));
            if (!posterUrl.isEmpty() && !posterUrl.startsWith("https://") && !posterUrl.startsWith("http://")) {
                throw new IllegalArgumentException("Link poster phải bắt đầu bằng http:// hoặc https://.");
            }
            String posterFile = savePoster(req.getPart("posterFile"));
            String poster = posterFile != null ? posterFile
                    : !posterUrl.isEmpty() ? posterUrl : editing ? oldPoster : null;

            video.setVideoId(normalizedId);
            video.setTitle(title);
            video.setDescription(description);
            video.setCategory(category);
            video.setActive(active);
            video.setPoster(poster);
            if (!editing) {
                video.setViews(0);
                video.setUser(getLoggedInUser(req));
            }

            if (editing) {
                videoService.update(video);
            } else {
                videoService.insert(video);
            }
            if (editing && oldPoster != null && !oldPoster.equals(poster)) {
                deleteLocalPoster(oldPoster);
            }
            resp.sendRedirect(req.getContextPath() + "/admin/videos");
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            showForm(req, resp, draftFromRequest(req, video), editing);
        }
    }

    private void deleteVideo(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String videoId = req.getParameter("videoId");
        if (videoId == null || videoId.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        videoId = videoId.trim();
        Video video = videoService.findById(videoId);
        if (video != null) {
            videoService.delete(videoId);
            deleteLocalPoster(video.getPoster());
        }
        resp.sendRedirect(req.getContextPath() + "/admin/videos");
    }

    private Video draftFromRequest(HttpServletRequest req, Video video) {
        video.setVideoId(trim(req.getParameter("videoId")));
        video.setTitle(trim(req.getParameter("title")));
        video.setDescription(trim(req.getParameter("description")));
        String posterUrl = trim(req.getParameter("posterUrl"));
        if (!posterUrl.isEmpty()) {
            video.setPoster(posterUrl);
        }
        try {
            video.setActive(parseActive(req.getParameter("active")));
        } catch (IllegalArgumentException ignored) {
            video.setActive(1);
        }
        return video;
    }

    private String savePoster(Part part) throws IOException {
        if (part == null || part.getSize() == 0) {
            return null;
        }
        if (part.getSize() > 5L * 1024 * 1024) {
            throw new IllegalArgumentException("Ảnh poster tối đa 5 MB.");
        }

        String submittedName = part.getSubmittedFileName();
        String normalizedName = submittedName == null ? "" : submittedName.replace('\\', '/');
        int dot = normalizedName.lastIndexOf('.');
        if (dot < 0 || dot == normalizedName.length() - 1) {
            throw new IllegalArgumentException("Ảnh phải có đuôi jpg, jpeg, png, gif hoặc webp.");
        }
        String extension = normalizedName.substring(dot + 1).toLowerCase(Locale.ROOT);
        if (!IMAGE_EXTENSIONS.contains(extension) || part.getContentType() == null
                || !part.getContentType().toLowerCase(Locale.ROOT).startsWith("image/")) {
            throw new IllegalArgumentException("File tải lên phải là ảnh jpg, jpeg, png, gif hoặc webp.");
        }

        Path uploadDirectory = Paths.get(Constant.DIR);
        Files.createDirectories(uploadDirectory);
        String fileName = UUID.randomUUID() + "." + extension;
        part.write(uploadDirectory.resolve(fileName).toAbsolutePath().toString());
        return fileName;
    }

    private void deleteLocalPoster(String poster) throws IOException {
        if (poster == null || poster.isBlank() || poster.startsWith("http://") || poster.startsWith("https://")) {
            return;
        }
        String fileName = Paths.get(poster).getFileName().toString();
        Files.deleteIfExists(Paths.get(Constant.DIR, fileName));
    }

    private boolean requireLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (getLoggedInUser(req) != null) {
            return true;
        }
        resp.sendRedirect(req.getContextPath() + "/login");
        return false;
    }

    private User getLoggedInUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session == null ? null : (User) session.getAttribute(Constant.SESSION_ACCOUNT);
    }

    private int parsePage(String value) {
        try {
            return Math.max(1, Integer.parseInt(value));
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    private int parsePositiveInt(String value, String label) {
        try {
            int parsed = Integer.parseInt(value);
            if (parsed > 0) {
                return parsed;
            }
        } catch (NumberFormatException ignored) {
        }
        throw new IllegalArgumentException("Vui lòng chọn " + label.toLowerCase(Locale.ROOT) + ".");
    }

    private int parseActive(String value) {
        if ("0".equals(value) || "1".equals(value)) {
            return Integer.parseInt(value);
        }
        throw new IllegalArgumentException("Trạng thái video không hợp lệ.");
    }

    private String normalizeVideoId(String value) {
        String candidate = trim(value);
        if (candidate.isEmpty()) {
            return candidate;
        }
        try {
            URI uri = URI.create(candidate);
            String host = uri.getHost();
            String path = uri.getPath() == null ? "" : uri.getPath();
            if (host == null) {
                return candidate;
            }
            host = host.toLowerCase(Locale.ROOT);
            if (host.equals("youtu.be") || host.endsWith(".youtu.be")) {
                return path.replaceFirst("^/", "").split("/", 2)[0];
            }
            if (host.contains("youtube.com") || host.contains("youtube-nocookie.com")) {
                if (path.equals("/watch") && uri.getRawQuery() != null) {
                    for (String pair : uri.getRawQuery().split("&")) {
                        if (pair.startsWith("v=")) {
                            return URLDecoder.decode(pair.substring(2), StandardCharsets.UTF_8);
                        }
                    }
                }
                for (String prefix : List.of("/embed/", "/shorts/", "/live/")) {
                    if (path.startsWith(prefix)) {
                        return path.substring(prefix.length()).split("/", 2)[0];
                    }
                }
            }
        } catch (IllegalArgumentException ignored) {
            return candidate;
        }
        return candidate;
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }
}
