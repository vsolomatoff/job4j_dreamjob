package controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class DownloadServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Started DownloadServlet.doGet");
        String name = req.getParameter("name");
        resp.setContentType("name=" + name);
        resp.setContentType("image/png");
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + name + "\"");
        File file = new File("images" + File.separator + name);
        try (FileInputStream in = new FileInputStream(file)) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead; // Количество прочитанных байт
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                resp.getOutputStream().write(dataBuffer, 0, bytesRead);
            }
        }
        System.out.println("Finished DownloadServlet.doGet");
    }
}
