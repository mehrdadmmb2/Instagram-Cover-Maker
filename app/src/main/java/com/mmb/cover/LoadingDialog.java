package com.mmb.cover;

import android.app.ProgressDialog;
import android.content.Context;

public class LoadingDialog {

    public static ProgressDialog pd;

    public static void show(Context context) {
        pd = new ProgressDialog(context);
        pd.setMessage("لطفا کمی صبر کنید...");
        pd.show();
    }

    public static void dismis() {
        pd.dismiss();
    }

}
