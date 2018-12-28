package com.example.fitdemo.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fitdemo.R;
import com.example.fitdemo.Utils.Class_select;

import java.util.List;

/**
 * Created by 最美人间四月天 on 2018/11/27.
 */

public class ClassSelectAdapter extends RecyclerView.Adapter<ClassSelectAdapter.ViewHolder> {

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private Context mContext;
    private List<Class_select> mDataSet;


    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView itr, coach, time;
        RelativeLayout relativeLayout;
        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.class_select_item_iv);
            itr = (TextView) itemView.findViewById(R.id.class_select_item_itr);
            coach = (TextView) itemView.findViewById(R.id.class_select_item_coach);
            time = (TextView) itemView.findViewById(R.id.class_select_item_time);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.class_select_item_relative);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.class_select_item_l);
        }
    }

    @Override
    public ClassSelectAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_select_item, parent, false);
        mContext = parent.getContext();
        final RecyclerView.ViewHolder vh = new ViewHolder(v);
        return (ViewHolder) vh;
    }

    public ClassSelectAdapter(List<Class_select> data) {
        mDataSet = data;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final ClassSelectAdapter.ViewHolder holder, int position) {
        Class_select class_select = mDataSet.get(position);
        if(class_select.getCheck() == 1){
            holder.relativeLayout.setBackgroundColor(mContext.getResources().getColor(R.color.colorBlue_W));
        }else {
            holder.relativeLayout.setBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));
        }
        if(class_select.getImage() != null){
            String url = class_select.getImage();
            if(url!=null){
                Glide.with(mContext)
                        .load(url)
                        .asBitmap()  //不可加载动图
                        .dontAnimate()//取消淡入淡出动画
                        .placeholder(R.mipmap.ic_run1111)
                        .error(R.mipmap.ic_run1111)
                        .thumbnail(0.1f) //先加载十分之一作为缩略图
                        .into(holder.imageView);
            }
        }else {
            holder.imageView.setImageResource(R.mipmap.ic_run1111);
        }
//        if(class_select.getPlace() == 0){
//            holder.itr.setText(class_select.getItr());
//        }else {
//            String s = "<font color=\"#000000\">" + class_select.getItr() + "</font>" +
//                    "<font color=\"#FF0000\">" + "(现场)" + "</font>";
//            holder.itr.setText(Html.fromHtml(s));
//        }
        holder.itr.setText(class_select.getItr());

        holder.coach.setText(class_select.getCoach());
        holder.time.setText(class_select.getTime());

        //判断是否设置了监听器
        if (mOnItemClickListener != null) {
            //为ItemView设置监听器
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition(); // 1
                    mOnItemClickListener.onItemClick(holder.itemView, position); // 2
                }
            });
        }
        if (mOnItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = holder.getLayoutPosition();
                    mOnItemLongClickListener.onItemLongClick(holder.itemView, position);
                    //返回true 表示消耗了事件 事件不会继续传递
                    return true;
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

//    public  class Class_Select {
//        private String itr,coach,time;
//        private String image;
//        private int check,place;
//
//        public int getPlace() {
//            return place;
//        }
//
//        public void setPlace(int place) {
//            this.place = place;
//        }
//
//        public int getCheck() {
//            return check;
//        }
//
//        public void setCheck(int check) {
//            this.check = check;
//        }
//
//        public String getItr() {
//            return itr;
//        }
//
//        public void setItr(String itr) {
//            this.itr = itr;
//        }
//
//        public String getCoach() {
//            return coach;
//        }
//
//        public void setCoach(String coach) {
//            this.coach = coach;
//        }
//
//        public String getTime() {
//            return time;
//        }
//
//        public void setTime(String time) {
//            this.time = time;
//        }
//
//        public String getImage() {
//            return image;
//        }
//
//        public void setImage(String image) {
//            this.image = image;
//        }
//
//        public Class_Select(String itr, String coach, String time, String image, int check, int place) {
//            this.itr = itr;
//            this.coach = coach;
//            this.time = time;
//            this.image = image;
//            this.check = check;
//            this.place = place;
//        }
//    }
}