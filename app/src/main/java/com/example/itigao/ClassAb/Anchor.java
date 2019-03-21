package com.example.itigao.ClassAb;

public class Anchor {
    private int anchor_id;
    private int anchor_classify;
    private String anchor_phone;
    private String anchor_name;
    private int anchor_bid;
    private int anchor_num;
    private String anchor_cover;
    private int anchor_state;
    private String anchor_data;
    private int anchor_room;

    public int getAnchor_id() {
        return anchor_id;
    }

    public void setAnchor_id(int anchor_id) {
        this.anchor_id = anchor_id;
    }

    public int getAnchor_classify() {
        return anchor_classify;
    }

    public void setAnchor_classify(int anchor_classify) {
        this.anchor_classify = anchor_classify;
    }

    public String getAnchor_phone() {
        return anchor_phone;
    }

    public void setAnchor_phone(String anchor_phone) {
        this.anchor_phone = anchor_phone;
    }

    public String getAnchor_name() {
        return anchor_name;
    }

    public void setAnchor_name(String anchor_name) {
        this.anchor_name = anchor_name;
    }

    public int getAnchor_bid() {
        return anchor_bid;
    }

    public void setAnchor_bid(int anchor_bid) {
        this.anchor_bid = anchor_bid;
    }

    public int getAnchor_num() {
        return anchor_num;
    }

    public void setAnchor_num(int anchor_num) {
        this.anchor_num = anchor_num;
    }

    public String getAnchor_cover() {
        return anchor_cover;
    }

    public void setAnchor_cover(String anchor_cover) {
        this.anchor_cover = anchor_cover;
    }

    public int getAnchor_state() {
        return anchor_state;
    }

    public void setAnchor_state(int anchor_state) {
        this.anchor_state = anchor_state;
    }

    public String getAnchor_data() {
        return anchor_data;
    }

    public void setAnchor_data(String anchor_data) {
        this.anchor_data = anchor_data;
    }

    public int getAnchor_room() {
        return anchor_room;
    }

    public void setAnchor_room(int anchor_room) {
        this.anchor_room = anchor_room;
    }

    public Anchor() {
    }

    public Anchor(String anchor_name, String anchor_cover) {
        this.anchor_name = anchor_name;
        this.anchor_cover = anchor_cover;
    }
}
