package com.example.btth3;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btth3.api.ApiClient;
import com.example.btth3.api.ApiService;
import com.google.gson.Gson; // Thêm Gson để parse error body (tùy chọn)


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivityAPI";

    private RecyclerView recyclerViewPlayers;
    private Button btnAddPlayer;
    private PlayerAdapter playerAdapter;
    private List<Player> playerList;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiService = ApiClient.getApiService();

        recyclerViewPlayers = findViewById(R.id.recyclerViewPlayers);
        btnAddPlayer = findViewById(R.id.btnAddPlayer);

        recyclerViewPlayers.setHasFixedSize(true);
        recyclerViewPlayers.setLayoutManager(new LinearLayoutManager(this));

        playerList = new ArrayList<>();
        playerAdapter = new PlayerAdapter(this, playerList, this::loadPlayersFromApi); // Truyền callback
        recyclerViewPlayers.setAdapter(playerAdapter);

        btnAddPlayer.setOnClickListener(v -> showAddPlayerDialog());

        loadPlayersFromApi();
    }

    private void loadPlayersFromApi() {
        apiService.getPlayers().enqueue(new Callback<PlayerListResponse>() {
            @Override
            public void onResponse(Call<PlayerListResponse> call, Response<PlayerListResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().status) {
                    playerList.clear();
                    playerList.addAll(response.body().data);
                    playerAdapter.notifyDataSetChanged();
                    Log.d(TAG, "Players loaded: " + playerList.size());
                } else {
                    Toast.makeText(MainActivity.this, "Lỗi tải danh sách: " + (response.body() != null ? response.body().message : "Lỗi không xác định"), Toast.LENGTH_SHORT).show();
                    try {
                        if (response.errorBody() != null) Log.e(TAG, "Load error body: " + response.errorBody().string());
                    } catch (Exception e) { e.printStackTrace(); }
                }
            }

            @Override
            public void onFailure(Call<PlayerListResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Load failure: " + t.getMessage());
            }
        });
    }

    private void showAddPlayerDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_player, null);
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

        dialogBuilder.setTitle("Thêm Hội Viên Mới");
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        btnDialogSave.setOnClickListener(v -> {
            String memberCode = etMemberCode.getText().toString().trim().toUpperCase(Locale.ROOT);
            String username = etUsername.getText().toString().trim();
            String birthday = etBirthday.getText().toString().trim(); // YYYY-MM-DD
            String hometown = etHometown.getText().toString().trim();
            String residence = etResidence.getText().toString().trim();
            String ratingSingleStr = etRatingSingle.getText().toString().trim();
            String ratingDoubleStr = etRatingDouble.getText().toString().trim();

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(memberCode)) {
                Toast.makeText(this, "Tên và Mã hội viên không được trống", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!memberCode.matches("MBR\\d{3,}")) {
                etMemberCode.setError("Mã phải có dạng MBRxxx (ví dụ: MBR001)");
                return;
            }


            double ratingSingle = 0;
            double ratingDouble = 0;
            try {
                if (!TextUtils.isEmpty(ratingSingleStr)) ratingSingle = Double.parseDouble(ratingSingleStr);
                if (!TextUtils.isEmpty(ratingDoubleStr)) ratingDouble = Double.parseDouble(ratingDoubleStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Điểm số không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            Player newPlayer = new Player(memberCode, username, birthday, hometown, residence, ratingSingle, ratingDouble);
            // Không cần set ID vì API sẽ tự tạo

            apiService.createPlayer(newPlayer).enqueue(new Callback<PlayerStoreResponse>() {
                @Override
                public void onResponse(Call<PlayerStoreResponse> call, Response<PlayerStoreResponse> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().status) {
                        Toast.makeText(MainActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                        loadPlayersFromApi(); // Reload data
                        alertDialog.dismiss();
                    } else {
                        String errorMsg = "Lỗi thêm hội viên";
                        if (response.body() != null) { // Lỗi từ logic API (ví dụ validation fail do server trả về JSON lỗi)
                            errorMsg += ": " + response.body().message;
                        } else if (response.errorBody() != null) { // Lỗi HTTP (ví dụ 422 từ Validator của Laravel)
                            try {
                                String errorBodyStr = response.errorBody().string();
                                Log.e(TAG, "Create player error body: " + errorBodyStr);
                                // Cố gắng parse lỗi validation từ Laravel
                                Gson gson = new Gson();
                                ErrorResponse errorResponse = gson.fromJson(errorBodyStr, ErrorResponse.class);
                                if (errorResponse != null && errorResponse.errors != null && !errorResponse.errors.isEmpty()) {
                                    StringBuilder errorsBuilder = new StringBuilder();
                                    for (Map.Entry<String, List<String>> entry : errorResponse.errors.entrySet()) {
                                        errorsBuilder.append(entry.getKey()).append(": ");
                                        for (String msg : entry.getValue()) {
                                            errorsBuilder.append(msg).append(". ");
                                        }
                                    }
                                    errorMsg = errorsBuilder.toString();
                                } else if (errorResponse != null && errorResponse.message != null) {
                                    errorMsg = errorResponse.message;
                                } else {
                                    errorMsg += ": " + errorBodyStr;
                                }
                            } catch (Exception e) {
                                errorMsg += ": Lỗi không thể parse response.";
                                Log.e(TAG, "Error parsing error body", e);
                            }
                        }
                        Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<PlayerStoreResponse> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Create player failure: " + t.getMessage());
                }
            });
        });
        btnDialogCancel.setOnClickListener(v -> alertDialog.dismiss());
    }
}