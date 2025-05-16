package com.example.a51;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText editNamDuongLich;
    TextView textViewNamAmLich;
    Button btnChuyen;

    String[] can = {"Canh", "Tân", "Nhâm", "Quý", "Giáp", "Ất", "Bính", "Đinh", "Mậu", "Kỷ"};
    String[] chi = {"Thân", "Dậu", "Tuất", "Hợi", "Tý", "Sửu", "Dần", "Mão", "Thìn", "Tỵ", "Ngọ", "Mùi"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Đảm bảo đúng tên file XML

        // Ánh xạ các phần tử từ giao diện XML
        editNamDuongLich = findViewById(R.id.editNamDuongLich);
        textViewNamAmLich = findViewById(R.id.textView4);
        btnChuyen = findViewById(R.id.button1);

        btnChuyen.setOnClickListener(v -> {
            String namStr = editNamDuongLich.getText().toString().trim();
            if (namStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập năm dương lịch!", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                int nam = Integer.parseInt(namStr);

                // Kiểm tra nếu năm hợp lệ
                if (nam < 0) {
                    Toast.makeText(this, "Vui lòng nhập năm dương lịch hợp lệ!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Tính chỉ số Can và Chi
                int chiIndex = nam % 12;
                int canIndex = nam % 10;
                String namAmLich = can[canIndex] + " " + chi[chiIndex];

                // Hiển thị kết quả
                textViewNamAmLich.setText(namAmLich);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Năm nhập không hợp lệ! Vui lòng nhập số.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}