package com.example.lab05;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class VideoPlayerActivity extends AppCompatActivity
{
    VideoView videoview;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        // 取得VideoView元件
        videoview = findViewById(R.id.videoView);
        // 建立MediaController物件
        MediaController mc = new MediaController(this);
        videoview.setMediaController(mc); // 指定控制物件
        // 指定媒體檔案的播放路徑的URI
        videoview.setVideoURI(Uri.parse(Environment.getExternalStorageDirectory().getPath()+"/lab05_myVideo.3gp"));
        // 指定元件取得焦點
        videoview.requestFocus();
    }
}
