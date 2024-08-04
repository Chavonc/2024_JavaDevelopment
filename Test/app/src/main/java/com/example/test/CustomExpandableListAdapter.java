package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.graphics.Typeface;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter
{
    private Context context;
    private List<String> expandableListTitle;
    private HashMap<String, List<String>> expandableListDetail;
    ArrayList<ArrayList<Integer>> check_states = new ArrayList<ArrayList<Integer>>();

    public CustomExpandableListAdapter(Context context, List<String> expandableListTitle,HashMap<String, List<String>> expandableListDetail)
    {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition)
    {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition)
    {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,boolean isLastChild, View convertView, ViewGroup parent)
    {
        final String expandedListText = (String) getChild(listPosition, expandedListPosition);
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.shopping_child_item, null);
        }
        TextView expandedListTextView = (TextView) convertView.findViewById(R.id.shopping_expandedListItem);
        expandedListTextView.setText(expandedListText);
        CheckBox cb = (CheckBox) convertView.findViewById( R.id.shopping_checkBox );
        if (isCheck(listTitle, expandedListPosition)) {cb.setChecked(true);}
        else {cb.setChecked(false);}
        return convertView;
    }
    @Override
    public int getChildrenCount(int listPosition)
    {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).size();
    }

    @Override
    public Object getGroup(int listPosition)
    {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount()
    {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition)
    {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,View convertView, ViewGroup parent)
    {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.shopping_parent_item, null);
        }
        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.shopping_listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition)
    {
        return true;
    }

    public boolean isCheck(String name, int expandedListPosition)
    {
        Integer id = 0;
        String items = "";
        String finish = "";
        try
        {
            Connection con = CONN();
            if (con == null)
            {
                Log.e("Failed to Connection", "Please check your connection");
            }
            else
            {
                String sql = "SELECT * FROM `purchasing` WHERE `purchasingname`= '" + name + "'";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next())
                {
                    id = rs.getInt(1);
                }
                sql = "SELECT * FROM `purchasingitem` WHERE `purchasingid`= '" + id + "'";
                rs = stmt.executeQuery(sql);
                while (rs.next())
                {
                    finish = rs.getString(3);
                }
                stmt.close();

                if (finish.split("\\|+")[expandedListPosition].equals("1")) { return true; }
            }
        }
        catch (Exception ex)
        {
            Log.e("Failed to Connection", "The action is not successful");
        }
        return false;
    }
    public Connection CONN()
    {
        Connection con = null;
        String mysql_ip = "10.0.2.2";
        int mysql_port = 3306; // Port 預設為 3306
        String db_name = "embeded";
        String urlGetDBParameters = "autoReconnect=false&useSSL=false";
        String url = "jdbc:mysql://"+mysql_ip+":"+mysql_port+"/"+db_name;
        String db_user = "root";
        String db_password = "Yejiching8";
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            Log.v("DB","加載驅動成功");
        }
        catch( ClassNotFoundException e)
        {
            Log.e("DB","加載驅動失敗");
        }
        try
        {
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