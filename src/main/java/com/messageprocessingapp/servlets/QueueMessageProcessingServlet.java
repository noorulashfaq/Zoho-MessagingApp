package com.messageprocessingapp.servlets;

import com.google.gson.JsonObject;
//import com.messageprocessingapp.utils.QueueMessageProcessing;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class QueueMessageProcessingServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
//            new QueueMessageProcessing().assignThreadsToTables();
//            ThreadsCreator tc = new ThreadsCreator();
//            tc.startThreads();
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            PrintWriter out = resp.getWriter();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("success", "All messages have been processed");

            resp.setStatus(HttpServletResponse.SC_OK);
            out.print(jsonObject);

        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
