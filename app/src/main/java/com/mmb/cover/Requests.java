package com.mmb.cover;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Requests {

    @POST("loginpage2.php")
    @FormUrlEncoded
    Call<Object> login(
            @Field("username") String username
            , @Field("password") String password);

    @POST("loginpage2.php")
    @FormUrlEncoded
    Call<Object> login2(
            @Field("username") String username
            , @Field("password") String password);


}
