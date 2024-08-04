package com.example.test;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentHostCallback;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump
{
    private static int userid;
    public static void setUserId(int id) { userid = id; }

    public static HashMap<String, List<String>> getData()
    {
        //Chavon Start
        //int userId=getUserIdFromSharedPreferences();
        System.out.println(6);
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();
        List list = Arrays.asList(getPurchasingid(userid));
        //Chavon End
        int purchasingid = 0;
        for (int i=0; i<list.size(); i++)
        {
            List<String> title = new ArrayList<String>();
            purchasingid = Integer.valueOf((String) list.get(i));
            for(String s : getPurchasingitem(purchasingid).split("\\|"))
            {
                title.add(s);
            }
            expandableListDetail.put(getPurchasingname(purchasingid), title);
        }
        return expandableListDetail;
    }

    public static String[] getPurchasingid(int userId)
    {
        //Chavon Start
        //userId=getUserIdFromSharedPreferences();
        //Chavon End
        ArrayList<String> PurchasingData = new ArrayList<String>();
        String NoteData[];
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
                String sql = "select * from `purchasing` where `userid` =" + String.valueOf(userId) + " order by `purchasingid`";
                //String sql = "select * from `purchasing` where `userid` =" + userId + " order by `purchasingid`";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next())
                {
                    PurchasingData.add(rs.getString(1));
                }
                stmt.close();   // 關閉連線
            }
        }
        catch (Exception ex)
        {
            Log.e("Failed to Connection", "Please check your connection");
        }
        NoteData = (String[])PurchasingData.toArray(new String[PurchasingData.size()]);
        return NoteData;
    }
    public static String getPurchasingname(int purchasingid)
    {
        String name = "";
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
                String sql = "select * from `purchasing` where `purchasingid` =" + String.valueOf(purchasingid) + " order by `purchasingid` ASC";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next())
                {
                    name = rs.getString(3);
                }
                stmt.close();   // 關閉連線
            }
        }
        catch (Exception ex)
        {
            Log.e("Failed to Connection", "Please check your connection");
        }
        return name;
    }
    public static String getPurchasingitem(int purchasingid)
    {
        String item = "";
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
                String sql = "select * from `purchasingitem` where `purchasingid` =" + String.valueOf(purchasingid);
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next())
                {
                    item = rs.getString(2);
                }
                stmt.close();   // 關閉連線
            }
        }
        catch (Exception ex)
        {
            Log.e("Failed to Connection", "Please check your connection");
        }
        return item;
    }
    public static Connection CONN()
    {
        String mysql_ip = "10.0.2.2";
        int mysql_port = 3306; // Port 預設為 3306
        String db_name = "embeded";
        String urlGetDBParameters = "autoReconnect=false&useSSL=false";
        String url = "jdbc:mysql://"+mysql_ip+":"+mysql_port+"/"+db_name;
        String db_user = "root";
        String db_password = "Yejiching8";
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
}
