package com.example.btth3;

import com.google.gson.annotations.SerializedName;

public class Player {
    // Khóa chính từ MySQL, thường là 'id'
    @SerializedName("id") // Quan trọng nếu tên field trong JSON khác tên biến Java
    private int id;

    @SerializedName("member_code")
    private String member_code;

    @SerializedName("username")
    private String username;

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("birthday")
    private String birthday; // Giữ String để đơn giản, API trả về Y-m-d

    @SerializedName("hometown")
    private String hometown;

    @SerializedName("residence")
    private String residence;

    @SerializedName("rating_single")
    private double rating_single;

    @SerializedName("rating_double")
    private double rating_double;

    @SerializedName("created_at")
    private String created_at; // Từ Laravel

    @SerializedName("updated_at")
    private String updated_at; // Từ Laravel


    // Constructor rỗng cần cho Gson
    public Player() {
    }

    // Constructor (tùy chọn, không bắt buộc cho Retrofit/Gson khi deserialize)
    public Player(String member_code, String username, String birthday, String hometown, String residence, double rating_single, double rating_double) {
        this.member_code = member_code;
        this.username = username;
        this.birthday = birthday;
        this.hometown = hometown;
        this.residence = residence;
        this.rating_single = rating_single;
        this.rating_double = rating_double;
    }


    // Getters và Setters (Cần thiết cho Gson và để truy cập từ code)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMember_code() { return member_code; }
    public void setMember_code(String member_code) { this.member_code = member_code; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public String getBirthday() { return birthday; }
    public void setBirthday(String birthday) { this.birthday = birthday; }

    public String getHometown() { return hometown; }
    public void setHometown(String hometown) { this.hometown = hometown; }

    public String getResidence() { return residence; }
    public void setResidence(String residence) { this.residence = residence; }

    public double getRating_single() { return rating_single; }
    public void setRating_single(double rating_single) { this.rating_single = rating_single; }

    public double getRating_double() { return rating_double; }
    public void setRating_double(double rating_double) { this.rating_double = rating_double; }

    public String getCreated_at() { return created_at; }
    public void setCreated_at(String created_at) { this.created_at = created_at; }

    public String getUpdated_at() { return updated_at; }
    public void setUpdated_at(String updated_at) { this.updated_at = updated_at; }
}