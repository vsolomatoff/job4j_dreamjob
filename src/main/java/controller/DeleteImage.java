package controller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

public class DeleteImage extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(DeleteImage.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        File file = new File("c:\\images\\"  + req.getParameter("id"));
        if (file.exists()) {
            file.delete();
            System.out.println("File deleted: " + "c:\\images\\"  + req.getParameter("id"));
            LOGGER.info("File deleted: " + "c:\\images\\"  + req.getParameter("id"));
        } else {
            System.out.println("File not found: " + "c:\\images\\"  + req.getParameter("id"));
            LOGGER.warn("File not found: " + "c:\\images\\"  + req.getParameter("id"));
        }
        resp.sendRedirect("candidates.do");
    }
}
