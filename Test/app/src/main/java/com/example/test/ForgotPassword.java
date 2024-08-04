package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ForgotPassword extends AppCompatActivity
{
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
        }
        catch( ClassNotFoundException e)
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
        setContentView(R.layout.activity_forgot_password);
        Button signup_btn=findViewById(R.id.btn_to_signuppage);
        Button signin_btn=findViewById(R.id.btn_to_signinpage);
        Button findpwd_btn=findViewById(R.id.btn_findpwd);
        signup_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(ForgotPassword.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        signin_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(ForgotPassword.this, MainActivity.class);
                startActivity(intent);
            }
        });
        findpwd_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                EditText name=findViewById(R.id.input_fname);
                EditText account=findViewById(R.id.input_femail);
                if (name.getText().toString().matches("") || account.getText().toString().matches(""))
                {
                    Toast.makeText(ForgotPassword.this, "請勿空白!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(account.getText().toString()).matches())
                    {
                        Toast.makeText(ForgotPassword.this, "Email格式錯誤", Toast.LENGTH_SHORT).show();
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
                            String sql = "SELECT `userid` FROM `user` WHERE `account`= ? AND `name` = ?";

                            try (PreparedStatement pstmt = con.prepareStatement(sql))
                            {
                                pstmt.setString(1, account.getText().toString());
                                pstmt.setString(2, name.getText().toString());
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
                        Toast.makeText(ForgotPassword.this, "姓名或帳號錯誤!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else
                    {
                        String newpwd = generateRandomPassword();
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
                                String sql = "UPDATE `user` SET `pwd` = ? WHERE `userid` = ?";
                                try (PreparedStatement pstmt = con.prepareStatement(sql))
                                {
                                    pstmt.setString(1, newpwd);
                                    pstmt.setInt(2, exist[0] );
                                    System.out.println(pstmt);
                                    pstmt.executeUpdate();
                                }
                                con.close();
                            }
                        }
                        catch (Exception ex)
                        {
                            Log.e("Failed to Connection", "Please check your connection");
                            return;
                        }
                        showPasswordDialog(newpwd);
                    }
                }
            }
        });
    }
    private void showPasswordDialog(String newPassword)
    {
        LayoutInflater inflater = getLayoutInflater();
        final View layout = inflater.inflate(R.layout.alertdialog_modifyinfo, (ViewGroup) findViewById(R.id.modifyinfo_alertdialog));
        EditText showPwd = layout.findViewById(R.id.modify_input);
        showPwd.setText(newPassword);
        showPwd.setInputType(InputType.TYPE_NULL);
        showPwd.setTextIsSelectable(true);

        AlertDialog.Builder inputDialog = new AlertDialog.Builder(ForgotPassword.this)
                .setView(layout)
                .setCancelable(false)
                .setTitle("以下是您的新密碼")
                .setPositiveButton("確認", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Intent intent = new Intent(ForgotPassword.this, MainActivity.class);
                        startActivity(intent);
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
            window.setLayout(dialogWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    private String generateRandomPassword()
    {
        int length = 12;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++)
        {
            int randomIndex = random.nextInt(characters.length());
            password.append(characters.charAt(randomIndex));
        }
        return password.toString();
    }
}