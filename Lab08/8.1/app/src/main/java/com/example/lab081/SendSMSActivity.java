package com.example.lab081;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class SendSMSActivity extends AppCompatActivity {

    EditText edtSmsRecipient, edtSmsMessage;
    ImageButton btnSendSmsAction; // Nút gửi SMS hình tin nhắn
    Button btnBack2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Sử dụng layout đã tạo ở XML
        setContentView(R.layout.activity_send_smsactivity); // Đảm bảo tên layout XML là chính xác

        edtSmsRecipient = findViewById(R.id.edtsms);
        edtSmsMessage = findViewById(R.id.edtmessage); // Thêm EditText cho nội dung tin nhắn
        btnSendSmsAction = findViewById(R.id.btnsms); // ID của ImageButton gửi SMS
        btnBack2 = findViewById(R.id.btnback2);

        btnSendSmsAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String recipient = edtSmsRecipient.getText().toString().trim();
                String message = edtSmsMessage.getText().toString().trim();

                if (recipient.isEmpty()) {
                    Toast.makeText(SendSMSActivity.this, "Please enter recipient number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (message.isEmpty()) {
                    Toast.makeText(SendSMSActivity.this, "Please enter message", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Tạo Intent để mở ứng dụng SMS mặc định
                Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
                smsIntent.setData(Uri.parse("smsto:" + recipient)); // Chỉ định người nhận
                smsIntent.putExtra("sms_body", message); // Đặt nội dung tin nhắn

                // Kiểm tra xem có ứng dụng nào xử lý Intent này không
                if (smsIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(smsIntent);
                } else {
                    Toast.makeText(SendSMSActivity.this, "No SMS app found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Đóng Activity hiện tại và quay lại màn hình trước
            }
        });
    }
}