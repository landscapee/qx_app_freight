package com.qxkj.positionapp.http;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface HttpService {
    @GET("cell/?")
    Call<CellLocationResponse> getLocationInfo(@Query("mcc") String mcc, @Query("mnc") String mnc, @Query("lac") String lac, @Query("ci") String ci);
}
