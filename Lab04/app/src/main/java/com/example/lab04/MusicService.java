package com.example.lab04;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
public class MusicService extends Service
{
    private MediaPlayer player;
    @Override
    public void onCreate()
    {
        player=MediaPlayer.create(this,R.raw.cantabile);
        player.setOnCompletionListener(listener);
    }
    //監聽
    private final MediaPlayer.OnCompletionListener listener = new MediaPlayer.OnCompletionListener()
    {
        @Override
        public void onCompletion(MediaPlayer nouse)
        {
            try
            {
                player.stop(); //停止播放
                player.prepare();
            }
            catch (Exception ex)
            {
                Log.d("Lab 04", "MusicService Listener: " + ex.getMessage());
            }
        }
    };
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        // 暫停時
        boolean isPause = intent.getBooleanExtra("ISPAUSE", true);
        try
        {
            if (isPause)
            {
                if(player.isPlaying())//播放中
                {
                    player.pause(); //暫停
                }
            }
            else
            {
                //沒有暫停
                player.start(); //開始播放
            }
        }
        catch (Exception ex)
        {
            Log.d("Lab 04", "onStartCommand(): " + ex.getMessage());
        }
        return START_STICKY;
    }
    @Override
    public void onDestroy()
    {
        try
        {
            player.stop(); //停止播放
            player.prepare();
        }
        catch (Exception ex)
        {
            Log.d("Lab 04", "onDestroy(): " + ex.getMessage());
        }
    }
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}
