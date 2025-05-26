package com.example.btth3;

import com.google.gson.annotations.SerializedName;

// Dùng cho response GET danh sách và GET chi tiết một player
public class PlayerResponse {
    @SerializedName("status")
    public boolean status;

    @SerializedName("message")
    public String message;

    @SerializedName("data")
    public Player data; // Dùng cho GET chi tiết
}
