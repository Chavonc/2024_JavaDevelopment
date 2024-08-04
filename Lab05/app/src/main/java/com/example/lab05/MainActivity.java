//package com.example.lab05;
//
//import android.Manifest;
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//
//public class MainActivity extends AppCompatActivity
//{
//    ImageView imageView;
//    TextView view1;
//    Button cameraBtn,recordBtn,stopRecording,showRecording;
//    //camera
//    public static final int CAMERA_PERM_CODE = 101;
//    public static final int CAMERA_REQUEST_CODE = 102;
//    private static final String IMAGE_FOLDER = "Lab05_Camera";
//    //video recording
//    private static final int REQUEST_VIDEO_CAPTURE = 104;
//    private static final int RECORD_PERMISSION_CODE = 103;
//    private File filePath;
//    @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        view1=findViewById(R.id.showText);
//        imageView = findViewById(R.id.imageView);
//        //Button
//        cameraBtn = findViewById(R.id.button1);
//        recordBtn = findViewById(R.id.button2);
//        showRecording = findViewById(R.id.button3);
//        //取得權限
//        cameraBtn.setOnClickListener(view -> askCameraPermissions());
//        recordBtn.setOnClickListener(view -> askRecordingPermissions());
//        //建立儲存檔案路徑的File
//        filePath = new File(Environment.getExternalStorageDirectory(), "lab05_myVideo.3gp");
//    }
//    private void askCameraPermissions()
//    {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
//        {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
//        }
//        else
//        {
//            openCamera();
//        }
//    }
//    private void askRecordingPermissions()
//    {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
//        {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_PERMISSION_CODE);
//            Log.d("Lab 05", "if裡的RECORD_PERMISSION_CODE是: "+RECORD_PERMISSION_CODE);
//        }
//        else
//        {
//            Log.d("Lab 05", "else裡的RECORD_PERMISSION_CODE是: "+RECORD_PERMISSION_CODE);
//            openRecording();
//        }
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
//    {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == CAMERA_PERM_CODE)
//        {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
//            {
//                openCamera();
//            }
//            else
//            {
//                Toast.makeText(this, "需要取得內建拍照權限", Toast.LENGTH_SHORT).show();
//            }
//        }
//        else if (requestCode == RECORD_PERMISSION_CODE)
//        {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
//            {
//                Log.d("Lab 05", "已取得內建錄影權限");
//                openRecording();
//            }
//            else
//            {
//                Toast.makeText(this, "需要取得內建錄影權限", Toast.LENGTH_SHORT).show();
//            }
//        }
//
//    }
//    private void openCamera()
//    {
//        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(camera, CAMERA_REQUEST_CODE);
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
//    {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == CAMERA_REQUEST_CODE)
//        {
//            assert data != null;
//            Bitmap image = (Bitmap) data.getExtras().get("data");
//            imageView.setImageBitmap(image);
//            saveImage(image);
//        }
//        if(requestCode==REQUEST_VIDEO_CAPTURE && resultCode == Activity.RESULT_OK)
//        {
//            // 取得傳回媒體儲存的檔案路徑
//            String path = data.getData().toString();
//            view1.setText(path); //顯示路徑
//        }
//
//    }
//    @SuppressLint("SetTextI18n")
//    private void saveImage(Bitmap image)
//    {
//        // 檢查是否有外部儲存許可權
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
//        {
//            File imageFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_FOLDER);
//            if (!imageFolder.exists())
//            {
//                imageFolder.mkdirs(); // 如果文件夾不存在，則創建
//            }
//
//            // 生成文件名
//            String fileName = "lab05_myImage.jpg";
//            File imageFile = new File(imageFolder, fileName);
//
//            try
//            {
//                FileOutputStream fos = new FileOutputStream(imageFile);
//                image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//                fos.flush();
//                fos.close();
//
//                // 通知系統圖片已保存
//                MediaStore.Images.Media.insertImage(getContentResolver(), imageFile.getAbsolutePath(), fileName, null);
//                view1.setText("圖片已保存在"+ imageFile.getAbsolutePath());
//                Toast.makeText(this, "圖片已保存在 " + imageFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
//            }
//            catch (IOException e)
//            {
//                e.printStackTrace();
//                Log.d("Lab 05", "保存圖片失敗: " + e);
//                view1.setText("保存圖片失敗"+ e);
//            }
//        }
//        else
//        {
//            // 如果沒有外部儲存許可權，請求許可權
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//            Toast.makeText(this, "請求外部儲存許可權", Toast.LENGTH_SHORT).show();
//        }
//    }
//    //Video Recording
//    private void openRecording()
//    {
//        // 建立使用內建程式錄影的Intent物件
//        Intent intent2 = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//        // 新增附件為儲存的媒體檔案
//        intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(filePath));
//        // 指定錄影品質
//        intent2.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
//        startActivityForResult(intent2, REQUEST_VIDEO_CAPTURE);
//    }
//    //Button元件的事件處理
//    public void useCameraTakePicture(View view)
//    {
//        Toast.makeText(this, "啟動內建相機照相功能", Toast.LENGTH_SHORT).show();
//    }
//    public void useRecording(View view)
//    {
//        Log.d("Lab 05", "成功啟動內建錄影功能");
//        askRecordingPermissions();
//        // 使用Intent物件啟動VideoRecorder活動
////        Intent intent = new Intent(this, VideoRecorder.class);
////        startActivity(intent);
//    }
//    public void showRecording(View view)
//    {
//        Toast.makeText(this, "正播放錄影...", Toast.LENGTH_SHORT).show();
//        Intent intent3 = new Intent(this, VideoPlayer.class);
//        intent3.putExtra("videoPath", filePath.getAbsolutePath());
//        startActivity(intent3);
//    }
//}
package com.example.lab05;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity
{
    ImageView imageView;
    TextView view1;
    Button cameraBtn,recordBtn;
    //camera
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    private static final String IMAGE_FOLDER = "Lab05_Camera";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view1=findViewById(R.id.showText);
        imageView = findViewById(R.id.imageView);
        //Button
        cameraBtn = findViewById(R.id.button1);
        recordBtn = findViewById(R.id.button2);
        //取得權限
        cameraBtn.setOnClickListener(view -> askCameraPermissions());
    }
    private void askCameraPermissions()
    {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        }
        else
        {
            openCamera();
        }
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERM_CODE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                openCamera();
            }
            else
            {
                Toast.makeText(this, "需要取得內建拍照權限", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void openCamera()
    {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, CAMERA_REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_REQUEST_CODE)
        {
            assert data != null;
            Bitmap image = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(image);
            saveImage(image);
        }
    }
    @SuppressLint("SetTextI18n")
    private void saveImage(Bitmap image)
    {
        // 檢查是否有外部儲存許可權
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            File imageFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_FOLDER);
            if (!imageFolder.exists())
            {
                imageFolder.mkdirs(); // 如果文件夾不存在，則創建
            }

            // 生成文件名
            String fileName = "lab05_myImage.jpg";
            File imageFile = new File(imageFolder, fileName);

            try
            {
                FileOutputStream fos = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();

                // 通知系統圖片已保存
                MediaStore.Images.Media.insertImage(getContentResolver(), imageFile.getAbsolutePath(), fileName, null);
                view1.setText("圖片已保存在"+ imageFile.getAbsolutePath());
                Toast.makeText(this, "圖片已保存在 " + imageFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            }
            catch (IOException e)
            {
                e.printStackTrace();
                Log.d("Lab 05", "保存圖片失敗: " + e);
                view1.setText("保存圖片失敗"+ e);
            }
        }
        else
        {
            // 如果沒有外部儲存許可權，請求許可權
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            Toast.makeText(this, "請求外部儲存許可權", Toast.LENGTH_SHORT).show();
        }
    }
    //Button元件的事件處理
    public void useCameraTakePicture(View view)
    {
        Toast.makeText(this, "啟動內建相機照相功能", Toast.LENGTH_SHORT).show();
    }
    public void useRecording(View view)
    {
        Log.d("Lab 05", "已跳至要進行內建錄影的畫面");
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }
}