package com.example.b13_customlistview;

import android.app.Activity;
import java.util.ArrayList;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PhoneAdapter extends ArrayAdapter<com.example.b13_customlistview.Phone> {

    Activity context;
    int layoutResourceId;
    ArrayList<Phone> phoneList;

    public PhoneAdapter(@NonNull Activity context, int layoutResourceId, @NonNull ArrayList<Phone> phoneList) {
        super(context, layoutResourceId, phoneList);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.phoneList = phoneList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        PhoneHolder holder;

        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new PhoneHolder();
            holder.imgPhone = row.findViewById(R.id.imageViewPhone);
            holder.txtName = row.findViewById(R.id.textViewPhoneName);
            holder.txtPrice = row.findViewById(R.id.textViewPhonePrice);

            row.setTag(holder);
        } else {
            holder = (PhoneHolder) row.getTag();
        }

        Phone phone = phoneList.get(position);

        holder.imgPhone.setImageResource(phone.getImageResId());
        holder.txtName.setText(phone.getName());
        holder.txtPrice.setText("Giá bán: " + phone.getPrice());

        return row;
    }

    static class PhoneHolder {
        ImageView imgPhone;
        TextView txtName;
        TextView txtPrice;
    }
}