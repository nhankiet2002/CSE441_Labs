package com.example.tabselector;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import java.util.ArrayList;

public class MainActivity extends Activity {
    EditText edta, edtb;
    Button btncong;
    ListView lv1;
    ArrayList<String> list;
    ArrayAdapter<String> myarrayAdapter; // Sửa tên biến cho đúng quy ước

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControl();
        addEvent();
    }

    private void addEvent() {
        btncong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Xulycong();
            }
        });
    }

    private void Xulycong() {
        try {
            int a = Integer.parseInt(edta.getText().toString());
            int b = Integer.parseInt(edtb.getText().toString());
            String c = a + " + " + b + " = " + (a + b);
            list.add(c);
            myarrayAdapter.notifyDataSetChanged(); // Cập nhật ListView
            edta.setText(""); // Xóa nội dung EditText
            edtb.setText("");
            edta.requestFocus(); // Đưa con trỏ về EditText a
        } catch (NumberFormatException e) {
            // Xử lý nếu người dùng không nhập số
            // Ví dụ: Toast.makeText(this, "Vui lòng nhập số hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }

    private void addControl() {
        TabHost tab = (TabHost) findViewById(R.id.tabhost);
        tab.setup(); // Quan trọng: Phải gọi trước khi thêm tab

        // Tạo Tab 1
        TabHost.TabSpec tab1Spec = tab.newTabSpec("t1"); // "t1" là ID của Tab
        tab1Spec.setContent(R.id.tab1); // Nội dung lấy từ LinearLayout có id "tab1"
        // Giả sử bạn có file cong.png trong drawable
        tab1Spec.setIndicator("", getResources().getDrawable(R.drawable.cong));
        tab.addTab(tab1Spec);

        // Tạo Tab 2
        TabHost.TabSpec tab2Spec = tab.newTabSpec("t2");
        tab2Spec.setContent(R.id.tab2);
        // Giả sử bạn có file lichsu.png trong drawable
        tab2Spec.setIndicator("", getResources().getDrawable(R.drawable.lichsu));
        tab.addTab(tab2Spec);

        edta = (EditText) findViewById(R.id.edta);
        edtb = (EditText) findViewById(R.id.edtb);
        btncong = (Button) findViewById(R.id.btncong);
        lv1 = (ListView) findViewById(R.id.lv1);

        list = new ArrayList<String>();
        myarrayAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, // Layout mặc định cho item
                list);
        lv1.setAdapter(myarrayAdapter);
    }
}