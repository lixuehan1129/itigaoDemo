package com.example.itigao.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.itigao.R;

import java.util.List;

public class OtherAdapter extends RecyclerView.Adapter<OtherAdapter.ViewHolder> {
    private List<String> mOther;


    class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.other_tv);
        }
    }
    @NonNull
    @Override
    public OtherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.other_item, parent, false);
        final RecyclerView.ViewHolder vh = new ViewHolder(v);
        return (ViewHolder) vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String other = mOther.get(position);
        holder.textView.setText(other);

    }
    public OtherAdapter(List<String> data){
        mOther = data;
    }


    @Override
    public int getItemCount() {
        return mOther.size();
    }

    public void addDataAt(List<String> data) {
        mOther = data;
        notifyDataSetChanged();//更新数据集，注意如果用adapter.notifyDataSetChanged()将没有动画效果
    }

    public void addDataAt(String data,int position) {
        mOther.add(position, data);
        notifyItemChanged(position);
    }




}
