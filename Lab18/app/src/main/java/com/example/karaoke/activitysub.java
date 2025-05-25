package com.example.karaoke;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class activitysub extends AppCompatActivity {
    TextView txtmaso_sub, txtbaihat_sub, txtloibaihat_sub, txttacgia_sub;
    ImageButton btnthich_sub, btnkhongthich_sub;
    String currentMaso; // Store the MABH of the current song

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subactivity);

        txtmaso_sub = findViewById(R.id.txtmaso_sub);
        txtbaihat_sub = findViewById(R.id.txtbaihat_sub);
        txtloibaihat_sub = findViewById(R.id.txtloibaihat_sub);
        txttacgia_sub = findViewById(R.id.txttacgia_sub);
        btnthich_sub = findViewById(R.id.btnthich_sub);
        btnkhongthich_sub = findViewById(R.id.btnkhongthich_sub);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("package"); // as per document
        if (bundle != null) {
            currentMaso = bundle.getString("maso");
            loadSongDetails(currentMaso);
        }

        btnthich_sub.setOnClickListener(v -> {
            updateYeuthichStatusInDB(currentMaso, 0); // Unlike
            btnthich_sub.setVisibility(View.INVISIBLE);
            btnkhongthich_sub.setVisibility(View.VISIBLE);
        });

        btnkhongthich_sub.setOnClickListener(v -> {
            updateYeuthichStatusInDB(currentMaso, 1); // Like
            btnkhongthich_sub.setVisibility(View.INVISIBLE);
            btnthich_sub.setVisibility(View.VISIBLE);
        });
    }

    private void loadSongDetails(String maso) {
        // Make sure MainActivity.database is not null
        if (MainActivity.database == null) return;

        Cursor c = MainActivity.database.rawQuery("SELECT * FROM ArirangSongList WHERE MABH = ?", new String[]{maso});
        if (c.moveToFirst()) {
            txtmaso_sub.setText("Mã số: " + c.getString(c.getColumnIndexOrThrow("MABH")));
            txtbaihat_sub.setText("Bài hát: " + c.getString(c.getColumnIndexOrThrow("TENBH1")));
            txtloibaihat_sub.setText("Lời: " + c.getString(c.getColumnIndexOrThrow("LOIBH"))); // Assuming LOIBH for lyrics
            txttacgia_sub.setText("Tác giả: " + c.getString(c.getColumnIndexOrThrow("TACGIA"))); // Assuming TACGIA

            int yeuthich = c.getInt(c.getColumnIndexOrThrow("YEUTHICH"));
            if (yeuthich == 1) {
                btnthich_sub.setVisibility(View.VISIBLE);
                btnkhongthich_sub.setVisibility(View.INVISIBLE);
            } else {
                btnthich_sub.setVisibility(View.INVISIBLE);
                btnkhongthich_sub.setVisibility(View.VISIBLE);
            }
        }
        c.close();
    }
    private void updateYeuthichStatusInDB(String maBH, int status) {
        if (MainActivity.database == null || maBH == null) return;
        ContentValues values = new ContentValues();
        values.put("YEUTHICH", status);
        MainActivity.database.update("ArirangSongList", values, "MABH = ?", new String[]{maBH});
    }
}