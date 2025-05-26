package com.example.btth3;

import com.google.gson.annotations.SerializedName;

public class PlayerStoreResponse {
    @SerializedName("status")
    public boolean status;

    @SerializedName("message")
    public String message;

    @SerializedName("data")
    public Player data;
}