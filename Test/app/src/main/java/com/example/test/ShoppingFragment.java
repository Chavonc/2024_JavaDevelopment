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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
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
import java.util.HashMap;
import java.util.List;

public class ShoppingFragment extends Fragment implements AdapterView.OnItemLongClickListener
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private FragmentManager fragmentManager;
    private ListView listViewData;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;
    ArrayAdapter<String> adapter;
    String[] arrayData={"聖誕節派對","零食","日常用品","約會"};
    String mysql_ip = "10.0.2.2";
    int mysql_port = 3306; // Port 預設為 3306
    String db_name = "embeded";
    String urlGetDBParameters = "autoReconnect=false&useSSL=false";
    String url = "jdbc:mysql://"+mysql_ip+":"+mysql_port+"/"+db_name;
    String db_user = "root";
    String db_password = "Yejiching8";
    public ShoppingFragment()
    {
        // Required empty public constructor
    }

    //Chavon add
    private int getUserIdFromSharedPreferences()
    {
        // 使用 SharedPreferences 讀取 UserId
        SharedPreferences preferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return preferences.getInt("UserId", 0);
    }
    public static ShoppingFragment newInstance(String param1, String param2)
    {
        ShoppingFragment fragment = new ShoppingFragment();
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
        super.onCreate(savedInstanceState);
        View contentView = inflater.inflate(R.layout.fragment_shopping,container,false);
        expandableListView = (ExpandableListView) contentView.findViewById(R.id.shopping_expandableListView);
        ExpandableListDataPump.setUserId(getUserIdFromSharedPreferences());
        expandableListDetail = ExpandableListDataPump.getData();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(getContext(), expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        return contentView;
    }
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.shopping_menu_done,menu);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton shopping_btn=view.findViewById(R.id.shopping_btn_shopping);
        FloatingActionButton note_btn=view.findViewById(R.id.shopping_btn_notes);
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
        expandableListView.setOnItemLongClickListener(this);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
        {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,int groupPosition, int childPosition, long id)
            {
                String name = expandableListTitle.get(groupPosition);
                String item = expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition);
                CheckBox cb = (CheckBox) v.findViewById( R.id.shopping_checkBox );
                if (cb.isChecked())
                {
                    cb.setChecked(false);
                    setFinish(name, childPosition, "0");
                }
                else
                {
                    cb.setChecked(true);
                    System.out.println("1");
                    setFinish(name, childPosition, "1");
                }
                return true;
            }
        });
    }
    private void showNoteDialog()
    {
        //int userId = getUserIdFromSharedPreferences();//Chavon add userId
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
                        if (TextUtils.isEmpty(inputName.getText()) || TextUtils.isEmpty(inputDate.getText()) || TextUtils.isEmpty(inputTime.getText()))
                        {
                            Toast.makeText(getActivity(), "請將內容填寫完整再送出", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            //Chavon change userId
                            addMemo(getUserIdFromSharedPreferences(), inputName.getText().toString(), inputDate.getText().toString(), inputTime.getText().toString());

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
        AlertDialog noteDialog=incomeBuilder.create();
        noteDialog.show();
        noteDialog.getWindow().setLayout(700, 800);
        noteDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_bg);
    }
    private void showShoppingDialog()
    {
        //int userId=getUserIdFromSharedPreferences();//Chavon add userId
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
                        else
                        {
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
                                    addedText += "|";
                                    addedText += addedEditText.getText().toString();
                                }
                            }
                            items = items + addedText;
                            //Chavon change userId
                            addPurchasing(getUserIdFromSharedPreferences(), category, items, num);

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
    private void showEditDialog(String name)
    {
        //int userId=getUserIdFromSharedPreferences();//Chavon add userId
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View editDialogView=inflater.inflate(R.layout.shopping_edit_data,null);
        final String selectedItem=name;

        AlertDialog.Builder editViewBuilder=new AlertDialog.Builder(getActivity());
        editViewBuilder.setTitle("刪除採購資料");
        final EditText editText=editDialogView.findViewById(R.id.shopping_edit_Name);
        editText.setText(selectedItem);
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false);
        editViewBuilder.setNegativeButton("刪除", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                //Chavon change userId
                deletePurchasing(getUserIdFromSharedPreferences(), name);
                Intent intent = getActivity().getIntent();
                getActivity().finish();
                startActivity(intent);;

                Toast.makeText(getActivity(), "刪除成功，請刷新頁面!", Toast.LENGTH_SHORT).show();
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
        editViewBuilder.setView(editDialogView);
        AlertDialog editDataDialog=editViewBuilder.create();
        editDataDialog.show();
        editDataDialog.getWindow().setLayout(700, 500);
        editDataDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_bg);
    }
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
    {
        String name = (String) parent.getItemAtPosition(position);
        if (isGroup(name))
        {
            showEditDialog(name);
            return true;
        }
        return false;
    }
    public void addMemo(int userid, String memoName, String memoDate, String memoTime)
    {
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
                String sql = "insert into `memo` (`memoid`, `userid`, `memocontent`, `memotime`, `memofinish`) VALUES (NULL, "+String.valueOf(userid)+", '"+memoName+"' ,'"+memoDate+" "+memoTime+"', '0')";
                Statement stmt = con.createStatement();
                stmt.executeUpdate(sql);
                stmt.close();
            }
        }
        catch (Exception ex)
        {
            Log.e("Failed to Connection", "The action is not successful");
        }
    }
    public void addPurchasing(int userid, String purchasingName, String items, int num)
    {
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
                String sql = "INSERT INTO `purchasing`(`purchasingid`, `userid`, `purchasingname`, `amount`, `purchasingtime`, `purchasingfinish`) VALUES (NULL, "+String.valueOf(userid)+", '"+purchasingName+"' ,NULL, NULL, 0)";
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
                for (int i = 1; i<num; i++) {finish += "|0";}
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
    public void deletePurchasing(int userid, String name)
    {
        try
        {
            Connection con = CONN();
            if (con == null)
            {
                Log.e("Failed to Connection", "Please check your connection");
            }
            else
            {
                String sql = "delete from `purchasing` where `userid` = " + String.valueOf(userid) + " and `purchasingname` = " +"'" + name +"'";
                Statement stmt = con.createStatement();
                stmt.executeUpdate(sql);
                stmt.close();
            }
        }
        catch (Exception ex)
        {
            Log.e("Failed to Connection", "The action is not successful");
        }
    }
    public void setFinish(String name, int childPosition, String state)
    {
        System.out.println("pos: "+childPosition);
        Integer id = 0;
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


                String [] splitFinish = finish.split("\\|+");
                splitFinish[childPosition] = state;
                finish = splitFinish[0];
                for (int i = 1; i<splitFinish.length; i++)
                {
                    finish += "|";
                    finish += splitFinish[i];
                }
                sql = "UPDATE `purchasingitem` SET `purchasingitemfinish`='" + finish + "' WHERE `purchasingid`= '" + id + "'";

                stmt = con.createStatement();
                stmt.executeUpdate(sql);
                stmt.close();

            }
        }
        catch (Exception ex)
        {
            Log.e("Failed to Connection", "The action is not successful");
        }
    }
    public boolean isGroup(String name)
    {
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
                    return true;
                }
                stmt.close();

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
