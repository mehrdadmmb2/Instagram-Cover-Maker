package com.mmb.cover;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mmb.cover.RetrofitClient.getClient;

public class RetrofitRequest {
    public static Requests client = getClient().create(Requests.class);
    public static Call<Object> call;

    public static void login(RequestHandler handler) {
        call = client.login("mehrdad", "123456");
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                handler.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                handler.onFail(call, t);
            }
        });
    }

    public static void login2(RequestHandler handler) {
        call = client.login("mehrdad", "123456");
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                handler.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                handler.onFail(call, t);
            }
        });
    }

    public static void cancelRequest() {
        call.cancel();
    }

}
