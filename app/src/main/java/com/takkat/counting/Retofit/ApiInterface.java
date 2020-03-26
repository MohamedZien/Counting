package com.takkat.counting.Retofit;


import com.takkat.counting.Login.SLoginModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public  interface ApiInterface {

    @POST("token")
    public Call<POST> login(@Body SLoginModel post);
}
