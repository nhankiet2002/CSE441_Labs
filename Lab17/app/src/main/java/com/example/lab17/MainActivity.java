package com.example.lab17;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    String DB_PATH_SUFFIX = "/databases/"; // Thư mục chứa CSDL của ứng dụng
    SQLiteDatabase database = null;
    String DATABASE_NAME = "qlsach.db"; // Tên file CSDL trong assets

    ListView lv;
    ArrayList<String> mylist;
    ArrayAdapter<String> myadapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bước 1: Đảm bảo CSDL đã được copy
        boolean dbCopied = ensureDatabaseCopied();

        if (!dbCopied) {
            // Nếu copy thất bại, không tiếp tục và hiển thị thông báo lỗi
            Toast.makeText(this, "Lỗi nghiêm trọng: Không thể chuẩn bị CSDL.", Toast.LENGTH_LONG).show();
            Log.e("DB_MAIN", "ensureDatabaseCopied trả về false. Không thể tiếp tục.");
            // Có thể bạn muốn vô hiệu hóa các UI hoặc thoát ứng dụng ở đây
            return;
        }

        // Bước 2: Mở CSDL (chỉ khi đã copy thành công)
        Log.i("DB_MAIN", "Đang cố gắng mở CSDL sau khi đảm bảo đã copy.");
        File dbFileToOpen = getDatabasePath(DATABASE_NAME);
        try {
            database = SQLiteDatabase.openDatabase(dbFileToOpen.getPath(), null, SQLiteDatabase.OPEN_READWRITE);
            Log.i("DB_MAIN_SUCCESS", "Mở CSDL thành công từ: " + dbFileToOpen.getPath());
        } catch (Exception e) {
            Log.e("DB_MAIN_ERROR", "Không thể mở database sau khi copy: " + e.getMessage(), e);
            Toast.makeText(this, "Lỗi mở CSDL: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }

        // Bước 3: Khởi tạo ListView và truy vấn dữ liệu (chỉ khi CSDL đã mở thành công)
        lv = findViewById(R.id.lv);
        mylist = new ArrayList<>();
        myadapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, mylist);
        lv.setAdapter(myadapter);

        if (database != null) {
            Log.i("DB_MAIN", "Bắt đầu truy vấn CSDL.");
            Cursor c = null;
            try {
                c = database.query("tbsach", null, null, null, null, null, null); // Đảm bảo tên bảng "tbsach" đúng
                if (c != null && c.moveToFirst()) {
                    String data = "";
                    do {
                        // Giả sử bảng tbsach có 3 cột: index 0, 1, 2
                        // Điều chỉnh index cho phù hợp với cấu trúc bảng của bạn
                        data = c.getString(0) + " - " + c.getString(1) + " - " + c.getString(2);
                        mylist.add(data);
                    } while (c.moveToNext());
                    myadapter.notifyDataSetChanged();
                    Log.i("DB_MAIN", "Tải dữ liệu vào ListView thành công.");
                } else {
                    Log.w("DB_MAIN", "Truy vấn không trả về dữ liệu hoặc bảng 'tbsach' không tồn tại/rỗng.");
                    Toast.makeText(this, "Không có dữ liệu sách hoặc bảng 'tbsach' không tồn tại/rỗng.", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e("DB_MAIN_QUERY_ERROR", "Lỗi khi truy vấn CSDL: " + e.getMessage(), e);
                Toast.makeText(this, "Lỗi truy vấn CSDL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            } finally {
                if (c != null) {
                    c.close();
                }
            }
            if (mylist.isEmpty() && (c == null || c.getCount() == 0) ) { // Kiểm tra kỹ hơn
                Toast.makeText(this, "Không có dữ liệu sách để hiển thị.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e("DB_MAIN", "CSDL là null, không thể truy vấn.");
            Toast.makeText(this, "CSDL chưa được khởi tạo.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean ensureDatabaseCopied() {
        File dbFile = getDatabasePath(DATABASE_NAME);
        Log.i("DB_ENSURE", "Kiểm tra file CSDL tại: " + dbFile.getPath());

        if (!dbFile.exists()) {
            Log.i("DB_ENSURE", "File CSDL không tồn tại. Bắt đầu copy từ assets.");
            try {
                CopyDataBaseFromAsset(); // Hàm này nên ném IOException nếu thất bại
                // Kiểm tra lại sau khi copy
                if (dbFile.exists()) {
                    Log.i("DB_ENSURE_SUCCESS", "Copy CSDL từ assets thành công. File tồn tại tại: " + dbFile.getPath());
                    return true;
                } else {
                    Log.e("DB_ENSURE_FAIL", "Copy CSDL thất bại. File KHÔNG tồn tại tại: " + dbFile.getPath() + " sau khi copy.");
                    return false;
                }
            } catch (IOException e) {
                Log.e("DB_ENSURE_COPY_IO_ERROR", "Lỗi IOException khi copy database: " + e.getMessage(), e);
                return false;
            } catch (Exception e) {
                Log.e("DB_ENSURE_COPY_GEN_ERROR", "Lỗi Exception chung khi copy database: " + e.getMessage(), e);
                return false;
            }
        } else {
            Log.i("DB_ENSURE", "File CSDL đã tồn tại. Không cần copy.");
            return true; // File đã tồn tại, coi như thành công
        }
    }


    // Hàm CopyDataBaseFromAsset() giữ nguyên như phiên bản có nhiều log bạn đã có
// Chỉ cần đảm bảo nó `throws IOException` ở signature.
    public void CopyDataBaseFromAsset() throws IOException {
        InputStream myInput = null;
        OutputStream myOutput = null;
        Log.i("DB_COPY_ASSET", "Bắt đầu hàm CopyDataBaseFromAsset.");

        try {
            Log.i("DB_COPY_ASSET", "Đang cố gắng mở file từ assets: " + DATABASE_NAME);
            myInput = getAssets().open(DATABASE_NAME); // Dòng này sẽ ném FileNotFoundException nếu file không có
            Log.i("DB_COPY_ASSET", "Mở file từ assets thành công.");

            File dbFile = getDatabasePath(DATABASE_NAME);
            File dbDir = new File(dbFile.getParent());

            Log.i("DB_COPY_ASSET", "Đường dẫn thư mục databases: " + dbDir.getPath());
            if (!dbDir.exists()) {
                Log.i("DB_COPY_ASSET", "Thư mục databases không tồn tại. Đang tạo...");
                boolean dirCreated = dbDir.mkdirs();
                if (dirCreated) {
                    Log.i("DB_COPY_ASSET", "Tạo thư mục databases thành công.");
                } else {
                    Log.e("DB_COPY_ASSET_ERROR", "Không thể tạo thư mục databases tại: " + dbDir.getPath());
                    throw new IOException("Không thể tạo thư mục databases: " + dbDir.getPath());
                }
            } else {
                Log.i("DB_COPY_ASSET", "Thư mục databases đã tồn tại.");
            }

            Log.i("DB_COPY_ASSET", "Đường dẫn file CSDL đích: " + dbFile.getPath());
            myOutput = new FileOutputStream(dbFile); // Dòng này có thể ném FileNotFoundException nếu đường dẫn không hợp lệ
            Log.i("DB_COPY_ASSET", "Mở output stream tới file đích thành công.");

            byte[] buffer = new byte[1024];
            int length;
            Log.i("DB_COPY_ASSET", "Bắt đầu vòng lặp copy bytes...");
            while ((length = myInput.read(buffer)) > 0) { // myInput.read có thể ném IOException
                myOutput.write(buffer, 0, length);    // myOutput.write có thể ném IOException
            }
            Log.i("DB_COPY_ASSET", "Kết thúc vòng lặp copy bytes.");
            Log.i("DB_COPY_ASSET_SUCCESS", "Sao chép CSDL hoàn tất.");

        }
        // Không cần bắt FileNotFoundException riêng ở đây nữa vì nó là con của IOException
        // và sẽ được ném ra ngoài để ensureDatabaseCopied() bắt.
        // catch (FileNotFoundException e) {
        //     Log.e("DB_COPY_ASSET_FNF_ERROR", "File '" + DATABASE_NAME + "' không tìm thấy trong thư mục assets hoặc đường dẫn đích không hợp lệ.", e);
        //     throw e;
        // }
        catch (IOException e) { // Bắt tất cả các IOException khác
            Log.e("DB_COPY_ASSET_IO_ERROR", "Lỗi IO trong quá trình copy: " + e.getMessage(), e);
            throw e;
        } finally {
            Log.i("DB_COPY_ASSET", "Vào khối finally để đóng streams.");
            if (myOutput != null) {
                try {
                    myOutput.flush();
                    myOutput.close();
                    Log.i("DB_COPY_ASSET", "Output stream đã đóng.");
                } catch (IOException e) {
                    Log.e("DB_COPY_ASSET_ERROR", "Lỗi khi đóng output stream: " + e.getMessage(), e);
                }
            } else {
                Log.w("DB_COPY_ASSET", "Output stream là null trong finally, có thể đã có lỗi trước đó.");
            }
            if (myInput != null) {
                try {
                    myInput.close();
                    Log.i("DB_COPY_ASSET", "Input stream đã đóng.");
                } catch (IOException e) {
                    Log.e("DB_COPY_ASSET_ERROR", "Lỗi khi đóng input stream: " + e.getMessage(), e);
                }
            } else {
                Log.w("DB_COPY_ASSET", "Input stream là null trong finally, có thể file assets không mở được.");
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (database != null) {
            database.close();
        }
    }
}