package com.messageprocessingapp.models;

public class MessageType {
    private int id;
    private String sub_table_name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSub_table_name() {
        return sub_table_name;
    }

    public void setSub_table_name(String sub_table_name) {
        this.sub_table_name = sub_table_name;
    }
}
