package com.example.btth2;


// Firestore không yêu cầu chú thích @PropertyName nếu tên biến Java khớp với tên field trong Firestore.
// Tuy nhiên, nếu khác, bạn có thể dùng @PropertyName("ten_field_trong_firestore")
// import com.google.firebase.firestore.PropertyName;

public class Player {
    public String member_code;
    public String username;
    public String avatar;
    public String birthday;
    public String hometown;
    public String residence;
    public double rating_single;
    public double rating_double;

    // Constructor rỗng là BẮT BUỘC cho Firestore để deserialize object
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

    // Getters và Setters (Tùy chọn nhưng hữu ích, Firestore có thể dùng reflection cho public fields)
    public String getMember_code() { return member_code; }
    public void setMember_code(String member_code) { this.member_code = member_code; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    // ... thêm getters/setters cho các trường khác nếu bạn muốn ...
}