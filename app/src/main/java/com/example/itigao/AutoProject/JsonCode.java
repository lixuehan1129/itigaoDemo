package com.example.itigao.AutoProject;



import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import cn.jmessage.support.qiniu.android.utils.StringUtils;


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

    public static String getData(String regData){
        String data = null;
        try {
            JSONObject jsonObject = new JSONObject(regData);
            data = jsonObject.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }

    // 将jsonArray字符串转换成List集合
    public static <T> List<T> jsonToList(String json, Class beanClass) {
        List<T> list;
        if (!StringUtils.isBlank(json)) {
            Type type = new ParameterizedTypeImpl(beanClass);
            list = new Gson().fromJson(json, type);
            return list;
        } else {
            return null;
        }
    }

    private static class ParameterizedTypeImpl implements ParameterizedType {
        Class clazz;

        public ParameterizedTypeImpl(Class clz) {
            clazz = clz;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{clazz};
        }

        @Override
        public Type getRawType() {
            return List.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }

}
