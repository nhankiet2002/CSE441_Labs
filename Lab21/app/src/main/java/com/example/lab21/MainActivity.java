package com.example.lab21;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btnparse;
    ListView lv;
    ArrayList<String> mylist;
    ArrayAdapter<String> myadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnparse = findViewById(R.id.btnparse);
        lv = findViewById(R.id.lv);

        mylist = new ArrayList<>();
        myadapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mylist);
        lv.setAdapter(myadapter);

        btnparse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parsejson();
            }
        });
    }

    private void parsejson() {
        mylist.clear(); // Xóa dữ liệu cũ trước khi parse
        String jsonContent = null;
        try {
            InputStream inputStream = getAssets().open("computer.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            jsonContent = new String(buffer, StandardCharsets.UTF_8); // Chỉ định UTF-8

            JSONObject reader = new JSONObject(jsonContent);
            mylist.add("Mã DM: " + reader.getString("MaDM"));
            mylist.add("Tên DM: " + reader.getString("TenDM"));
            mylist.add("--- Sản phẩm ---"); // Thêm dòng phân cách

            JSONArray sanphamsArray = reader.getJSONArray("Sanphams");
            for (int i = 0; i < sanphamsArray.length(); i++) {
                JSONObject sanphamObj = sanphamsArray.getJSONObject(i);
                String masp = sanphamObj.getString("MaSP");
                String tensp = sanphamObj.getString("TenSP");
                String soluong = sanphamObj.getString("SoLuong");
                String dongia = sanphamObj.getString("DonGia");
                String thanhtien = sanphamObj.getString("ThanhTien");
                String hinh = sanphamObj.getString("Hinh");

                mylist.add(masp + " - " + tensp);
                mylist.add(soluong + " * " + dongia + " = " + thanhtien);
                mylist.add("Hình: " + hinh);
                if (i < sanphamsArray.length() - 1) { // Thêm dòng phân cách giữa các sản phẩm
                    mylist.add("--------------------");
                }
            }
            myadapter.notifyDataSetChanged();

        } catch (IOException e1) {
            e1.printStackTrace();
            Toast.makeText(this, "Lỗi đọc file: " + e1.getMessage(), Toast.LENGTH_LONG).show();
        } catch (JSONException e2) {
            e2.printStackTrace();
            Toast.makeText(this, "Lỗi parse JSON: " + e2.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}