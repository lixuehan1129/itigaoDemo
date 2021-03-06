package com.example.itigao.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.itigao.R;

import java.util.List;

/**
 * Created by 最美人间四月天 on 2018/11/30.
 */

public class ClassActivityAdapter extends RecyclerView.Adapter<ClassActivityAdapter.ViewHolder> {

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private List<Class_Activity> mDataSet;
    private Context mContext;


    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView class_video_iv;
        TextView class_video_itr;


        public ViewHolder(View itemView) {
            super(itemView);
            class_video_iv = (ImageView) itemView.findViewById(R.id.class_activity_item_iv);
            class_video_itr = (TextView) itemView.findViewById(R.id.class_activity_item_itr);
        }
    }
    @Override
    public ClassActivityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_activity_item,parent,false);
        mContext = parent.getContext();
        final RecyclerView.ViewHolder vh = new ViewHolder(v);
        return (ViewHolder) vh;
    }

    public ClassActivityAdapter(List<Class_Activity> data){
        mDataSet = data;
    }

    @Override
    public void onBindViewHolder(final ClassActivityAdapter.ViewHolder holder, int position) {
        Class_Activity class_activity = mDataSet.get(position);
        if(class_activity.getClass_video_itr() == null){
            holder.class_video_itr.setText("还没有介绍");
        }else {
            holder.class_video_itr.setText(class_activity.getClass_video_itr());
        }
        if(class_activity.getClass_video_iv() != null){
            String url = class_activity.getClass_video_iv();
            if(url!=null){
                Glide.with(mContext)
                        .load(url)
                        .asBitmap()  //不可加载动图
                        .dontAnimate()//取消淡入淡出动画
                        .placeholder(R.mipmap.ic_download)
                        .error(R.mipmap.ic_download)
                        .thumbnail(0.1f) //先加载十分之一作为缩略图
                        .into(holder.class_video_iv);
            }
        }else {
            holder.class_video_iv.setImageResource(R.mipmap.ic_download);
        }

        //判断是否设置了监听器
        if(mOnItemClickListener != null){
            //为ItemView设置监听器
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition(); // 1
                    mOnItemClickListener.onItemClick(holder.itemView,position); // 2
                }
            });
        }
        if(mOnItemLongClickListener != null){
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = holder.getLayoutPosition();
                    mOnItemLongClickListener.onItemLongClick(holder.itemView,position);
                    //返回true 表示消耗了事件 事件不会继续传递
                    return true;
                }
            });
        }
    }

    /**
     * 添加并更新数据，同时具有动画效果
     */
    public void addDataAt(List<Class_Activity> data) {
        mDataSet = data;
        notifyDataSetChanged();//更新数据集，注意如果用adapter.notifyDataSetChanged()将没有动画效果
    }


    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener{
        void onItemLongClick(View view, int position);
    }
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    public class Class_Activity {
        private String class_video_itr;
        private String class_video_iv;

        public String getClass_video_itr() {
            return class_video_itr;
        }

        public void setClass_video_itr(String class_video_itr) {
            this.class_video_itr = class_video_itr;
        }

        public String getClass_video_iv() {
            return class_video_iv;
        }

        public void setClass_video_iv(String class_video_iv) {
            this.class_video_iv = class_video_iv;
        }

        public Class_Activity(String class_video_itr, String class_video_iv) {
            this.class_video_itr = class_video_itr;
            this.class_video_iv = class_video_iv;
        }

    }
}
