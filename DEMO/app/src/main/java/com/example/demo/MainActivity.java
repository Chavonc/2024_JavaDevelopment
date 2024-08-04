package com.example.demo;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
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
    }
    public void clickBtn1(View view)
    {
        Toast.makeText(MainActivity.this, "按了選項按鈕", Toast.LENGTH_SHORT).show();
    }
    public void clickBtn3(View view)
    {
        openContextMenu(view);
        //Toast.makeText(MainActivity.this, "按了上下文選單按鈕", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("NonConstantResourceId")
    public void clickBtn2(View view)
    {
        PopupMenu popupMenu = new PopupMenu(MainActivity.this,view );
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(menuItem ->
        {
            int id = menuItem.getItemId();
            switch (id)
            {
                case R.id.blue_pop:
                    Btn2.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                    Toast.makeText(MainActivity.this, "已選擇藍色", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.purple_pop:
                    Btn2.setBackgroundColor(getResources().getColor(android.R.color.holo_purple));
                    Toast.makeText(MainActivity.this, "已選擇紫色", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.green_pop:
                    Btn2.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                    Toast.makeText(MainActivity.this, "已選擇綠色", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            return true;
        });
        popupMenu.show();
    }
    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(contextMenu,v,menuInfo);
        contextMenu.setHeaderTitle("選擇要更換的按鈕顏色");
        getMenuInflater().inflate(R.menu.context_menu, contextMenu);
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.context_red:
                Btn3.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                Toast.makeText(MainActivity.this, "已選擇更改為紅色", Toast.LENGTH_LONG).show();
                textView.setText("上下文選單: \n紅色按鈕主題");
                break;
            case R.id.context_orange:
                Btn3.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
                Toast.makeText(MainActivity.this, "已選擇更改為橘色", Toast.LENGTH_LONG).show();
                textView.setText("上下文選單: \n橘色按鈕主題");
                break;
            case R.id.context_green:
                Btn3.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                Toast.makeText(MainActivity.this, "已選擇更改為綠色", Toast.LENGTH_LONG).show();
                textView.setText("上下文選單: \n綠色按鈕主題");
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
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
                return true;
            case R.id.blueRadioButton:
                Btn1.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                Toast.makeText(MainActivity.this, "已選擇更改為藍色", Toast.LENGTH_SHORT).show();
                textView.setText("選項按鈕選單: \n藍色按鈕主題");
                item.setChecked(!item.isChecked());
                return true;
            case R.id.purpleRadioButton:
                Btn1.setBackgroundColor(getResources().getColor(android.R.color.holo_purple));
                Toast.makeText(MainActivity.this, "已選擇更改為紫色", Toast.LENGTH_SHORT).show();
                textView.setText("選項按鈕選單: \n紫色按鈕主題");
                item.setChecked(!item.isChecked());
                return true;
            case R.id.greenRadioButton:
                Btn1.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                Toast.makeText(MainActivity.this, "已選擇更改為綠色", Toast.LENGTH_SHORT).show();
                textView.setText("選項按鈕選單: \n綠色按鈕主題");
                item.setChecked(!item.isChecked());
                return true;
            case R.id.jot_notes:
                Toast.makeText(MainActivity.this, "已轉換成記事本模式", Toast.LENGTH_SHORT).show();
                textView.setText("目前在記事本頁面");
                return true;
            case R.id.todoList:
                Toast.makeText(MainActivity.this, "已轉換成待辦事項模式", Toast.LENGTH_SHORT).show();
                textView.setText("目前在待辦事項頁面");
                return true;
            case R.id.schedule:
                Toast.makeText(MainActivity.this, "已選擇查看課表", Toast.LENGTH_SHORT).show();
                textView.setText("目前在課表頁面");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}