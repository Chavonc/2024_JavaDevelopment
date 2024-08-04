package com.example.lab04;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;

public class MainActivity extends AppCompatActivity
{
    private TextView view1;//output
    //video
    private boolean isPlaying = false;
    private VideoView video;
    //recording
    private boolean isRecording = false;
    File filePath;
    private MediaRecorder recorder;
    private Button startRecording,stopRecording;
    private static final int RECORD_AUDIO_PERMISSION_CODE = 1;
    private static final int WRITE_EXTERNAL_STORAGE_PERMISSION_CODE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //for music
        view1=findViewById(R.id.showText);
        //for video
        video=findViewById(R.id.videoView);
        video.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/raw/cantabile_video"));
        video.setMediaController(new MediaController(this));
        //for recording
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO_PERMISSION_CODE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_PERMISSION_CODE);
        }
        startRecording=findViewById(R.id.StartRecording);
        stopRecording=findViewById(R.id.StopRecording);
        recorder=new MediaRecorder();
        filePath = new File(Environment.getExternalStorageDirectory(), "myLabRecord.3gp");
    }
    //重設錄音機
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void resetRecorder()
    {
        File directory = getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        if (directory != null)
        {
            File file = new File(directory, "myLabRecord.3gp");
            filePath = new File(file.getAbsolutePath());
            recorder.setOutputFile(filePath);
        }
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        recorder.setOutputFile(filePath.getAbsolutePath());
    }
    //音樂播放Music
    public void musicStart(View view)
    {
        Intent intent = new Intent(this, MusicService.class);
        if(!isPlaying)
        {
            intent.putExtra("ISPAUSE", false); // 不是暫停
            startService(intent);
            isPlaying=true;
            view1.setText("音樂播放中...");
        }
        else
        {
            intent.putExtra("ISPAUSE", true);
            startService(intent);
            isPlaying = false;
            view1.setText("已暂停音樂");
        }
    }
    public void musicStop(View view)
    {
        Intent intent = new Intent(this, MusicService.class);
        stopService(intent);
        isPlaying=false;
        view1.setText("結束播放音樂");
    }
    //視訊播放Video
    public void videoControl(View view)
    {
        if(video.isPlaying())
        {
            video.pause();
            view1.setText("已暫停視訊");
        }
        else
        {
            video.start();
            view1.setText("視訊播放中...");
        }
    }
    @Override
    public void onStop()
    {
        super.onStop();
        video.stopPlayback();
    }
    //錄音Recording
    @SuppressLint("NewApi")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RECORD_AUDIO_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "已授權錄音權限", Toast.LENGTH_SHORT).show();
                resetRecorder();
            }
            else
            {
                Toast.makeText(this, "拒絕授權錄音權限", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == WRITE_EXTERNAL_STORAGE_PERMISSION_CODE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "已授權存儲權限", Toast.LENGTH_SHORT).show();
                resetRecorder();
            }
            else
            {
                Toast.makeText(this, "拒絕授權存儲權限", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @SuppressLint("NewApi")
    public void recordingStart(View view)
    {
        if(!isRecording)
        {
            resetRecorder();
            try
            {
                recorder.prepare();
                recorder.start(); // 開始錄音
                isRecording=true;
                view1.setText("開始錄音....");
                startRecording.setEnabled(false);
                stopRecording.setEnabled(true);
            }
            catch (Exception ex)
            {
                Log.d("Lab 04", "Error in recordingStart " + ex.getMessage());
            }
        }
    }
    public void recordingStop(View view)
    {
        if(isRecording)
        {
            try
            {
                recorder.stop();//停止錄音
                recorder.reset();
                recorder.release();
                recorder = null;
                isRecording=false;
                view1.setText("已停止錄音並保存");
                startRecording.setEnabled(true);
                stopRecording.setEnabled(false);
                Log.d("Lab 04", "Recording file path: " + filePath);
            }
            catch (Exception ex)
            {
                Log.d("Lab 04", "Error in recordingStop " + ex.getMessage());
            }
        }
    }
    //Draw2D
    public static class Draw2D extends View
    {
        public Draw2D(Context context)
        {
            super(context);
        }
        @Override
        protected void onDraw(Canvas canvas)
        {
            super.onDraw(canvas);
            // 建立Paint物件
            @SuppressLint("DrawAllocation") Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            // 在整個Canvas物件的背景填滿色彩
            paint.setColor(Color.WHITE);
            canvas.drawPaint(paint);
            //畫出文字內容(Lab04個人報告)
            paint.setColor(Color.BLUE);
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true);
            paint.setTextSize(80);
            canvas.drawText("Lab04個人報告", 310, 80, paint);
            //畫圓
            paint.setAntiAlias(true);
            paint.setColor(Color.GREEN);
            canvas.drawCircle(420, 300, 50, paint);
            //畫長方形(𣄃子)
            paint.setColor(Color.GREEN);
            canvas.drawRect(400, 200, 450, 250, paint);//左上角,右下角
            //畫長方形(底部)
            paint.setColor(Color.GREEN);
            canvas.drawRect(350, 300, 500, 350, paint);//左上角,右下角


        }
    }
    public void drawing(View view)
    {
        Draw2D d= new Draw2D(this);
        setContentView(d);
    }
}
