package com.messageprocessingapp.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.messageprocessingapp.interfaces.IMessageRepository;
import com.messageprocessingapp.models.Message;
import com.messageprocessingapp.repository.MessageRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class MessageServlet extends HttpServlet {
    IMessageRepository messageRepository = new MessageRepository();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String contentType = req.getContentType();

        if(contentType != null && contentType.equals(("application/json"))){
            BufferedReader reader = req.getReader();

            JsonObject body = new Gson().fromJson(reader, JsonObject.class);

            Message msg = new Message();

            msg.setMessage_content(body.get("message_content").getAsString());
            msg.setMessage_type(body.get("message_type").getAsString());
            msg.setPriority(body.get("priority").getAsString());
            msg.setUser_id(body.get("user_id").getAsInt());


            JsonObject jsonObject = new JsonObject();
            PrintWriter out = resp.getWriter();
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            try{
                boolean result = messageRepository.addMessage(msg);
                if(result){
                    resp.setStatus(HttpServletResponse.SC_CREATED);
                    jsonObject.addProperty("success", "New message posted");
                    out.print(jsonObject);
                }
            } catch (RuntimeException e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                jsonObject.addProperty("Failed", e.getMessage());
                out.print(jsonObject);
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonObject.addProperty("error", e.getMessage());
                out.print(jsonObject);
            }
        }
    }
}
