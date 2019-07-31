package com.example.itigao.Personal;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.itigao.Adapter.OtherAdapter;
import com.example.itigao.AutoProject.AppConstants;
import com.example.itigao.Database.DataBaseHelper;
import com.example.itigao.R;
import com.example.itigao.Utils.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 最美人间四月天 on 2018/12/15.
 */

public class PersonIndoorActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OtherAdapter otherAdapter;
    private DataBaseHelper dataBaseHelper;
    private List<String> mOther = new ArrayList<>();
    private static int C_ID = 2;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_exam);
        StatusBarUtils.setWindowStatusBarColor(PersonIndoorActivity.this, R.color.colorWhite);
        initView();
    }

    private void initView(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.person_exam_mainTool);
        toolbar.setTitle("学习历程");
        back(toolbar);

        mOther = new ArrayList<>();
        dataBaseHelper = new DataBaseHelper(PersonIndoorActivity.this, AppConstants.SQL_VISION);
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query("other",null,"other_i = ?",new String[]{String.valueOf(C_ID)},
                null,null,"id");
        while (cursor.moveToNext()){
            mOther.add(cursor.getString(cursor.getColumnIndex("other_con")));
        }
        if(mOther.size() == 0){
            mOther.add("开启你的新生活!!!!");
        }
        cursor.close();
        sqLiteDatabase.close();


        recyclerView = findViewById(R.id.person_exam_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(PersonIndoorActivity.this));
        otherAdapter = new OtherAdapter(mOther);
        recyclerView.setAdapter(otherAdapter);

        Button button = findViewById(R.id.person_exam_bt);
        button.setOnClickListener(v -> {
            LayoutInflater factory = LayoutInflater.from(PersonIndoorActivity.this);//提示框
            final View view = factory.inflate(R.layout.other_edit, null);//这里必须是final的
            editText = view.findViewById(R.id.other_ed);
            new AlertDialog.Builder(this)
                    .setView(view)
                    .setPositiveButton("确定", (dialogInterface, i) -> {

                        SQLiteDatabase sqLiteDatabase1 = dataBaseHelper.getWritableDatabase();

                        ContentValues values = new ContentValues();
                        values.put("other_con",editText.getText().toString());
                        values.put("other_i",C_ID);
                        sqLiteDatabase1.insert("other",null,values);
                        sqLiteDatabase1.close();
                        otherAdapter.addDataAt(editText.getText().toString(),otherAdapter.getItemCount());
                    }).setNegativeButton("取消",null).show();

        });


    }

    //返回注销事件
    private void back(Toolbar toolbar){
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
