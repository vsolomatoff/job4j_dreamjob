package controller;

import model.Candidate;
import model.City;
import store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CandidateServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Started CandidateServlet.doGet");
        req.setAttribute("candidates", PsqlStore.instOf().findAllCandidates());
        req.setAttribute("user", req.getSession().getAttribute("user"));
        req.getRequestDispatcher("/candidate/candidates.jsp").forward(req, resp);
        System.out.println("Finished CandidateServlet.doGet");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Started CandidateServlet.doPost");
        req.setCharacterEncoding("UTF-8");
        String cityId = req.getParameter("cityId");
        System.out.println("$$$ cityId = " + cityId);
        int idCity = Integer.parseInt(cityId);
        City city = PsqlStore.instOf().findCityById(idCity);
        System.out.println("city = " + city);
        PsqlStore.instOf().save(
                new Candidate(
                        Integer.parseInt(req.getParameter("id")),
                        req.getParameter("name"),
                        idCity,
                        city.getName()
                )
        );
        resp.sendRedirect(req.getContextPath() + "/candidates.do");
        System.out.println("Finished CandidateServlet.doPost");
    }
}
