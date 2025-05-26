package com.example.btth3.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    // URL gốc của API Laravel của bạn
    // QUAN TRỌNG: Điều chỉnh cho đúng với môi trường của bạn!
    // 1. Nếu chạy Android Emulator trên cùng máy với Laravel server (ví dụ: php artisan serve ở 127.0.0.1:8000):
    public static final String BASE_URL = "http://10.0.2.2:8000/api/";

    // 2. Nếu chạy trên thiết bị Android THẬT và thiết bị đó cùng mạng Wi-Fi với máy tính chạy Laravel server:
    //    Thay 10.0.2.2 bằng địa chỉ IP LAN của máy tính của bạn (ví dụ: 192.168.1.5)
    //    Ví dụ: public static final String BASE_URL = "http://192.168.1.5:8000/api/";

    private static Retrofit retrofit = null; // Biến static để giữ instance duy nhất

    /**
     * Phương thức này tạo và trả về một instance duy nhất của Retrofit.
     * Nếu instance đã tồn tại, nó sẽ trả về instance đó.
     * Nếu chưa, nó sẽ tạo mới.
     * @return Retrofit instance
     */
    public static Retrofit getClient() {
        if (retrofit == null) { // Chỉ khởi tạo nếu chưa có (Singleton pattern)
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) // Thiết lập URL cơ sở cho tất cả các request
                    .addConverterFactory(GsonConverterFactory.create()) // Sử dụng Gson để parse JSON
                    .build(); // Xây dựng đối tượng Retrofit
        }
        return retrofit;
    }

    /**
     * Phương thức tiện ích để lấy trực tiếp instance của ApiService.
     * @return ApiService instance
     */
    public static ApiService getApiService() {
        // Gọi getClient() để đảm bảo Retrofit đã được khởi tạo
        // Sau đó, dùng Retrofit instance để tạo ra triển khai của ApiService
        return getClient().create(ApiService.class);
    }
}
