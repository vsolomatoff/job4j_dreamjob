package controller;

import model.Candidate;
import store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

public class CandidateServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Started CandidateServlet.doGet");
        Collection<Candidate> candidates = PsqlStore.instOf().findAllCandidates();
        candidates.forEach(candidate -> {
            System.out.println("    *** candidate = " + candidate.getId() + ", " + candidate.getName());
        });
        req.setAttribute("candidates", candidates);
        req.getRequestDispatcher("candidates.jsp").forward(req, resp);
        System.out.println("Finished CandidateServlet.doGet");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        PsqlStore.instOf().save(
                new Candidate(
                        Integer.parseInt(req.getParameter("id")),
                        req.getParameter("name")
                )
        );
        resp.sendRedirect(req.getContextPath() + "/candidates.do");
    }
}
