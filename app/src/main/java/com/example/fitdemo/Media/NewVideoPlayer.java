package com.example.fitdemo.Media;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;

import com.example.fitdemo.R;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

/**
 * Created by 最美人间四月天 on 2019/1/11.
 */

public class NewVideoPlayer extends StandardGSYVideoPlayer {


    SeekBar mSeekBar;

    public NewVideoPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public NewVideoPlayer(Context context) {
        super(context);
    }

    public NewVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        mSeekBar = (SeekBar) findViewById(R.id.progress);
        mSeekBar.setEnabled(false);

    }


    @Override
    public int getLayoutId() {
        return R.layout.new_video_player;
    }



    @Override
    protected void touchSurfaceMoveFullLogic(float absDeltaX, float absDeltaY) {
        super.touchSurfaceMoveFullLogic(absDeltaX, absDeltaY);
        //不给触摸快进，如果需要，屏蔽下方代码即可
        mChangePosition = false;

        //不给触摸音量，如果需要，屏蔽下方代码即可
       // mChangeVolume = false;

        //不给触摸亮度，如果需要，屏蔽下方代码即可
       // mBrightness = false;
    }
}
