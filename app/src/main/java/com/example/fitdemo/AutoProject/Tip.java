package com.example.fitdemo.AutoProject;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by 最美人间四月天 on 2018/12/11.
 */

public class Tip {

    public static void showTip(Context context, String tip){
        Toast.makeText(context,tip,Toast.LENGTH_LONG).show();
    }
}
