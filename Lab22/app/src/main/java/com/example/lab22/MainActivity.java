package com.example.lab22;

import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast; // Để hiển thị thông báo
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.FileNotFoundException; // Thêm import này
import java.io.IOException;
import java.io.StringReader; // Để đọc từ chuỗi XML
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btnparse;
    ListView lv1;
    ArrayAdapter<String> myadapter;
    ArrayList<String> mylist;
    String URL = "https://vnexpress.net/rss/tin-moi-nhat.rss";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnparse = findViewById(R.id.btnparse);
        lv1 = findViewById(R.id.lv1);
        mylist = new ArrayList<>();
        myadapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mylist);
        lv1.setAdapter(myadapter);

        btnparse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadExampleTask task = new LoadExampleTask();
                task.execute();
            }
        });
    }

    class LoadExampleTask extends AsyncTask<Void, Void, ArrayList<String>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            myadapter.clear(); // Xóa dữ liệu cũ
            Toast.makeText(MainActivity.this, "Đang tải dữ liệu...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            ArrayList<String> list = new ArrayList<>();
            try {
                // Tạo đối tượng Parser để chứa dữ liệu từ file XML
                XmlPullParserFactory fc = XmlPullParserFactory.newInstance();
                XmlPullParser parser = fc.newPullParser();

                // Tạo mới và gọi đến phương thức getXmlFromUrl(URL)
                XMLParser myparser = new XMLParser();
                String xml = myparser.getXmlFromUrl(URL); // getting XML from URL

                if (xml == null) {
                    // Xử lý trường hợp không lấy được XML (ví dụ: không có mạng)
                    // Có thể return list rỗng hoặc ném một Exception để onPostExecute xử lý
                    return list; // Trả về danh sách rỗng
                }

                // Copy dữ liệu từ String xml vào đối tượng parser
                parser.setInput(new StringReader(xml));

                // Bắt đầu duyệt parser
                int eventType = -1;
                String nodeName;
                String datashow = "";

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    eventType = parser.next();
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            nodeName = parser.getName();
                            if (nodeName.equals("id")) {
                                datashow += parser.nextText() + "-";
                            } else if (nodeName.equals("name")) {
                                datashow += parser.nextText(); // Không có "-" ở cuối
                            }
                            // Bạn có thể thêm các trường khác như cost, description nếu muốn
                            break;
                        case XmlPullParser.END_TAG:
                            nodeName = parser.getName();
                            if (nodeName.equals("item")) {
                                list.add(datashow);
                                datashow = ""; // Reset for next item
                            }
                            break;
                    }
                }
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) { // Mặc dù không đọc file trực tiếp, nhưng có thể giữ lại
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return list;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            super.onPostExecute(result);
            if (result != null && !result.isEmpty()) {
                myadapter.clear();
                myadapter.addAll(result);
                Toast.makeText(MainActivity.this, "Tải dữ liệu thành công!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Không thể tải dữ liệu hoặc không có dữ liệu.", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            // Có thể dùng để cập nhật UI trong quá trình doInBackground nếu cần
        }
    }
}