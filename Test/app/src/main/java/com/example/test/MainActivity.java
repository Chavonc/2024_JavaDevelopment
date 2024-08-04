package com.example.test;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.test.databinding.ActivityMainBinding;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity
{
    private ActivityMainBinding binding;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    EditText account,pwd;
    String mysql_ip = "10.0.2.2";
    int mysql_port = 3306; // Port 預設為 3306
    String db_name = "embeded";
    String urlGetDBParameters = "autoReconnect=false&useSSL=false";
    String url = "jdbc:mysql://"+mysql_ip+":"+mysql_port+"/"+db_name;
    String db_user = "root";
    String db_password = "Yejiching8";
    public Connection CONN()
    {
        Connection con = null;
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            Log.v("DB","加載驅動成功");
        }catch( ClassNotFoundException e)
        {
            Log.e("DB","加載驅動失敗");
        }
        // 連接資料庫
        try
        {
            // 解開一些複雜的限制，主要是遠許可以直接從UI或主要thread中連接資料庫。
            // 馬的超煩
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            con = DriverManager.getConnection(url,db_user,db_password);
            Log.v("DB","遠端連接成功");
        }
        catch(SQLException e)
        {
            Log.e("DB",url+"遠端連接失敗");
            Log.e("DB", e.toString());
        }
        return con;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        createNotificationChannel();
        setContentView(R.layout.activity_main);
        Button signin_btn=findViewById(R.id.btn_signin);
        Button signup_btn=findViewById(R.id.btn_to_signuppage);
        Button forgetpw_btn=findViewById(R.id.btn_to_forgotpage);
        signin_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                account = findViewById(R.id.input_email);
                pwd = findViewById(R.id.input_password);
                if (account.getText().toString().matches("") || pwd.getText().toString().matches(""))
                {
                    Toast.makeText(MainActivity.this, "請輸入帳號密碼!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(account.getText().toString()).matches())
                    {
                        Toast.makeText(MainActivity.this, "Email格式錯誤", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int[] exist = new int[1];
                    // 連線
                    try
                    {
                        // 連線
                        Connection con = CONN();
                        if (con == null)
                        {
                            Log.e("Failed to Connection", "Please check your connection");
                        }
                        else
                        {
                            String sql = "SELECT `userid` FROM `user` WHERE `account`= ? AND `pwd` = ?";
                            try (PreparedStatement pstmt = con.prepareStatement(sql))
                            {
                                pstmt.setString(1, account.getText().toString());
                                pstmt.setString(2, pwd.getText().toString());
                                System.out.println(pstmt);
                                // 將執行的結果存到rs裡面
                                try (ResultSet rs = pstmt.executeQuery())
                                {
                                    if (rs.next())
                                    {
                                        exist[0] = rs.getInt(1);
                                    }
                                }
                            }
                            // 關閉連線
                            con.close();
                        }
                    }
                    catch (Exception ex)
                    {
                        Log.e("Failed to Connection", "Please check your connection");
                    }
                    if (exist[0] == 0)
                    {
                        Toast.makeText(MainActivity.this, "帳號或密碼錯誤!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else
                    {
                        setAlarm();
                        Intent intent = new Intent(MainActivity.this, FunctionActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("UserId", exist[0]);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
            }
        });
        signup_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
        forgetpw_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(MainActivity.this,ForgotPassword.class);
                startActivity(intent);
            }
        });
    }
    private void createNotificationChannel()
    {
        CharSequence name = "foxandroidReminderChannel";
        String description = "Channel For Alarm Manager";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel("foxandroid", name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
    private void setAlarm()
    {
        Calendar calendar = Calendar.getInstance();
        System.out.println(calendar.getTime());
        calendar.set(Calendar.HOUR_OF_DAY, 22);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S)
        {
            pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        }
        else
        {
            pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        }
        //alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        //alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), calendar.getTimeInMillis(), pendingIntent);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 2*60*1000, pendingIntent);//設了每2分鐘提醒一次
        System.out.println("Setting Alarm Successful");
        Toast.makeText(this, "Alarm setting", Toast.LENGTH_LONG).show();
    }
}