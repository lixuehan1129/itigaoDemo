package com.example.fitdemo.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fitdemo.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by 最美人间四月天 on 2018/12/7.
 */

public class AnchorAdapter extends RecyclerView.Adapter<AnchorAdapter.ViewHolder> {

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private Context mContext;
    private List<Anchor> mAnchor;

    class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (CircleImageView) itemView.findViewById(R.id.anchor_item_image);
        }
    }

    @Override
    public AnchorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.anchor_item, parent, false);
        mContext = parent.getContext();
        final RecyclerView.ViewHolder vh = new ViewHolder(v);
        return (ViewHolder) vh;
    }

    public AnchorAdapter(List<Anchor> data){
        mAnchor = data;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final AnchorAdapter.ViewHolder holder, int position) {

        Anchor anchor = mAnchor.get(position);

        if(anchor.getImage() != null){
            holder.imageView.setImageResource(anchor.getImage());
        }else {
            holder.imageView.setImageResource(R.mipmap.ic_touxiang2);
        }

        if(anchor.getState() == 0){
            holder.imageView.setBorderColorResource(R.color.colorDarkBlue);
        }else {
            holder.imageView.setBorderColorResource(R.color.colorGray_1);
        }

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
        return mAnchor.size();
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


    public class Anchor{
        private Integer image;
        private int state;

        public Anchor(Integer image, int state) {
            this.image = image;
            this.state = state;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public Integer getImage() {
            return image;
        }

        public void setImage(Integer image) {
            this.image = image;
        }

    }
}
