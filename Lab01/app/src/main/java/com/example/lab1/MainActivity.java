package com.example.lab1;

import android.os.Bundle;
import android.view.View; // Sửa ở đây
import android.widget.Button; // Thêm import này
import android.widget.EditText; // Thêm import này

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    EditText edtA, edtB, edtKQ;
    Button btncong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ Id cho các biến giao diện
        edtA = findViewById(R.id.edtA);
        edtB = findViewById(R.id.edtB);
        edtKQ = findViewById(R.id.edtKQ);
        btncong = findViewById(R.id.btntong);

        // Xử lý tương tác với người dùng
        btncong.setOnClickListener(new View.OnClickListener() { // Đổi thành android.view.View
            @Override
            public void onClick(View view) {
                int a = Integer.parseInt(edtA.getText().toString());
                int b = Integer.parseInt(edtB.getText().toString());
                int c = a + b;
                edtKQ.setText(String.valueOf(c));
            }
        });
    }
}
