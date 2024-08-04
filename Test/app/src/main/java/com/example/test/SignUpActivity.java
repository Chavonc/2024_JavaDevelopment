package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SignUpActivity extends AppCompatActivity
{
    EditText name,account,pwd;
    String mysql_ip = "10.0.2.2";
    int mysql_port = 3306; // Port 預設為 3306
    String db_name = "embeded";
    String urlGetDBParameters = "autoReconnect=false&useSSL=false";
    String url = "jdbc:mysql://"+mysql_ip+":"+mysql_port+"/"+db_name;
    String db_user = "root";
    String db_password = "Yejiching8";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Button signin_btn=findViewById(R.id.btn_to_signinpage);
        Button signup_btn=findViewById(R.id.btn_signup);
        signin_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(SignUpActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        signup_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                name = findViewById(R.id.input_name);
                account = findViewById(R.id.input_email);
                pwd = findViewById(R.id.input_password);
                EditText pwdagain = findViewById(R.id.input_passwordagain);
                String first_pwd = pwd.getText().toString();
                String second_pwd = pwdagain.getText().toString();
                if (name.getText().toString().matches("") || account.getText().toString().matches("") || first_pwd.matches("") || second_pwd.matches(""))
                {
                    Toast.makeText(SignUpActivity.this, "欄位不能是空白!!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(account.getText().toString()).matches())
                    {
                        Toast.makeText(SignUpActivity.this, "Email格式錯誤", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!first_pwd.equals(second_pwd))
                    {
                        Toast.makeText(SignUpActivity.this, "兩次密碼不一致!!", Toast.LENGTH_SHORT).show();
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
                            String sql = "SELECT 1 FROM `user` WHERE `account`= ?";

                            try (PreparedStatement pstmt = con.prepareStatement(sql))
                            {
                                pstmt.setString(1, account.getText().toString());
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
                    if (exist[0] == 1)
                    {
                        Toast.makeText(SignUpActivity.this, "此帳號已存在!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else
                    {
                        showSignUpAlertDialog();
                    }
                }
            }
        });
    }
    public Connection CONN()
    {
        Connection con = null;
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            Log.v("DB","加載驅動成功");
        }
        catch( ClassNotFoundException e)
        {
            Log.e("DB","加載驅動失敗");
        }

        // 連接資料庫
        try
        {
            // 解開一些複雜的限制，主要是遠許可以直接從UI或主要thread中連接資料庫。
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
    private void showSignUpAlertDialog()
    {
        LayoutInflater inflater = getLayoutInflater();
        final View layout = inflater.inflate(R.layout.alertdialog_sign_up,(ViewGroup) findViewById(R.id.signup_alertdialog));
        AlertDialog.Builder inputDialog = new AlertDialog.Builder(this)
                .setTitle("請輸入以下資料")
                .setView(layout)
                .setPositiveButton("確認",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                EditText remain = layout.findViewById(R.id.remain_input);
                                EditText limit = layout.findViewById(R.id.limit_input);
                                if (remain.getText().toString().matches("") || limit.getText().toString().matches("") )
                                {
                                    Toast.makeText(SignUpActivity.this, "請勿空白!!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
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
                                        String sql = "INSERT INTO `user` (`userid`, `name`, `account`, `pwd`, `remains`, `limitamount`) VALUES (DEFAULT, '" + name.getText().toString() +"', '"+account.getText().toString()+"', '"+pwd.getText().toString()+"', '"+remain.getText().toString()+"', '"+limit.getText().toString()+"')";
                                        System.out.println(sql); // 印出SQL語句
                                        Statement stmt = con.createStatement();
                                        stmt.executeUpdate(sql);
                                        stmt.close();   // 關閉連線
                                        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                    }
                                }
                                catch (Exception ex)
                                {
                                    Log.e("Failed to Connection", "Please check your connection");
                                }
                            }
                        });

        AlertDialog alertDialog = inputDialog.create();
        alertDialog.show();
        alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTypeface(null, Typeface.BOLD);
        alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FFA8DAFF"));

        Window window = alertDialog.getWindow();
        if (window != null)
        {
            window.setBackgroundDrawableResource(R.drawable.shape_rectangle);
            int screenWidth = getResources().getDisplayMetrics().widthPixels;
            int dialogWidth = (int) (screenWidth * 0.8);
            // 設置對話框的寬度
            window.setLayout(dialogWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}