package com.example.karaoke;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class myarrayAdapter extends ArrayAdapter<Item> {
    Activity context;
    int layoutId; // This will be R.layout.listitem
    ArrayList<Item> myArray;

    public myarrayAdapter(Activity context, int layoutId, ArrayList<Item> myArray) {
        super(context, layoutId, myArray);
        this.context = context;
        this.layoutId = layoutId;
        this.myArray = myArray;
    }

    private static class ViewHolder {
        TextView txtMaso;
        TextView txtTieude;
        ImageButton btnLike;
        ImageButton btnUnlike;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(layoutId, null);
            holder = new ViewHolder();
            holder.txtMaso = convertView.findViewById(R.id.txtmaso);
            holder.txtTieude = convertView.findViewById(R.id.txttieude);
            holder.btnLike = convertView.findViewById(R.id.btnlike);
            holder.btnUnlike = convertView.findViewById(R.id.btnunlike);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Item myItem = myArray.get(position);

        holder.txtMaso.setText(myItem.getMaso());
        holder.txtTieude.setText(myItem.getTieude());

        // Reset text color for recycled views
        holder.txtMaso.setTextColor(Color.BLACK); // Or your default color
        holder.txtTieude.setTextColor(Color.BLACK); // Or your default color


        if (myItem.getThich() == 1) {
            holder.btnLike.setVisibility(View.VISIBLE);
            holder.btnUnlike.setVisibility(View.INVISIBLE);
        } else {
            holder.btnLike.setVisibility(View.INVISIBLE);
            holder.btnUnlike.setVisibility(View.VISIBLE);
        }

        holder.btnLike.setOnClickListener(v -> {
            updateYeuthichStatusInDB(myItem.getMaso(), 0); // Update DB to unlike
            myItem.setThich(0); // Update local item
            holder.btnLike.setVisibility(View.INVISIBLE);
            holder.btnUnlike.setVisibility(View.VISIBLE);
            if(context instanceof MainActivity){ // Notify MainActivity to refresh lists if needed
                ((MainActivity)context).refreshCurrentTabAndFavorites();
            }
        });

        holder.btnUnlike.setOnClickListener(v -> {
            updateYeuthichStatusInDB(myItem.getMaso(), 1); // Update DB to like
            myItem.setThich(1); // Update local item
            holder.btnUnlike.setVisibility(View.INVISIBLE);
            holder.btnLike.setVisibility(View.VISIBLE);
            if(context instanceof MainActivity){
                ((MainActivity)context).refreshCurrentTabAndFavorites();
            }
        });

        // Click on title to open details
        holder.txtTieude.setOnClickListener(v -> {
            holder.txtTieude.setTextColor(Color.RED);
            holder.txtMaso.setTextColor(Color.RED);

            Intent intent = new Intent(context, activitysub.class);
            Bundle bundle = new Bundle();
            bundle.putString("maso", myItem.getMaso());
            intent.putExtra("package", bundle); // As per document
            context.startActivity(intent);
        });
        return convertView;
    }

    private void updateYeuthichStatusInDB(String maBH, int status) {
        ContentValues values = new ContentValues();
        values.put("YEUTHICH", status);
        // Assuming MainActivity.database is accessible and initialized
        MainActivity.database.update("ArirangSongList", values, "MABH = ?", new String[]{maBH});
    }
}