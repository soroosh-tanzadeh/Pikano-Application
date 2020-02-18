package com.ternobo.pikano.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ternobo.pikano.R;
import com.ternobo.pikano.RESTobjects.User;
import com.ternobo.pikano.database.*;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.google.gson.Gson;
import com.ternobo.pikano.tools.Alert;

public class LoginAcitivity extends BaseAcitivty {

     private EditText phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataFileAccess dataFileAccess = new DataFileAccess(this);
        try {
            MainDB mainDB = dataFileAccess.readLocalDB();
            if(mainDB != null){
                User currentUser = mainDB.getCurrentUser();
                if(currentUser != null){
                    Intent i = new Intent(LoginAcitivity.this,HomeAcitivity.class);
                    startActivity(i);
                    finish();
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_login);
        phone = findViewById(R.id.phoneinput);
    }

    public void sendvcode(View v){
        String phonenum = phone.getText().toString();
        if(phonenum.matches("\\b\\d{4}[-.]?\\d{3}[-.]?\\d{4}\\b")) {
            DataAccess dataAccess = new DataAccess(this);
            Request request = dataAccess.sendVerificationCode(phone.getText().toString());
            OkHttpClient client = new OkHttpClient();
            final ProgressDialog dialog = ProgressDialog.show(LoginAcitivity.this, "درحال برقراری ارتباط", "لطفا صبر کنید");
            phone.setInputType(InputType.TYPE_NULL);
            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    LoginAcitivity.this.runOnUiThread(() -> {
                        dialog.dismiss();
                        Toast.makeText(LoginAcitivity.this, "ارتباط خود را با اینترنت بررسی کنید", Toast.LENGTH_SHORT).show();
                    });
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    try {
                        JSONObject result = new JSONObject(response.body().string());
                        if (result.getBoolean("result")) {
                            int user_id = result.getInt("user");
                            Intent i = new Intent(LoginAcitivity.this, VerifyPhone.class);
                            i.putExtra("user", user_id);
                            i.putExtra("phonenumber", phone.getText().toString());
                            startActivity(i);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }else{
            Alert.make("اطلاعات نامعتبر","شماره موبایل وارد شده نامعتبر است!",this).show();
        }
    }
}
