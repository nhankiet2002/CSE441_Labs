package com.example.btth1;


public class Player {
    public String member_code;
    public String username;
    public String avatar;
    public String birthday;
    public String hometown;
    public String residence;
    public double rating_single;
    public double rating_double;

    // !!! Firebase yêu cầu một constructor rỗng (không tham số) để deserialize !!!
    public Player() {
    }

    public Player(String member_code, String username, String avatar, String birthday,
                  String hometown, String residence, double rating_single, double rating_double) {
        this.member_code = member_code;
        this.username = username;
        this.avatar = avatar;
        this.birthday = birthday;
        this.hometown = hometown;
        this.residence = residence;
        this.rating_single = rating_single;
        this.rating_double = rating_double;
    }

    // (Tùy chọn) Bạn có thể thêm Getters và Setters nếu cần
    // Tuy nhiên, Firebase có thể truy cập trực tiếp các public fields.

    public String getMember_code() {
        return member_code;
    }

    public void setMember_code(String member_code) {
        this.member_code = member_code;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    // ... thêm getters/setters cho các trường khác nếu muốn
}


