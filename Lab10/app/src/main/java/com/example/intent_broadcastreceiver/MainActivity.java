package com.example.intent_broadcastreceiver;

import android.Manifest; // Added this line
import android.content.pm.PackageManager; // Added this line
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat; // Added this line
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new
                    String[]{Manifest.permission.RECEIVE_SMS,Manifest.permission.READ_SMS},1);
            // It's good practice to return here or handle the case where permission is not granted yet.
            // For example, you might disable functionality that depends on this permission
            // or explain to the user why the permission is needed.
            return;
        }
    }
}