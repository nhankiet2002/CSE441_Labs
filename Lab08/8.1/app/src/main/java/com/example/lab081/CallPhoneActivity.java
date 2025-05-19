package com.example.lab081;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class CallPhoneActivity extends AppCompatActivity {

    EditText edtCall;
    ImageButton btnCallAction; // Nút gọi hình điện thoại
    Button btnBack1;
    private static final int REQUEST_CALL_PHONE_PERMISSION = 1;
    private String phoneNumberToCall;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Sử dụng layout đã tạo ở XML
        setContentView(R.layout.activity_call_phone);

        edtCall = findViewById(R.id.edtcall);
        btnCallAction = findViewById(R.id.btncall);
        btnBack1 = findViewById(R.id.btnback1);

        btnCallAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumberToCall = edtCall.getText().toString().trim();
                if (phoneNumberToCall.isEmpty()) {
                    Toast.makeText(CallPhoneActivity.this, "Please enter a phone number", Toast.LENGTH_SHORT).show();
                    return;
                }
                makePhoneCall();
            }
        });

        btnBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void makePhoneCall() {
        if (ContextCompat.checkSelfPermission(CallPhoneActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // Quyền chưa được cấp, yêu cầu quyền
            ActivityCompat.requestPermissions(CallPhoneActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE_PERMISSION);
        } else {
            // Quyền đã được cấp, thực hiện cuộc gọi
            if (phoneNumberToCall != null && !phoneNumberToCall.isEmpty()) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phoneNumberToCall));
                startActivity(callIntent);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PHONE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Quyền được cấp sau khi yêu cầu
                makePhoneCall();
            } else {
                // Quyền bị từ chối
                Toast.makeText(this, "Call permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}