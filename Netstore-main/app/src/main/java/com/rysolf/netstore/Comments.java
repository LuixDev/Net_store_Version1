package com.rysolf.netstore;

import java.util.Date;

public class Comments {
    private String message, user_id;
    private String reciente;
    private boolean isseen;
    private int   count;
    private String sender;
    private Date timestamp;
    private Object time;
    private String receiver;
    private  String name,image,categoria;
    public Comments() {}

    public Comments(String message, String user_id, String  reciente, String sender,int count, String receiver, boolean isseen, Date timestamp,String name,String image,String categoria,Object time) {
        this.message = message;
        this.user_id = user_id;
        this. reciente =  reciente;
        this.sender = sender;
        this.receiver = receiver;
        this.timestamp = timestamp;
        this.isseen=isseen;
        this.count=count;
        this.name = name;
        this.image=image;
        this.categoria=categoria;
        this.time=time;
    }

    public int getCount() {
        return count;
    }



    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getReciente() {
        return reciente;
    }

    public Object getTime() {
        return time;
    }

    public void setTime(Object time) {
        this.time = time;
    }

    public void setReciente(String reciente) {
        this.reciente = reciente;
    }



}
