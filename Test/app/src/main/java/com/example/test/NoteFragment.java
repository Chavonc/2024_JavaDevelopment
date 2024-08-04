package com.example.test;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoteFragment extends Fragment implements AdapterView.OnItemLongClickListener
{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText inputTime,inputDate;
    private FragmentManager fragmentManager;
    private ListView listViewData;
    public ArrayAdapter<String> adapter;
    public String arrayData[] = {"考試","期末報告","作業","約會"};

    // Furry: 資料庫的部分
    // 資料庫設定
    String mysql_ip = "10.0.2.2";
    int mysql_port = 3306; // Port 預設為 3306
    String db_name = "embeded";
    String urlGetDBParameters = "autoReconnect=false&useSSL=false";
    String url = "jdbc:mysql://"+mysql_ip+":"+mysql_port+"/"+db_name;
    String db_user = "root";
    String db_password = "Yejiching8";
    // FurryEnd
    private int getUserIdFromSharedPreferences()//Chavon change userId
    {
        // 使用 SharedPreferences 讀取 UserId
        SharedPreferences preferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return preferences.getInt("UserId", 0);
    }
    public NoteFragment()
    {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static NoteFragment newInstance(String param1, String param2)
    {
        NoteFragment fragment = new NoteFragment();
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
        int userId=getUserIdFromSharedPreferences();//Chavon add userId
        // Furry: 更改arrayData，改成從資料庫拿的資料
        arrayData = getMemo(userId);//Chavon change userId
        // FurryEnd
        View contentView=inflater.inflate(R.layout.fragment_note,container,false);
        listViewData=contentView.findViewById(R.id.note_listview);
        adapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_multiple_choice, arrayData);
        listViewData.setAdapter(adapter);
        return contentView;
    }
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.note_menu_done,menu);
    }
    @Override
    public void onViewCreated(View view,Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        int userId=getUserIdFromSharedPreferences();//Chavon add userId
        // Furry: 新增一些參數
        String bufferCheck[];
        // FurryEnd
        //floatingButton
        FloatingActionButton shopping_btn=view.findViewById(R.id.note_btn_shopping);
        FloatingActionButton note_btn=view.findViewById(R.id.note_btn_notes);
        note_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showNoteDialog();
            }
        });
        shopping_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showShoppingDialog();
            }
        });

        // Furry: 將資料庫中，已完成的部分打勾
        bufferCheck = getMemoChecked(1);
        for(int i = 0; i < bufferCheck.length; i++)
        {
            if (bufferCheck[i].equals("1"))
            {
                listViewData.setSelection(i);
                listViewData.setItemChecked(i, true);
            }
        }
        // FurryEnd
        listViewData.setOnItemLongClickListener(this);
        // Furry: 當備忘錄完成打勾或取消打勾，要同步更新到資料庫
        listViewData.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Object item = listViewData.getItemAtPosition(position);
                // System.out.println(item);
                String itemName = item.toString();
                System.out.println(listViewData.isItemChecked(position));
                if (listViewData.isItemChecked(position))
                {
                    setFinish(userId, itemName, "1");//Chavon change userId
                }
                else
                {
                    setFinish(userId, itemName, "0");//Chavon change userId
                }
            }
        });
        // FurryEnd
    }
    // 新增備忘錄的填寫畫面
    private void showNoteDialog()
    {
        int userId=getUserIdFromSharedPreferences();//Chavon add userId
        LayoutInflater note=LayoutInflater.from(getActivity());
        final View textView=note.inflate(R.layout.note_text,null);
        final EditText inputName=textView.findViewById(R.id.inputName);
        final EditText inputDate=textView.findViewById(R.id.inputDate);
        final EditText inputTime=textView.findViewById(R.id.inputTime);
        inputName.setText("", TextView.BufferType.EDITABLE);
        inputDate.setText("", TextView.BufferType.EDITABLE);
        inputTime.setText("", TextView.BufferType.EDITABLE);
        inputDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Date
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
        incomeBuilder.setTitle("新增備忘錄").setView(textView)
                .setPositiveButton("新增", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // Furry: 新增功能的防呆跟匯入資料庫的部分
                        if (TextUtils.isEmpty(inputName.getText()) || TextUtils.isEmpty(inputDate.getText()) || TextUtils.isEmpty(inputTime.getText()))
                        {
                            Toast.makeText(getActivity(), "請將內容填寫完整再送出", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            System.out.println("備注: "+inputName.getText()+"日期: "+inputDate.getText()+"時間: "+inputTime.getText());
                            //Chavon change userId
                            addMemo(userId, inputName.getText().toString(), inputDate.getText().toString(), inputTime.getText().toString());
                            Intent intent = getActivity().getIntent();
                            getActivity().finish();
                            startActivity(intent);
                            Toast.makeText(getActivity(), "新增成功，請刷新頁面!", Toast.LENGTH_SHORT).show();
                        }
                        // FurryEnd
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
        AlertDialog noteDialog=incomeBuilder.create();
        noteDialog.show();
        noteDialog.getWindow().setLayout(700, 800);
        noteDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_bg);
    }
    private void showShoppingDialog()
    {
        int userId=getUserIdFromSharedPreferences();//Chavon add userId
        LayoutInflater shopping_memo=LayoutInflater.from(getActivity());
        final View textView=shopping_memo.inflate(R.layout.shopping_text,null);
        final EditText inputName=textView.findViewById(R.id.inputName);
        final EditText inputItems=textView.findViewById(R.id.inputItems);
        final ImageButton addButton=textView.findViewById(R.id.imageButton_add);
        inputName.setText("", TextView.BufferType.EDITABLE);
        inputItems.setText("", TextView.BufferType.EDITABLE);
        addButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //add editText
                LinearLayout linearLayout=textView.findViewById(R.id.addedEditTextLayout);
                EditText addEditText=new EditText(getActivity());
                addEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_notes_24,0,0,0);
                addEditText.setGravity(Gravity.CENTER);
                LinearLayout.LayoutParams params =new LinearLayout.LayoutParams(550,ViewGroup.LayoutParams.WRAP_CONTENT,1);
                addEditText.setLayoutParams(params);
                linearLayout.addView(addEditText);
            }
        });

        AlertDialog.Builder expenseBuilder=new AlertDialog.Builder(getActivity());
        expenseBuilder.setTitle("新增購物清單").setView(textView)
                .setPositiveButton("新增", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (TextUtils.isEmpty(inputName.getText()) || TextUtils.isEmpty(inputItems.getText()))
                        {
                            Toast.makeText(getActivity(), "請將內容填寫完整再送出", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            String category = inputName.getText().toString();
                            String items = inputItems.getText().toString();
                            String addedText = "";
                            LinearLayout addedEditTextLayout = textView.findViewById(R.id.addedEditTextLayout);
                            int num = 1;
                            for (int i = 0; i < addedEditTextLayout.getChildCount(); i++)
                            {
                                EditText addedEditText = (EditText) addedEditTextLayout.getChildAt(i);
                                if (!addedEditText.getText().toString().equals(""))
                                {
                                    num++;
                                    addedText += " ";
                                    addedText += addedEditText.getText().toString();
                                }
                            }
                            items = items + addedText;
                            addPurchasing(userId, category, items, num);//Chavonc change userId

                            Intent intent = getActivity().getIntent();
                            getActivity().finish();
                            startActivity(intent);;

                            Toast.makeText(getActivity(), "新增成功，請刷新頁面!", Toast.LENGTH_SHORT).show();
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
        AlertDialog shoppingMemoDialog=expenseBuilder.create();
        shoppingMemoDialog.show();
        shoppingMemoDialog.getWindow().setLayout(900, 1200);
        shoppingMemoDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_bg);
    }
    private void showEditDialog(int position)
    {
        int userId=getUserIdFromSharedPreferences();//Chavon add userId
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View editDialogView=inflater.inflate(R.layout.note_edit_data,null);
        final String selectedItem=arrayData[position];
        AlertDialog.Builder editViewBuilder=new AlertDialog.Builder(getActivity());
        // Furry: 我這裡有修改一些顯示的部分
        // 修改dialog名稱
        editViewBuilder.setTitle("刪除備忘錄資料");
        final EditText editText=editDialogView.findViewById(R.id.note_edit_Name);
        editText.setText(selectedItem);
        // 設定成不能更改，並改成顯示memo時間與日期
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false);
        final EditText editDate=editDialogView.findViewById(R.id.note_editDate);
        final EditText editTime=editDialogView.findViewById(R.id.note_editTime);
        String temp[] = getMemoDate(userId, selectedItem);//Chavon change userId
        editDate.setText(temp[0]);
        editTime.setText(temp[1]);
        // 把編輯的功能刪除 -> 因為不太好實現
//        editViewBuilder.setPositiveButton("更新", new DialogInterface.OnClickListener()
//        {
//            @Override
//            public void onClick(DialogInterface dialog, int which)
//            {
//                String updatedText=editText.getText().toString();
//                arrayData[position]=updatedText;
//                adapter.notifyDataSetChanged();
//                Toast.makeText(getActivity(), "已更新備忘錄", Toast.LENGTH_SHORT).show();
//            }
//        });
        editViewBuilder.setNegativeButton("刪除", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                DeleteMemo(userId, arrayData[position]);//Chavon change userId
                Intent intent = getActivity().getIntent();
                getActivity().finish();
                startActivity(intent);
                Toast.makeText(getActivity(), "刪除成功!", Toast.LENGTH_SHORT).show();
            }
        });
        editViewBuilder.setPositiveButton("取消", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        // FurryEnd
        editViewBuilder.setView(editDialogView);
        AlertDialog editDataDialog=editViewBuilder.create();
        editDataDialog.show();
        editDataDialog.getWindow().setLayout(700, 800);
        editDataDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_bg);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
    {
        showEditDialog(position);
        return true;
    }

    //  Furry: 連接資料庫，取得想要的內容
    public String[] getMemo(int userId)
    {
        userId=getUserIdFromSharedPreferences();//Chavon add userId
        ArrayList<String> MemoData = new ArrayList<String>();
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
                //Chavon change userId
                // SQL語句(顯示)
                //String sql = "select * from `memo` where `userid` =" + String.valueOf(userid) + " order by `memofinish` ASC";
                String sql = "select * from `memo` where `userid` =" + userId + " order by `memofinish` ASC";
                //System.out.println(sql); // 印出SQL語句
                // 我不知道下面是拿來幹嘛的
                Statement stmt = con.createStatement();
                // 將執行的結果存到rs裡面
                ResultSet rs = stmt.executeQuery(sql);

                // 將存回來的結果放到一個ArrayList
                while (rs.next())
                {
                    MemoData.add(rs.getString(3));
                }
                stmt.close();   // 關閉連線
            }
        }
        catch (Exception ex)
        {
            Log.e("Failed to Connection", "Please check your connection");
        }
        // 將ArrayList轉為Array儲存
        NoteData = (String[])MemoData.toArray(new String[MemoData.size()]);
//        System.out.println("MemoData: "+ Arrays.toString(NoteData));
        return NoteData;
    }
    // FurryEnd
    // Furry: 得到memo完成狀態
    public String[] getMemoChecked(int userId)
    {
        userId=getUserIdFromSharedPreferences();//Chavon add userId
        ArrayList<String> MemoChecked = new ArrayList<String>();
        String CheckedData[];
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
                //Chavon change userId
                // SQL語句(顯示)
                //String sql = "select * from `memo` where `userid` =" + String.valueOf(userid) + " order by `memofinish`, `memotime` ASC";
                String sql = "select * from `memo` where `userid` =" + userId + " order by `memofinish`, `memotime` ASC";
                //System.out.println(sql); // 印出SQL語句
                // 我不知道下面是拿來幹嘛的
                Statement stmt = con.createStatement();
                // 將執行的結果存到rs裡面
                ResultSet rs = stmt.executeQuery(sql);

                // 將存回來的結果放到一個ArrayList
                while (rs.next())
                {
                    MemoChecked.add(rs.getString(5));
                }
                stmt.close();   // 關閉連線
            }
        }
        catch (Exception ex)
        {
            Log.e("Failed to Connection", "Please check your connection");
        }
        // 將ArrayList轉為Array儲存
        CheckedData = (String[])MemoChecked.toArray(new String[MemoChecked.size()]);
        //System.out.println("MemoData: "+ Arrays.toString(CheckedData));
        return CheckedData;
    }
    // FurryEnd
    // Furry" 得到memo的日期
    public String[] getMemoDate(int userId, String content)
    {
        userId=getUserIdFromSharedPreferences();//Chavon add userId
        ArrayList<String> MemoDateTime = new ArrayList<String>();
        String DateData[];
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
                //Chavon change userId
                // SQL語句(顯示)
                //String sql = "select *, date(`memotime`), time(`memotime`) from `memo` where `userid` =" + String.valueOf(userid) + " and `memocontent` = '"+content+"'";
                String sql = "select *, date(`memotime`), time(`memotime`) from `memo` where `userid` =" + userId + " and `memocontent` = '"+content+"'";
                //System.out.println(sql); // 印出SQL語句
                // 我不知道下面是拿來幹嘛的
                Statement stmt = con.createStatement();
                // 將執行的結果存到rs裡面
                ResultSet rs = stmt.executeQuery(sql);

                // 將存回來的結果放到一個ArrayList
                while (rs.next())
                {
                    MemoDateTime.add(rs.getString(6));
                    MemoDateTime.add(rs.getString(7));
                }
                stmt.close();   // 關閉連線
            }
        }
        catch (Exception ex)
        {
            Log.e("Failed to Connection", "Please check your connection");
        }
        // 將ArrayList轉為Array儲存
        DateData = (String[])MemoDateTime.toArray(new String[MemoDateTime.size()]);
        //System.out.println("MemoData: "+ Arrays.toString(DateData));
        return DateData;
    }
    // FurryEnd
    // Furry: 刪除
    public void DeleteMemo(int userId, String item)
    {
        userId=getUserIdFromSharedPreferences();//Chavon add userId
        try
        {
            // 連線 & 找id
            Connection con = CONN();
            if (con == null)
            {
                Log.e("Failed to Connection", "Please check your connection");
            }
            else
            {
                //Chavon change userId
                // SQL語句(顯示)
                //String sql = "delete from `memo` where `userid` = " + String.valueOf(userid) + " and `memocontent` = " +"'" + item +"'";
                String sql = "delete from `memo` where `userid` = " + userId + " and `memocontent` = " +"'" + item +"'";
                //System.out.println(sql); // 印出SQL語句
                // 我不知道下面是拿來幹嘛的
                Statement stmt = con.createStatement();
                // 刪除
                stmt.executeUpdate(sql);

                stmt.close();   // 關閉連線
            }
        }
        catch (Exception ex)
        {
            Log.e("Failed to Connection", "The action is not successful");
        }
    }
    // FurryEnd
    // Furry: 新增
    public void addMemo(int userId, String memoName, String memoDate, String memoTime)
    {
        userId=getUserIdFromSharedPreferences();//Chavon add userId
        try
        {
            // 連線 & 找id
            Connection con = CONN();
            if (con == null)
            {
                Log.e("Failed to Connection", "Please check your connection");
            }
            else
            {
                //Chavon change userId
                // SQL語句(顯示)
                //String sql = "insert into `memo` (`memoid`, `userid`, `memocontent`, `memotime`, `memofinish`) VALUES (NULL, "+String.valueOf(userid)+", '"+memoName+"' ,'"+memoDate+" "+memoTime+"', '0')";
                String sql = "insert into `memo` (`memoid`, `userid`, `memocontent`, `memotime`, `memofinish`) VALUES (NULL, "+userId+", '"+memoName+"' ,'"+memoDate+" "+memoTime+"', '0')";
                //System.out.println(sql); // 印出SQL語句
                // 我不知道下面是拿來幹嘛的
                Statement stmt = con.createStatement();
                // 新增
                stmt.executeUpdate(sql);

                stmt.close();   // 關閉連線
            }
        }
        catch (Exception ex)
        {
            Log.e("Failed to Connection", "The action is not successful");
        }
    }
    public void addPurchasing(int userId, String purchasingName, String items, int num)
    {
        userId=getUserIdFromSharedPreferences();//Chavon add userId
        int purchasingid = 0;
        try
        {
            Connection con = CONN();
            if (con == null)
            {
                Log.e("Failed to Connection", "Please check your connection");
            }
            else
            {
                //Chavon change userId
                //String sql = "INSERT INTO `purchasing`(`purchasingid`, `userid`, `purchasingname`, `amount`, `purchasingtime`, `purchasingfinish`) VALUES (NULL, "+String.valueOf(userid)+", '"+purchasingName+"' ,NULL, NULL, 0)";
                String sql = "INSERT INTO `purchasing`(`purchasingid`, `userid`, `purchasingname`, `amount`, `purchasingtime`, `purchasingfinish`) VALUES (NULL, "+userId+", '"+purchasingName+"' ,NULL, NULL, 0)";
                Statement stmt = con.createStatement();
                stmt.executeUpdate(sql);

                sql = "SELECT * FROM `purchasing` WHERE `purchasingname`= '" + purchasingName + "'";
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next())
                {
                    purchasingid = rs.getInt(1);
                }
                System.out.println("items: "+items);
                String finish = "0";
                for (int i = 1; i<num; i++) {finish += " 0";}
                sql = "INSERT INTO `purchasingitem`(`purchasingid`, `purchasingitem`, `purchasingitemfinish`) VALUES ('" + purchasingid + "','" + items + "','" + finish + "')";
                stmt.executeUpdate(sql);
                stmt.close();
            }
        }
        catch (Exception ex)
        {
            Log.e("Failed to Connection", "The action is not successful");
        }
    }
    // FurryEnd
    // Furry: 更新完成狀態
    public void setFinish(int userId, String itemName, String state)
    {
        userId=getUserIdFromSharedPreferences();//Chavon add userId
        try
        {
            // 連線 & 找id
            Connection con = CONN();
            if (con == null)
            {
                Log.e("Failed to Connection", "Please check your connection");
            }
            else
            {
                //Chavon change userId
                // SQL語句(更新)
                //String sql = "update `memo` set `memofinish` = '"+state+"' where `memocontent` = '"+itemName+"' and `userid` = '"+String.valueOf(userid)+"'";
                String sql = "update `memo` set `memofinish` = '"+state+"' where `memocontent` = '"+itemName+"' and `userid` = '"+userId+"'";
                //System.out.println(sql); // 印出SQL語句
                // 我不知道下面是拿來幹嘛的
                Statement stmt = con.createStatement();
                // 新增
                stmt.executeUpdate(sql);

                stmt.close();   // 關閉連線
            }
        }
        catch (Exception ex)
        {
            Log.e("Failed to Connection", "The action is not successful");
        }

    }
    // FurryEnd
    // Furry: 連線用的function
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
    // FurryEnd
}