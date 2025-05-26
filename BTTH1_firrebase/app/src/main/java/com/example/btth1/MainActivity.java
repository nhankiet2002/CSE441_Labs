package com.example.btth1;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPlayers;
    private Button btnAddPlayer;
    private PlayerAdapter playerAdapter;
    private List<Player> playerList;
    private DatabaseReference databasePlayers; // Tham chiếu đến node 'players'

    // Biến đếm để tự sinh member_code (ví dụ đơn giản)
    // Trong thực tế, nên có cơ chế sinh ID phức tạp hơn hoặc dùng push keys của Firebase
    private int memberCounter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo Firebase Database reference đến node "players"
        databasePlayers = FirebaseDatabase.getInstance().getReference("players");
        // Để lấy số lượng player hiện có để tiếp tục đánh số member_code
        databasePlayers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    memberCounter = (int) snapshot.getChildrenCount();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });


        recyclerViewPlayers = findViewById(R.id.recyclerViewPlayers);
        btnAddPlayer = findViewById(R.id.btnAddPlayer);

        recyclerViewPlayers.setHasFixedSize(true);
        recyclerViewPlayers.setLayoutManager(new LinearLayoutManager(this));

        playerList = new ArrayList<>();
        playerAdapter = new PlayerAdapter(this, playerList);
        recyclerViewPlayers.setAdapter(playerAdapter);

        btnAddPlayer.setOnClickListener(v -> showAddPlayerDialog());

        // Lắng nghe sự thay đổi dữ liệu từ Firebase
        databasePlayers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                playerList.clear(); // Xóa danh sách cũ
                for (DataSnapshot playerSnapshot : dataSnapshot.getChildren()) {
                    Player player = playerSnapshot.getValue(Player.class);
                    playerList.add(player);
                }
                playerAdapter.notifyDataSetChanged(); // Cập nhật RecyclerView
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi (ví dụ: hiển thị Toast)
                Toast.makeText(MainActivity.this, "Lỗi đọc dữ liệu: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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
            // Lấy giá trị từ EditText bên trong OnClickListener
            // Điều này đảm bảo chúng là "effectively final" tại thời điểm sử dụng trong các inner class tiếp theo
            final String memberCode = etMemberCode.getText().toString().trim().toUpperCase();
            final String username = etUsername.getText().toString().trim();
            final String birthday = etBirthday.getText().toString().trim(); // Sửa ở đây
            final String hometown = etHometown.getText().toString().trim(); // Sửa ở đây
            final String residence = etResidence.getText().toString().trim(); // Sửa ở đây
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

            final double ratingSingle; // Sửa ở đây
            final double ratingDouble; // Sửa ở đây
            try {
                ratingSingle = TextUtils.isEmpty(ratingSingleStr) ? 0 : Double.parseDouble(ratingSingleStr);
                ratingDouble = TextUtils.isEmpty(ratingDoubleStr) ? 0 : Double.parseDouble(ratingDoubleStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Điểm số không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra xem member_code đã tồn tại chưa
            databasePlayers.child(memberCode).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Toast.makeText(MainActivity.this, "Mã hội viên đã tồn tại!", Toast.LENGTH_SHORT).show();
                        etMemberCode.setError("Mã đã tồn tại");
                    } else {
                        // Các biến birthday, hometown, residence, ratingSingle, ratingDouble giờ đã là final (hoặc effectively final)
                        // khi được sử dụng ở đây.
                        Player player = new Player(memberCode, username, "default_avatar.png",
                                birthday, hometown, residence, ratingSingle, ratingDouble); // Dòng 159

                        databasePlayers.child(memberCode).setValue(player).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Thêm hội viên thành công!", Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                            } else {
                                Toast.makeText(MainActivity.this, "Lỗi khi thêm!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, "Lỗi kiểm tra mã: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
        btnDialogCancel.setOnClickListener(v -> alertDialog.dismiss());
    }
}
