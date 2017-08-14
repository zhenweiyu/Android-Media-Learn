package com.example.zwy.androidmediahandler;

import android.media.MediaPlayer;

import java.io.File;
import java.io.IOException;
import java.io.PipedReader;

/**
 * Created by Zhen Weiyu on 2017/8/8.
 */

public class MediaPlay {

    private static volatile MediaPlay instance;

    private MediaPlayer mediaPlayer;

    private MediaPlayListener mediaPlayListener;

    private MediaPlay(){
        mediaPlayer = new MediaPlayer();
    }

    public static MediaPlay getInstance(){
        if(instance == null){
            synchronized (MediaPlay.class){
                if(instance == null){
                    instance = new MediaPlay();
                }
            }
        }
        return instance;
    }

    public void startPlayVoice(String sourcePath){
        if(mediaPlayer == null){
            mediaPlayer = new MediaPlayer();
        }
        if("".equals(sourcePath)){
            return;
        }
        File file = new File(sourcePath);
        if(!file.exists()){
            return;
        }
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
        try {
            mediaPlayer.setDataSource(file.getAbsolutePath());
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    if(mediaPlayListener != null){
                        mediaPlayListener.onErrorListener(what,extra);
                    }
                    return false;
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if(mediaPlayListener != null){
                        mediaPlayListener.onCompletionListener();
                    }
                }
            });
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void stopPlayVoice(){
        if(mediaPlayer != null){
            if(mediaPlayer.isPlaying()){
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }
    }

    public static void release(){
        if(instance!=null){
            if(instance.mediaPlayer!=null){
                instance.mediaPlayer.release();
                instance.mediaPlayer = null;
                instance.mediaPlayListener = null;
            }
        }
        instance = null;
    }

    public void setMediaPlayListener(MediaPlayListener listener){
        this.mediaPlayListener = listener;
    }


    public interface MediaPlayListener{

        void onErrorListener(int what, int extra);

        void onCompletionListener();
    }



}
