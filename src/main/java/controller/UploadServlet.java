package controller;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UploadServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> images = new ArrayList<>();
        for (File name : new File("images").listFiles()) {
            images.add(name.getName());
        }
        req.setAttribute("images", images);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/upload.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Started UploadServlet.doPost");
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletContext servletContext = this.getServletConfig().getServletContext();
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        System.out.println("    repository = " + repository);
        factory.setRepository(repository);

        ServletFileUpload upload = new ServletFileUpload(factory);
        try {
            List<FileItem> items = upload.parseRequest(req);
            File folder = new File("images");
            if (!folder.exists()) {
                folder.mkdir();
            }
            for (FileItem item : items) {
                System.out.println("    item = " + item);
                if (!item.isFormField()) {
                    File file = new File(folder + File.separator + item.getName());
                    System.out.println("    file = " + file);
                    try (FileOutputStream out = new FileOutputStream(file)) {
                        InputStream in = item.getInputStream();
                        // Записываем содержимое файла в новый файл (в репозитории)
                        byte[] dataBuffer = new byte[1024];
                        int bytesRead; // Количество прочитанных байт
                        while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                            out.write(dataBuffer, 0, bytesRead);
                        }
                    }
                }
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        }
        doGet(req, resp);
        System.out.println("Finished UploadServlet.doPost");
    }
}
