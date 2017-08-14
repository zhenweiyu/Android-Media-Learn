package com.example.zwy.androidmediahandler;

import android.os.Environment;

import java.io.File;

/**
 * Created by Zhen Weiyu on 2017/8/8.
 */

public class VideoRecordParam {

    public int videoWidthSize = 480;

    public int videoHeightSize = 800;

    public int frameRate = 50;

    public String outputFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
            File.separator + "recordVideo" + System.currentTimeMillis() + ".acc";


    private VideoRecordParam(Builder builder) {
        videoWidthSize = builder.videoWidthSize;
        videoHeightSize = builder.videoHeightSize;
        frameRate = builder.frameRate;
        outputFilePath = builder.outputFilePath;
    }

    public static Builder newBuilder() {
        return new Builder();
    }


    public static final class Builder {
        private int videoWidthSize;
        private int videoHeightSize;
        private int frameRate;
        private String outputFilePath;

        private Builder() {
        }

        public Builder videoWidthSize(int val) {
            videoWidthSize = val;
            return this;
        }

        public Builder videoHeightSize(int val) {
            videoHeightSize = val;
            return this;
        }

        public Builder frameRate(int val) {
            frameRate = val;
            return this;
        }

        public Builder outputFilePath(String val) {
            outputFilePath = val;
            return this;
        }

        public VideoRecordParam build() {
            return new VideoRecordParam(this);
        }
    }
}
