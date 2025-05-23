package com.example.a132;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends Activity {
    //Dùng mảng 1 chiều hoặc ArrayList để lưu một số dữ liệu
    String arr[]={"Ipad","Iphone","New Ipad",
            "SamSung","Nokia","Sony Ericson",
            "LG","Q-Mobile","HTC","Blackberry",
            "G Phone","FPT - Phone","HK Phone"
    };
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//Tối tượng này dùng để hiển thị phần tử được chọn trong GridView
        final TextView selection = (TextView)
                findViewById(R.id.selection);
        final GridView gv = (GridView) findViewById(R.id.gridView1);
//Gán DataSource vào ArrayAdapter
        ArrayAdapter<String> da = new ArrayAdapter<String>
                (
                        this,
                        android.R.layout.simple_list_item_1, arr
                );

        gv.setAdapter(da);

        gv.setOnItemClickListener(new AdapterView
                .OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0,
                                    View arg1, int arg2,
                                    long arg3) {
//Hiển thị phần tử được chọn trong GridView lên TextView
                selection.setText(arr[arg2]);
            }
        });
    }}