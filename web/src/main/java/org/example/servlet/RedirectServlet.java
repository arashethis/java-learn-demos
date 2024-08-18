package org.example.servlet;


import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/hi")
public class RedirectServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        String redirectUrl = "/" + (name == null ? "" : "?name=" + name);

        // 永久重定向，只需执行以下代码，不再需要 sendRedirect
        // resp.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        // resp.setHeader("Location", redirectUrl);

        // sendRedirect 默认为 302，setStatus 不会生效
        resp.sendRedirect(redirectUrl);
    }
}
