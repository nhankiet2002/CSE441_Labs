package com.example.a82;

import static android.provider.MediaStore.ACTION_IMAGE_CAPTURE;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageView myimg;
    ImageButton btncapture;

    private static final int REQUEST_CAMERA_PERMISSION = 101;
    private static final int REQUEST_IMAGE_CAPTURE = 99; // Giữ nguyên 99 như trong tài liệu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myimg = findViewById(R.id.myimg);
        btncapture = findViewById(R.id.btncapture);

        btncapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kiểm tra quyền CAMERA
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Nếu chưa có quyền, yêu cầu quyền
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                } else {
                    // Nếu đã có quyền, mở camera
                    openCamera();
                }
            }
        });
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(ACTION_IMAGE_CAPTURE); // Hoặc MediaStore.ACTION_IMAGE_CAPTURE
        // Kiểm tra xem có ứng dụng camera nào để xử lý intent này không
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            Toast.makeText(this, "Không tìm thấy ứng dụng Camera", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Quyền đã được cấp, mở camera
                openCamera();
            } else {
                // Quyền bị từ chối
                Toast.makeText(this, "Quyền Camera bị từ chối", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                if (photo != null) {
                    myimg.setImageBitmap(photo);
                } else {
                    Toast.makeText(this, "Không lấy được ảnh", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Không có dữ liệu trả về", Toast.LENGTH_SHORT).show();
            }
        }
    }
}