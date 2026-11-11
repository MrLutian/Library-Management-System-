package cn.edu.library.servlet;

import cn.edu.library.util.DB;
import cn.edu.library.util.Web;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/books")
public class BooksServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = Web.p(req, "action");
        try (Connection c = DB.getConnection()) {
            if ("delete".equals(action)) {
                try (PreparedStatement ps = c.prepareStatement("DELETE FROM books WHERE id=?")) {
                    ps.setLong(1, Web.number(req, "id")); ps.executeUpdate();
                }
                Web.message(req, "图书已删除");
                Web.redirect(req, resp, "/books"); return;
            }
            req.setAttribute("books", c.createStatement().executeQuery(
                "SELECT b.*, COALESCE(c.name,'未分类') category_name FROM books b LEFT JOIN categories c ON b.category_id=c.id ORDER BY b.id DESC"));
            req.getRequestDispatcher("/WEB-INF/views/books.jsp").forward(req, resp);
        } catch (SQLException e) { throw new ServletException("图书查询或删除失败", e); }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        long id = Web.number(req, "id");
        try (Connection c = DB.getConnection()) {
            if (id > 0) {
                try (PreparedStatement ps = c.prepareStatement(
                    "UPDATE books SET isbn=?,title=?,author=?,publisher=?,total_count=?,available_count=? WHERE id=?")) {
                    bind(ps, req); ps.setLong(7,id); ps.executeUpdate();
                }
            } else {
                try (PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO books(isbn,title,author,publisher,total_count,available_count) VALUES(?,?,?,?,?,?)")) {
                    bind(ps, req); ps.executeUpdate();
                }
            }
            Web.message(req, "图书信息已保存");
            Web.redirect(req, resp, "/books");
        } catch (SQLException e) { throw new ServletException("保存图书失败，请检查 ISBN 是否重复及数据格式", e); }
    }
    private void bind(PreparedStatement ps, HttpServletRequest req) throws SQLException {
        ps.setString(1, Web.p(req,"isbn")); ps.setString(2, Web.p(req,"title"));
        ps.setString(3, Web.p(req,"author")); ps.setString(4, Web.p(req,"publisher"));
        int total = (int)Math.max(0, Web.number(req,"totalCount"));
        int available = (int)Math.max(0, Web.number(req,"availableCount"));
        ps.setInt(5,total); ps.setInt(6,Math.min(total,available));
    }
}
