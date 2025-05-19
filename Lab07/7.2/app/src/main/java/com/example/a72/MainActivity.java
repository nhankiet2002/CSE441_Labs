package com.example.a72;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.a72.R; // Or your actual package name
import androidx.appcompat.app.AppCompatActivity;
import com.example.a72.R;

public class MainActivity extends AppCompatActivity {
    EditText txta, txtb;
    Button btnketqua;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txta = findViewById(R.id.txta);
        txtb = findViewById(R.id.txtb);
        btnketqua = findViewById(R.id.btnketqua);

        btnketqua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double a = Double.parseDouble(txta.getText().toString());
                double b = Double.parseDouble(txtb.getText().toString());

                Intent intent = new Intent(MainActivity.this, ketqua.class);
                Bundle bundle = new Bundle();
                bundle.putDouble("soa", a);
                bundle.putDouble("sob", b);
                intent.putExtra("mybundle", bundle);

                startActivity(intent);
            }
        });
    }
}
