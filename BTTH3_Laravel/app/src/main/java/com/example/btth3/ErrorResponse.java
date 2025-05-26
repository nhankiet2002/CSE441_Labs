package com.example.btth3;


import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;

public class ErrorResponse {
    @SerializedName("status")
    public boolean status;

    @SerializedName("message")
    public String message;

    @SerializedName("errors")
    public Map<String, List<String>> errors;
}