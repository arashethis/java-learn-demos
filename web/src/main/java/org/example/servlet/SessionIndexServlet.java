package org.example.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = "/")
public class SessionIndexServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String user = (String) req.getSession().getAttribute("user");
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter pw = resp.getWriter();

        String lang = this.getLangCookie(req);
        boolean isEN = lang.equals("en");

        String welcome = isEN
                ? "<h1>Welcome, " + (user != null ? user : "Guest") + "</h1>"
                : "<h1>你好，" + (user != null ? user : "Guest") + "</h1>";
        pw.write(welcome);

        if (user != null) {
            pw.write("<a href='/signOut'>" + (isEN ? "Sign Out" : "退出登录") + "</a>");
        } else {
            pw.write("<a href='/signIn'>" + (isEN ? "Sign In" : "登录") + "</a>");
        }
        pw.write("<p>");
        pw.write("<a href='/pref?lang=en'>English</a>");
        pw.write("  ");
        pw.write("<a href='/pref?lang=zh'>中文</a>");
        pw.write("</p>");

        pw.write("</html>");
    }

    private String getLangCookie(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("lang")) {
                    return cookie.getValue();
                }
            }
        }
        return "en";
    }
}
