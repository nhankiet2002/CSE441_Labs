package com.example.parsexml;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.IOException;
import java.io.InputStream;
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
                parsexml();
            }
        });
    }

    private void parsexml() {
        mylist.clear(); // Xóa dữ liệu cũ trước khi parse mới
        try {
            InputStream myinput = getAssets().open("employee.xml");
            XmlPullParserFactory fc = XmlPullParserFactory.newInstance();
            XmlPullParser parser = fc.newPullParser();
            parser.setInput(myinput, null);

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
                        if (nodeName.equals("employee")) {
                            datashow += parser.getAttributeValue(0) + "-"; // id
                            datashow += parser.getAttributeValue(1) + "-"; // title
                        } else if (nodeName.equals("name")) {
                            // Di chuyển parser.next() vào trong để lấy text
                            // và đảm bảo nó không bỏ qua các thẻ khác nếu cấu trúc phức tạp hơn
                            if(parser.next() == XmlPullParser.TEXT) {
                                datashow += parser.getText() + "-";
                            }
                        } else if (nodeName.equals("phone")) {
                            if(parser.next() == XmlPullParser.TEXT) {
                                datashow += parser.getText();
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        nodeName = parser.getName();
                        if (nodeName.equals("employee")) {
                            mylist.add(datashow);
                            datashow = ""; // Reset for next employee
                        }
                        break;
                }
            }
            myadapter.notifyDataSetChanged();

        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (XmlPullParserException e2) {
            e2.printStackTrace();
        }
    }
}