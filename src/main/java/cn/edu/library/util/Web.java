package cn.edu.library.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public final class Web {
    private Web() {}
    public static String p(HttpServletRequest req, String name) {
        String value = req.getParameter(name);
        return value == null ? "" : value.trim();
    }
    public static long number(HttpServletRequest req, String name) {
        try { return Long.parseLong(p(req, name)); }
        catch (NumberFormatException e) { return 0L; }
    }
    public static void redirect(HttpServletRequest req, HttpServletResponse resp, String path) throws IOException {
        resp.sendRedirect(req.getContextPath() + path);
    }
    public static void message(HttpServletRequest req, String msg) {
        req.getSession().setAttribute("flash", msg);
    }
}
