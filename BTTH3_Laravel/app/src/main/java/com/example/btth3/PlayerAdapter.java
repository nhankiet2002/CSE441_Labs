package com.example.btth3;

import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btth3.api.ApiClient;
import com.example.btth3.api.ApiService;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {
    private static final String TAG = "PlayerAdapter";
    private Context context;
    private List<Player> playerList;
    private ApiService apiService;
    private Runnable refreshDataCallback; // Callback để refresh MainActivity

    public PlayerAdapter(Context context, List<Player> playerList, Runnable refreshCallback) {
        this.context = context;
        this.playerList = playerList;
        this.apiService = ApiClient.getApiService();
        this.refreshDataCallback = refreshCallback;
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
        holder.tvPlayerName.setText(player.getUsername());
        holder.tvPlayerCode.setText("Mã: " + player.getMember_code() + " (ID: " + player.getId() +")");
        holder.tvPlayerHometown.setText("Quê quán: " + player.getHometown());

        holder.btnEditPlayer.setOnClickListener(v -> showUpdateDialog(player));
        holder.btnDeletePlayer.setOnClickListener(v -> deletePlayerApi(player.getId()));
    }

    @Override
    public int getItemCount() {
        return playerList.size();
    }

    private void deletePlayerApi(int playerId) {
        new AlertDialog.Builder(context)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa hội viên này không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    apiService.deletePlayer(playerId).enqueue(new Callback<PlayerDeleteResponse>() {
                        @Override
                        public void onResponse(Call<PlayerDeleteResponse> call, Response<PlayerDeleteResponse> response) {
                            if (response.isSuccessful() && response.body() != null && response.body().status) {
                                Toast.makeText(context, response.body().message, Toast.LENGTH_SHORT).show();
                                if (refreshDataCallback != null) refreshDataCallback.run(); // Refresh list
                            } else {
                                Toast.makeText(context, "Lỗi xóa: " + (response.body() != null ? response.body().message : "Lỗi không xác định"), Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e(TAG, "Delete error body: " + response.errorBody().string());
                                } catch (Exception e) { e.printStackTrace(); }
                            }
                        }
                        @Override
                        public void onFailure(Call<PlayerDeleteResponse> call, Throwable t) {
                            Toast.makeText(context, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "Delete failure: " + t.getMessage());
                        }
                    });
                })
                .setNegativeButton("Hủy", null)
                .show();
    }


    private void showUpdateDialog(Player playerToUpdate) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_add_player, null); // Tái sử dụng dialog layout
        dialogBuilder.setView(dialogView);

        final EditText etMemberCode = dialogView.findViewById(R.id.etMemberCode);
        final EditText etUsername = dialogView.findViewById(R.id.etUsername);
        final EditText etBirthday = dialogView.findViewById(R.id.etBirthday);
        final EditText etHometown = dialogView.findViewById(R.id.etHometown);
        final EditText etResidence = dialogView.findViewById(R.id.etResidence);
        final EditText etRatingSingle = dialogView.findViewById(R.id.etRatingSingle);
        final EditText etRatingDouble = dialogView.findViewById(R.id.etRatingDouble);
        Button btnDialogSave = dialogView.findViewById(R.id.btnDialogSave);
        Button btnDialogCancel = dialogView.findViewById(R.id.btnDialogCancel);

        dialogBuilder.setTitle("Cập nhật Hội viên (ID: " + playerToUpdate.getId() + ")");
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        // Điền thông tin cũ
        etMemberCode.setText(playerToUpdate.getMember_code());
        etMemberCode.setEnabled(false); // Không cho sửa member_code qua dialog này, API không hỗ trợ đổi member_code khi update
        etUsername.setText(playerToUpdate.getUsername());
        etBirthday.setText(playerToUpdate.getBirthday());
        etHometown.setText(playerToUpdate.getHometown());
        etResidence.setText(playerToUpdate.getResidence());
        etRatingSingle.setText(String.valueOf(playerToUpdate.getRating_single()));
        etRatingDouble.setText(String.valueOf(playerToUpdate.getRating_double()));
        btnDialogSave.setText("Cập nhật");

        btnDialogSave.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String birthday = etBirthday.getText().toString().trim();
            String hometown = etHometown.getText().toString().trim();
            String residence = etResidence.getText().toString().trim();
            String ratingSingleStr = etRatingSingle.getText().toString().trim();
            String ratingDoubleStr = etRatingDouble.getText().toString().trim();

            if (TextUtils.isEmpty(username)) {
                Toast.makeText(context, "Tên hội viên không được trống", Toast.LENGTH_SHORT).show();
                return;
            }

            double ratingSingle = playerToUpdate.getRating_single(); // Giữ giá trị cũ nếu không nhập
            double ratingDouble = playerToUpdate.getRating_double();
            try {
                if (!TextUtils.isEmpty(ratingSingleStr)) ratingSingle = Double.parseDouble(ratingSingleStr);
                if (!TextUtils.isEmpty(ratingDoubleStr)) ratingDouble = Double.parseDouble(ratingDoubleStr);
            } catch (NumberFormatException e) {
                Toast.makeText(context, "Điểm số không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            Player updatedPlayerData = new Player(); // Tạo object mới để gửi đi, không bao gồm ID
            updatedPlayerData.setMember_code(playerToUpdate.getMember_code()); // Vẫn gửi member_code nếu API cần
            updatedPlayerData.setUsername(username);
            updatedPlayerData.setBirthday(birthday);
            updatedPlayerData.setHometown(hometown);
            updatedPlayerData.setResidence(residence);
            updatedPlayerData.setRating_single(ratingSingle);
            updatedPlayerData.setRating_double(ratingDouble);
            // updatedPlayerData.setAvatar(playerToUpdate.getAvatar()); // Giữ avatar cũ

            apiService.updatePlayer(playerToUpdate.getId(), updatedPlayerData).enqueue(new Callback<PlayerStoreResponse>() {
                @Override
                public void onResponse(Call<PlayerStoreResponse> call, Response<PlayerStoreResponse> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().status) {
                        Toast.makeText(context, response.body().message, Toast.LENGTH_SHORT).show();
                        if (refreshDataCallback != null) refreshDataCallback.run();
                        alertDialog.dismiss();
                    } else {
                        String errorMsg = "Lỗi cập nhật";
                        if (response.body() != null) errorMsg += ": " + response.body().message;
                        else if (response.errorBody() != null) {
                            try { errorMsg += ": " + response.errorBody().string(); } catch (Exception e) {}
                        }
                        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Update error: " + errorMsg);
                    }
                }
                @Override
                public void onFailure(Call<PlayerStoreResponse> call, Throwable t) {
                    Toast.makeText(context, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Update failure: " + t.getMessage());
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
