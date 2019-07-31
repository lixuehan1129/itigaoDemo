package com.example.itigao.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.itigao.R;

import java.util.List;

public class WeekTarAdapter extends RecyclerView.Adapter<WeekTarAdapter.ViewHolder> {

    private Context mContext;
    private List<WeekTar> mWeekTar;

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView con,pro;
        public ViewHolder(View itemView) {
            super(itemView);
            con = (TextView) itemView.findViewById(R.id.week_tar_con);
            pro = (TextView) itemView.findViewById(R.id.week_tar_pro);
        }
    }
    @NonNull
    @Override
    public WeekTarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.week_target_item, parent, false);
        mContext = parent.getContext();
        final RecyclerView.ViewHolder vh = new ViewHolder(v);
        return (ViewHolder) vh;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull WeekTarAdapter.ViewHolder holder, int position) {
        WeekTar weekTar = mWeekTar.get(position);

        holder.con.setText(weekTar.getCon());
        holder.pro.setTextColor(weekTar.getPro() == 0 ? Color.parseColor("#43CD80") : Color.parseColor("#FF0000"));
        holder.pro.setText("√");
    }

    public WeekTarAdapter(List<WeekTar> data){
        mWeekTar = data;
    }

    public void addDataAt(List<WeekTar> data) {
        mWeekTar = data;
        notifyDataSetChanged();//更新数据集，注意如果用adapter.notifyDataSetChanged()将没有动画效果
    }

    public void addDataAt(WeekTar data, int position) {
        mWeekTar.set(position,data);
        notifyItemChanged(position);
    }

    public void rmData(int position) {
        mWeekTar.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return mWeekTar.size();
    }

    public class WeekTar{
        private String con;
        private int pro, wh;

        public String getCon() {
            return con;
        }

        public void setCon(String con) {
            this.con = con;
        }

        public int getPro() {
            return pro;
        }

        public void setPro(int pro) {
            this.pro = pro;
        }

        public int getWh() {
            return wh;
        }

        public void setWh(int wh) {
            this.wh = wh;
        }

        public WeekTar(String con, int pro, int wh) {
            this.con = con;
            this.pro = pro;
            this.wh = wh;
        }
    }
}
