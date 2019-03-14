package com.example.itigao.Utils;

/**
 * Created by 最美人间四月天 on 2018/12/21.
 */

public class Class_select {
    private String itr,coach,time;
    private String image;
    private int check,place,bid,week;

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }

    public String getItr() {
        return itr;
    }

    public void setItr(String itr) {
        this.itr = itr;
    }

    public String getCoach() {
        return coach;
    }

    public void setCoach(String coach) {
        this.coach = coach;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Class_select(String itr, String coach, String time, String image, int check, int place, int bid, int week) {
        this.itr = itr;
        this.coach = coach;
        this.time = time;
        this.image = image;
        this.check = check;
        this.place = place;
        this.bid = bid;
        this.week = week;
    }

}
