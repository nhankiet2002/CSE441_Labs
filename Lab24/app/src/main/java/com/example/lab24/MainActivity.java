    package com.example.lab24;

    import androidx.appcompat.app.AppCompatActivity;
    import android.os.AsyncTask;
    import android.os.Bundle;
    import android.util.Log;
    import android.widget.ListView;
    import android.widget.TextView;
    import org.jsoup.Jsoup;
    import org.jsoup.nodes.Document;
    import org.jsoup.nodes.Element;
    import org.jsoup.select.Elements;
    import java.io.IOException;
    import java.util.ArrayList;
    import java.util.List;
    import java.text.SimpleDateFormat;
    import java.util.Date;
    import java.util.Locale;

    public class MainActivity extends AppCompatActivity {

        private TextView txtNgayCapNhat;
        private ListView listViewVietinbank;
        private MyArrayAdapter adapter;
        private List<Tygia> dsTyGia;

        private static final String VIETINBANK_URL = "https://www.vietinbank.vn/ca-nhan/ty-gia-khcn";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            txtNgayCapNhat = findViewById(R.id.txtNgayCapNhat);
            listViewVietinbank = findViewById(R.id.listViewVietinbank);
            dsTyGia = new ArrayList<>();
            adapter = new MyArrayAdapter(this, R.layout.layout_listview, dsTyGia);
            listViewVietinbank.setAdapter(adapter);

            new FetchTyGiaTask().execute(VIETINBANK_URL);

            // Set current date for update time (initial placeholder)
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());
            txtNgayCapNhat.setText("Dữ liệu cập nhật lúc: " + currentDateandTime); // Sẽ được cập nhật lại từ web nếu có
        }

        private class FetchTyGiaTask extends AsyncTask<String, Void, Document> {
            private String ngayCapNhatTuWeb = "";

            @Override
            protected Document doInBackground(String... urls) {
                try {
                    // Thiết lập timeout để tránh chờ quá lâu
                    return Jsoup.connect(urls[0]).timeout(10000).get();
                } catch (IOException e) {
                    Log.e("FetchTyGiaTask", "Error fetching HTML", e);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Document document) {
                if (document == null) {
                    txtNgayCapNhat.setText("Lỗi tải dữ liệu. Vui lòng kiểm tra kết nối mạng.");
                    return;
                }

                dsTyGia.clear(); // Xóa dữ liệu cũ trước khi thêm mới

                try {
                    // Cố gắng lấy ngày cập nhật từ thẻ <p class="date-silver">
                    Element dateElement = document.selectFirst("p.date-silver");
                    if (dateElement != null) {
                        ngayCapNhatTuWeb = dateElement.text();
                        txtNgayCapNhat.setText(ngayCapNhatTuWeb);
                    }


                    // Tìm bảng chứa tỷ giá. Selector này có thể cần điều chỉnh.
                    // Kiểm tra cấu trúc HTML của trang để có selector chính xác.
                    // Giả sử bảng có id="exchange-rate-table" hoặc class="tbl-exchange-rate"
                    Element tyGiaTable = document.selectFirst("table.tbl-exchange-rate"); // Cần kiểm tra selector này
                    if (tyGiaTable == null) {
                        tyGiaTable = document.select("table").get(1); // Thử lấy bảng thứ 2 nếu selector trên không hoạt động
                    }


                    if (tyGiaTable != null) {
                        Elements rows = tyGiaTable.select("tbody tr"); // Lấy các hàng trong tbody

                        for (Element row : rows) {
                            Elements cols = row.select("td"); // Lấy các ô trong hàng

                            // VietinBank có thể có hàng trống hoặc hàng không đúng định dạng
                            if (cols.size() >= 7) { // Cần ít nhất 7 cột dữ liệu
                                Tygia tyGia = new Tygia();

                                // Tên ngoại tệ và mã ngoại tệ có thể nằm trong cùng một cột hoặc tách biệt
                                // Giả sử cột đầu tiên chứa tên và mã
                                String tenVaMa = cols.get(0).text();
                                // Cố gắng tách tên và mã (ví dụ: "Đô la Mỹ (USD)")
                                if (tenVaMa.contains("(")) {
                                    tyGia.setTenNgoaiTe(tenVaMa.substring(0, tenVaMa.indexOf("(")).trim());
                                    tyGia.setMaNgoaiTe(tenVaMa.substring(tenVaMa.indexOf("(") + 1, tenVaMa.indexOf(")")).trim());
                                } else {
                                    tyGia.setTenNgoaiTe(tenVaMa.trim()); // Nếu không có mã trong ngoặc
                                    tyGia.setMaNgoaiTe(tenVaMa.trim()); // Hoặc đặt mã là tên luôn
                                }

                                // Lấy dữ liệu từ các cột còn lại
                                tyGia.setMuaTienMat(cols.get(1).text().trim());
                                tyGia.setMuaChuyenKhoan(cols.get(2).text().trim());
                                tyGia.setBanTienMat(cols.get(3).text().trim());
                                tyGia.setBanChuyenKhoan(cols.get(4).text().trim());
                                tyGia.setTyGiaBanETax(cols.get(5).text().trim()); // Vietinbank có cột này, cột thứ 6 (index 5)

                                // Ảnh ngoại tệ: Trang Vietinbank không có ảnh cờ dễ lấy như Đông Á
                                // Bạn có thể bỏ qua hoặc thêm logic tìm ảnh nếu có
                                tyGia.setAnhNgoaiTe(null); // Hoặc tìm ảnh từ một nguồn khác dựa trên mã ngoại tệ

                                dsTyGia.add(tyGia);
                            }
                        }
                    } else {
                        Log.e("FetchTyGiaTask", "Không tìm thấy bảng tỷ giá.");
                        txtNgayCapNhat.setText(txtNgayCapNhat.getText() + " (Không tìm thấy bảng tỷ giá)");
                    }

                } catch (Exception e) {
                    Log.e("FetchTyGiaTask", "Error parsing HTML", e);
                    txtNgayCapNhat.setText(txtNgayCapNhat.getText() + " (Lỗi phân tích dữ liệu)");
                }

                adapter.notifyDataSetChanged(); // Cập nhật ListView
            }
        }
    }