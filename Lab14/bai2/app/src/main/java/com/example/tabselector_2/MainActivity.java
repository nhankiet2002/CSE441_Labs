package com.example.tabselector_2;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import java.util.ArrayList;

public class MainActivity extends Activity {
    EditText edttim;
    ListView lv1, lv2, lv3;
    TabHost tab;
    ArrayList<Item> list1, list2, list3;
    myarrayAdapter myadapter1, myadapter2, myadapter3; // Sửa tên biến cho đúng quy ước

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControl();
        addEvent();
        // Khởi tạo dữ liệu ban đầu cho các tab (có thể bỏ qua nếu bạn muốn load khi click tab)
        loadDataForTab1();
        loadDataForTab2();
        loadDataForTab3();
    }

    private void addEvent() {
        tab.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equalsIgnoreCase("t1_new_songs")) {
                    loadDataForTab1();
                } else if (tabId.equalsIgnoreCase("t2_hot_songs")) {
                    loadDataForTab2();
                } else if (tabId.equalsIgnoreCase("t3_favourite_songs")) {
                    loadDataForTab3();
                }
            }
        });

        // (Tùy chọn) Thêm sự kiện tìm kiếm cho edttim nếu cần
        // edttim.addTextChangedListener(new TextWatcher() { ... });
    }

    private void loadDataForTab1() {
        list1.clear(); // Xóa dữ liệu cũ
        list1.add(new Item("52300", "Em là ai Tôi là ai", 0));
        list1.add(new Item("52600", "Chén Đắng", 1));
        list1.add(new Item("52567", "Buồn của Anh", 0));
        myadapter1.notifyDataSetChanged();
    }

    private void loadDataForTab2() {
        list2.clear();
        list2.add(new Item("57236", "Gởi em ở cuối sông hồng", 0));
        list2.add(new Item("51548", "Quê hương tuổi thơ tôi", 0));
        list2.add(new Item("51748", "Em gì ơi", 1));
        myadapter2.notifyDataSetChanged();
    }

    private void loadDataForTab3() {
        list3.clear();
        list3.add(new Item("57689", "Hát với dòng sông", 1));
        list3.add(new Item("58716", "Say tình - Remix", 0));
        list3.add(new Item("58916", "Người hãy quên em đi", 1));
        myadapter3.notifyDataSetChanged();
    }


    private void addControl() {
        tab = (TabHost) findViewById(R.id.tabhost);
        tab.setup();

        // Tab 1: Bài hát mới
        TabHost.TabSpec tab1Spec = tab.newTabSpec("t1_new_songs");
        tab1Spec.setContent(R.id.tab_new_songs);
        tab1Spec.setIndicator("Bài hát mới", getResources().getDrawable(R.drawable.search)); // Hoặc chỉ text
        tab.addTab(tab1Spec);

        // Tab 2: Bài hát Hot
        TabHost.TabSpec tab2Spec = tab.newTabSpec("t2_hot_songs");
        tab2Spec.setContent(R.id.tab_hot_songs);
        tab2Spec.setIndicator("Bài hát Hot", getResources().getDrawable(R.drawable.list));
        tab.addTab(tab2Spec);

        // Tab 3: Yêu thích
        TabHost.TabSpec tab3Spec = tab.newTabSpec("t3_favourite_songs");
        tab3Spec.setContent(R.id.tab_favourite_songs);
        tab3Spec.setIndicator("Yêu thích", getResources().getDrawable(R.drawable.favourite));
        tab.addTab(tab3Spec);

        edttim = (EditText) findViewById(R.id.edttim);
        lv1 = (ListView) findViewById(R.id.lv1);
        lv2 = (ListView) findViewById(R.id.lv2);
        lv3 = (ListView) findViewById(R.id.lv3);

        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        list3 = new ArrayList<>();

        myadapter1 = new myarrayAdapter(MainActivity.this, R.layout.listitem, list1);
        myadapter2 = new myarrayAdapter(MainActivity.this, R.layout.listitem, list2);
        myadapter3 = new myarrayAdapter(MainActivity.this, R.layout.listitem, list3);

        lv1.setAdapter(myadapter1);
        lv2.setAdapter(myadapter2);
        lv3.setAdapter(myadapter3);
    }
}