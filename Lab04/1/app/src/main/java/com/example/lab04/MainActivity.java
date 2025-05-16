package com.example.lab04;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    EditText edtdoC, edtdoF;
    Button btncf, btnfc, btnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtdoC = findViewById(R.id.edtdoC);
        edtdoF = findViewById(R.id.edtdoF);
        btncf = findViewById(R.id.btncf); // Convert to Fahrenheit
        btnfc = findViewById(R.id.btnfc); // Convert to Celsius
        btnClear = findViewById(R.id.btnClear);

        btncf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String doC = edtdoC.getText().toString();
                if (!doC.isEmpty()) {
                    double C = Double.parseDouble(doC);
                    DecimalFormat dcf = new DecimalFormat("#.00");
                    edtdoF.setText(dcf.format(C * 1.8 + 32));
                }
            }
        });

        btnfc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String doF = edtdoF.getText().toString();
                if (!doF.isEmpty()) {
                    double F = Double.parseDouble(doF);
                    DecimalFormat dcf = new DecimalFormat("#.00");
                    edtdoC.setText(dcf.format((F - 32) / 1.8));
                }
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtdoC.setText("");
                edtdoF.setText("");
            }
        });
    }
}
