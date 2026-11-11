package cn.edu.library.servlet;

import cn.edu.library.util.DB;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection c = DB.getConnection()) {
            req.setAttribute("bookCount", count(c, "books"));
            req.setAttribute("readerCount", count(c, "readers"));
            req.setAttribute("borrowCount", scalar(c, "SELECT COUNT(*) FROM borrow_records WHERE status='BORROWED'"));
            req.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(req, resp);
        } catch (SQLException e) { throw new ServletException(e); }
    }
    private int count(Connection c, String table) throws SQLException {
        try (Statement s = c.createStatement(); ResultSet r = s.executeQuery("SELECT COUNT(*) FROM " + table)) {
            r.next(); return r.getInt(1);
        }
    }
    private int scalar(Connection c, String sql) throws SQLException {
        try (Statement s = c.createStatement(); ResultSet r = s.executeQuery(sql)) { r.next(); return r.getInt(1); }
    }
}
