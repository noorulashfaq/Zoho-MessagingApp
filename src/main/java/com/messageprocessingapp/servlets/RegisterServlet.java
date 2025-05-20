package com.messageprocessingapp.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.messageprocessingapp.exceptions.UserAlreadyExistsException;
import com.messageprocessingapp.interfaces.IUserRepository;
import com.messageprocessingapp.repository.UserRepository;
import com.messageprocessingapp.utils.AppLogger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class RegisterServlet extends HttpServlet {

    IUserRepository userRepository = new UserRepository();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String contentType = req.getContentType();

        if(contentType != null && contentType.equals("application/json")) {
            BufferedReader reader = req.getReader();

            JsonObject body = new Gson().fromJson(reader, JsonObject.class);

            JsonObject jsonObject = new JsonObject();
            PrintWriter out = resp.getWriter();
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            try {
                String username = body.get("username").getAsString();
                String password = body.get("password").getAsString();

                if(userRepository.getUserByName(username)){
                    throw new UserAlreadyExistsException();
                }

                boolean result = userRepository.createUser(username, password);

                if (result) {
                    resp.setStatus(HttpServletResponse.SC_CREATED);
                    AppLogger.logger.info("User " + username + " created");
                    jsonObject.addProperty("success", "New user created");
                } else {
                    throw new RuntimeException();
                }
            } catch (UserAlreadyExistsException e){
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                jsonObject.addProperty("Warning", "User already exists");
            }catch (RuntimeException e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                jsonObject.addProperty("Failed", "User not created");
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonObject.addProperty("Error", e.getMessage());
            }
            out.print(jsonObject);
        }
    }
}
