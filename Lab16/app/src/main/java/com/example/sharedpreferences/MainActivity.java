package com.example.sharedpreferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    EditText edta, edtb, edtkq;
    Button btntong, btnclear;
    TextView txtlichsu;
    String lichsu = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ
        edta = findViewById(R.id.edta);
        edtb = findViewById(R.id.edtb);
        edtkq = findViewById(R.id.edtkq);
        btntong = findViewById(R.id.btntong);
        btnclear = findViewById(R.id.btnclear);
        txtlichsu = findViewById(R.id.txtlichsu);

        // Lấy dữ liệu lịch sử đã lưu
        SharedPreferences myprefs = getSharedPreferences("mysave", MODE_PRIVATE);
        lichsu = myprefs.getString("ls", "");
        txtlichsu.setText(lichsu);

        // Nút tính tổng
        btntong.setOnClickListener(v -> {
            try {
                int a = Integer.parseInt(edta.getText().toString());
                int b = Integer.parseInt(edtb.getText().toString());
                int kq = a + b;
                edtkq.setText(String.valueOf(kq));
                lichsu += a + " + " + b + " = " + kq + "\n";
                txtlichsu.setText(lichsu);
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Nhập số hợp lệ!", Toast.LENGTH_SHORT).show();
            }
        });

        // Nút xoá lịch sử
        btnclear.setOnClickListener(v -> {
            lichsu = "";
            txtlichsu.setText("");
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Lưu lịch sử khi rời Activity
        SharedPreferences myprefs = getSharedPreferences("mysave", MODE_PRIVATE);
        SharedPreferences.Editor editor = myprefs.edit();
        editor.putString("ls", lichsu);
        editor.apply();
    }
}
