package cn.edu.library.servlet;

import cn.edu.library.util.DB;
import cn.edu.library.util.Web;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/borrows")
public class BorrowsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        try(Connection c=DB.getConnection()){
            req.setAttribute("books", load(c, "SELECT id,title,available_count FROM books ORDER BY title"));
            req.setAttribute("readers", load(c, "SELECT id,reader_no,name FROM readers ORDER BY name"));
            req.setAttribute("records", load(c,
                "SELECT br.id,b.title,r.name,r.reader_no,br.borrow_date,br.due_date,br.return_date,br.status " +
                "FROM borrow_records br JOIN books b ON br.book_id=b.id JOIN readers r ON br.reader_id=r.id ORDER BY br.id DESC"));
            req.getRequestDispatcher("/WEB-INF/views/borrows.jsp").forward(req,resp);
        }catch(SQLException e){throw new ServletException("借阅信息查询失败",e);}
    }

    /** 将查询结果缓存到内存，避免 JSP 页面使用已关闭的数据库连接。 */
    private javax.sql.rowset.CachedRowSet load(Connection c, String sql) throws SQLException {
        try(Statement statement=c.createStatement(); ResultSet result=statement.executeQuery(sql)){
            javax.sql.rowset.CachedRowSet cached=javax.sql.rowset.RowSetProvider.newFactory().createCachedRowSet();
            cached.populate(result);
            return cached;
        }
    }
    @Override
    protected void doPost(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        req.setCharacterEncoding("UTF-8");
        String action=Web.p(req,"action");
        try(Connection c=DB.getConnection()){
            c.setAutoCommit(false);
            try{
                if("return".equals(action)){
                    long id=Web.number(req,"id");
                    long bookId;
                    try(PreparedStatement p=c.prepareStatement("SELECT book_id FROM borrow_records WHERE id=? AND status='BORROWED' FOR UPDATE")){
                        p.setLong(1,id); try(ResultSet r=p.executeQuery()){if(!r.next())throw new SQLException("借阅记录不存在或已经归还");bookId=r.getLong(1);}
                    }
                    try(PreparedStatement p=c.prepareStatement("UPDATE borrow_records SET status='RETURNED',return_date=CURRENT_DATE WHERE id=?")){p.setLong(1,id);p.executeUpdate();}
                    try(PreparedStatement p=c.prepareStatement("UPDATE books SET available_count=available_count+1 WHERE id=?")){p.setLong(1,bookId);p.executeUpdate();}
                    Web.message(req,"归还成功");
                }else{
                    long bookId=Web.number(req,"bookId"), readerId=Web.number(req,"readerId");
                    try(PreparedStatement p=c.prepareStatement("UPDATE books SET available_count=available_count-1 WHERE id=? AND available_count>0")){
                        p.setLong(1,bookId);if(p.executeUpdate()!=1)throw new SQLException("图书不存在或库存不足");
                    }
                    try(PreparedStatement p=c.prepareStatement(
                        "INSERT INTO borrow_records(book_id,reader_id,borrow_date,due_date,status) VALUES(?,?,CURRENT_DATE,DATE_ADD(CURRENT_DATE,INTERVAL 30 DAY),'BORROWED')")){
                        p.setLong(1,bookId);p.setLong(2,readerId);p.executeUpdate();
                    }
                    Web.message(req,"借阅登记成功");
                }
                c.commit();
            }catch(Exception e){c.rollback();throw e;}
            finally{c.setAutoCommit(true);}
            Web.redirect(req,resp,"/borrows");
        }catch(SQLException e){throw new ServletException("借还书操作失败："+e.getMessage(),e);}
    }
}
