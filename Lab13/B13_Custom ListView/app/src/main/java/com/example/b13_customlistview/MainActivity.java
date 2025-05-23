package com.example.b13_customlistview;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    ListView listViewPhones;
    ArrayList<Phone> phoneArrayList;
    PhoneAdapter adapter;

    // ...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewPhones = findViewById(R.id.listViewPhones);
        phoneArrayList = new ArrayList<>();

        // Dữ liệu mẫu
        String[] phoneNames = {"Điện thoại Sky", "Điện thoại SamSung", "Điện thoại IP", "Điện thoại HTC", "Điện thoại LG", "Điện thoại WP"};
        String[] phonePrices = {"5,000,000đ", "10,000,000đ", "20,000,000đ", "8,000,000đ", "8,500,000đ", "6,000,000đ"};
        int[] phoneImages = {
                R.drawable.sky,
                R.drawable.samsung,
                R.drawable.ip,
                R.drawable.htc,
                R.drawable.lg,
                R.drawable.wp
        };


        for (int i = 0; i < phoneNames.length; i++) {

            phoneArrayList.add(new Phone(phoneNames[i], phonePrices[i], phoneImages[i]));
        }

        adapter = new PhoneAdapter(this, R.layout.list_item_phone_ver2, phoneArrayList);
        listViewPhones.setAdapter(adapter);

        // ...
    }
}