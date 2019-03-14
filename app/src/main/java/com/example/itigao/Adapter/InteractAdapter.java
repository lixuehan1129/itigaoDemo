package com.example.itigao.Adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.itigao.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 最美人间四月天 on 2018/12/11.
 */

public class InteractAdapter extends RecyclerView.Adapter<InteractAdapter.ViewHolder> {

    private OnItemClickListener mOnItemClickListener;
    private List<Interact> interacts = new ArrayList<>();


    class ViewHolder extends RecyclerView.ViewHolder{

        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.interact_tv);
        }
    }

    @Override
    public InteractAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.interact_item, parent, false);
        final RecyclerView.ViewHolder vh = new ViewHolder(v);
        return (ViewHolder) vh;
    }

    @Override
    public void onBindViewHolder(final InteractAdapter.ViewHolder holder, int position) {

        Interact interact = interacts.get(position);
        String s = "<font color=\"#FF7F24\">" + interact.getName() + "\t" + ":" + "\t\t" + "</font>" +
                "<font color=\"#757575\">" + interact.getContent() + "</font>";
        holder.textView.setText(Html.fromHtml(s));

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
