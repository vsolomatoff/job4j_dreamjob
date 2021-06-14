package controller;

import model.User;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class RegServlet extends HttpServlet {

    private static final Logger LOGGER = LogManager.getLogger(RegServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("reg.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Started RegServlet.doPost");
        String email = req.getParameter("emailUser");
        if (email != null && !email.isEmpty()) {
            User user = PsqlStore.instOf().findUserByEmail(email);
            if (user != null) {
                LOGGER.error("Пользователь с данным email (" + email + ") уже зарегистрирован!");
                req.setAttribute("error",  "Пользователь с данным email (" + email + ") уже зарегистрирован!");
                req.getRequestDispatcher("reg.jsp").forward(req, resp);
            } else {
                PsqlStore.instOf().saveUser(
                        new User(0, req.getParameter("nameUser"), email, req.getParameter("passwordUser"))
                );
                LOGGER.info("Пользователь (" + email + ") зарегистрирован");
                resp.sendRedirect(req.getContextPath() + "/posts.do");
            }
        } else {
            LOGGER.error("email не может быть пустым!");
            req.setAttribute("error",  "email не может быть пустым!");
            req.getRequestDispatcher("reg.jsp").forward(req, resp);
        }
        System.out.println("Finished RegServlet.doPost");
    }
}
