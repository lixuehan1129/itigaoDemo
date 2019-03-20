package com.example.itigao.AutoProject;


import org.json.JSONException;
import org.json.JSONObject;

public class JsonCode {

    public static int getCode(String regData){
        int code = 0;
        try {
            JSONObject jsonObject = new JSONObject(regData);
            code = Integer.parseInt(jsonObject.getString("code"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return code;
    }
}
