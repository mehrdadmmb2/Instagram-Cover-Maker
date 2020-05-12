package com.mmb.cover;

import com.google.gson.Gson;

public class GsonHandler {

    public static Gson gson = new Gson();

    public static LoginModel login(String body, String tag) {
        LoginModel model;

        switch (tag) {
            case "login":
                return model = gson.fromJson(body, LoginModel.class);
            case "login2":
                return model = gson.fromJson(body, LoginModel.class);
            default:
                return model = new LoginModel();
        }
    }


}
