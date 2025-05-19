package com.example.a72;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.a72.R;
import androidx.appcompat.app.AppCompatActivity;

public class ketqua extends AppCompatActivity {
    TextView txtketqua;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ketqua);
        txtketqua = findViewById(R.id.txtketqua);
        btnBack = findViewById(R.id.btnBack);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("mybundle");
        double a = bundle.getDouble("soa");
        double b = bundle.getDouble("sob");

        String result;
        if (a == 0) {
            result = (b == 0) ? "Vô số nghiệm" : "Vô nghiệm";
        } else {
            double x = -b / a;
            result = String.format("x = %.2f", x);
        }

        txtketqua.setText(result);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Trở về MainActivity
            }
        });
    }
}
