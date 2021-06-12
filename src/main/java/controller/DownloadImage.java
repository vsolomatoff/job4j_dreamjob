package controller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

public class DownloadImage extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(DownloadImage.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        File downloadFile = null;
        for (File file : Objects.requireNonNull(new File("c:\\images\\").listFiles())) {
            if (id.equals(file.getName())) {
                downloadFile = file;
                break;
            }
        }
        if (downloadFile != null) {
            resp.setContentType("application/octet-stream");
            resp.setHeader("Content-Disposition", "attachment; filename=\"" + downloadFile.getName() + "\"");
            try (FileInputStream stream = new FileInputStream(downloadFile)) {
                resp.getOutputStream().write(stream.readAllBytes());
            } catch (Exception e) {
                LOGGER.error(e.toString(), e);
            }
            System.out.println("Downloaded file " + id);
            LOGGER.info("Downloaded file " + id);
        } else {
            resp.sendRedirect("candidates.do");
        }
    }
}
