package com.example.zwy.androidmediahandler;

import android.media.MediaRecorder;
import android.view.SurfaceHolder;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Zhen Weiyu on 2017/8/8.
 */

public class MediaRecord {

    private static volatile MediaRecord instance;

    private MediaRecorder mediaRecorder;

    private MediaRecordListener mediaRecordListener;

    private AtomicBoolean isRecording = new AtomicBoolean(false);

    private MediaRecord(){
        mediaRecorder = new MediaRecorder();
    }

    public static MediaRecord getInstance(){
        if(instance == null){
            synchronized (MediaRecord.class){
                if(instance == null){
                    instance = new MediaRecord();
                }
            }
        }
        return instance;
    }

    /*
       voice record
     */
    public void startVoiceRecord(String desPath){
        if(mediaRecorder == null){
            mediaRecorder = new MediaRecorder();
        }
        if("".equals(desPath)){
            return;
        }
        if(isRecording.get()){
            return;
        }
        File file = new File(desPath);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setOutputFile(file.getAbsolutePath());
        mediaRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
            @Override
            public void onError(MediaRecorder mr, int what, int extra) {
                if(mediaRecordListener != null){
                    mediaRecordListener.onErrorListener(what,extra);
                }
            }
        });
        mediaRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
            @Override
            public void onInfo(MediaRecorder mr, int what, int extra) {
                if(mediaRecordListener != null){
                    mediaRecordListener.onInfoListener(what,extra);
                }
            }
        });
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            isRecording.set(true);
        } catch (IOException e) {
            e.printStackTrace();
            mediaRecorder.release();
            isRecording.set(false);
        }
    }

    public void stopVoiceRecord(){
        if(mediaRecorder!=null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            isRecording.set(false);
        }
    }

    /*
       camera record
     */
    public void startCameraRecord(VideoRecordParam videoRecordParam, SurfaceHolder surfaceHolder){
        if(mediaRecorder == null){
             mediaRecorder = new MediaRecorder();
        }
        if(videoRecordParam == null || surfaceHolder == null){
            throw new InvalidParameterException("param == null");
        }
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mediaRecorder.setVideoSize(videoRecordParam.videoWidthSize, videoRecordParam.videoHeightSize);
        mediaRecorder.setVideoFrameRate(videoRecordParam.frameRate);
        mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
        mediaRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
            @Override
            public void onError(MediaRecorder mr, int what, int extra) {
                if(mediaRecordListener != null){
                    mediaRecordListener.onErrorListener(what,extra);
                }
            }
        });
        mediaRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
            @Override
            public void onInfo(MediaRecorder mr, int what, int extra) {
                if(mediaRecordListener != null){
                    mediaRecordListener.onInfoListener(what,extra);
                }
            }
        });
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            isRecording.set(true);
        } catch (IOException e) {
            e.printStackTrace();
            mediaRecorder.release();
            isRecording.set(false);
        }

    }

    public void stopCameraRecord(){
        if(mediaRecorder!=null){
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            isRecording.set(false);
        }
    }


    public static void release(){
        if(instance != null){
            if(instance.mediaRecorder !=null){
                instance.mediaRecorder.release();
                instance.mediaRecordListener = null;
                instance.mediaRecorder = null;
            }
        }
        instance = null;
    }

    public void setMediaRecordListener(MediaRecordListener listener){
        this.mediaRecordListener = listener;
    }


    public interface MediaRecordListener{

        void onErrorListener(int what, int extra);

        void onInfoListener(int what, int extra);

    }










}
