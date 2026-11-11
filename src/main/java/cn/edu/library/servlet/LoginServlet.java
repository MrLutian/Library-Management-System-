package cn.edu.library.servlet;

import cn.edu.library.util.DB;
import cn.edu.library.util.Web;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.IOException;
import java.sql.*;

@WebServlet(urlPatterns = {"/login", "/logout"})
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("/logout".equals(req.getServletPath())) {
            req.getSession().invalidate();
            Web.redirect(req, resp, "/login");
            return;
        }
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String username = Web.p(req, "username");
        String password = Web.p(req, "password");
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT id,username,real_name FROM admins WHERE username=? AND password=?")) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    HttpSession session = req.getSession();
                    session.setAttribute("admin", rs.getString("username"));
                    session.setAttribute("realName", rs.getString("real_name"));
                    Web.redirect(req, resp, "/dashboard");
                } else {
                    req.setAttribute("error", "用户名或密码错误");
                    req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
                }
            }
        } catch (SQLException e) {
            throw new ServletException("登录失败，请检查数据库连接配置。", e);
        }
    }
}
