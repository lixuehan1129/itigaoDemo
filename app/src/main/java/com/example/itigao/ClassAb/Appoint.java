package com.example.itigao.ClassAb;

public class Appoint {
    private int appoint_id;
    private int appoint_classify;
    private String appoint_name;
    private String appoint_coach;
    private int appoint_bid;
    private int appoint_capacity;
    private int appoint_num;
    private int appoint_week;
    private int appoint_time;
    private int appoint_place;
    private String appoint_cover;

    private int appoint_yu_check;

    public Appoint() {
    }

    public Appoint(String appoint_name, String appoint_coach, int appoint_time, String appoint_cover, int appoint_yu_check,int appoint_bid) {
        this.appoint_name = appoint_name;
        this.appoint_coach = appoint_coach;
        this.appoint_time = appoint_time;
        this.appoint_cover = appoint_cover;
        this.appoint_yu_check = appoint_yu_check;
        this.appoint_bid = appoint_bid;
    }

    public int getAppoint_id() {
        return appoint_id;
    }

    public void setAppoint_id(int appoint_id) {
        this.appoint_id = appoint_id;
    }

    public int getAppoint_classify() {
        return appoint_classify;
    }

    public void setAppoint_classify(int appoint_classify) {
        this.appoint_classify = appoint_classify;
    }

    public String getAppoint_name() {
        return appoint_name;
    }

    public void setAppoint_name(String appoint_name) {
        this.appoint_name = appoint_name;
    }

    public String getAppoint_coach() {
        return appoint_coach;
    }

    public void setAppoint_coach(String appoint_coach) {
        this.appoint_coach = appoint_coach;
    }

    public int getAppoint_bid() {
        return appoint_bid;
    }

    public void setAppoint_bid(int appoint_bid) {
        this.appoint_bid = appoint_bid;
    }

    public int getAppoint_capacity() {
        return appoint_capacity;
    }

    public void setAppoint_capacity(int appoint_capacity) {
        this.appoint_capacity = appoint_capacity;
    }

    public int getAppoint_num() {
        return appoint_num;
    }

    public void setAppoint_num(int appoint_num) {
        this.appoint_num = appoint_num;
    }

    public int getAppoint_week() {
        return appoint_week;
    }

    public void setAppoint_week(int appoint_week) {
        this.appoint_week = appoint_week;
    }

    public int getAppoint_time() {
        return appoint_time;
    }

    public void setAppoint_time(int appoint_time) {
        this.appoint_time = appoint_time;
    }

    public int getAppoint_place() {
        return appoint_place;
    }

    public void setAppoint_place(int appoint_place) {
        this.appoint_place = appoint_place;
    }

    public String getAppoint_cover() {
        return appoint_cover;
    }

    public void setAppoint_cover(String appoint_cover) {
        this.appoint_cover = appoint_cover;
    }

    public int getAppoint_yu_check() {
        return appoint_yu_check;
    }

    public void setAppoint_yu_check(int appoint_yu_check) {
        this.appoint_yu_check = appoint_yu_check;
    }
}
