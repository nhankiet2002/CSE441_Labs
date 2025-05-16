package com.example.a52;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends Activity {

    Button btnTieptuc, btnGiai, btnThoat;
    EditText edita, editb, editc;
    TextView txtkq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTieptuc = findViewById(R.id.btnTiepTuc);
        btnGiai = findViewById(R.id.btnGiaiPT);
        btnThoat = findViewById(R.id.btnThoat);

        edita = findViewById(R.id.editA);
        editb = findViewById(R.id.editB);
        editc = findViewById(R.id.editC);

        txtkq = findViewById(R.id.txtKQ);

        btnGiai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sa = edita.getText().toString();
                String sb = editb.getText().toString();
                String sc = editc.getText().toString();

                if (sa.isEmpty() || sb.isEmpty() || sc.isEmpty()) {
                    txtkq.setText("Vui lòng nhập đầy đủ a, b, c");
                    return;
                }

                try {
                    int a = Integer.parseInt(sa);
                    int b = Integer.parseInt(sb);
                    int c = Integer.parseInt(sc);
                    String kq = "";
                    DecimalFormat dcf = new DecimalFormat("0.00");

                    if (a == 0) {
                        if (b == 0) {
                            if (c == 0)
                                kq = "PT vô số nghiệm";
                            else
                                kq = "PT vô nghiệm";
                        } else {
                            kq = "PT có 1 nghiệm: x = " + dcf.format(-1.0 * c / b);
                        }
                    } else {
                        double delta = b * b - 4 * a * c;
                        if (delta < 0) {
                            kq = "PT vô nghiệm";
                        } else if (delta == 0) {
                            kq = "PT có nghiệm kép: x1 = x2 = " + dcf.format(-1.0 * b / (2 * a));
                        } else {
                            double x1 = (-b + Math.sqrt(delta)) / (2 * a);
                            double x2 = (-b - Math.sqrt(delta)) / (2 * a);
                            kq = "PT có 2 nghiệm: x1 = " + dcf.format(x1) + "; x2 = " + dcf.format(x2);
                        }
                    }

                    txtkq.setText(kq);
                } catch (NumberFormatException e) {
                    txtkq.setText("Lỗi định dạng số!");
                }
            }
        });

        btnTieptuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edita.setText("");
                editb.setText("");
                editc.setText("");
                txtkq.setText("");
                edita.requestFocus();
            }
        });

        btnThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
