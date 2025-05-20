package com.example.intent_service;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    ImageButton btnplay, btnstop;
    Boolean flag = true; //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnplay = findViewById(R.id.btnplay);
        btnstop = findViewById(R.id.btnstop);

        btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentService = new Intent(MainActivity.this, MyService.class);
                startService(intentService);

                if (flag) {
                    btnplay.setImageResource(R.drawable.stop);
                    flag = false;
                } else {
                    btnplay.setImageResource(R.drawable.play);
                    flag = true;
                }
            }
        });

        btnstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentService = new Intent(MainActivity.this, MyService.class);
                stopService(intentService);

                btnplay.setImageResource(R.drawable.play);
                flag = true;
            }
        });
    }
}