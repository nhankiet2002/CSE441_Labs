package com.example.a73;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class SubActivity extends AppCompatActivity {
    EditText edtA, edtB;
    Button btnTong, btnHieu;

    int a, b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        edtA = findViewById(R.id.edtAA);
        edtB = findViewById(R.id.edtBB);
        btnTong = findViewById(R.id.btnsendtong);
        btnHieu = findViewById(R.id.btnsendhieu);

        Intent intent = getIntent();
        a = intent.getIntExtra("soa", 0);
        b = intent.getIntExtra("sob", 0);

        edtA.setText(String.valueOf(a));
        edtB.setText(String.valueOf(b));

        btnTong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tong = a + b;
                Intent resultIntent = new Intent();
                resultIntent.putExtra("kq", tong);
                setResult(33, resultIntent);
                finish();
            }
        });

        btnHieu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hieu = a - b;
                Intent resultIntent = new Intent();
                resultIntent.putExtra("kq", hieu);
                setResult(34, resultIntent);
                finish();
            }
        });
    }
}
