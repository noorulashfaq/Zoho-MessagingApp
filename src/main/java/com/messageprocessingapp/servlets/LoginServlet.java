package com.messageprocessingapp.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.messageprocessingapp.interfaces.IUserRepository;
import com.messageprocessingapp.models.User;
import com.messageprocessingapp.repository.UserRepository;
import com.messageprocessingapp.utils.PasswordHasher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class LoginServlet extends HttpServlet {
    IUserRepository userRepository = new UserRepository();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String contentType = req.getContentType();

        if(contentType != null && contentType.equals("application/json")){
            BufferedReader reader = req.getReader();
            JsonObject body = new Gson().fromJson(reader, JsonObject.class);

            JsonObject jsonObject = new JsonObject();
            PrintWriter out = resp.getWriter();
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            try{
                String userName = body.get("username").getAsString();
                String password = body.get("password").getAsString();

                User user = userRepository.login(userName, password);

                if(user.getUser_id() == 0){
                    jsonObject.addProperty("info", "User not found");
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print(jsonObject);
                    return;
                }

                jsonObject.addProperty("success", "Successfully logged in");
                resp.setStatus(HttpServletResponse.SC_ACCEPTED);

                String json = new Gson().toJson(user);
                out.print(json);
                out.flush();

            } catch (RuntimeException e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                jsonObject.addProperty("error", e.getMessage());
                out.print(jsonObject);
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonObject.addProperty("error", e.getMessage());
                out.print(jsonObject);
            }

        }
    }
}
