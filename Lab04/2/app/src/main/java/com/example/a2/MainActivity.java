package com.example.a2;

import com.example.a2.R;
import java.text.DecimalFormat;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    Button btnBMI;
    EditText editTen, editChieucao, editCannang, editBMI, editChandoan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ view
        btnBMI = findViewById(R.id.btnBMI);
        editTen = findViewById(R.id.edtten);
        editChieucao = findViewById(R.id.edtchieucao);
        editCannang = findViewById(R.id.edtcannang);
        editBMI = findViewById(R.id.edtBMI);
        editChandoan = findViewById(R.id.edtChuanDoan);

        btnBMI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    double H=Double.parseDouble(editChieucao.getText()+"");
                    double W=Double.parseDouble(editCannang.getText()+"");

                    if (H <= 0 || W <= 0) {
                        Toast.makeText(getApplicationContext(), "Chiều cao và cân nặng phải > 0", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    double BMI = W / Math.pow(H, 2);
                    String chandoan= "";

                    if (BMI < 18) {
                        chandoan = "Bạn gầy";
                    } else if (BMI <= 24.9) {
                        chandoan = "Bạn bình thường";
                    } else if (BMI <= 29.9) {
                        chandoan = "Bạn béo phì độ 1";
                    } else if (BMI <= 34.9) {
                        chandoan = "Bạn béo phì độ 2";
                    } else {
                        chandoan = "Bạn béo phì độ 3";
                    }

                    DecimalFormat dcf = new DecimalFormat("#.0");
                    editBMI.setText(dcf.format(BMI));
                    editChandoan.setText(chandoan);

                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập đúng định dạng số!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
