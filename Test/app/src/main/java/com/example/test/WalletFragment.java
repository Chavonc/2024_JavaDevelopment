package com.example.test;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WalletFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WalletFragment extends Fragment implements  WalletListAdapter.OnItemClickListener
{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1,mParam2;
    private FragmentManager fragmentManager;
    private RecyclerView recyclerView;
    private ImageView avatar_image;
    private WalletListAdapter adapter;
    private boolean isShowingBtn=false;
    int type=0,calTotal=0,total=0,expenseMoney=0,incomeMoney=0,addEditedMoney=0,loseEditedMoney=0;
    private Button incomeShowBtn, expenseShowBtn;
    private TextView walletRemain;
    // 資料庫設定
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
        catch(SQLException | java.sql.SQLException e)
        {
            Log.e("DB",url+"遠端連接失敗");
            Log.e("DB", e.toString());
        }
        return con;
    }
    private int getUserIdFromSharedPreferences()
    {
        // 使用 SharedPreferences 讀取 UserId
        SharedPreferences preferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return preferences.getInt("UserId", 0);
    }
    private int getTotalExpenseFromSharedPreferences()
    {
        // 使用 SharedPreferences 讀取 支出總和
        SharedPreferences preferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return preferences.getInt("totalExpense",0);
    }
    private int getOriginalAvatarFromSharedPreferences()
    {
        // 使用 SharedPreferences 讀取 頭像
        SharedPreferences preferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return preferences.getInt("avatar_choice",0);
    }
    private int getLimitExpenseFromSharedPreferences()
    {
        // 使用 SharedPreferences 讀取 支出上限
        SharedPreferences preferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return preferences.getInt("limitExpense",0);
    }
    public WalletFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WalletFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WalletFragment newInstance(String param1, String param2)
    {
        WalletFragment fragment = new WalletFragment();
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
        View view=inflater.inflate(R.layout.fragment_wallet,container,false);
        return view;
    }
    @Override
    public void onViewCreated(View view,Bundle savedInstanceState)
    {
        super.onViewCreated(view,savedInstanceState);
        getExpenseLimit();//取得支出上限
        int userId = getUserIdFromSharedPreferences();
        int userExpense=getTotalExpenseFromSharedPreferences();
        int userLimit=getLimitExpenseFromSharedPreferences();
        int userAvatarId=getOriginalAvatarFromSharedPreferences();
        System.out.println("已取得支出總和: "+userExpense+" 已取得支出上限: "+userLimit);
        recyclerView = view.findViewById(R.id.wallet_recyclerview);
        incomeShowBtn = view.findViewById(R.id.wallet_incomeBtn);
        expenseShowBtn=view.findViewById(R.id.wallet_expenseBtn);
        walletRemain=view.findViewById(R.id.wallet_total);
        adapter=new WalletListAdapter(getActivity(),R.layout.wallet_list,new ArrayList<>());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //顯示自變性頭像
        avatar_image=view.findViewById(R.id.avatar_imageView);
        if(userAvatarId==0)
        {
            avatar_image.setImageResource(R.drawable.rich);
        }
        else if(userAvatarId==1)
        {
            avatar_image.setImageResource(R.drawable.usemoney);
        }
        else if(userAvatarId==2)
        {
            avatar_image.setImageResource(R.drawable.normal);
        }
        else if(userAvatarId==3)
        {
            avatar_image.setImageResource(R.drawable.nomoney);
        }
        else if(userAvatarId==4)
        {
            avatar_image.setImageResource(R.drawable.poor);
        }
        else
        {
            avatar_image.setImageResource(R.drawable.qq);
        }
        //顯示錢包金額
        try
        {
            Connection con=CONN();
            if(con==null)
            {
                Log.e("con==nullFailed to Connection", "Please check your connection");
            }
            else
            {
                //取得註冊時所輸入的錢包金額
                String sql1="SELECT `remains` FROM `user` WHERE `userid`=?";
                try(PreparedStatement pstmt=con.prepareStatement(sql1))
                {
                    pstmt.setInt(1,userId);
                    System.out.println("所輸入的錢包金額:"+pstmt);
                    try(ResultSet rs=pstmt.executeQuery())
                    {
                        while(rs.next())
                        {
                            int remains=rs.getInt("remains");
                            walletRemain.setText(String.valueOf(remains));
                            System.out.println("Remains: "+remains);
                            //保存目前取得的錢包金額
                            total=Integer.parseInt(String.valueOf(remains));
                        }
                    }
                }
                con.close();// 關閉連線
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e("Catch:Failed to Connection", "Please check your connection");
        }
        //顯示每筆收入記錄
        incomeShowBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                isShowingBtn=true;
                int userId = getUserIdFromSharedPreferences();
                if(isShowingBtn==true)
                {
                    expenseShowBtn.setBackgroundColor(getResources().getColor(R.color.white));
                    incomeShowBtn.setBackgroundColor(getResources().getColor(R.color.btn_blue));
                }
                isShowingBtn=false;
                    try
                    {
                        Connection con = CONN();
                        if (con == null)
                        {
                            Log.e("con==null:Failed to Connection", "Please check your connection");
                        }
                        else
                        {
                            // 根據當前的模式選擇 SQL 查詢
                            String sql = "SELECT `amount`, `walletcontent`, DATE_FORMAT(`wallettime`, '%Y-%m-%d') AS itemDate FROM `wallet` WHERE `userid` = ? AND `wallettype`='0' ORDER BY `wallettime` DESC" ;
                            try (PreparedStatement pstmt = con.prepareStatement(sql))
                            {
                                pstmt.setInt(1, userId);
                                System.out.println("SQL Query: " + pstmt);

                                try (ResultSet rs = pstmt.executeQuery())
                                {
                                    // 解析結果並更新 RecyclerView 的資料
                                    ArrayList<walletListData> newData = new ArrayList<>();
                                    while (rs.next())
                                    {
                                        int amount = rs.getInt("amount");
                                        String content = rs.getString("walletcontent");
                                        String itemsDate = rs.getString("itemDate");
                                        newData.add(new walletListData(itemsDate, content, String.valueOf(amount)));
                                    }
                                    adapter.setData(newData);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                            type=0;//設定為收入
                            con.close(); // 關閉連線
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        Log.e("Catch:Failed to Connection", "Please check your connection");
                    }

            }
        });
        //顯示每筆支出記錄
        expenseShowBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                isShowingBtn=true;
                int userId = getUserIdFromSharedPreferences();
                if(isShowingBtn==true)
                {
                    incomeShowBtn.setBackgroundColor(getResources().getColor(R.color.white));
                    expenseShowBtn.setBackgroundColor(getResources().getColor(R.color.btn_blue));
                }
                    try
                    {
                        Connection con = CONN();
                        if (con == null)
                        {
                            Log.e("con==null:Failed to Connection", "Please check your connection");
                        }
                        else
                        {
                            // 根據當前的模式選擇 SQL 查詢
                            String sql = "SELECT `amount`, `walletcontent`, DATE_FORMAT(`wallettime`, '%Y-%m-%d') AS itemDate FROM `wallet` WHERE `userid` = ? AND `wallettype`='1' ORDER BY `wallettime` DESC";
                            try (PreparedStatement pstmt = con.prepareStatement(sql))
                            {
                                pstmt.setInt(1, userId);
                                System.out.println("SQL Query: " + pstmt);

                                try (ResultSet rs = pstmt.executeQuery())
                                {
                                    // 解析結果並更新 RecyclerView 的資料
                                    ArrayList<walletListData> newData = new ArrayList<>();
                                    while (rs.next())
                                    {
                                        int amount = rs.getInt("amount");
                                        String content = rs.getString("walletcontent");
                                        String itemsDate = rs.getString("itemDate");
                                        newData.add(new walletListData(itemsDate, content, String.valueOf(amount)));
                                    }
                                    adapter.setData(newData);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                            type=1;//設定為支出
                            con.close(); // 關閉連線
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        Log.e("Catch:Failed to Connection", "Please check your connection");
                    }

            }
        });
        //編輯收入及記錄
        adapter.setOnItemClickListener(new WalletListAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(int position)
            {
                editDataDialog(position);
            }
        });
        FloatingActionButton in_btn=view.findViewById(R.id.wallet_btn_in);
        FloatingActionButton out_btn=view.findViewById(R.id.wallet_btn_out);
        in_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showIncomeDialog();
            }
        });
        out_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showExpenseDialog();
            }
        });
    }
    //按每筆記錄
    @Override
    public void onItemClick(int position)
    {
        editDataDialog(position);
    }
    //右下方新增收入按鈕
    private void showIncomeDialog()
    {
        LayoutInflater income=LayoutInflater.from(getActivity());
        final View textView=income.inflate(R.layout.income_text,null);
        final EditText inputName=textView.findViewById(R.id.inputName);
        final EditText inputDate=textView.findViewById(R.id.inputDate);
        final EditText inputMoney=textView.findViewById(R.id.inputMoney);
        final EditText inputTime=textView.findViewById(R.id.inputTime);
        inputName.setText("", TextView.BufferType.EDITABLE);
        inputDate.setText("", TextView.BufferType.EDITABLE);
        inputMoney.setText("", TextView.BufferType.EDITABLE);
        inputTime.setText("",TextView.BufferType.EDITABLE);
        inputDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                fragmentManager=getParentFragmentManager();
                DialogFragment dateFragment=new DatePickerFragment();
                ((DatePickerFragment)dateFragment).setInputDate(inputDate);
                dateFragment.setCancelable(false);
                dateFragment.show(getParentFragmentManager(),"datePicker");
            }
        });
        inputTime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Time
                DialogFragment timeFragment= new TimePickerFragment();
                ((TimePickerFragment)timeFragment).setInputTime(inputTime);
                timeFragment.setCancelable(false);
                timeFragment.show(getParentFragmentManager(),"timePicker");
            }
        });
        AlertDialog.Builder incomeBuilder=new AlertDialog.Builder(getActivity());
        incomeBuilder.setTitle("新增收入").setView(textView)
                .setPositiveButton("新增", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                            if (TextUtils.isEmpty(inputName.getText()) || TextUtils.isEmpty(inputDate.getText()) || TextUtils.isEmpty(inputTime.getText()))
                            {
                                Toast.makeText(getActivity(), "請將內容填寫完整再送出", Toast.LENGTH_SHORT).show();
                            }
                            int userId=getUserIdFromSharedPreferences();
                            String dateTime= inputDate.getText() +" "+ inputTime.getText();
                            System.out.println(dateTime);
                            try
                            {
                                Connection con = CONN();
                                if (con == null)
                                {
                                    Log.e("con==null:Failed to Connection", "Please check your connection");
                                }
                                else
                                {
                                    // 根據當前的模式選擇 SQL 查詢
                                    String sql = "INSERT INTO `wallet` (`walletid`, `userid`, `wallettype`, `walletcontent`, `amount`, `wallettime`) " +
                                            "VALUES (DEFAULT, ?, 0, ?, ?,?)";
                                    try (PreparedStatement pstmt = con.prepareStatement(sql))
                                    {
                                        pstmt.setInt(1, userId);
                                        pstmt.setString(2, inputName.getText().toString());
                                        pstmt.setInt(3, Integer.parseInt(inputMoney.getText().toString()));
                                        pstmt.setString(4, dateTime);
                                        incomeMoney=Integer.parseInt(inputMoney.getText().toString());
                                        System.out.println("新增收入incomeMoney: " + incomeMoney);
                                        System.out.println("ADD SQL Query: " + pstmt);
                                        int rowsAffected=pstmt.executeUpdate();
                                        if(rowsAffected>0)
                                        {
                                            Toast.makeText(getActivity(), "成功新增收入! ", Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            Toast.makeText(getActivity(), "資料輸入不完整! ", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    //更新錢包目前總金額(加錢)
                                    calTotal=total+incomeMoney;
                                    System.out.println("錢包最新總金額:"+calTotal);
                                    String sql2="UPDATE `user` SET `remains`="+calTotal+" WHERE `remains`="+total;
                                    try(PreparedStatement pstmt=con.prepareStatement(sql2))
                                    {
                                        System.out.println("更新錢包目前總金額(加錢) SQL Query: " + pstmt);
                                        int rowsAffected=pstmt.executeUpdate();
                                        updateRecyclerView();
                                        if(rowsAffected>0)
                                        {
                                            updateRemain();
                                            Toast.makeText(getActivity(), "成功加錢! ", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    con.close(); // 關閉連線
                                    updateRecyclerView();
                                }
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                                Log.e("Catch:Failed to Connection", "Please check your connection");
                            }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });
        AlertDialog incomeDialog=incomeBuilder.create();
        incomeDialog.show();
        incomeDialog.getWindow().setLayout(700, 1000);
        incomeDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_bg);
    }
    //右下方新增支出按鈕
    private void showExpenseDialog()
    {
        LayoutInflater expense=LayoutInflater.from(getActivity());
        final View textView=expense.inflate(R.layout.expense_text,null);
        final EditText inputName=textView.findViewById(R.id.inputName);
        final EditText inputDate=textView.findViewById(R.id.inputDate);
        final EditText inputMoney=textView.findViewById(R.id.inputMoney);
        final EditText inputTime=textView.findViewById(R.id.inputTime);
        inputName.setText("", TextView.BufferType.EDITABLE);
        inputDate.setText("", TextView.BufferType.EDITABLE);
        inputMoney.setText("", TextView.BufferType.EDITABLE);
        inputTime.setText("", TextView.BufferType.EDITABLE);
        inputDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                fragmentManager=getParentFragmentManager();
                DialogFragment dateFragment=new DatePickerFragment();
                ((DatePickerFragment)dateFragment).setInputDate(inputDate);
                dateFragment.setCancelable(false);
                dateFragment.show(getParentFragmentManager(),"datePicker");
            }
        });
        inputTime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Time
                DialogFragment timeFragment= new TimePickerFragment();
                ((TimePickerFragment)timeFragment).setInputTime(inputTime);
                timeFragment.setCancelable(false);
                timeFragment.show(getParentFragmentManager(),"timePicker");
            }
        });
        AlertDialog.Builder expenseBuilder=new AlertDialog.Builder(getActivity());
        expenseBuilder.setTitle("新增支出").setView(textView)
                .setPositiveButton("新增", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (TextUtils.isEmpty(inputName.getText()) || TextUtils.isEmpty(inputDate.getText()) || TextUtils.isEmpty(inputTime.getText()))
                        {
                            Toast.makeText(getActivity(), "請將內容填寫完整再送出", Toast.LENGTH_SHORT).show();
                        }
                        int userId=getUserIdFromSharedPreferences();
                        String dateTime= inputDate.getText() +" "+ inputTime.getText();
                        System.out.println(dateTime);
                        try
                        {
                            Connection con = CONN();
                            if (con == null)
                            {
                                Log.e("con==null:Failed to Connection", "Please check your connection");
                            }
                            else
                            {
                                // 根據當前的模式選擇 SQL 查詢
                                String sql = "INSERT INTO `wallet` (`walletid`, `userid`, `wallettype`, `walletcontent`, `amount`, `wallettime`) " +
                                        "VALUES (DEFAULT, ?, 1, ?, ?,?)";
                                try (PreparedStatement pstmt = con.prepareStatement(sql))
                                {
                                    pstmt.setInt(1, userId);
                                    pstmt.setString(2, inputName.getText().toString());
                                    pstmt.setInt(3, Integer.parseInt(inputMoney.getText().toString()));
                                    pstmt.setString(4, dateTime);
                                    //得到該筆支出Amount
                                    expenseMoney=Integer.parseInt(inputMoney.getText().toString());
                                    System.out.println("該筆支出Amount:"+expenseMoney);
                                    System.out.println("ADD SQL Query: " + pstmt);
                                    int rowsAffected=pstmt.executeUpdate();
                                    if(rowsAffected>0)
                                    {
                                        getQuestion();//新增支出之後要跳出來問小Game
                                        Toast.makeText(getActivity(), "成功新增支出! ", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(getActivity(), "資料輸入不完整! ", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                //更新錢包目前總金額(扣錢)
                                calTotal=total-expenseMoney;
                                System.out.println("錢包最新總金額:"+calTotal);
                                String sql2="UPDATE `user` SET `remains`="+calTotal+" WHERE `remains`="+total;
                                try(PreparedStatement pstmt=con.prepareStatement(sql2))
                                {
                                    System.out.println("更新錢包目前總金額(扣錢) SQL Query: " + pstmt);
                                    int rowsAffected=pstmt.executeUpdate();
                                    if(rowsAffected>0)
                                    {
                                        updateRemain();
                                        updateRecyclerView();//更新顯示
                                        Toast.makeText(getActivity(), "成功扣錢! ", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                con.close(); // 關閉連線
                                updateExpense();//更新支出總和
                                getExpenseLimit();//更新支出上限
                                updateAvatar();//更新頭像
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                            Log.e("Catch:Failed to Connection", "Please check your connection");
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });
        AlertDialog expenseDialog=expenseBuilder.create();
        expenseDialog.show();
        expenseDialog.getWindow().setLayout(700, 1000);
        expenseDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_bg);
    }
    private void editDataDialog(int position)
    {
        LayoutInflater edit=LayoutInflater.from(getActivity());
        final View wallet_textView=edit.inflate(R.layout.wallet_edit_data,null);
        final EditText editName=wallet_textView.findViewById(R.id.edit_Name);
        final EditText editDate=wallet_textView.findViewById(R.id.edit_Date);
        final EditText editMoney=wallet_textView.findViewById(R.id.edit_Money);
        walletListData selectedData=adapter.getItem(position);
        if(selectedData!=null)
        {
            editName.setText(selectedData.getItemName());
            editName.setFocusable(false);
            editName.setFocusableInTouchMode(false);
            editDate.setText(selectedData.getItemDate());
            editDate.setFocusable(false);
            editDate.setFocusableInTouchMode(false);
            editMoney.setText(selectedData.getItemCost());
            editMoney.setFocusable(false);
            editMoney.setFocusableInTouchMode(false);
        }
        //給編輯記錄用的
//        editDate.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                fragmentManager=getParentFragmentManager();
//                DialogFragment dateFragment=new DatePickerFragment();
//                ((DatePickerFragment)dateFragment).setInputDate(editDate);
//                dateFragment.setCancelable(false);
//                dateFragment.show(getParentFragmentManager()," update datePicker");
//            }
//        });
        AlertDialog.Builder editBuilder=new AlertDialog.Builder(getActivity());
        editBuilder.setTitle("刪除記帳資料").setView(wallet_textView)
                .setPositiveButton("刪除", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        int userId=getUserIdFromSharedPreferences();
                        // 執行刪除資料的操作
                            try
                            {
                                Connection con = CONN();
                                if (con == null)
                                {
                                    Log.e("con==null:Failed to Connection", "Please check your connection");
                                }
                                else
                                {
                                    // 根據當前的模式選擇 SQL 查詢
                                    String sql = "DELETE FROM `wallet` WHERE `userid` = ? AND `walletcontent` = ? AND `amount` = ? AND DATE_FORMAT(`wallettime`, '%Y-%m-%d') = ? ";
                                    try (PreparedStatement pstmt = con.prepareStatement(sql))
                                    {
                                        pstmt.setInt(1, userId);
                                        pstmt.setString(2, String.valueOf(editName.getText()));
                                        pstmt.setInt(3, Integer.parseInt(String.valueOf(editMoney.getText())));
                                        pstmt.setString(4, String.valueOf(editDate.getText()));
                                        System.out.println("SQL Query: " + pstmt);
                                        int rowsAffected=pstmt.executeUpdate();
                                        updateRecyclerView();
                                        if(rowsAffected>0)
                                        {
                                            Toast.makeText(getActivity(), "成功刪除! ", Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            Toast.makeText(getActivity(), "未找到匹配的資料，刪除失敗! ", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    if(type==1)//是支出
                                    {
                                        addEditedMoney=Integer.parseInt(String.valueOf(editMoney.getText()));
                                        calTotal=total+addEditedMoney;
                                        String sql2="UPDATE `user` SET `remains`="+calTotal+" WHERE `remains`="+total;
                                        try(PreparedStatement pstmt=con.prepareStatement(sql2))
                                        {
                                            System.out.println("更新錢包目前總金額(加錢) SQL Query: " + pstmt);
                                            int rowsAffected=pstmt.executeUpdate();
                                            if(rowsAffected>0)
                                            {
                                                updateRemain();
                                                updateRecyclerView();
                                                Toast.makeText(getActivity(), "刪除支出之後成功把錢加回去! ", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                    if(type==0)//是收入
                                    {
                                        loseEditedMoney=Integer.parseInt(String.valueOf(editMoney.getText()));
                                        calTotal=total-loseEditedMoney;
                                        String sql2="UPDATE `user` SET `remains`="+calTotal+" WHERE `remains`="+total;
                                        try(PreparedStatement pstmt=con.prepareStatement(sql2))
                                        {
                                            System.out.println("更新錢包目前總金額(扣錢) SQL Query: " + pstmt);
                                            int rowsAffected=pstmt.executeUpdate();
                                            if(rowsAffected>0)
                                            {
                                                updateRemain();
                                                updateRecyclerView();
                                                Toast.makeText(getActivity(), "刪除收入之後失去部分錢! ", Toast.LENGTH_SHORT).show();
                                            }
                                            else
                                            {
                                                Toast.makeText(getActivity(), "刪除收入之後沒有任何改變! ", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    }
                                    con.close(); // 關閉連線
                                    updateRecyclerView();
                                    updateExpense();//更新支出總和
                                    getExpenseLimit();//更新支出上限
                                    updateAvatar();//更新頭像
                                }
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                                Log.e("Catch:Failed to Connection", "Please check your connection");
                            }

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });
        AlertDialog editDialog=editBuilder.create();
        editDialog.show();
        editDialog.getWindow().setLayout(700, 800);
        editDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_bg);
    }
    //更新顯示每筆記錄
    private void updateRecyclerView()
    {
        int userId=getUserIdFromSharedPreferences();
        String sql="";
            if (type == 0)
            {
                sql = "SELECT `amount`, `walletcontent`, DATE_FORMAT(`wallettime`, '%Y-%m-%d') AS itemDate FROM `wallet` WHERE `userid` = ? AND `wallettype`='0' ORDER BY `wallettime` DESC";
            }
            else if (type == 1)
            {
                sql = "SELECT `amount`, `walletcontent`, DATE_FORMAT(`wallettime`, '%Y-%m-%d') AS itemDate FROM `wallet` WHERE `userid` = ? AND `wallettype`='1' ORDER BY `wallettime` DESC";
            }
            try
            {
                Connection con = CONN();
                if (con == null)
                {
                    Log.e("con==null:Failed to Connection", "Please check your connection");
                }
                else
                {
                    try (PreparedStatement pstmt = con.prepareStatement(sql))
                    {
                        pstmt.setInt(1, userId);
                        System.out.println("SQL Query: " + pstmt);
                        try (ResultSet rs = pstmt.executeQuery())
                        {
                            // 解析結果並更新 RecyclerView 的資料
                            ArrayList<walletListData> newData = new ArrayList<>();
                            while (rs.next())
                            {
                                int amount = rs.getInt("amount");
                                String content = rs.getString("walletcontent");
                                String itemsDate = rs.getString("itemDate");
                                newData.add(new walletListData(itemsDate, content, String.valueOf(amount)));
                            }
                            adapter.setData(newData);
                            adapter.notifyDataSetChanged();
                        }
                    }
                    con.close(); // 關閉連線
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Log.e("Catch:Failed to Connection", "Please check your connection");
            }
    }
    //更新錢包總金額
    private void updateRemain()
    {
        int userId=getUserIdFromSharedPreferences();
        try
        {
            Connection con=CONN();
            if(con==null)
            {
                Log.e("con==nullFailed to Connection", "Please check your connection");
            }
            else
            {
                //取得錢包金額
                String sql="SELECT `remains` FROM `user` WHERE `userid`=?";
                try(PreparedStatement pstmt=con.prepareStatement(sql))
                {
                    pstmt.setInt(1,userId);
                    System.out.println("所輸入的錢包金額:"+pstmt);
                    try(ResultSet rs=pstmt.executeQuery())
                    {
                        while(rs.next())
                        {
                            int remains=rs.getInt("remains");
                            walletRemain.setText(String.valueOf(remains));
                            System.out.println("Remains: "+remains);
                            //保存目前取得的錢包金額
                            total=Integer.parseInt(String.valueOf(remains));
                        }
                    }
                }
                con.close();// 關閉連線
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e("Catch:Failed to Connection", "Please check your connection");
        }
    }
    //更新支出總和
    private void updateExpense()
    {
        int userId=getUserIdFromSharedPreferences();
        SharedPreferences preferences=getActivity().getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        String nowDate = new SimpleDateFormat("yyyy-MM").format(new Date());
        try
        {
            Connection con=CONN();
            if(con==null)
            {
                Log.e("con==nullFailed to Connection", "Please check your connection");
            }
            else
            {
                //取得該月支出總和
                String sql="SELECT SUM(`amount`) as totalExpense, `wallettype` FROM `wallet` WHERE `userid` = ? AND DATE_FORMAT(`wallettime`, '%Y-%m') = ? AND `wallettype`=?";
                try(PreparedStatement pstmt=con.prepareStatement(sql))
                {
                    pstmt.setInt(1,userId);
                    pstmt.setString(2,nowDate);
                    pstmt.setInt(3,1);//wallettype=支出
                    System.out.println("取得該月支出總和:"+pstmt);
                    try(ResultSet rs=pstmt.executeQuery())
                    {
                        while(rs.next())
                        {
                            int totalAmount=rs.getInt("totalExpense");
                            System.out.println("更新的支出總和: " + totalAmount);
                            editor.putInt("totalExpense",totalAmount);//保存支出總和
                            editor.apply();
                        }
                    }
                }
                con.close();// 關閉連線
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e("Catch:Failed to Connection", "Please check your connection");
        }
    }
    //取得該月支出上限
    private void getExpenseLimit()
    {
        int userId=getUserIdFromSharedPreferences();
        SharedPreferences preferences=getActivity().getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        try
        {
            Connection con=CONN();
            if(con==null)
            {
                Log.e("con==nullFailed to Connection", "Please check your connection");
            }
            else
            {
                //取得該月支出上限
                String sql="SELECT `limitamount` FROM `user`WHERE `userid`=?";
                try(PreparedStatement pstmt=con.prepareStatement(sql))
                {
                    pstmt.setInt(1,userId);
                    System.out.println("取得該月支出上限:"+pstmt);
                    try(ResultSet rs=pstmt.executeQuery())
                    {
                        while(rs.next())
                        {
                            int limitAmount=rs.getInt("limitamount");
                            System.out.println("更新的支出上限: " + limitAmount);
                            editor.putInt("limitExpense",limitAmount);//保存支出上限
                            editor.apply();
                        }
                    }
                }
                con.close();// 關閉連線
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e("Catch:Failed to Connection", "Please check your connection");
        }
    }
    //更新要顯示的頭像
    private void updateAvatar()
    {
        int userExpense=getTotalExpenseFromSharedPreferences();
        int userLimit=getLimitExpenseFromSharedPreferences();
        System.out.println("已取得支出總和: "+userExpense+" 已取得支出上限: "+userLimit);
        if(userExpense==0)
        {
            avatar_image.setImageResource(R.drawable.rich);
        }
        else if(userExpense > 0 && userExpense < userLimit / 3)
        {
            avatar_image.setImageResource(R.drawable.usemoney);
        }
        else if(userExpense >= userLimit / 3 && userExpense < 2 * userLimit / 3)
        {
            avatar_image.setImageResource(R.drawable.normal);
        }
        else if(userExpense >= 2 * userLimit / 3 && userExpense < 7 * userLimit / 8)
        {
            avatar_image.setImageResource(R.drawable.nomoney);
        }
        else if(userExpense >= 7 * userLimit / 8 && userExpense <= userLimit)
        {
            avatar_image.setImageResource(R.drawable.poor);
        }
        else
        {
            avatar_image.setImageResource(R.drawable.qq);
        }
    }
    //支出時小遊戲的部分
    public void getQuestion()
    {
        new Thread(new Runnable()
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run()
            {
                QuestionCon con = new QuestionCon();
                con.run();
                ArrayList<String> data = con.getData();
                //Log.v("Get Id", data.get(0));
                String listString = String.join(", ", data);
                Log.v("OK", listString);
                new Handler(Looper.getMainLooper()).post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        setQuestion(data);
                    }
                });
            }
        }).start();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setQuestion(ArrayList<String> data)
    {
        String ans = data.get(4);
        AlertDialog.Builder question_dialog = new AlertDialog.Builder(getActivity());
        question_dialog.setTitle("小問題！")
                .setMessage(data.get(1))
                .setPositiveButton("選項1: "+data.get(2), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int i)
                    {
                        checkAns(ans, 1);
                    }
                })
                .setNegativeButton("選項2: "+data.get(3), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int i)
                    {
                        checkAns(ans, 2);
                    }
                })
                .setCancelable(false);
        AlertDialog questionDialog=question_dialog.create();
        questionDialog.show();
        questionDialog.getWindow().setLayout(700, 800);
        questionDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_bg);
    }
    public void checkAns(String ans, int select)
    {
        String message = "";
        if(ans.equals(String.valueOf(select)))
        {
            message = "你答對了!";
        }
        else
        {
            message = "噗噗 你答錯了!";
        }
        AlertDialog.Builder messageDialog = new AlertDialog.Builder(getActivity());
        messageDialog.setTitle(message)
                .setPositiveButton("離開 ", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int i)
                    {
                        dialog.dismiss();
                    }
                })
                .setCancelable(true);
        AlertDialog leaveDialog=messageDialog.create();
        leaveDialog.show();
        leaveDialog.getWindow().setLayout(700, 300);
        leaveDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_bg);

    }
}