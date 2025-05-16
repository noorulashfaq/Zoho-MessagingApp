package com.messageprocessingapp.models;

import java.sql.Timestamp;

public class MessageProcess {
//    private int p_id;
    private String thread_id;
    private int user_id;
    private String message_content;
    private double content_length;
    private String message_type;
    private String priority;
    private Timestamp posted_at;

//    public int getP_id() {
//        return p_id;
//    }
//
//    public void setP_id(int p_id) {
//        this.p_id = p_id;
//    }

    public String getThread_id() {
        return thread_id;
    }

    public void setThread_id(String thread_id) {
        this.thread_id = thread_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getMessage_content() {
        return message_content;
    }

    public void setMessage_content(String message_content) {
        this.message_content = message_content;
    }

    public double getContent_length() {
        return content_length;
    }

    public void setContent_length(double content_length) {
        this.content_length = content_length;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Timestamp getPosted_at() {
        return posted_at;
    }

    public void setPosted_at(Timestamp posted_at) {
        this.posted_at = posted_at;
    }
}
