package com.example.spinner;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    String arrCategories[] = {"Hàng Điện tử", "Hàng Hóa Chất",
            "Hàng Gia dụng", "Hàng xây dựng"};
    TextView txtSelection;
    Spinner spinner1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtSelection = (TextView) findViewById(R.id.txtSelection);
        spinner1 = (Spinner) findViewById(R.id.spinner1);


        ArrayAdapter<String> adapterCategories = new ArrayAdapter<String>(
                MainActivity.this,
                android.R.layout.simple_spinner_item,
                arrCategories
        );

        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(adapterCategories);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                txtSelection.setText(arrCategories[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }
}