package com.example.btth3.api;

import com.example.btth3.Player;
import com.example.btth3.PlayerDeleteResponse;
import com.example.btth3.PlayerListResponse;
import com.example.btth3.PlayerResponse; // Cho GET chi tiết
import com.example.btth3.PlayerStoreResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    // GET danh sách players
    @GET("players")
    Call<PlayerListResponse> getPlayers();

    // POST tạo player mới
    // Gửi object Player, nhận về PlayerStoreResponse
    @POST("players")
    Call<PlayerStoreResponse> createPlayer(@Body Player player);

    // GET thông tin một player theo ID
    @GET("players/{id}")
    Call<PlayerResponse> getPlayer(@Path("id") int playerId); // Sửa ở đây, nhận về PlayerResponse chứa 1 player

    // PUT cập nhật player
    // Gửi object Player, nhận về PlayerStoreResponse
    @PUT("players/{id}")
    Call<PlayerStoreResponse> updatePlayer(@Path("id") int playerId, @Body Player player);

    // DELETE player
    @DELETE("players/{id}")
    Call<PlayerDeleteResponse> deletePlayer(@Path("id") int playerId);
}
