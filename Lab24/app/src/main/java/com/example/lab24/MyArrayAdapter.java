package com.example.lab24;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.List;

public class MyArrayAdapter extends ArrayAdapter<Tygia> {
    Activity context;
    int resource;
    List<Tygia> objects;

    public MyArrayAdapter(@NonNull Activity context, int resource, @NonNull List<Tygia> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View item = inflater.inflate(this.resource, null);

        Tygia tyGia = this.objects.get(position);

        ImageView imgNgoaiTe = item.findViewById(R.id.imgNgoaiTe); // Nếu có ảnh
        TextView txtTenNgoaiTe = item.findViewById(R.id.txtTenNgoaiTe);
        TextView txtMaNgoaiTe = item.findViewById(R.id.txtMaNgoaiTe);
        TextView txtMuaTM = item.findViewById(R.id.txtMuaTM_VTB);
        TextView txtMuaCK = item.findViewById(R.id.txtMuaCK_VTB);
        TextView txtBanTM = item.findViewById(R.id.txtBanTM_VTB);
        TextView txtBanCK = item.findViewById(R.id.txtBanCK_VTB);
        TextView txtBanETax = item.findViewById(R.id.txtBanETax_VTB);


        // Xử lý ảnh nếu có, nếu không có thể ẩn ImageView hoặc đặt placeholder
        if (tyGia.getAnhNgoaiTe() != null) {
            imgNgoaiTe.setImageBitmap(tyGia.getAnhNgoaiTe());
        } else {
            imgNgoaiTe.setVisibility(View.GONE); // Hoặc đặt placeholder
            // imgNgoaiTe.setImageResource(R.mipmap.ic_launcher);
        }

        txtTenNgoaiTe.setText(tyGia.getTenNgoaiTe());
        txtMaNgoaiTe.setText(tyGia.getMaNgoaiTe());
        txtMuaTM.setText(tyGia.getMuaTienMat());
        txtMuaCK.setText(tyGia.getMuaChuyenKhoan());
        txtBanTM.setText(tyGia.getBanTienMat());
        txtBanCK.setText(tyGia.getBanChuyenKhoan());
        txtBanETax.setText(tyGia.getTyGiaBanETax());


        return item;
    }
}