package com.example.qlsv;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

        // Ánh xạ
        edtmalop = findViewById(R.id.edtmalop);
        edttenlop = findViewById(R.id.edttenlop);
        edtsiso = findViewById(R.id.edtsiso);
        btninsert = findViewById(R.id.btninsert);
        btndelete = findViewById(R.id.btndelete);
        btnupdate = findViewById(R.id.btnupdate);
        btnquery = findViewById(R.id.btnquery);
        lv = findViewById(R.id.lv);

        mylist = new ArrayList<>();
        myadapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mylist);
        lv.setAdapter(myadapter);

        // Tạo hoặc mở DB
        mydatabase = openOrCreateDatabase("qlsinhvien.db", MODE_PRIVATE, null);

        // Tạo bảng nếu chưa có
        try {
            String sql = "CREATE TABLE tbllop(malop TEXT PRIMARY KEY, tenlop TEXT, siso INTEGER)";
            mydatabase.execSQL(sql);
        } catch (Exception e) {
            Log.e("DB", "Bảng đã tồn tại");
        }

        // Insert
        btninsert.setOnClickListener(v -> {
            ContentValues values = new ContentValues();
            values.put("malop", edtmalop.getText().toString());
            values.put("tenlop", edttenlop.getText().toString());
            values.put("siso", Integer.parseInt(edtsiso.getText().toString()));
            long kq = mydatabase.insert("tbllop", null, values);
            Toast.makeText(this, kq == -1 ? "Lỗi thêm" : "Thêm thành công", Toast.LENGTH_SHORT).show();
        });

        // Delete
        btndelete.setOnClickListener(v -> {
            int n = mydatabase.delete("tbllop", "malop = ?", new String[]{edtmalop.getText().toString()});
            Toast.makeText(this, n == 0 ? "Không có dữ liệu" : "Xoá thành công", Toast.LENGTH_SHORT).show();
        });

        // Update
        btnupdate.setOnClickListener(v -> {
            ContentValues values = new ContentValues();
            values.put("siso", Integer.parseInt(edtsiso.getText().toString()));
            int n = mydatabase.update("tbllop", values, "malop = ?", new String[]{edtmalop.getText().toString()});
            Toast.makeText(this, n == 0 ? "Không có dữ liệu để cập nhật" : "Cập nhật thành công", Toast.LENGTH_SHORT).show();
        });

        // Query
        btnquery.setOnClickListener(v -> {
            mylist.clear();
            Cursor c = mydatabase.query("tbllop", null, null, null, null, null, null);
            while (c.moveToNext()) {
                String row = c.getString(0) + " - " + c.getString(1) + " - " + c.getInt(2);
                mylist.add(row);
            }
            c.close();
            myadapter.notifyDataSetChanged();
        });
    }
}
