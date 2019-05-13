package com.example.itigao.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.itigao.ClassAb.Anchor;
import com.example.itigao.R;

import java.util.List;

public class AnchorActivityAdapter extends RecyclerView.Adapter<AnchorActivityAdapter.ViewHolder> {

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private List<Anchor> mDataSet;
    private Context mContext;


    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView class_video_iv;
        TextView class_video_itr;


        public ViewHolder(View itemView) {
            super(itemView);
            class_video_iv = (ImageView) itemView.findViewById(R.id.anchor_activity_item_iv);
            class_video_itr = (TextView) itemView.findViewById(R.id.anchor_activity_item_itr);
        }
    }
    @Override
    public AnchorActivityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.anchor_item_activity,parent,false);
        mContext = parent.getContext();
        final RecyclerView.ViewHolder vh = new AnchorActivityAdapter.ViewHolder(v);
        return (ViewHolder) vh;
    }

    public AnchorActivityAdapter(List<Anchor> data){
        mDataSet = data;
    }

    @Override
    public void onBindViewHolder(final AnchorActivityAdapter.ViewHolder holder, int position) {
        Anchor class_activity = mDataSet.get(position);
        if(class_activity.getAnchor_name() == null){
            holder.class_video_itr.setText("还没有介绍");
        }else {
            holder.class_video_itr.setText(class_activity.getAnchor_name());
        }
        if(class_activity.getAnchor_cover() != null){
            String url = class_activity.getAnchor_cover();
            if(url!=null){
                Glide.with(mContext)
                        .load(url)
                        .asBitmap()  //不可加载动图
                        .dontAnimate()//取消淡入淡出动画
                        .placeholder(R.mipmap.ic_touxiang21)
                        .error(R.mipmap.ic_touxiang21)
                        .thumbnail(0.1f) //先加载十分之一作为缩略图
                        .into(holder.class_video_iv);
            }
        }else {
            holder.class_video_iv.setImageResource(R.mipmap.ic_touxiang21);
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
    public void addDataAt(List<Anchor> data) {
        mDataSet = data;
        notifyDataSetChanged();
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
}
