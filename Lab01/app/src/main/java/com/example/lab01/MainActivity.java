package com.example.lab01;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    // Khai báo các biến giao diện
    EditText edtA, edtB, edtKQ;
    Button btncong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ ID cho các biến giao diện
        edtA = findViewById(R.id.edtA);
        edtB = findViewById(R.id.edtB);
        edtKQ = findViewById(R.id.edtKQ);
        btncong = findViewById(R.id.btntong);

        // Xử lý sự kiện click
        btncong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int a = Integer.parseInt(edtA.getText().toString());
                    int b = Integer.parseInt(edtB.getText().toString());
                    int c = a + b;
                    edtKQ.setText(String.valueOf(c));
                } catch (NumberFormatException e) {
                    edtKQ.setText("Lỗi nhập số");
                }
            }
        });
    }
}
