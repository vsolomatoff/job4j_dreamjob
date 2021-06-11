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
import java.util.Objects;

public class UploadServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Started UploadServlet.doGet");
        List<String> images = new ArrayList<>();
        System.out.println("    *** images = " + images);
        for (File name : Objects.requireNonNull(new File("c:\\images\\").listFiles())) {
            images.add(name.getName());
            System.out.println("    Added file " + name);
        }
        req.setAttribute("images", images);
        System.out.println("    ### images = " + images);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/upload.jsp");
        dispatcher.forward(req, resp);
        System.out.println("Finished UploadServlet.doGet");
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

            System.out.println("    items = " + items);
            // For test
            items.forEach(System.out::println);

            File folder = new File("c:\\images\\");
            if (!folder.exists()) {
                folder.mkdir();
            }
            for (FileItem item : items) {
                System.out.println("    item = " + item);
                if (!item.isFormField()) {
                    File file = new File(folder + File.separator + item.getName());
                    try (FileOutputStream out = new FileOutputStream(file)) {
                        InputStream in = item.getInputStream();
                        // Записываем содержимое файла в новый файл
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
