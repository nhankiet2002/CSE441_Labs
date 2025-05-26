package com.example.webservice;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List; // Sử dụng java.util.List

public class MyArrayAdapter extends ArrayAdapter<ListArticle> {
    private Activity context;
    private int layoutID;
    private ArrayList<ListArticle> arr; // Danh sách các đối tượng ListArticle

    public MyArrayAdapter(@NonNull Activity context, int resource, @NonNull ArrayList<ListArticle> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutID = resource;
        this.arr = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(layoutID, null);

        if (arr.size() > 0 && position < arr.size()) { // Kiểm tra ràng buộc
            final ListArticle currentArticle = arr.get(position);

            ImageView imgItem = convertView.findViewById(R.id.imgView);
            TextView txtTitleItem = convertView.findViewById(R.id.txtTitle);
            TextView txtInfoItem = convertView.findViewById(R.id.txtInfo);

            if (currentArticle.getImg() != null) {
                imgItem.setImageBitmap(currentArticle.getImg());
            } else {
                // Có thể đặt ảnh placeholder nếu không có ảnh
                imgItem.setImageResource(R.mipmap.ic_launcher); // Ví dụ ảnh mặc định
            }
            txtTitleItem.setText(currentArticle.getTitle());
            txtInfoItem.setText(currentArticle.getInfo());
        }
        return convertView;
    }
}
