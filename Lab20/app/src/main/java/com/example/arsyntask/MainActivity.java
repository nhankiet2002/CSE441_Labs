package com.example.arsyntask;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btnStart;
    MyAsyncTask mytt;
    ProgressBar paCha;
    TextView txtmsg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = findViewById(R.id.button1);
        // Ánh xạ các View đã khai báo
        paCha = findViewById(R.id.progressBar1);
        txtmsg = findViewById(R.id.textView1);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                doStart();
            }
        });
    }

    private void doStart() {
        // truyền this (chính là MainActivity hiện tại) qua background Thread
        mytt = new MyAsyncTask(this);
        // Kích hoạt Tiến trình
        // khi gọi hàm này thì onPreExecute của mytt sẽ thực thi trước
        mytt.execute();
    }

    // Lớp AsyncTask lồng bên trong MainActivity
    public class MyAsyncTask extends AsyncTask<Void, Integer, Void> {
        // khai báo Activity để lưu trữ context của MainActivity
        Activity contextCha;

        // constructor này được truyền vào là MainActivity
        public MyAsyncTask(Activity ctx) {
            contextCha = ctx;
        }

        // hàm này sẽ được thực hiện đầu tiên
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(contextCha, "onPreExecute!", Toast.LENGTH_LONG).show();
            // Reset ProgressBar và TextView khi bắt đầu
            if (paCha != null) {
                paCha.setProgress(0);
            }
            if (txtmsg != null) {
                txtmsg.setText("0%");
            }
        }

        // sau đó tới hàm doInBackground
        // tuyệt đối không được cập nhật giao diện trong hàm này
        @Override
        protected Void doInBackground(Void... arg0) {
            for (int i = 0; i <= 100; i++) {
                // nghỉ 100 milisecond thì tiến hành update UI
                SystemClock.sleep(100);
                // khi gọi hàm này thì onProgressUpdate sẽ thực thi
                publishProgress(i);
            }
            return null;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            // thông qua contextCha để lấy được control trong MainActivity
            // ProgressBar paCha=(ProgressBar) contextCha.findViewById(R.id.progressBar1); // Không cần tìm lại vì đã ánh xạ ở onCreate của MainActivity

            // vì publishProgress chỉ truyền 1 đối số
            // nên mảng values chỉ có 1 phần tử
            int giatri = values[0];
            // tăng giá trị của Progressbar lên
            if (paCha != null) {
                paCha.setProgress(giatri);
            }

            // đồng thời hiện thị giá trị là % lên TextView
            // TextView txtmsg=(TextView) contextCha.findViewById(R.id.textView1); // Không cần tìm lại
            if (txtmsg != null) {
                txtmsg.setText(giatri + "%");
            }
        }

        /**
         * sau khi tiến trình thực hiện xong thì hàm này sảy ra
         */
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Toast.makeText(contextCha, "Update xong roi do!", Toast.LENGTH_LONG).show();
        }
    }
}