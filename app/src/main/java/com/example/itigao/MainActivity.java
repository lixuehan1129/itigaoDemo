package com.example.itigao;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.itigao.Adapter.SectionsPagerAdapter;
import com.example.itigao.AutoProject.AppConstants;
import com.example.itigao.AutoProject.SharePreferences;
import com.example.itigao.Classes.ClassesFragment;
import com.example.itigao.Database.DataBaseHelper;
import com.example.itigao.Personal.PersonalFragment;
import com.example.itigao.Recommend.RecommendFragment;
import com.example.itigao.Sport.SportFragment;
import com.example.itigao.Subscribe.SubscribeFragment;
import com.example.itigao.Utils.PermissionUtils;
import com.example.itigao.Utils.StatusBarUtils;
import com.example.itigao.ViewHelper.NoScollViewPager;
import com.mob.MobSDK;


import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

/**
 * 主界面
 * @author 最美人间四月天
 * @version 1.0
 */


public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener, ViewPager.OnPageChangeListener{

    private DataBaseHelper dataBaseHelper;
    private NoScollViewPager viewPager;
    private BottomNavigationBar bottomNavigationBar;

    private String userId,userName,userPicture;
    private String todayTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        StatusBarUtils.setWindowStatusBarColor(MainActivity.this, R.color.colorWhite);
        MobSDK.init(this);
        localData();
        initView();
        PermissionUtils.setPer(MainActivity.this);//获取权限
    }

    private void initView(){
        viewPager = (NoScollViewPager) findViewById(R.id.viewpager);
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation);
        viewPager.setOffscreenPageLimit(5);
        initBottomNavigationBar();
        initViewPager();
    }

    private void localData(){
        userId = SharePreferences.getString(MainActivity.this, AppConstants.USER_PHONE);
        dataBaseHelper = new DataBaseHelper(MainActivity.this,AppConstants.SQL_VISION);
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("user",null,"user_phone = ?",new String[]{
                userId},null,null,null,"1");
        if(cursor.moveToFirst()){
            userName = cursor.getString(cursor.getColumnIndex("user_name"));
            userPicture = cursor.getString(cursor.getColumnIndex("user_picture"));
        }
        cursor.close();
        sqLiteDatabase.close();
        Im();
    }

    private void Im(){
        JMessageClient.login(userId, AppConstants.IM_PASS, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                System.out.println("登录" + s);
            }
        });
    }


    private void initBottomNavigationBar() {
        bottomNavigationBar.setTabSelectedListener(this);
        bottomNavigationBar.clearAll();
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar.setActiveColor(R.color.colorBlack)
                .setInActiveColor(R.color.colorGray_1);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.mipmap.ic_sub, "推荐"))
                .addItem(new BottomNavigationItem(R.mipmap.ic_class, "课程"))
                .addItem(new BottomNavigationItem(R.mipmap.ic_rec, "预约"))
                .addItem(new BottomNavigationItem(R.mipmap.ic_sport, "目标"))
                .addItem(new BottomNavigationItem(R.mipmap.ic_preson, "个人"))
                .initialise();//所有的设置需在调用该方法前完成

        //设置图标文字大小
        setBottomNavigationItem(bottomNavigationBar,6,25,12);
    }

    private void initViewPager() {
        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(new SubscribeFragment());
        fragments.add(new ClassesFragment());
        fragments.add(new RecommendFragment());
        fragments.add(new SportFragment());
        fragments.add(new PersonalFragment());
        viewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager(), fragments));
        viewPager.addOnPageChangeListener(this);
        viewPager.setCurrentItem(0);
    }

    /**
     * 判断是否是当日第一次登陆
     */
    private void isTodayFirstLogin() {
        //取
        SharedPreferences preferences = getSharedPreferences("LastLoginTime", MODE_PRIVATE);
        String lastTime = preferences.getString("LoginTime", "2019-02-25");
        // Toast.makeText(MainActivity.this, "value="+date, Toast.LENGTH_SHORT).show();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
        todayTime = df.format(new Date());// 获取当前的日期

        if (lastTime.equals(todayTime)) { //如果两个时间段相等
            Toast.makeText(this, "不是当日首次登陆", Toast.LENGTH_SHORT).show();
            Log.e("Time", lastTime);
        } else {
            Toast.makeText(this, "当日首次登陆", Toast.LENGTH_SHORT).show();
            Log.e("date", lastTime);
            Log.e("todayDate", todayTime);
        }
    }

    /**
     * 保存每次退出的时间
     * @param extiLoginTime
     */
    private void saveExitTime(String extiLoginTime) {
        SharedPreferences.Editor editor = getSharedPreferences("LastLoginTime", MODE_PRIVATE).edit();
        editor.putString("LoginTime", extiLoginTime);
        //这里用apply()而没有用commit()是因为apply()是异步处理提交，不需要返回结果，而我也没有后续操作
        //而commit()是同步的，效率相对较低
        //apply()提交的数据会覆盖之前的,这个需求正是我们需要的结果
        editor.apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveExitTime(todayTime);
    }



   //
   // Android按返回键，程序进入后台运行，不关闭程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // moveTaskToBack(false);
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        bottomNavigationBar.selectTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTabSelected(int position) {
        viewPager.setCurrentItem(position);
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    /**
     @param bottomNavigationBar，需要修改的 BottomNavigationBar
     @param space 图片与文字之间的间距
     @param imgLen 单位：dp，图片大小，应 <= 36dp
     @param textSize 单位：dp，文字大小，应 <= 20dp

     使用方法：直接调用setBottomNavigationItem(bottomNavigationBar, 6, 26, 10);
     代表将bottomNavigationBar的文字大小设置为10dp，图片大小为26dp，二者间间距为6dp
     **/

    private void setBottomNavigationItem(BottomNavigationBar bottomNavigationBar, int space, int imgLen, int textSize){
        Class barClass = bottomNavigationBar.getClass();
        Field[] fields = barClass.getDeclaredFields();
        for(int i = 0; i < fields.length; i++){
            Field field = fields[i];
            field.setAccessible(true);
            if(field.getName().equals("mTabContainer")){
                try{
                    //反射得到 mTabContainer
                    LinearLayout mTabContainer = (LinearLayout) field.get(bottomNavigationBar);
                    for(int j = 0; j < mTabContainer.getChildCount(); j++){
                        //获取到容器内的各个Tab

                        View view = mTabContainer.getChildAt(j);
                        //获取到Tab内的各个显示控件
                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dip2px(56));
                        FrameLayout container = (FrameLayout) view.findViewById(R.id.fixed_bottom_navigation_container);
                        container.setLayoutParams(params);
                        container.setPadding(dip2px(12), dip2px(0), dip2px(12), dip2px(0));

                        //获取到Tab内的文字控件
                        TextView labelView = (TextView) view.findViewById(com.ashokvarma.bottomnavigation.R.id.fixed_bottom_navigation_title);
                        //计算文字的高度DP值并设置，setTextSize为设置文字正方形的对角线长度，所以：文字高度（总内容高度减去间距和图片高度）*根号2即为对角线长度，此处用DP值，设置该值即可。
                        labelView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
                        labelView.setIncludeFontPadding(false);
                        labelView.setPadding(0,0,0,dip2px(20-textSize - space/2));

                        //获取到Tab内的图像控件
                        ImageView iconView = (ImageView) view.findViewById(com.ashokvarma.bottomnavigation.R.id.fixed_bottom_navigation_icon);
                        //设置图片参数，其中，MethodUtils.dip2px()：换算dp值
                        params = new FrameLayout.LayoutParams(dip2px(imgLen), dip2px(imgLen));
                        params.setMargins(0,0,0,space/2);
                        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                        iconView.setLayoutParams(params);
                    }
                } catch (IllegalAccessException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public int dip2px(float dpValue) {
        final float scale = getApplication().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
