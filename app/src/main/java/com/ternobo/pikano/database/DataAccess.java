package com.ternobo.pikano.database;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.ternobo.pikano.RESTobjects.requests.Code;
import com.ternobo.pikano.RESTobjects.requests.Phone;
import com.ternobo.pikano.RESTobjects.requests.URLs;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.google.gson.Gson;
import com.ternobo.pikano.activities.BaseAcitivty;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class DataAccess {

    private final String token = "58cgi4zgwa5ofu1ie09dnynox1ydnk";
    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private Context context;

    public DataAccess(Context context) {
        this.context = context;
    }

    protected Request getRequest(String... params) {
        Request.Builder builder = new Request.Builder();
        builder.url(params[0]);
        builder.addHeader("token", this.token);
        RequestBody body = RequestBody.create(JSON, params[1]);
        builder.post(body);
        Request request = builder.build();
        return request;
    }


    public Request sendVerificationCode(String phone) {
        Gson gson = new Gson();
        return this.getRequest(URLs.sendVerification, gson.toJson(new Phone(phone)));
    }

    public Request verifyCode(int user_id,String code) {
        Gson gson = new Gson();
        return this.getRequest(URLs.verifyPhone, gson.toJson(new Code(user_id,code)));
    }

    private boolean isNetworkAvailable() {
        final ConnectivityManager cm = (ConnectivityManager)
                this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        return (networkInfo != null && networkInfo.isConnected());
    }

    public void callRequest(Request request, final ResponsRun runnable){
        if(isNetworkAvailable()) {
            final ProgressDialog progressDialog = ProgressDialog.show(context, "درحال برقراری ارتباط", "لطفا صبر کنید...");
            OkHttpClient client = new OkHttpClient();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    BaseAcitivty.current.runOnUiThread(() -> {
                        progressDialog.dismiss();
                        Toast.makeText(DataAccess.this.context, "ارتباط خود را با اینترنت بررسی کنید", Toast.LENGTH_SHORT).show();
                    });
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    runnable.run(response);
                }
            });
        }else{
            Toast.makeText(DataAccess.this.context, "ارتباط خود را با اینترنت بررسی کنید", Toast.LENGTH_SHORT).show();
        }
    }


}
