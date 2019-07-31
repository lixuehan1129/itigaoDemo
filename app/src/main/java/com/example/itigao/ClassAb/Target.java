package com.example.itigao.ClassAb;

import org.litepal.crud.LitePalSupport;

public class Target extends LitePalSupport {
    private String cont;

    public String getCont() {
        return cont;
    }

    public void setCont(String cont) {
        this.cont = cont;
    }

    public Target(String cont) {
        this.cont = cont;
    }
}
