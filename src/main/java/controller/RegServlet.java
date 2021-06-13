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
        User user = PsqlStore.instOf().findUserByEmail(email);
        if (user != null) {
            LOGGER.error("Пользователь с данным email (" + email + ") уже зарегистрирован!");
            req.setAttribute("error",  "Пользователь с данным email (" + email + ") уже зарегистрирован!");
            req.getRequestDispatcher("reg.jsp").forward(req, resp);
        } else {
            String name = req.getParameter("nameUser");
            user = PsqlStore.instOf().findUserByName(name);
            if (user != null) {
                LOGGER.error("Данное имя пользователя (" + name + ") уже занято!");
                req.setAttribute("error",  "Данное имя пользователя (" + name + ") уже занято!");
                req.getRequestDispatcher("reg.jsp").forward(req, resp);
            } else {
                PsqlStore.instOf().saveUser(
                        new User(0, req.getParameter("nameUser"), email, req.getParameter("passwordUser"))
                );
                LOGGER.info("Пользователь (" + name + ") зарегистрирован");
                resp.sendRedirect(req.getContextPath() + "/posts.do");
            }
        }
        System.out.println("Finished RegServlet.doPost");
    }
}
