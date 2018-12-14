package com.example.fitdemo.Utils;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.example.fitdemo.R;

/**
 * Created by 最美人间四月天 on 2018/12/14.
 */

public class CountDownTimerUtils extends CountDownTimer {
    private TextView mTextView;

    public CountDownTimerUtils(TextView textView,long millisInFuture, long countDownInterval) {//控件，定时总时间,间隔时间
        super(millisInFuture, countDownInterval);
        this.mTextView = textView;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onTick(long millisUntilFinished) {
        mTextView.setClickable(false);//设置不可点击
        mTextView.setText("获取验证码（" + millisUntilFinished/1000 + "）");//设置倒计时时间
        /* SpannableString spannableString=new SpannableString(bt_getcord.getText().toString());//获取按钮上的文字
        ForegroundColorSpan span=new ForegroundColorSpan(Color.RED);//设置文字颜色
        bt_getcord.setAllCaps(false);
        spannableString.setSpan(span,0,2,Spannable.SPAN_INCLUSIVE_EXCLUSIVE);////将倒计时的时间设置为红色
        bt_getcord.setText(spannableString);*/
        mTextView.setBackgroundResource(R.color.colorGray);

    }

    @Override
    public void onFinish() {
        mTextView.setClickable(true);//重新获得点击
        mTextView.setText("  获取验证码    ");
        mTextView.setBackgroundResource(R.color.colorBlue);

    }


}
