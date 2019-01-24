package com.example.fitdemo.okHttp;

/**
 * Created by 最美人间四月天 on 2019/1/24.
 */

public interface ProgressListener {
    //已完成的 总的文件长度 是否完成
    void onProgress(long currentBytes, long contentLength, boolean done);
}
