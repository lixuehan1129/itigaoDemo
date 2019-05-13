package com.example.itigao.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.itigao.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class VideoRankAdapter extends RecyclerView.Adapter<VideoRankAdapter.ViewHolder> {

    private Context mContext;
    private List<Rank> ranks;

    class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView imageView;
        TextView name,num;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (CircleImageView) itemView.findViewById(R.id.video_rank_item_iv);
            name = (TextView) itemView.findViewById(R.id.video_rank_item_name);
            num = (TextView) itemView.findViewById(R.id.video_rank_item_num);
        }
    }

    @Override
    public VideoRankAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_rank_item, parent, false);
        mContext = parent.getContext();
        final RecyclerView.ViewHolder vh = new VideoRankAdapter.ViewHolder(v);
        return (ViewHolder) vh;
    }

    public VideoRankAdapter(List<Rank> data){
        ranks = data;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final VideoRankAdapter.ViewHolder holder, int position) {

        Rank rank = ranks.get(position);
        if(rank.getRankName().equals("我")){
            holder.num.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
            holder.num.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }
        holder.name.setText(rank.getRankName());
        holder.num.setText(rank.getRankNum());
        if(rank.getRankImage() != null){
            Glide.with(mContext)
                    .load(rank.getRankImage())
                    .asBitmap()  //不可加载动图
                    .dontAnimate()//取消淡入淡出动画
                    .placeholder(R.mipmap.ic_touxiang21)
                    .error(R.mipmap.ic_touxiang21)
                    .thumbnail(0.1f) //先加载十分之一作为缩略图
                    .into(holder.imageView);
        }else {
            holder.imageView.setImageResource(R.mipmap.ic_touxiang21);
        }
    }

    @Override
    public int getItemCount() {
        return ranks.size();
    }

    public void addDataAt(List<Rank> data) {
        ranks = data;
        notifyDataSetChanged();
    }


    public class Rank{
        private String rankImage;
        private String rankName;
        private String rankNum;

        public String getRankImage() {
            return rankImage;
        }

        public void setRankImage(String rankImage) {
            this.rankImage = rankImage;
        }

        public String getRankName() {
            return rankName;
        }

        public void setRankName(String rankName) {
            this.rankName = rankName;
        }

        public String getRankNum() {
            return rankNum;
        }

        public void setRankNum(String rankNum) {
            this.rankNum = rankNum;
        }

        public Rank(String rankImage, String rankName, String rankNum) {
            this.rankImage = rankImage;
            this.rankName = rankName;
            this.rankNum = rankNum;
        }
    }
}
