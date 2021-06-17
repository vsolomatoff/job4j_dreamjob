package controller;

import model.City;
import store.PsqlStore;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CityServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Started CityServlet.doGet");
        List<City> citiesList = new ArrayList<>(PsqlStore.instOf().findAllCities());
        ObjectMapper objectMapper = new ObjectMapper();

        String cities = objectMapper.writeValueAsString(citiesList);
        System.out.println("*** cities = " + cities);

        response.setCharacterEncoding("UTF-8");
        //response.setContentType("json");
        response.getWriter().write(cities);
        System.out.println("Finished CityServlet.doGet");
    }

}
