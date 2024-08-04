package com.example.test;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment
{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
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
    public AccountFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2)
    {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }
    private int getUserIdFromSharedPreferences()
    {
        // 使用 SharedPreferences 讀取 UserId
        SharedPreferences preferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return preferences.getInt("UserId", 0);
    }
    @Override
    public void onViewCreated(View view,Bundle savedInstanceState)
    {
        super.onViewCreated(view,savedInstanceState);
        int userId = getUserIdFromSharedPreferences();
        String nowDate = new SimpleDateFormat("yyyy-MM").format(new Date());
        Map<Integer,Integer> totalAmount = new HashMap<>();
        int[] limit= new int[1];
        String[] name = new String[1];
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
                //支出與收入分別總和
                String sql = "SELECT SUM(`amount`) as totalAmount, `wallettype` FROM `wallet` WHERE `userid` = ? AND DATE_FORMAT(`wallettime`, '%Y-%m') = ? GROUP BY `wallettype`";

                try (PreparedStatement pstmt = con.prepareStatement(sql))
                {
                    pstmt.setInt(1, userId);
                    pstmt.setString(2, nowDate);
                    System.out.println(pstmt);
                    try (ResultSet rs = pstmt.executeQuery())
                    {
                        while (rs.next())
                        {
                            int Amount = rs.getInt("totalAmount");
                            int walletType = rs.getInt("wallettype");
                            System.out.println("Wallet Type: " + walletType + ", Total Amount: " + Amount);
                            totalAmount.put(walletType,Amount);
                        }
                    }
                }

                String sql2 = "SELECT `name`,`limitamount` FROM `user` WHERE `userid` = ?";
                try (PreparedStatement pstmt = con.prepareStatement(sql2))
                {
                    pstmt.setInt(1, userId);
                    System.out.println(pstmt);

                    try (ResultSet rs = pstmt.executeQuery())
                    {
                        if (rs.next())
                        {
                            limit[0] = rs.getInt("limitamount");
                            name[0] = rs.getString("name");
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
        Button changeLimitBtn = view.findViewById(R.id.info_Limit);
        Button changrPassword = view.findViewById(R.id.ChangePassword);
        Button logout=view.findViewById(R.id.logout);
        TextView showName = view.findViewById(R.id.info_Name);
        TextView showUsed = view.findViewById(R.id.info_Used);
        TextView showIncome = view.findViewById(R.id.info_Income);
        ImageView imageView = view.findViewById(R.id.imageView3);
        changeLimitBtn.setText(String.valueOf(limit[0]));
        showName.setText(name[0]);
        showIncome.setText(String.valueOf(totalAmount.get(0) != null ? totalAmount.get(0) : 0));
        showUsed.setText(String.valueOf(totalAmount.get(1) != null ? totalAmount.get(1) : 0));
        changeimg(imageView,Integer.parseInt(showUsed.getText().toString()),limit[0]);
        //登出
        logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
            }
        });
        changrPassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                LayoutInflater inflater = getLayoutInflater();
                final View layout = inflater.inflate(R.layout.alertdialog_changepassword,
                        (ViewGroup) view.findViewById(R.id.changepassword_alertdialog));

                AlertDialog.Builder inputDialog = new AlertDialog.Builder(getContext())
                        .setView(layout)
                        .setPositiveButton("確認",
                                new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        EditText oldPwd = layout.findViewById(R.id.oldpwd_input);
                                        EditText newPwd = layout.findViewById(R.id.newpwd_input);
                                        EditText newPwdAgain = layout.findViewById(R.id.newpwdagain_input);
                                        if (oldPwd.getText().toString().matches("") || newPwd.getText().toString().matches("") || newPwdAgain.getText().toString().matches(""))
                                        {
                                            Toast.makeText(getContext(), "請勿空白!!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (!newPwd.getText().toString().equals(newPwdAgain.getText().toString()))
                                        {
                                            Toast.makeText(getContext(), "兩次密碼不一致!!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        int[] check = new int[1];
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
                                                String sql = "SELECT 1 FROM `user` WHERE `userid`= ? AND `pwd`= ?";

                                                try (PreparedStatement pstmt = con.prepareStatement(sql))
                                                {
                                                    pstmt.setInt(1, userId);
                                                    pstmt.setString(2, oldPwd.getText().toString());
                                                    System.out.println(pstmt);

                                                    // 將執行的結果存到rs裡面
                                                    try (ResultSet rs = pstmt.executeQuery())
                                                    {
                                                        if (rs.next())
                                                        {
                                                            check[0] = rs.getInt(1);
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
                                        if (check[0] == 1)
                                        {
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
                                                        pstmt.setString(1, newPwd.getText().toString());
                                                        pstmt.setInt(2, userId);
                                                        System.out.println(pstmt);
                                                        pstmt.executeUpdate(); // 使用 executeUpdate() 執行更新操作
                                                    }

                                                    con.close();
                                                }
                                            }
                                            catch (Exception ex)
                                            {
                                                Log.e("Failed to Connection", "Please check your connection");
                                            }
                                        }
                                        else
                                        {
                                            Toast.makeText(getContext(), "密碼錯誤!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i)
                                    {
                                        dialogInterface.dismiss();
                                    }
                                });

                AlertDialog alertDialog = inputDialog.create();
                alertDialog.show();
                alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTypeface(null, Typeface.BOLD);
                alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FFA8DAFF"));
                alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#FF333333"));

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
        });

        changeLimitBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                LayoutInflater inflater = getLayoutInflater();
                final View layout = inflater.inflate(R.layout.alertdialog_modifyinfo,
                        (ViewGroup) view.findViewById(R.id.modifyinfo_alertdialog));
                ((EditText) layout.findViewById(R.id.modify_input)).setText(changeLimitBtn.getText().toString());
                AlertDialog.Builder inputDialog = new AlertDialog.Builder(getContext())
                        .setView(layout)
                        .setPositiveButton("確認",
                                new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        EditText newlimit = layout.findViewById(R.id.modify_input);
                                        SharedPreferences preferences=getActivity().getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);
                                        SharedPreferences.Editor limitEditor=preferences.edit();
                                        if (TextUtils.isEmpty(newlimit.getText().toString()))
                                        {
                                            Toast.makeText(getContext(), "請填入每月支出上限!!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        if (!TextUtils.isDigitsOnly(newlimit.getText().toString()))
                                        {
                                            Toast.makeText(getContext(), "請輸入有效的數字", Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        int limitValue = Integer.parseInt(newlimit.getText().toString());
                                        if (limitValue < 0)
                                        {
                                            Toast.makeText(getContext(), "金額不可小於0", Toast.LENGTH_SHORT).show();
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
                                                String sql = "UPDATE `user` SET `limitamount` = ? WHERE `userid` = ?";

                                                try (PreparedStatement pstmt = con.prepareStatement(sql))
                                                {
                                                    pstmt.setString(1, newlimit.getText().toString());
                                                    pstmt.setInt(2, userId);
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
                                        changeLimitBtn.setText(newlimit.getText().toString());
                                        changeimg(imageView,Integer.parseInt(showUsed.getText().toString()),Integer.parseInt(newlimit.getText().toString()));
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                dialogInterface.dismiss();
                            }
                        });

                AlertDialog alertDialog = inputDialog.create();
                alertDialog.show();
                alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTypeface(null, Typeface.BOLD);
                alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FFA8DAFF"));
                alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#FF333333"));


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
        });
    }
    private void changeimg (ImageView person, int used, int limitused)//used=支出總和,limitused=支出上限
    {
        SharedPreferences preferences=getActivity().getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        if (used == 0)
        {
            person.setImageResource(R.drawable.rich);
            editor.putInt("avatar_choice",0);
        }
        else if (used > 0 && used < limitused / 3)
        {
            person.setImageResource(R.drawable.usemoney);
            editor.putInt("avatar_choice",1);
        }
        else if (used >= limitused / 3 && used < 2 * limitused / 3)
        {
            person.setImageResource(R.drawable.normal);
            editor.putInt("avatar_choice",2);
        }
        else if (used >= 2 * limitused / 3 && used < 7 * limitused / 8)
        {
            person.setImageResource(R.drawable.nomoney);
            editor.putInt("avatar_choice",3);
        }
        else if (used >= 7 * limitused / 8 && used <= limitused)
        {
            person.setImageResource(R.drawable.poor);
            editor.putInt("avatar_choice",4);
        }
        else
        {
            person.setImageResource(R.drawable.qq);
            editor.putInt("avatar_choice",5);
        }
        editor.apply();
    }
}