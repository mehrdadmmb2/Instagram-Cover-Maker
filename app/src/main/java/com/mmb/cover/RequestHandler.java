package com.mmb.cover;

import retrofit2.Call;
import retrofit2.Response;

public interface RequestHandler {
    void onResponse(Call<Object> cal, Response<Object> response);

    void onFail(Call<Object> cal, Throwable t);
}
