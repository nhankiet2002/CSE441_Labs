package com.example.btth1;

import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {

    private Context context;
    private List<Player> playerList;
    private DatabaseReference databasePlayers; // Tham chiếu đến node 'players' trên Firebase

    public PlayerAdapter(Context context, List<Player> playerList) {
        this.context = context;
        this.playerList = playerList;
        // Khởi tạo tham chiếu đến node 'players'
        this.databasePlayers = FirebaseDatabase.getInstance().getReference("players");
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_player, parent, false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        Player player = playerList.get(position);

        holder.tvPlayerName.setText(player.username);
        holder.tvPlayerCode.setText("Mã: " + player.member_code);
        holder.tvPlayerHometown.setText("Quê quán: " + player.hometown);

        holder.btnEditPlayer.setOnClickListener(v -> {
            showUpdateDialog(player);
        });

        holder.btnDeletePlayer.setOnClickListener(v -> {
            deletePlayer(player.member_code);
        });
    }

    @Override
    public int getItemCount() {
        return playerList.size();
    }

    // Hàm để xóa Player
    private void deletePlayer(String memberCode) {
        // Xóa player khỏi Firebase dựa trên member_code
        databasePlayers.child(memberCode).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(context, "Đã xóa hội viên!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Lỗi khi xóa!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Hàm để hiển thị Dialog cập nhật Player
    private void showUpdateDialog(Player playerToUpdate) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_add_player, null); // Tạo layout dialog_add_player.xml
        dialogBuilder.setView(dialogView);

        final EditText etMemberCode = dialogView.findViewById(R.id.etMemberCode);
        final EditText etUsername = dialogView.findViewById(R.id.etUsername);
        final EditText etBirthday = dialogView.findViewById(R.id.etBirthday);
        final EditText etHometown = dialogView.findViewById(R.id.etHometown);
        final EditText etResidence = dialogView.findViewById(R.id.etResidence);
        final EditText etRatingSingle = dialogView.findViewById(R.id.etRatingSingle);
        final EditText etRatingDouble = dialogView.findViewById(R.id.etRatingDouble);
        Button btnDialogUpdate = dialogView.findViewById(R.id.btnDialogSave); // Đổi tên nút trong dialog layout
        Button btnDialogCancel = dialogView.findViewById(R.id.btnDialogCancel);

        dialogBuilder.setTitle("Cập nhật Hội viên");
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        // Điền thông tin cũ vào dialog
        etMemberCode.setText(playerToUpdate.member_code);
        etMemberCode.setEnabled(false); // Không cho sửa Member Code vì nó là key
        etUsername.setText(playerToUpdate.username);
        etBirthday.setText(playerToUpdate.birthday);
        etHometown.setText(playerToUpdate.hometown);
        etResidence.setText(playerToUpdate.residence);
        etRatingSingle.setText(String.valueOf(playerToUpdate.rating_single));
        etRatingDouble.setText(String.valueOf(playerToUpdate.rating_double));
        btnDialogUpdate.setText("Cập nhật");


        btnDialogUpdate.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String birthday = etBirthday.getText().toString().trim();
            String hometown = etHometown.getText().toString().trim();
            String residence = etResidence.getText().toString().trim();
            String ratingSingleStr = etRatingSingle.getText().toString().trim();
            String ratingDoubleStr = etRatingDouble.getText().toString().trim();

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(playerToUpdate.member_code)) {
                Toast.makeText(context, "Tên và Mã hội viên không được trống", Toast.LENGTH_SHORT).show();
                return;
            }

            double ratingSingle = 0;
            double ratingDouble = 0;
            try {
                if (!TextUtils.isEmpty(ratingSingleStr)) ratingSingle = Double.parseDouble(ratingSingleStr);
                if (!TextUtils.isEmpty(ratingDoubleStr)) ratingDouble = Double.parseDouble(ratingDoubleStr);
            } catch (NumberFormatException e) {
                Toast.makeText(context, "Điểm số không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tạo đối tượng Player mới với thông tin cập nhật
            Player updatedPlayer = new Player(
                    playerToUpdate.member_code, // Giữ nguyên member_code
                    username,
                    playerToUpdate.avatar, // Giữ avatar cũ, có thể thêm chức năng cập nhật avatar sau
                    birthday, hometown, residence,
                    ratingSingle, ratingDouble
            );

            // Cập nhật trên Firebase
            databasePlayers.child(playerToUpdate.member_code).setValue(updatedPlayer)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        } else {
                            Toast.makeText(context, "Lỗi khi cập nhật!", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        btnDialogCancel.setOnClickListener(v -> alertDialog.dismiss());
    }


    static class PlayerViewHolder extends RecyclerView.ViewHolder {
        TextView tvPlayerName, tvPlayerCode, tvPlayerHometown;
        Button btnEditPlayer, btnDeletePlayer;

        public PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPlayerName = itemView.findViewById(R.id.tvPlayerName);
            tvPlayerCode = itemView.findViewById(R.id.tvPlayerCode);
            tvPlayerHometown = itemView.findViewById(R.id.tvPlayerHometown);
            btnEditPlayer = itemView.findViewById(R.id.btnEditPlayer);
            btnDeletePlayer = itemView.findViewById(R.id.btnDeletePlayer);
        }
    }
}
