package com.example.lab02;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
    AlertDialog alertdialog1,alertdialog2;
    String[] items = {"本課堂簡介", "記事本", "待辦事項", "課表"};
    boolean[] itemsChecked = new boolean[items.length];
    boolean atLeastOneSelected = false;
    int index=0;
    String msg = "";
    Button Btn1,Btn2,Btn3;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Btn1=findViewById(R.id.btn1);
        Btn2=findViewById(R.id.btn2);
        Btn3=findViewById(R.id.btn3);
        textView = findViewById(R.id.textView);
        registerForContextMenu(Btn3);
        //alertdialog1
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                switch(which)
                {
                    case 0:
                        Btn3.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                        textView.setText("單選對話方塊: 藍色");
                        break;
                    case 1:
                        Btn3.setBackgroundColor(getResources().getColor(android.R.color.holo_purple));
                        textView.setText("單選對話方塊: 紫色");
                        break;
                    case 2:
                        Btn3.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                        textView.setText("單選對話方塊: 綠色");
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("選擇要更改的顏色");
        String[] options = {"藍色", "紫色", "綠色" };
        builder.setItems(options, listener);
        builder.setNegativeButton("取消", null);
        alertdialog1 = builder.create();
        //alertdialog2
        alertdialog2 = new AlertDialog.Builder(this)
                .setTitle("請勾選喜歡什麼模式(可複選)?")
                .setPositiveButton("確定",
                        new DialogInterface.OnClickListener()
                        {
                            @SuppressLint("SetTextI18n")
                            public void onClick(DialogInterface dialoginterface, int i)
                            {
                                msg="";
                                for (index = 0; index < items.length; index++)
                                {
                                    if (itemsChecked[index])
                                    {
                                        msg += items[index] + "\n";
                                        atLeastOneSelected = true;
                                    }
                                }
                                if(atLeastOneSelected)
                                {
                                    textView.setText("謝謝您的意見!\n你的選項是:\n"+msg);
                                }
                                else
                                {
                                    Toast.makeText(MainActivity.this, "請至少選擇一個選項", Toast.LENGTH_SHORT).show();
                                }
                                for (index = 0; index < itemsChecked.length; index++)
                                {
                                    itemsChecked[index] = false;
                                }
                                ((AlertDialog) dialoginterface).getListView().clearChoices();
                                ((AlertDialog) dialoginterface).getListView().requestLayout();
                            }
                        })
                .setNegativeButton("取消", null)
                .setMultiChoiceItems(items,itemsChecked,
                        (dialog, which, isChecked) -> Toast.makeText(MainActivity.this, (isChecked ? "勾選 " : "取消勾選 ")+items[which], Toast.LENGTH_SHORT).show())
                .create();
    }
    public void clickBtn3(View view)
    {
        alertdialog1.show();
        Toast.makeText(MainActivity.this, "使用單選對話方塊", Toast.LENGTH_SHORT).show();
    }
    public void clickBtn1(View view)
    {
        alertdialog2.show();
        Toast.makeText(MainActivity.this, "使用複選對話方塊", Toast.LENGTH_SHORT).show();
    }
    public void clickBtn2(View view)
    {
        AlertDialog.Builder builder5 = new AlertDialog.Builder(this);
        builder5.setTitle("確認")
                .setMessage("確認結束本程式?")
                .setPositiveButton("確定",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialoginterface, int i)
                            {
                                finish();
                            }
                        })
                .setNegativeButton("取消", null)
                .show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }
    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.theme_setting:
                Toast.makeText(MainActivity.this, "已選擇查看本課堂簡介", Toast.LENGTH_SHORT).show();
                textView.setText("目前在課堂簡介頁面");
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setTitle("關於本課堂");
                builder1.setMessage("版本: 4.4.2版\n課程:嵌入式軟體設計");
                builder1.setPositiveButton
                        (
                                "好的",
                                new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialoginterface, int i)
                                    {
                                        textView.setText("查看訊息對話方塊完畢");
                                    }
                                }
                        );
                builder1.show();
                return true;
            case R.id.jot_notes:
                Toast.makeText(MainActivity.this, "已選擇查看我的記事本", Toast.LENGTH_SHORT).show();
                textView.setText("目前在記事本頁面");
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setTitle("關於我的記事本");
                builder2.setMessage("嵌入式軟體設計繳交事項:\n1.小組報告\n2.個人報告");
                builder2.setPositiveButton
                        (
                                "我記住了",
                                new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialoginterface, int i)
                                    {
                                        textView.setText("查看訊息對話方塊完畢");
                                    }
                                }
                        );
                builder2.show();
                return true;
            case R.id.todoList:
                Toast.makeText(MainActivity.this, "已選擇查看我的待辦事項", Toast.LENGTH_SHORT).show();
                textView.setText("目前在待辦事項頁面");
                AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
                builder3.setTitle("關於我的待辦事項");
                builder3.setMessage("1.中秋過節\n2.開會開會QAQ");
                builder3.setPositiveButton
                        (
                                "我記住了",
                                new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialoginterface, int i)
                                    {
                                        textView.setText("查看訊息對話方塊完畢");
                                    }
                                }
                        );
                builder3.show();
                return true;
            case R.id.schedule:
                Toast.makeText(MainActivity.this, "已選擇查看我的課表", Toast.LENGTH_SHORT).show();
                textView.setText("目前在課表頁面");
                AlertDialog.Builder builder4 = new AlertDialog.Builder(this);
                builder4.setTitle("關於我的課表");
                builder4.setMessage("學期: 112-1\n課程如下:\n週一: 15:10 嵌入式軟體設計\n週二:\n週三:\n週四:\n週五: 09:10 商業攝影");
                builder4.setPositiveButton
                        (
                                "我記住了",
                                new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialoginterface, int i)
                                    {
                                        textView.setText("查看訊息對話方塊完畢");
                                    }
                                }
                        );
                builder4.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}