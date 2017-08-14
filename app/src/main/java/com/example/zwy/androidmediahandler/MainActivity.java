package com.example.zwy.androidmediahandler;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private TextView tvFilePath;

    private String filePath = "";

    private static final int REQUEST_RECORD_AUDIO_CODE = 0X123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvFilePath = (TextView) findViewById(R.id.tv_file_path);
        filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "media_demo.acc";
        tvFilePath.setText(filePath);
    }

    public void startRecordVoice(View view) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                MediaRecord.getInstance().startVoiceRecord(filePath);
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {
                    Toast.makeText(this, "录音权限对程序运行很重要!", Toast.LENGTH_SHORT).show();
                } else {
                    requestPermissions(new String[]{android.Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_CODE);
                }
            }
        } else {
            MediaRecord.getInstance().startVoiceRecord(filePath);
        }

    }

    public void stopRecordVoice(View view) {
        MediaRecord.getInstance().stopVoiceRecord();
    }

    public void startPlayVoice(View view) {
        MediaPlay.getInstance().startPlayVoice(filePath);
    }

    public void stopPlayVoice(View view) {
        MediaPlay.getInstance().stopPlayVoice();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_RECORD_AUDIO_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                MediaRecord.getInstance().startVoiceRecord(filePath);
            } else {
                Toast.makeText(this, "用户拒绝了权限", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void extractVideo(View view) {
        final String inputPath = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"20160406.mp4";
        File file = new File(inputPath);
        if(!file.exists()){
            Toast.makeText(this,"file is not exist!",Toast.LENGTH_SHORT).show();
            return;
        }
        final String audioPath = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"extract_audio.aac";
        final String videoPath = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"extract_video.wav";
        new Thread(new Runnable() {
            @Override
            public void run() {
                MediaHandler.getInstance().extractVideo(inputPath,videoPath,audioPath);
            }
        }).start();

    }
}
