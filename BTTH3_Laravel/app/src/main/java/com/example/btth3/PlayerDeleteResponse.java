package com.example.btth3;

import com.google.gson.annotations.SerializedName;

// Dùng cho response DELETE (thành công)
public class PlayerDeleteResponse {
    @SerializedName("status")
    public boolean status;

    @SerializedName("message")
    public String message;
}
