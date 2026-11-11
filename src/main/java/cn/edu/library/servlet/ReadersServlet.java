package cn.edu.library.servlet;

import cn.edu.library.util.DB;
import cn.edu.library.util.Web;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/readers")
public class ReadersServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("delete".equals(Web.p(req,"action"))) {
            try (Connection c=DB.getConnection(); PreparedStatement ps=c.prepareStatement("DELETE FROM readers WHERE id=?")) {
                ps.setLong(1,Web.number(req,"id")); ps.executeUpdate();
                Web.message(req,"读者已删除");
            } catch (SQLException e) { throw new ServletException("删除失败：该读者可能存在借阅记录",e); }
            Web.redirect(req,resp,"/readers"); return;
        }
        try (Connection c=DB.getConnection(); PreparedStatement ps=c.prepareStatement("SELECT * FROM readers ORDER BY id DESC");
             ResultSet rs=ps.executeQuery()) {
            req.setAttribute("readers",rs);
            req.getRequestDispatcher("/WEB-INF/views/readers.jsp").forward(req,resp);
        } catch(SQLException e){throw new ServletException(e);}
    }
    @Override
    protected void doPost(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        req.setCharacterEncoding("UTF-8");
        long id=Web.number(req,"id");
        String sql=id>0?"UPDATE readers SET reader_no=?,name=?,phone=?,email=? WHERE id=?":
                         "INSERT INTO readers(reader_no,name,phone,email) VALUES(?,?,?,?)";
        try(Connection c=DB.getConnection();PreparedStatement ps=c.prepareStatement(sql)){
            ps.setString(1,Web.p(req,"readerNo")); ps.setString(2,Web.p(req,"name"));
            ps.setString(3,Web.p(req,"phone")); ps.setString(4,Web.p(req,"email"));
            if(id>0)ps.setLong(5,id);
            ps.executeUpdate(); Web.message(req,"读者信息已保存"); Web.redirect(req,resp,"/readers");
        }catch(SQLException e){throw new ServletException("保存读者失败，请检查读者编号是否重复",e);}
    }
}
