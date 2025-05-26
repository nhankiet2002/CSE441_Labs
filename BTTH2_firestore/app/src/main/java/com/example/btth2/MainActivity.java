package com.example.btth2;


import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale; // Thêm import này

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivityFirestore"; // Tag cho Logcat

    private RecyclerView recyclerViewPlayers;
    private Button btnAddPlayer;
    private PlayerAdapter playerAdapter;
    private List<Player> playerList;
    private FirebaseFirestore db; // Instance của Firestore
    private CollectionReference playersRef; // Tham chiếu đến collection 'players'

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo Firestore và tham chiếu đến collection "players"
        db = FirebaseFirestore.getInstance();
        playersRef = db.collection("players");

        recyclerViewPlayers = findViewById(R.id.recyclerViewPlayers);
        btnAddPlayer = findViewById(R.id.btnAddPlayer);

        recyclerViewPlayers.setHasFixedSize(true);
        recyclerViewPlayers.setLayoutManager(new LinearLayoutManager(this));

        playerList = new ArrayList<>();
        playerAdapter = new PlayerAdapter(this, playerList);
        recyclerViewPlayers.setAdapter(playerAdapter);

        btnAddPlayer.setOnClickListener(v -> showAddPlayerDialog());

        // Lắng nghe sự thay đổi dữ liệu từ Firestore (realtime updates)
        loadPlayersData();
    }

    private void loadPlayersData() {
        playersRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    Toast.makeText(MainActivity.this, "Lỗi đọc dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                playerList.clear();
                if (snapshots != null) {
                    for (DocumentSnapshot doc : snapshots.getDocuments()) {
                        Player player = doc.toObject(Player.class);
                        if (player != null) {
                            // Firestore không tự động gán ID của document vào object Player
                            // nếu member_code không phải là ID.
                            // Nếu member_code của bạn chính là ID document, thì nó đã có trong player.
                            // Nếu bạn muốn lấy ID document riêng, bạn có thể làm:
                            // player.setDocumentId(doc.getId()); // (Cần thêm trường documentId và setter trong Player.java)
                            // Tuy nhiên, theo đề bài, member_code chính là ID, nên không cần bước này.
                            playerList.add(player);
                        }
                    }
                    playerAdapter.notifyDataSetChanged();
                    Log.d(TAG, "Data loaded. Player count: " + playerList.size());
                } else {
                    Log.d(TAG, "Current data: null");
                }
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
            final String memberCode = etMemberCode.getText().toString().trim().toUpperCase(Locale.ROOT); // Sử dụng Locale
            final String username = etUsername.getText().toString().trim();
            final String birthday = etBirthday.getText().toString().trim();
            final String hometown = etHometown.getText().toString().trim();
            final String residence = etResidence.getText().toString().trim();
            String ratingSingleStr = etRatingSingle.getText().toString().trim();
            String ratingDoubleStr = etRatingDouble.getText().toString().trim();

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(memberCode)) {
                Toast.makeText(this, "Tên và Mã hội viên không được trống", Toast.LENGTH_SHORT).show();
                return;
            }
            // Yêu cầu ID document Firestore không được chứa ký tự '/'
            if (!memberCode.matches("MBR\\d{3,}") || memberCode.contains("/")) {
                etMemberCode.setError("Mã phải có dạng MBRxxx và không chứa '/'");
                return;
            }

            final double ratingSingle;
            final double ratingDouble;
            try {
                ratingSingle = TextUtils.isEmpty(ratingSingleStr) ? 0 : Double.parseDouble(ratingSingleStr);
                ratingDouble = TextUtils.isEmpty(ratingDoubleStr) ? 0 : Double.parseDouble(ratingDoubleStr);
            } catch (NumberFormatException ex) {
                Toast.makeText(this, "Điểm số không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra member_code (Document ID) đã tồn tại chưa
            playersRef.document(memberCode).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        Toast.makeText(MainActivity.this, "Mã hội viên đã tồn tại!", Toast.LENGTH_SHORT).show();
                        etMemberCode.setError("Mã đã tồn tại");
                    } else {
                        // Mã chưa tồn tại, tiến hành thêm
                        Player player = new Player(memberCode, username, "default_avatar.png",
                                birthday, hometown, residence, ratingSingle, ratingDouble);

                        // Thêm player vào Firestore, sử dụng memberCode làm Document ID
                        playersRef.document(memberCode).set(player)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(MainActivity.this, "Thêm hội viên thành công!", Toast.LENGTH_SHORT).show();
                                    alertDialog.dismiss();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(MainActivity.this, "Lỗi khi thêm: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, "Error adding document", e);
                                });
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Lỗi kiểm tra mã: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error checking document", task.getException());
                }
            });
        });
        btnDialogCancel.setOnClickListener(v -> alertDialog.dismiss());
    }
}