package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    ListView lv;
    ArrayList<String> arraywork;
    ArrayAdapter<String> arrAdapater;
    EditText edtwork, edthour, edtminute;
    TextView txtdate;
    Button btnadd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        edthour = findViewById(R.id.edthour);
        edtminute = findViewById(R.id.edtminute);
        edtwork = findViewById(R.id.edtwork);
        btnadd = findViewById(R.id.btnadd);
        lv = findViewById(R.id.listView1);
        txtdate = findViewById(R.id.txtdate);


        arraywork = new ArrayList<>();
        arrAdapater = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arraywork);
        lv.setAdapter(arrAdapater);


        Date currentDate = Calendar.getInstance().getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        txtdate.setText("Hôm Nay: " + sdf.format(currentDate));


        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String work = edtwork.getText().toString().trim();
                String hour = edthour.getText().toString().trim();
                String minute = edtminute.getText().toString().trim();

                if (work.isEmpty() || hour.isEmpty() || minute.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Info missing");
                    builder.setMessage("Please enter all information of the work");
                    builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    builder.show();
                } else {

                    String workItem = work + " - " + hour + ":" + minute;
                    arraywork.add(workItem);
                    arrAdapater.notifyDataSetChanged();

                    edtwork.setText("");
                    edthour.setText("");
                    edtminute.setText("");
                    edtwork.requestFocus();
                }
            }
        });
    }
}