package com.example.itigao.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.itigao.ClassAb.Appoint;
import com.example.itigao.ClassAb.HuDong;
import com.example.itigao.R;

import java.util.List;

/**
 * Created by 最美人间四月天 on 2018/12/14.
 */

public class HuDongAdapter extends RecyclerView.Adapter<HuDongAdapter.ViewHolder>{
    private OnItemClickListener mOnItemClickListener;
    private List<HuDong> mDataSet;
    private Context mContext;


    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView1, textView2;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.hudong_item_iv);
            textView1 = (TextView) itemView.findViewById(R.id.hudong_item_tv1);
            textView2 = (TextView) itemView.findViewById(R.id.hudong_item_tv2);
        }
    }
    @Override
    public HuDongAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.hudong_item,parent,false);
        mContext = parent.getContext();
        final RecyclerView.ViewHolder vh = new ViewHolder(v);
        return (ViewHolder) vh;
    }

    public HuDongAdapter(List<HuDong> data){
        mDataSet = data;
    }

    @Override
    public void onBindViewHolder(final HuDongAdapter.ViewHolder holder, int position) {

        HuDong huDong = mDataSet.get(position);
        holder.textView1.setText(huDong.getHudong_name());
        holder.textView2.setText(huDong.getHudong_content());
        if(huDong.getHudong_cover() != null){
            String url = huDong.getHudong_cover();
            if(url!=null){
                Glide.with(mContext)
                        .load(url)
                        .asBitmap()  //不可加载动图
                        .dontAnimate()//取消淡入淡出动画
                        .placeholder(R.mipmap.hudong_head2)
                        .error(R.mipmap.hudong_head2)
                        .thumbnail(0.1f) //先加载十分之一作为缩略图
                        .into(holder.imageView);
            }
        }else {
            holder.imageView.setImageResource(R.mipmap.hudong_head2);
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

    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    /**
     * 添加并更新数据，同时具有动画效果
     */
    public void addDataAt(List<HuDong> data) {
        mDataSet = data;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }

}
