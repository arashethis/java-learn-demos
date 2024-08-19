package org.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Set;

@WebServlet(urlPatterns = "/pref")
public class CookieLanguageServlet extends HttpServlet {
    static final Set<String> LANGUAGES = Set.of("en", "zh");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String lang = req.getParameter("lang");

        if (LANGUAGES.contains(lang)) {
            Cookie cookie = new Cookie("lang", lang);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 7);
            resp.addCookie(cookie);
        }

        resp.sendRedirect("/");
    }
}
