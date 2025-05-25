package com.example.karaoke;

import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String DB_PATH_SUFFIX = "/databases/";
    public static SQLiteDatabase database = null;
    public static String DATABASE_NAME = "arirang.sqlite";

    EditText edttim;
    ListView lv1, lv2, lv3; // lv1: Search, lv2: All songs, lv3: Favorites
    ArrayList<Item> list1, list2, list3;
    myarrayAdapter myarrayAdapter1, myarrayAdapter2, myarrayAdapter3;
    TabHost tabHost;
    ImageButton btnxoa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        processCopy(); // Copy DB from assets if it doesn't exist
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

        addControls();
        addEvents();

        // Load initial data for Tab 2 (All Songs) and Tab 3 (Favorites)
        loadAllSongs();
        loadFavoriteSongs();
    }

    private void addControls() {
        edttim = findViewById(R.id.edttim);
        btnxoa = findViewById(R.id.btnxoa);

        tabHost = findViewById(R.id.tabhost);
        tabHost.setup();

        // Tab 1: Search
        TabHost.TabSpec tab1Spec = tabHost.newTabSpec("t1");
        tab1Spec.setContent(R.id.tab1); // This should be the ID of the LinearLayout for Tab 1
        tab1Spec.setIndicator("", getResources().getDrawable(R.drawable.search)); // Your search icon
        tabHost.addTab(tab1Spec);

        // Tab 2: All Songs (using lv2 which is a direct child of FrameLayout for tab content)
        TabHost.TabSpec tab2Spec = tabHost.newTabSpec("t2");
        tab2Spec.setContent(R.id.lv2); // ID of the ListView for Tab 2
        tab2Spec.setIndicator("", getResources().getDrawable(R.drawable.list)); // Your list icon
        tabHost.addTab(tab2Spec);

        // Tab 3: Favorites (using lv3)
        TabHost.TabSpec tab3Spec = tabHost.newTabSpec("t3");
        tab3Spec.setContent(R.id.lv3); // ID of the ListView for Tab 3
        tab3Spec.setIndicator("", getResources().getDrawable(R.drawable.favourite)); // Your favorite icon
        tabHost.addTab(tab3Spec);

        lv1 = findViewById(R.id.lv1); // ListView inside Tab 1 LinearLayout
        lv2 = findViewById(R.id.lv2); // ListView for Tab 2
        lv3 = findViewById(R.id.lv3); // ListView for Tab 3

        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        list3 = new ArrayList<>();

        myarrayAdapter1 = new myarrayAdapter(MainActivity.this, R.layout.listitem, list1);
        myarrayAdapter2 = new myarrayAdapter(MainActivity.this, R.layout.listitem, list2);
        myarrayAdapter3 = new myarrayAdapter(MainActivity.this, R.layout.listitem, list3);

        lv1.setAdapter(myarrayAdapter1);
        lv2.setAdapter(myarrayAdapter2);
        lv3.setAdapter(myarrayAdapter3);
    }

    private void addEvents() {
        btnxoa.setOnClickListener(v -> edttim.setText(""));

        tabHost.setOnTabChangedListener(tabId -> {
            if (tabId.equalsIgnoreCase("t2")) {
                loadAllSongs();
            } else if (tabId.equalsIgnoreCase("t3")) {
                loadFavoriteSongs();
            }
            // Search tab (t1) is handled by TextWatcher
        });

        edttim.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                performSearch();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void performSearch() {
        String query = edttim.getText().toString().trim();
        list1.clear();
        if (!query.isEmpty()) {
            Cursor c = database.rawQuery("SELECT * FROM ArirangSongList WHERE TENBH1 LIKE ? OR MABH LIKE ?",
                    new String[]{"%" + query + "%", "%" + query + "%"});
            while (c.moveToNext()) {
                list1.add(new Item(
                        c.getString(c.getColumnIndexOrThrow("MABH")),
                        c.getString(c.getColumnIndexOrThrow("TENBH1")),
                        c.getInt(c.getColumnIndexOrThrow("YEUTHICH"))
                ));
            }
            c.close();
        }
        myarrayAdapter1.notifyDataSetChanged();
    }

    private void loadAllSongs() {
        list2.clear();
        Cursor c = database.rawQuery("SELECT * FROM ArirangSongList", null);
        while (c.moveToNext()) {
            list2.add(new Item(
                    c.getString(c.getColumnIndexOrThrow("MABH")),
                    c.getString(c.getColumnIndexOrThrow("TENBH1")),
                    c.getInt(c.getColumnIndexOrThrow("YEUTHICH"))
            ));
        }
        c.close();
        myarrayAdapter2.notifyDataSetChanged();
    }

    private void loadFavoriteSongs() {
        list3.clear();
        Cursor c = database.rawQuery("SELECT * FROM ArirangSongList WHERE YEUTHICH = 1", null);
        while (c.moveToNext()) {
            list3.add(new Item(
                    c.getString(c.getColumnIndexOrThrow("MABH")),
                    c.getString(c.getColumnIndexOrThrow("TENBH1")),
                    c.getInt(c.getColumnIndexOrThrow("YEUTHICH"))
            ));
        }
        c.close();
        myarrayAdapter3.notifyDataSetChanged();
    }

    // Call this method from adapter after a like/unlike action
    public void refreshCurrentTabAndFavorites() {
        String currentTab = tabHost.getCurrentTabTag();
        if ("t1".equals(currentTab)) {
            performSearch(); // Re-filter search results
        } else if ("t2".equals(currentTab)) {
            loadAllSongs(); // Refresh all songs list
        }
        loadFavoriteSongs(); // Always refresh favorites list as it might have changed
    }


    private void processCopy() {
        File dbFile = getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists()) {
            try {
                CopyDataBaseFromAsset();
                Toast.makeText(this, "Database copied successfully!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Error copying database: " + e.toString(), Toast.LENGTH_LONG).show();
                Log.e("DB_COPY_ERROR", "Error copying database", e);
            }
        }
    }

    private String getDatabasePathInternal() { // Renamed to avoid conflict if you have another getDatabasePath
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX;
    }

    public void CopyDataBaseFromAsset() throws IOException {
        InputStream myInput = getAssets().open(DATABASE_NAME);
        String outFileName = getDatabasePathInternal() + DATABASE_NAME; // Corrected path construction
        File f = new File(getDatabasePathInternal());
        if (!f.exists()) {
            f.mkdir();
        }
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the current tab's data when returning to MainActivity
        String currentTab = tabHost.getCurrentTabTag();
        if (currentTab != null) { // Ensure currentTab is not null, especially on first launch
            if (currentTab.equalsIgnoreCase("t1")) {
                performSearch();
            } else if (currentTab.equalsIgnoreCase("t2")) {
                loadAllSongs();
            } else if (currentTab.equalsIgnoreCase("t3")) {
                loadFavoriteSongs();
            }
        } else { // Default action if currentTab is null (e.g., on first launch after addControls)
            if (tabHost.getCurrentTab() == 1) loadAllSongs(); // Index for tab2
            else if (tabHost.getCurrentTab() == 2) loadFavoriteSongs(); // Index for tab3
        }
    }
}