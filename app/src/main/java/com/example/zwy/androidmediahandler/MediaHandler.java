package com.example.zwy.androidmediahandler;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by Zhen Weiyu on 2017/5/25.
 */

public class MediaHandler {

    private File videoFile;

    private File audioFile;

    private static volatile MediaHandler instance;


    private MediaHandler() {

    }

    public static MediaHandler getInstance() {
        if (instance == null) {
            synchronized (MediaHandler.class) {
                if (instance == null) {
                    instance = new MediaHandler();
                }
            }
        }
        return instance;
    }


    public void extractVideo(String inputPath, String videoPath, String audioPath) {
        videoFile = new File(videoPath);
        audioFile = new File(audioPath);
        FileOutputStream videoOutputStream = null;
        FileOutputStream audioOutputStream = null;

        MediaExtractor mediaExtractor = new MediaExtractor();
        try {
            videoOutputStream = new FileOutputStream(videoFile);
            audioOutputStream = new FileOutputStream(audioFile);
            mediaExtractor.setDataSource(inputPath);
            int trackCount = mediaExtractor.getTrackCount();
            int audioTrackIndex = -1;
            int videoTrackIndex = -1;
            for (int i = 0; i < trackCount; i++) {
                MediaFormat trackFormat = mediaExtractor.getTrackFormat(i);
                String mineType = trackFormat.getString(MediaFormat.KEY_MIME);
                //video track
                if (mineType.startsWith("video/")) {
                    videoTrackIndex = i;
                }
                //audio track
                if (mineType.startsWith("audio/")) {
                    audioTrackIndex = i;
                }
            }
            ByteBuffer byteBuffer = ByteBuffer.allocate(500 * 1024);
            if(videoTrackIndex!=-1) {
                mediaExtractor.selectTrack(videoTrackIndex);
                writeByteToFile(mediaExtractor, videoOutputStream, byteBuffer);
            }
            if(audioTrackIndex!=-1) {
                mediaExtractor.selectTrack(audioTrackIndex);
                writeByteToFile(mediaExtractor, audioOutputStream, byteBuffer);
            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            mediaExtractor.release();
            if (audioOutputStream != null) {
                try {
                    audioOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (videoOutputStream != null) {
                try {
                    videoOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    private void combineVideo(String videoPath, String audioPath, String desPath) {
//        try {
//            MediaExtractor videoExtractor = new MediaExtractor();
//            videoExtractor.setDataSource(videoPath);
//            int videoTrackCount = videoExtractor.getTrackCount();
//            int videoTrackIndex = -1;
//            MediaFormat videoFormat = null;
//            for (int i = 0; i < videoTrackCount; i++) {
//                videoFormat = videoExtractor.getTrackFormat(i);
//                String mimeType = videoFormat.getString(MediaFormat.KEY_MIME);
//                if (mimeType.startsWith("video/")) {
//                    videoTrackIndex = i;
//                    break;
//                }
//            }
//            MediaExtractor audioExtractor = new MediaExtractor();
//            audioExtractor.setDataSource(audioPath);
//            int audioTrackIndex = -1;
//            MediaFormat audioFormat = null;
//            for (int j = 0; j < audioTrackIndex; j++) {
//                audioFormat = audioExtractor.getTrackFormat(j);
//                String mimeType = audioFormat.getString(MediaFormat.KEY_MIME);
//                if (mimeType.startsWith("audio/")) {
//                    audioTrackIndex = j;
//                    break;
//                }
//            }
//
//            videoExtractor.selectTrack(videoTrackIndex);
//            audioExtractor.selectTrack(audioTrackIndex);
//
//            MediaCodec.BufferInfo videoBufferInfo = new MediaCodec.BufferInfo();
//            MediaCodec.BufferInfo audioBufferInfo = new MediaCodec.BufferInfo();
//
//            MediaMuxer mediaMuxer = new MediaMuxer(desPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
//
//            int writeVideoTrackIndex = mediaMuxer.addTrack(videoFormat);
//            int writeAudioTrackIndex = mediaMuxer.addTrack(audioFormat);
//            mediaMuxer.start();
//
//            boolean sawEOS = false;
//            int frameCount = 0;
//            int offset = 100;
//            int sampleSize = 256 * 1024;
//            ByteBuffer videoBuf = ByteBuffer.allocate(sampleSize);
//            ByteBuffer audioBuf = ByteBuffer.allocate(sampleSize);
//
//            while (!sawEOS) {
//                videoBufferInfo.offset = offset;
//                videoBufferInfo.size = videoExtractor.readSampleData(videoBuf, offset);
//
//
//                if (videoBufferInfo.size < 0 || audioBufferInfo.size < 0) {
//                    // Log.d(TAG, "saw input EOS.");
//                    sawEOS = true;
//                    videoBufferInfo.size = 0;
//
//                } else {
//                    videoBufferInfo.presentationTimeUs = videoExtractor.getSampleTime();
//                    videoBufferInfo.flags = videoExtractor.getSampleFlags();
//                    mediaMuxer.writeSampleData(writeVideoTrackIndex, videoBuf, videoBufferInfo);
//                    videoExtractor.advance();
//                    frameCount++;
//
//                }
//            }
//
//        } catch (Exception e) {
//
//        }
//
//
//    }


    private void writeByteToFile(MediaExtractor mediaExtractor, FileOutputStream fileOutputStream, ByteBuffer byteBuffer) throws IOException {
        for (; ; ) {
            int readDataLength = mediaExtractor.readSampleData(byteBuffer, 0);
            if (readDataLength < 0) {
                break;
            }
            byte[] bytes = new byte[readDataLength];
            byteBuffer.get(bytes);
            fileOutputStream.write(bytes);
            byteBuffer.clear();
            mediaExtractor.advance();
        }
    }


}
