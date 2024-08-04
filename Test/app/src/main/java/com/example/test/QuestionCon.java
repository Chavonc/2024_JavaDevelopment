package com.example.test;
import android.util.Log;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class QuestionCon
{
    // 資料庫定義
    String mysql_ip = "10.0.2.2";
    int mysql_port = 3306; // Port 預設為 3306
    String db_name = "embeded";
    String urlGetDBParameters = "autoReconnect=false&useSSL=false";
    String url = "jdbc:mysql://"+mysql_ip+":"+mysql_port+"/"+db_name;
    String db_user = "root";
    String db_password = "Yejiching8";
    public void run()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            Log.v("DB","加載驅動成功");
        }
        catch( ClassNotFoundException e)
        {
            Log.e("DB","加載驅動失敗");
            return;
        }
        // 連接資料庫
        try
        {
            Connection con = DriverManager.getConnection(url,db_user,db_password);
            Log.v("DB","遠端連接成功");
        }
        catch(SQLException e)
        {
            Log.e("DB",url+"遠端連接失敗");
            Log.e("DB", e.toString());
        }
    }

    public ArrayList<String> getData()
    {
        ArrayList<String> data = new ArrayList<String>();
        try
        {
            Connection con = DriverManager.getConnection(url, db_user, db_password);
            String sql = "";
            sql = "select * from `questions` order by rand() limit 1";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next())
            {
                String id = rs.getString("questionid");
                data.add(id);
                String content = rs.getString("questioncontent");
                data.add(content);
                String select1 = rs.getString("select1");
                data.add(select1);
                String select2 = rs.getString("select2");
                data.add(select2);
                String ans = rs.getString("answer");
                data.add(ans);
            }
            st.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return data;
    }

    //    刪除類似這樣
    public void DeleteData(ArrayList<String> data)
    {
        try
        {
            Connection con = DriverManager.getConnection(url, db_user, db_password);
            String sql = "";
            sql = "DELETE FROM questions WHERE `questions`.`questionid` " + data.get(0);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            st.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    //    新增類似這樣
    public void InsertData(ArrayList<String> data)
    {
        try
        {
            Connection con = DriverManager.getConnection(url, db_user, db_password);
            String sql = "";
            sql = "INSERT INTO `user` (`userid`, `name`, `account`, `pwd`, `remains`, `limitamount`) VALUES (NULL, '12345', '00000000', '12345678', '12345678', '1000')";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            st.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    //    更改類似這樣
    public void UpdateData(ArrayList<String> data)
    {
        try
        {
            Connection con = DriverManager.getConnection(url, db_user, db_password);
            String sql = "";
            sql = "UPDATE `user` SET `remains` = '1000' WHERE `user`.`userid` = 1";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            st.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}