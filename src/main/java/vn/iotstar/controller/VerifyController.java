package vn.iotstar.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.service.IUserService;
import vn.iotstar.service.impl.UserServiceImpl;

@WebServlet(urlPatterns = { "/verify", "/resend-otp" })
public class VerifyController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private IUserService service = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        if (email == null || email.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/register");
            return;
        }

        if (req.getRequestURI().contains("/resend-otp")) {
            boolean sent = service.sendOtp(email, "Kích hoạt tài khoản ShoppingJPA");
            String encoded = URLEncoder.encode(email, StandardCharsets.UTF_8);
            resp.sendRedirect(req.getContextPath() + "/verify?email=" + encoded + (sent ? "&resent=true" : "&sent=false"));
            return;
        }

        req.setAttribute("email", email);
        req.getRequestDispatcher("/views/verify.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String email = req.getParameter("email");
        String otp = req.getParameter("otp");

        if (otp != null && service.activate(email, otp.trim())) {
            resp.sendRedirect(req.getContextPath() + "/login?activated=true");
            return;
        }

        req.setAttribute("email", email);
        req.setAttribute("alert", "Mã OTP không đúng hoặc đã hết hạn");
        req.getRequestDispatcher("/views/verify.jsp").forward(req, resp);
    }
}