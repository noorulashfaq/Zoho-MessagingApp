package com.messageprocessingapp.models;

import java.sql.Timestamp;

public class Message {
    private int message_id;
    private String message_content;
    private String message_type;
    private String priority;
    private Timestamp posted_at;
    private int user_id;

    public Timestamp getPosted_at() {
        return posted_at;
    }

    public void setPosted_at(Timestamp posted_at) {
        this.posted_at = posted_at;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }


    public int getMessage_id() {
        return message_id;
    }

    public void setMessage_id(int message_id) {
        this.message_id = message_id;
    }

    public String getMessage_content() {
        return message_content;
    }

    public void setMessage_content(String message_content) {
        this.message_content = message_content;
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

}
