package vn.iotstar.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import vn.iotstar.util.Constant;

@MultipartConfig(fileSizeThreshold = 1024 * 1024,
        maxFileSize = 5 * 1024 * 1024,
        maxRequestSize = 25 * 1024 * 1024)
@WebServlet("/multiPartServlet")
public class MultipartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/views/upload.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Part part;
        try {
            part = req.getPart("multiPartServlet");
        } catch (IllegalStateException e) {
            req.setAttribute("error", "File vượt quá giới hạn 5 MB.");
            req.getRequestDispatcher("/views/upload.jsp").forward(req, resp);
            return;
        }
        if (part == null || part.getSize() == 0 || part.getSubmittedFileName() == null
                || part.getSubmittedFileName().isBlank()) {
            req.setAttribute("error", "Bạn chưa chọn file để tải lên.");
            req.getRequestDispatcher("/views/upload.jsp").forward(req, resp);
            return;
        }

        String originalName = Paths.get(part.getSubmittedFileName().replace('\\', '/')).getFileName().toString();
        String extension = "";
        int dot = originalName.lastIndexOf('.');
        if (dot >= 0 && dot < originalName.length() - 1) {
            extension = originalName.substring(dot).replaceAll("[^A-Za-z0-9.]", "");
        }

        Path uploadDirectory = Paths.get(Constant.DIR);
        Files.createDirectories(uploadDirectory);
        String savedName = UUID.randomUUID() + extension;
        part.write(uploadDirectory.resolve(savedName).toAbsolutePath().toString());

        req.setAttribute("originalName", originalName);
        req.setAttribute("savedName", savedName);
        req.setAttribute("fileSize", part.getSize());
        req.getRequestDispatcher("/views/result.jsp").forward(req, resp);
    }
}
