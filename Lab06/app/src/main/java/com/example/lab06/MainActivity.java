package com.example.lab06;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Button;
import android.widget.Toast;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    EditText edtten, editCMND, editBosung;
    CheckBox chkdocbao, chkdocsach, chkdoccoding;
    Button btnsend;
    RadioGroup group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ các thành phần từ XML
        edtten = findViewById(R.id.editHoten);
        editCMND = findViewById(R.id.editCMND);
        editBosung = findViewById(R.id.editBosung);

        chkdocbao = findViewById(R.id.chkdocbao);
        chkdocsach = findViewById(R.id.chkdocsach);
        chkdoccoding = findViewById(R.id.chkdoccoding);

        btnsend = findViewById(R.id.btnGuiTT);
        group = findViewById(R.id.radioGroup1);

        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                doShowInformation();
            }
        });
    }

    public void doShowInformation() {
        // Kiểm tra tên hợp lệ
        String ten = edtten.getText().toString().trim();
        if (ten.length() < 3) {
            edtten.requestFocus();
            edtten.selectAll();
            Toast.makeText(this, "Tên phải >= 3 ký tự", Toast.LENGTH_LONG).show();
            return;
        }

        // Kiểm tra CMND hợp lệ
        String cmnd = editCMND.getText().toString().trim();
        if (cmnd.length() != 9 || !cmnd.matches("\\d{9}")) {
            editCMND.requestFocus();
            editCMND.selectAll();
            Toast.makeText(this, "CMND phải đúng 9 chữ số", Toast.LENGTH_LONG).show();
            return;
        }

        // Kiểm tra bằng cấp
        int id = group.getCheckedRadioButtonId();
        if (id == -1) {
            Toast.makeText(this, "Phải chọn bằng cấp", Toast.LENGTH_LONG).show();
            return;
        }
        RadioButton rad = findViewById(id);
        String bang = rad.getText().toString();

        // Kiểm tra sở thích
        String sothich = "";
        if (chkdocbao.isChecked()) sothich += chkdocbao.getText() + "\n";
        if (chkdocsach.isChecked()) sothich += chkdocsach.getText() + "\n";
        if (chkdoccoding.isChecked()) sothich += chkdoccoding.getText() + "\n";

        if (sothich.equals("")) {
            Toast.makeText(this, "Phải chọn ít nhất 1 sở thích", Toast.LENGTH_LONG).show();
            return;
        }

        // Lấy thông tin bổ sung
        String bosung = editBosung.getText().toString();

        // Hiển thị AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông tin cá nhân");
        builder.setMessage(
                "Họ tên: " + ten + "\n" +
                        "CMND: " + cmnd + "\n" +
                        "Bằng cấp: " + bang + "\n" +
                        "Sở thích:\n" + sothich +
                        "—————————–\n" +
                        "Thông tin bổ sung:\n" + bosung + "\n" +
                        "—————————–"
        );
        builder.setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Question");
        b.setMessage("Are you sure you want to exit?");
        b.setIcon(android.R.drawable.ic_dialog_alert); // icon cảnh báo mặc định của Android
        b.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        b.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        b.create().show();
    }
}
