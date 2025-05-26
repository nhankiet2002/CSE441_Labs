package com.example.btth3;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlayerListResponse {
    @SerializedName("status")
    public boolean status;

    @SerializedName("message")
    public String message;

    @SerializedName("data")
    public List<Player> data; // Dùng cho GET danh sách
}
