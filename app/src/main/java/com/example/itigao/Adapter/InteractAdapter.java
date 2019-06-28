package com.example.itigao.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.itigao.Emotion.utils.SpanStringUtils;
import com.example.itigao.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 最美人间四月天 on 2018/12/11.
 */

public class InteractAdapter extends RecyclerView.Adapter<InteractAdapter.ViewHolder> {

    private OnItemClickListener mOnItemClickListener;
    private List<Interact> interacts = new ArrayList<>();
    private Context mContext;


    class ViewHolder extends RecyclerView.ViewHolder{

        TextView textView,textViewN;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.interact_tv);
            textViewN = (TextView) itemView.findViewById(R.id.interact_name);
        }
    }

    @Override
    public InteractAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.interact_item, parent, false);
        mContext = parent.getContext();
        final RecyclerView.ViewHolder vh = new ViewHolder(v);
        return (ViewHolder) vh;
    }

    @Override
    public void onBindViewHolder(final InteractAdapter.ViewHolder holder, int position) {

        Interact interact = interacts.get(position);
        holder.textViewN.setText(interact.getName() + " :");
        StringBuilder sb = new StringBuilder(interact.content);
        holder.textView.setText(SpanStringUtils.getEmotionContent(1,
                mContext, holder.textView, sb.toString()));
        //holder.textView.setText(interact.getContent());

        if (mOnItemClickListener != null) {
            //为ItemView设置监听器
            holder.itemView.setOnClickListener(v -> {
                int position1 = holder.getLayoutPosition(); // 1
                mOnItemClickListener.onItemClick(holder.itemView, position1); // 2
            });
        }

    }



    /**
     * 添加并更新数据，同时具有动画效果
     */
    public void addDataAt(int position, Interact data) {
        interacts.add(position, data);
        notifyItemInserted(position);//更新数据集，注意如果用adapter.notifyDataSetChanged()将没有动画效果
    }

    public InteractAdapter(List<Interact> interact){
        interacts = interact;
    }

    @Override
    public int getItemCount() {
        if(interacts != null){
            return interacts.size();
        }else {
            return 0;
        }

    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }


    public class Interact{
        private String name;
        private String content;

        public Interact(String name, String content) {
            this.name = name;
            this.content = content;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
