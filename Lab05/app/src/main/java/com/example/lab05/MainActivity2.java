package com.example.lab05;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
public class MainActivity2 extends AppCompatActivity
{
    private static final int REQUEST_VIDEO_CAPTURE = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        // 建立儲存檔案路徑的File物件
        String fileName = "Lab05_myVideo.3gp";
        File file = new File(Environment.getExternalStorageDirectory(), fileName);
    }
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_VIDEO_CAPTURE &&resultCode == Activity.RESULT_OK)
        {
            // 取得傳回媒體儲存的檔案路徑
            String path = data.getData().toString();
            TextView output = findViewById(R.id.showText2);
            output.setText(path); // 顯示路徑
        }
    }
    // Button元件的事件處理
    public void button_Click(View view)
    {
        // 使用Intent物件啟動VideoRecorder活動
        Intent intent = new Intent(this, VideoRecorder.class);
        startActivity(intent);
    }
    public void button2_Click(View view)
    {
        // 建立使用內建程式錄影的Intent物件
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        // 新增附件為儲存的媒體檔案
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        // 指定錄影品質
        //intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
        startActivityForResult(intent, REQUEST_VIDEO_CAPTURE);
    }
    public void button3_Click(View view)
    {
        // 使用Intent物件啟動VideoPlayer活動
        Intent intent = new Intent(this, VideoPlayerActivity.class);
        startActivity(intent);
    }
}
