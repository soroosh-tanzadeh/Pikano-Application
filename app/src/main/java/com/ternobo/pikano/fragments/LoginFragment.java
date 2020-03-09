package com.ternobo.pikano.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ternobo.pikano.R;
import com.ternobo.pikano.RESTobjects.User;
import com.ternobo.pikano.activities.AuthActivity;
import com.ternobo.pikano.activities.HomeAcitivity;
import com.ternobo.pikano.database.DataAccess;
import com.ternobo.pikano.database.DataFileAccess;
import com.ternobo.pikano.database.MainDB;
import com.ternobo.pikano.tools.Alert;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginFragment extends Fragment {

     private EditText phone;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataFileAccess dataFileAccess = new DataFileAccess(this.getContext());
        try {
            MainDB mainDB = dataFileAccess.readLocalDB();
            if(mainDB != null){
                User currentUser = mainDB.getCurrentUser();
                if (currentUser != null) {
                    if (mainDB.isFullSignup()) {
                        Intent i = new Intent(LoginFragment.this.getContext(), HomeAcitivity.class);
                        startActivity(i);
                        getActivity().finish();
                        return;
                    } else {
                        ((AuthActivity) getActivity()).replaceFragments(new UserinfoFragment());
                        return;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button sendvcode = getView().findViewById(R.id.sendvcode);
        sendvcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendvcode();
            }
        });
    }

    public void sendvcode() {
        phone = getView().findViewById(R.id.phoneinput);
        String phonenum = phone.getText().toString();
        if(phonenum.matches("\\b\\d{4}[-.]?\\d{3}[-.]?\\d{4}\\b")) {
            DataAccess dataAccess = new DataAccess(this.getContext());
            Request request = dataAccess.sendVerificationCode(phone.getText().toString());
            OkHttpClient client = new OkHttpClient();
            final ProgressDialog dialog = ProgressDialog.show(LoginFragment.this.getContext(), "درحال برقراری ارتباط", "لطفا صبر کنید");
            phone.setInputType(InputType.TYPE_NULL);
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    LoginFragment.this.getActivity().runOnUiThread(() -> {
                        dialog.dismiss();
                        Toast.makeText(LoginFragment.this.getContext(), "ارتباط خود را با اینترنت بررسی کنید", Toast.LENGTH_SHORT).show();
                    });
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    try {
                        JSONObject result = new JSONObject(response.body().string());
                        if (result.getBoolean("result")) {
                            int user_id = result.getInt("user");
                            VerifyPhoneFragment phoneFragment = new VerifyPhoneFragment(user_id, phonenum);
                            dialog.dismiss();
                            ((AuthActivity) getActivity()).replaceFragments(phoneFragment);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }else{
            Alert.make("اطلاعات نامعتبر", "شماره موبایل وارد شده نامعتبر است!", this.getContext()).show();
        }
    }
}
