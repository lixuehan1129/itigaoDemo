package com.example.itigao.Media;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.SeekBar;

import com.example.itigao.R;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;

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
        mSeekBar.setVisibility(INVISIBLE);

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

    @Override
    public GSYBaseVideoPlayer startWindowFullscreen(Context context, boolean actionBar, boolean statusBar) {
        GSYBaseVideoPlayer gsyBaseVideoPlayer = super.startWindowFullscreen(context, actionBar, statusBar);
        if (gsyBaseVideoPlayer != null) {
            StandardGSYVideoPlayer gsyVideoPlayer = (StandardGSYVideoPlayer) gsyBaseVideoPlayer;
            gsyVideoPlayer.setLockClickListener(mLockClickListener);
            gsyVideoPlayer.setNeedLockFull(isNeedLockFull());
            initFullUI(gsyVideoPlayer);
            //比如你自定义了返回案件，但是因为返回按键底层已经设置了返回事件，所以你需要在这里重新增加的逻辑
        }
        return gsyBaseVideoPlayer;
    }

    /**
     * 全屏的UI逻辑
     */
    private void initFullUI(StandardGSYVideoPlayer standardGSYVideoPlayer) {
        standardGSYVideoPlayer.setBottomProgressBarDrawable(null);
    }
}
