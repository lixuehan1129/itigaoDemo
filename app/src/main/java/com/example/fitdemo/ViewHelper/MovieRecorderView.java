package com.example.fitdemo.ViewHelper;

import android.content.Context;
import android.content.res.TypedArray;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.LinearLayout;

import com.example.fitdemo.R;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 最美人间四月天 on 2019/1/22.
 */

public class MovieRecorderView extends LinearLayout implements MediaRecorder.OnErrorListener {

    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;


    //屏幕方向
    //手机顶部朝上
    public static final int UP = 0;
    //手机底部朝上
    public static final int DOWN = 1;
    //手机左边朝上
    public static final int LEFT = 2;
    //手机右边朝上
    public static final int RIGHT = 3;

    private int orientation = UP;

    private MediaRecorder mMediaRecorder;
    private Camera mCamera;
    private Timer mTimer;// 计时器
    private OnRecordFinishListener mOnRecordFinishListener;// 录制完成回调接口

    private int mWidth;// 视频分辨率宽度
    private int mHeight;// 视频分辨率高度
    private boolean isOpenCamera;// 是否一开始就打开摄像头
    private int mRecordMaxTime;// 一次拍摄最长时间
    private int mTimeCount;// 时间计数
    private File mRecordFile = null;// 文件
    private final int FRONT_CAMERA = 0; //前置摄像头
    private final int BACK_CAMERA = 1;  //后置摄像头
    private int cameraType = BACK_CAMERA;


    public MovieRecorderView(Context context) {
        this(context, null);
    }

    public MovieRecorderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MovieRecorderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // 初始化各项组件
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MovieRecorderView, defStyle, 0);
        mWidth = a.getInteger(R.styleable.MovieRecorderView_mWidth, 320);// 默认320
        mHeight = a.getInteger(R.styleable.MovieRecorderView_mHeight, 240);// 默认240

        isOpenCamera = a.getBoolean(R.styleable.MovieRecorderView_is_open_camera, true);// 默认打开
        mRecordMaxTime = a.getInteger(R.styleable.MovieRecorderView_record_max_time, 10);// 默认为10

        LayoutInflater.from(context).inflate(R.layout.movie_recorder_view, this);
        mSurfaceView = (SurfaceView) findViewById(R.id.recorder_surfaceView);

        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(new CustomCallBack());
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        a.recycle();
    }

    /**
     *
     */
    private class CustomCallBack implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (!isOpenCamera)
                return;
            try {
                initCamera();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (!isOpenCamera)
                return;
            freeCameraResource();
        }

    }

    /**
     * 初始化摄像头
     *
     * @date 2015-2-5
     * @throws IOException
     */
    @SuppressWarnings("deprecation")
    public void initCamera() throws IOException {
        if (mCamera != null) {
            freeCameraResource();
        }

        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();// 得到摄像头的个数
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraType == FRONT_CAMERA) {
                //切换成前置
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    //释放资源
                    freeCameraResource();
                    try {
                        mCamera = Camera.open(camIdx);
                        if (mCamera == null)
                            return;
                        setCameraParams();
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                        freeCameraResource();
                    }
                }
            }else{
                //切换成后置
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    //释放资源
                    freeCameraResource();
                    try {
                        mCamera = Camera.open(camIdx);
                        if (mCamera == null)
                            return;
                        setCameraParams();
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                        freeCameraResource();
                    }
                }
            }
        }
//        try {
//            mCamera = Camera.open();
//        } catch (Exception e) {
//            e.printStackTrace();
//            freeCameraResource();
//        }
//        if (mCamera == null)
//            return;
//
//        setCameraParams();

    }

    /**
     * 设置摄像头属性
     */
    @SuppressWarnings("deprecation")
    public void setCameraParams() {
        try {
            if (mCamera != null) {
                Camera.Parameters params = mCamera.getParameters();
//              params.set("orientation", "portrait");
                setPreviewSize(params);
                mCamera.setParameters(params);
            }
            //设置预览竖屏
            mCamera.setDisplayOrientation(90);
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();
            mCamera.unlock();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 切换前后摄像头
     */
    @SuppressWarnings("deprecation")
    public void toggleCamera(){

        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();// 得到摄像头的个数


        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraType == BACK_CAMERA) {
                //切换成前置
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    //释放资源
                    freeCameraResource();
                    try {
                        mCamera = Camera.open(camIdx);
                        cameraType = FRONT_CAMERA;
                        if (mCamera == null)
                            return;
                        setCameraParams();
                        return;
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                        freeCameraResource();
                    }
                }
            }else{
                //切换成后置
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    //释放资源
                    freeCameraResource();
                    try {
                        mCamera = Camera.open(camIdx);
                        cameraType = BACK_CAMERA;
                        if (mCamera == null)
                            return;
                        setCameraParams();
                        return;
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                        freeCameraResource();
                    }
                }
            }
        }
    }


    /**
     * 释放摄像头资源
     *
     */
    private void freeCameraResource() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.lock();
            mCamera.release();
            mCamera = null;
        }
    }

    private void createRecordDir() {
        File sampleDir = new File(Environment.getExternalStorageDirectory() + File.separator + "com.example.fitdemo/video/");
        if (!sampleDir.exists()) {
            sampleDir.mkdirs();
        }
        File vecordDir = sampleDir;
        // 创建文件
        try {
            mRecordFile = File.createTempFile("recording", ".mp4", vecordDir); //mp4格式
            Log.i("TAG",mRecordFile.getAbsolutePath());
        } catch (IOException e) {
        }
    }

    /**
     * 初始化
     *
     * @throws IOException
     */
    private void initRecord() throws IOException {
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.reset();
        if (mCamera != null)
            mMediaRecorder.setCamera(mCamera);
        mMediaRecorder.setOnErrorListener(this);
        mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);// 视频源
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 音频源
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);// 视频输出格式
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);// 音频格式
//        mMediaRecorder.setVideoFrameRate(16);// 去掉这一行，有些手机会mMediaRecorder.start()失败
        mMediaRecorder.setVideoEncodingBitRate(1 * 1280 * 720);// 设置帧频率，然后就清晰了

        //输出角度
        switch (orientation) {
            case UP:
                mMediaRecorder.setOrientationHint(90);// 输出旋转90度，顶部朝上录制
                break;
            case DOWN:
                mMediaRecorder.setOrientationHint(270);
                break;
            case LEFT:
                mMediaRecorder.setOrientationHint(0);
                break;
            case RIGHT:
                mMediaRecorder.setOrientationHint(180);
                break;
        }

        mMediaRecorder.setVideoSize(mWidth, mHeight);// 设置分辨率：
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);// 视频录制格式

        // mediaRecorder.setMaxDuration(Constant.MAXVEDIOTIME * 1000);

        mMediaRecorder.setOutputFile(mRecordFile.getAbsolutePath());
        mMediaRecorder.prepare();
        try {
            mMediaRecorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始录制视频
     *
     * @param onRecordFinishListener
     *            达到指定时间之后回调接口
     */
    public void record(final OnRecordFinishListener onRecordFinishListener) {
        this.mOnRecordFinishListener = onRecordFinishListener;
        createRecordDir();
        try {
            if (!isOpenCamera)// 如果未打开摄像头，则打开
                initCamera();
            initRecord();
            mTimeCount = 0;// 时间计数器重新赋值
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {

                @Override
                public void run() {
                    mTimeCount++;
//                    mProgressBar.setProgress(mTimeCount);
                    // 设置进度
                    if (mOnRecordFinishListener != null)
                        mOnRecordFinishListener.onRecord(mTimeCount);
                    if (mTimeCount == mRecordMaxTime) {// 达到指定时间，停止拍摄
                        stop();
                        if (mOnRecordFinishListener != null)
                            mOnRecordFinishListener.onRecordFinish();
                    }
                }
            }, 0, 1000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止拍摄
     *
     * @author liuyinjun
     * @date 2015-2-5
     */
    public void stop() {
        stopRecord();
        releaseRecord();
        freeCameraResource();
    }

    /**
     * 停止录制
     *
     * @author liuyinjun
     * @date 2015-2-5
     */
    public void stopRecord() {
//      mProgressBar.setProgress(0);
        if (mTimer != null)
            mTimer.cancel();
        if (mMediaRecorder != null) {
            // 设置后不会崩
            mMediaRecorder.setOnErrorListener(null);
            try {
                mMediaRecorder.stop();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (RuntimeException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mMediaRecorder.setPreviewDisplay(null);
        }
    }

    /**
     * 释放资源
     *
     * @author liuyinjun
     * @date 2015-2-5
     */
    private void releaseRecord() {
        if (mMediaRecorder != null) {
            mMediaRecorder.setOnErrorListener(null);
            try {
                mMediaRecorder.release();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mMediaRecorder = null;
    }

    public int getTimeCount() {
        return mTimeCount;
    }

    /**
     * @return the mVecordFile
     */
    public File getmRecordFile() {
        return mRecordFile;
    }

    /**
     * 录制完成回调接口
     *
     * @author liuyinjun
     *
     * @date 2015-2-5
     */
    public interface OnRecordFinishListener {
        public void onRecordFinish();
        void onRecord(int progress);
    }

    /**
     * 设置最大录制时间
     * @param max
     */
    public void setMaxTime(int max){
        mRecordMaxTime = max;
    }


    @Override
    public void onError(MediaRecorder mr, int what, int extra) {
        try {
            if (mr != null)
                mr.reset();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 根据手机支持的视频分辨率，设置预览尺寸
     *
     * @param params
     */
    @SuppressWarnings("deprecation")
    private void setPreviewSize(Camera.Parameters params) {
        if (mCamera == null) {
            return;
        }
        //获取手机支持的分辨率集合，并以宽度为基准降序排序
        List<Camera.Size> previewSizes = params.getSupportedPreviewSizes();
        Collections.sort(previewSizes, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size lhs, Camera.Size rhs) {
                if (lhs.width > rhs.width) {
                    return -1;
                } else if (lhs.width == rhs.width) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });

        float tmp = 0f;
        float minDiff = 100f;
        float ratio = 3.0f / 4.0f;// 高宽比率3:4，且最接近屏幕宽度的分辨率，可以自己选择合适的想要的分辨率
        Camera.Size best = null;
        for (Camera.Size s : previewSizes) {
            tmp = Math.abs(((float) s.height / (float) s.width) - ratio);
//            LogUtil.e(LOG_TAG,"tmp:" + tmp);
//            if (tmp == 0 && getWindowWidth(getContext()) <= s.width) {
//              break;
//          }
            if (tmp < minDiff) {
                minDiff = tmp;
                best = s;
                Log.e("屏幕", "setPreviewSize: width:" + s.width + "...height:" + s.height);
            }
        }
//        List<int[]> range = params.getSupportedPreviewFpsRange();
//        int[] fps = range.get(0);
//        LogUtil.e(LOG_TAG,"min="+fps[0]+",max="+fps[1]);
//        params.setPreviewFpsRange(3,7);

        params.setPreviewSize(best.width, best.height);//预览比率

//        params.setPictureSize(480, 720);//拍照保存比率

//        Log.e(LOG_TAG, "setPreviewSize BestSize: width:" + best.width + "...height:" + best.height);

        // 大部分手机支持的预览尺寸和录制尺寸是一样的，也有特例，有些手机获取不到，那就把设置录制尺寸放到设置预览的方法里面
        if (params.getSupportedVideoSizes() == null || params.getSupportedVideoSizes().size() == 0) {
            mWidth = best.width;
            mHeight = best.height;
        } else {
            setVideoSize(params);
        }
    }

    /**
     * 根据手机支持的视频分辨率，设置录制尺寸
     *
     * @param params
     */
    @SuppressWarnings("deprecation")
    private void setVideoSize(Camera.Parameters params) {
        if (mCamera == null) {
            return;
        }
        //获取手机支持的分辨率集合，并以宽度为基准降序排序
        List<Camera.Size> previewSizes = params.getSupportedVideoSizes();
        Collections.sort(previewSizes, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size lhs, Camera.Size rhs) {
                if (lhs.width > rhs.width) {
                    return -1;
                } else if (lhs.width == rhs.width) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });

        float tmp = 0f;
        float minDiff = 100f;
        float ratio = 720.0f / 1080.0f;//高宽比率3:4，且最接近屏幕宽度的分辨率
        Camera.Size best = null;
        for (Camera.Size s : previewSizes) {
            tmp = Math.abs(((float) s.height / (float) s.width) - ratio);
//            if (tmp == 0 && getWindowWidth(getContext()) <= s.width) {
//              break;
//          }
            if (tmp < minDiff) {
                minDiff = tmp;
                best = s;
                Log.e("摄像头", "setVideoSize: width:" + s.width + "...height:" + s.height);
            }
        }
//        Log.e(LOG_TAG, "setVideoSize BestSize: width:" + best.width + "...height:" + best.height);
        //设置录制尺寸
        mWidth = best.width;
        mHeight = best.height;
    }

    public void setOrientation(int orientation){
        this.orientation = orientation;
    }
}

