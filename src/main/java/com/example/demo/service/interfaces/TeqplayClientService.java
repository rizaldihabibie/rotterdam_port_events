package com.example.demo.service.interfaces;

import com.example.demo.model.teqplay.Ship;
import com.example.demo.model.teqplay.TeqplayToken;
import com.example.demo.model.teqplay.TeqplayTokenRequestBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface TeqplayClientService {

    @POST("/auth/login ")
    Call<TeqplayToken> getToken(@Body TeqplayTokenRequestBody tokenRequestBody);

    @GET("/ship")
    Call<List<Ship>> getShipsData(@Header("Authorization") String authToken);
}
