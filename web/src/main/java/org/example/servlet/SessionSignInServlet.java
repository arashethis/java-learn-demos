package org.example.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet(urlPatterns = "/signIn")
public class SessionSignInServlet extends HttpServlet {
    private final Map<String, String> users = Map.of("admin", "admin", "user", "user");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        PrintWriter pw = resp.getWriter();
        pw.write("<h1>Sign In</h1>");
        pw.write("<form action='/signIn' method='post'>");
        pw.write("<p>Username: <input name='username'></p>");
        pw.write("<p>Password: <input name='password' type='password'></p>");
        pw.write("<p><button type='submit'>Sign In</button> <a href='/'>Cancel</a></p>");
        pw.write("</form>");
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String user = req.getParameter("username");
        String password = req.getParameter("password");
        String expectedPassword = users.get(user);
        if (expectedPassword != null && expectedPassword.equals(password)) {
            req.getSession().setAttribute("user", user);
            resp.sendRedirect("/");
        } else {
            resp.sendRedirect("/signIn");
        }
    }
}
