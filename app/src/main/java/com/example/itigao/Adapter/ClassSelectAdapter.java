package com.example.itigao.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.itigao.ClassAb.Appoint;
import com.example.itigao.R;

import java.util.List;

/**
 * Created by 最美人间四月天 on 2018/11/27.
 */

public class ClassSelectAdapter extends RecyclerView.Adapter<ClassSelectAdapter.ViewHolder> {

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private Context mContext;
    private List<Appoint> mDataSet;


    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView itr, coach, time;
        RelativeLayout relativeLayout;
       // LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.class_select_item_iv);
            itr = (TextView) itemView.findViewById(R.id.class_select_item_itr);
            coach = (TextView) itemView.findViewById(R.id.class_select_item_coach);
            time = (TextView) itemView.findViewById(R.id.class_select_item_time);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.class_select_item_relative);
            //linearLayout = (LinearLayout) itemView.findViewById(R.id.class_select_item_l);
        }
    }

    @Override
    public ClassSelectAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_select_item, parent, false);
        mContext = parent.getContext();
        final RecyclerView.ViewHolder vh = new ViewHolder(v);
        return (ViewHolder) vh;
    }

    public ClassSelectAdapter(List<Appoint> data) {
        mDataSet = data;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final ClassSelectAdapter.ViewHolder holder, int position) {
        Appoint class_select = mDataSet.get(position);
        if(class_select.getAppoint_yu_check() == 1){
            holder.relativeLayout.setBackgroundColor(mContext.getResources().getColor(R.color.colorBlue_W));
        }else {
            holder.relativeLayout.setBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));
        }
        if(class_select.getAppoint_cover() != null){
            String url = class_select.getAppoint_cover();
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
        holder.itr.setText(class_select.getAppoint_name());

        holder.coach.setText(class_select.getAppoint_coach());
        String rTime;
        if(class_select.getAppoint_time() == 1){
            rTime = "08:30-10:00";
        }else {
            rTime = "14:30-16:00";
        }
        holder.time.setText(rTime);

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

    /**
     * 添加并更新数据，同时具有动画效果
     */
    public void addDataAt(List<Appoint> data) {
        mDataSet = data;
        notifyDataSetChanged();
    }

    public void changeData(int position,  Appoint data) {
        mDataSet.set(position, data);
        notifyItemChanged(position);//更新数据集，注意如果用adapter.notifyDataSetChanged()将没有动画效果
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

}