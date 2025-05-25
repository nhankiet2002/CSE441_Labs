package com.example.qlsv;


import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText edtmalop, edttenlop, edtsiso;
    Button btninsert, btndelete, btnupdate, btnquery;
    ListView lv;
    ArrayList<String> mylist;
    ArrayAdapter<String> myadapter;
    SQLiteDatabase mydatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtmalop = findViewById(R.id.edtmalop);
        edttenlop = findViewById(R.id.edttenlop);
        edtsiso = findViewById(R.id.edtsiso);
        btninsert = findViewById(R.id.btninsert);
        btndelete = findViewById(R.id.btndelete);
        btnupdate = findViewById(R.id.btnupdate);
        btnquery = findViewById(R.id.btnquery);

        // Tạo ListView
        lv = findViewById(R.id.lv);
        mylist = new ArrayList<>();
        myadapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mylist);
        lv.setAdapter(myadapter);

        // Tạo và mở Cơ sở dữ liệu SQLite
        mydatabase = openOrCreateDatabase("qlsinhvien.db", MODE_PRIVATE, null);

        // Tạo Table để chứa dữ liệu
        try {
            String sql = "CREATE TABLE tbllop(malop TEXT primary key, tenlop TEXT, siso INTEGER)";
            mydatabase.execSQL(sql);
        } catch (Exception e) {
            Log.e("Error", "Table đã tồn tại");
        }

        btninsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String malop = edtmalop.getText().toString();
                String tenlop = edttenlop.getText().toString();
                String sisoStr = edtsiso.getText().toString();

                if (malop.isEmpty() || tenlop.isEmpty() || sisoStr.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập đủ thông tin.", Toast.LENGTH_SHORT).show();
                    return;
                }
                int siso;
                try {
                    siso = Integer.parseInt(sisoStr);
                } catch (NumberFormatException e){
                    Toast.makeText(MainActivity.this, "Sĩ số phải là một số.", Toast.LENGTH_SHORT).show();
                    return;
                }


                ContentValues myvalue = new ContentValues();
                myvalue.put("malop", malop);
                myvalue.put("tenlop", tenlop);
                myvalue.put("siso", siso);
                String msg = "";
                if (mydatabase.insert("tbllop", null, myvalue) == -1) {
                    msg = "Fail to Insert Record!";
                } else {
                    msg = "Insert record Successfully";
                    edtmalop.setText("");
                    edttenlop.setText("");
                    edtsiso.setText("");
                }
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String malop = edtmalop.getText().toString();
                if (malop.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập Mã lớp để xóa.", Toast.LENGTH_SHORT).show();
                    return;
                }
                int n = mydatabase.delete("tbllop", "malop = ?", new String[]{malop});
                String msg = "";
                if (n == 0) {
                    msg = "No record to Delete";
                } else {
                    msg = n + " record is deleted";
                    edtmalop.setText(""); // Clear mã lớp sau khi xóa
                }
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String malop = edtmalop.getText().toString();
                String sisoStr = edtsiso.getText().toString();

                if (malop.isEmpty() || sisoStr.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập Mã lớp và Sĩ số mới.", Toast.LENGTH_SHORT).show();
                    return;
                }
                int siso;
                try {
                    siso = Integer.parseInt(sisoStr);
                } catch (NumberFormatException e){
                    Toast.makeText(MainActivity.this, "Sĩ số phải là một số.", Toast.LENGTH_SHORT).show();
                    return;
                }

                ContentValues myvalue = new ContentValues();
                myvalue.put("siso", siso); // Chỉ cập nhật sĩ số
                // Có thể thêm cập nhật tên lớp nếu muốn: myvalue.put("tenlop", edttenlop.getText().toString());

                int n = mydatabase.update("tbllop", myvalue, "malop = ?", new String[]{malop});
                String msg = "";
                if (n == 0) {
                    msg = "No record to Update";
                } else {
                    msg = n + " record is updated";
                }
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

        btnquery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mylist.clear();
                Cursor c = mydatabase.query("tbllop", null, null, null, null, null, null);
                // c.moveToNext(); // Trong tài liệu là moveToNext() trước, nhưng nên là moveToFirst()
                c.moveToFirst();
                String data = "";
                while (!c.isAfterLast()) { // (c.isAfterLast() == false)
                    data = c.getString(0) + " - " + c.getString(1) + " - " + c.getString(2);
                    mylist.add(data);
                    c.moveToNext();
                }
                c.close();
                myadapter.notifyDataSetChanged();
                if(mylist.isEmpty()){
                    Toast.makeText(MainActivity.this, "Không có dữ liệu hiển thị.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}