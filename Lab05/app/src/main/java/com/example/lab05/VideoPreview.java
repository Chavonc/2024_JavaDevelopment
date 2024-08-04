package com.example.lab05;
import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class VideoPreview extends SurfaceView implements SurfaceHolder.Callback
{
    private MediaRecorder recorder;
    public VideoPreview(Context context) {super(context);}
    public VideoPreview(Context context, MediaRecorder recorder)
    {
        super(context);
        this.recorder = recorder;
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
    }
    // 實作SurfaceHolder.Callback介面方法
    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        // 指定預覽檢視
        recorder.setPreviewDisplay(holder.getSurface());
        // 指定輸出檔案路徑
        recorder.setOutputFile(Environment.getExternalStorageDirectory().getPath()+"/myVideo.3gp");
        try
        {
            recorder.prepare(); // 準備錄影
            recorder.start();   // 開始錄影
        }
        catch (Exception e)
        {
            e.printStackTrace();
            recorder.release();
            recorder = null;
        }
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        if (recorder != null)
        {
            // 釋放MediaRecorder物件佔用的資源
            recorder.release();
            recorder = null;
        }
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,int height)
    {
    }
}
