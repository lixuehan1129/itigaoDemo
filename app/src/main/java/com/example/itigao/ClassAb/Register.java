package com.example.itigao.ClassAb;

public class Register {
    private String user_phone,
            user_password,
            user_name,
            user_create_time,
            user_picture,
            user_birth;
    private int user_level,
            user_sex,
            user_sort,
            user_online_time;

    public Register() {
    }

    public Register(String user_phone, String user_password, String user_name, String user_create_time, String user_picture, int user_level, int user_sex, int user_sort, int user_online_time) {
        this.user_phone = user_phone;
        this.user_password = user_password;
        this.user_name = user_name;
        this.user_create_time = user_create_time;
        this.user_picture = user_picture;
        this.user_level = user_level;
        this.user_sex = user_sex;
        this.user_sort = user_sort;
        this.user_online_time = user_online_time;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_create_time() {
        return user_create_time;
    }

    public void setUser_create_time(String user_create_time) {
        this.user_create_time = user_create_time;
    }

    public String getUser_picture() {
        return user_picture;
    }

    public void setUser_picture(String user_picture) {
        this.user_picture = user_picture;
    }

    public int getUser_level() {
        return user_level;
    }

    public void setUser_level(int user_level) {
        this.user_level = user_level;
    }

    public int getUser_sex() {
        return user_sex;
    }

    public void setUser_sex(int user_sex) {
        this.user_sex = user_sex;
    }

    public int getUser_sort() {
        return user_sort;
    }

    public void setUser_sort(int user_sort) {
        this.user_sort = user_sort;
    }

    public int getUser_online_time() {
        return user_online_time;
    }

    public void setUser_online_time(int user_online_time) {
        this.user_online_time = user_online_time;
    }
}
