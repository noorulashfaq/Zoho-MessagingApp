package com.messageprocessingapp.models;

public class MessageResponse {
    private int message_id;
    private int user_id;
    private String sub_table_name;

    public int getMessage_id() {
        return message_id;
    }

    public void setMessage_id(int message_id) {
        this.message_id = message_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getSub_table_name() {
        return sub_table_name;
    }

    public void setSub_table_name(String sub_table_name) {
        this.sub_table_name = sub_table_name;
    }
}
