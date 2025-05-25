package com.example.tabselector_2;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class myarrayAdapter extends ArrayAdapter<Item> { // Sửa tên class cho đúng quy ước
    Activity context;
    int layoutId; // Sửa tên biến cho đúng quy ước
    ArrayList<Item> myArray; // Sửa tên biến cho đúng quy ước

    public myarrayAdapter(@NonNull Activity context, int layoutId, @NonNull ArrayList<Item> arr) {
        super(context, layoutId, arr);
        this.context = context;
        this.layoutId = layoutId;
        this.myArray = arr;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(layoutId, null); // Sử dụng layoutId đã truyền vào

        if (myArray.size() > 0 && position < myArray.size()) { // Kiểm tra để tránh lỗi
            final Item myItem = myArray.get(position);

            TextView txtMaso = convertView.findViewById(R.id.txtmaso);
            txtMaso.setText(myItem.getMaso());

            TextView txtTieude = convertView.findViewById(R.id.txttieude);
            txtTieude.setText(myItem.getTieude());

            ImageButton btnLike = convertView.findViewById(R.id.btnlike);
            if (myItem.getThich() == 1) {
                btnLike.setImageResource(R.drawable.like);
            } else {
                btnLike.setImageResource(R.drawable.unlike);
            }

            // (Tùy chọn) Thêm sự kiện click cho nút like/unlike nếu cần
            // btnLike.setOnClickListener(new View.OnClickListener() {
            // @Override
            // public void onClick(View v) {
            // // Xử lý thay đổi trạng thái 'thich' và cập nhật giao diện/dữ liệu
            // if (myItem.getThich() == 1) {
            // myItem.setThich(0);
            // btnLike.setImageResource(R.drawable.unlike);
            // } else {
            // myItem.setThich(1);
            // btnLike.setImageResource(R.drawable.like);
            // }
            // // Cần có cơ chế lưu trữ trạng thái like này (ví dụ SharedPreferences hoặc Database)
            // }
            // });
        }
        return convertView;
    }
}
