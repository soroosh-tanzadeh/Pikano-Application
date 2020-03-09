package com.ternobo.pikano.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ternobo.pikano.R;
import com.ternobo.pikano.RESTobjects.User;
import com.ternobo.pikano.activities.AuthActivity;
import com.ternobo.pikano.database.DataAccess;
import com.ternobo.pikano.database.DataFileAccess;
import com.ternobo.pikano.database.MainDB;
import com.ternobo.pikano.database.ResponsRun;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

@SuppressLint("ValidFragment")
public class VerifyPhoneFragment extends Fragment {
    private int user_id;
    private EditText code;
    String phoneNumber;

    @SuppressLint("ValidFragment")
    public VerifyPhoneFragment(int user, String phonenumber) {
        super();
        this.user_id = user;
        this.phoneNumber = phonenumber;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_verify_phone, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button verfiyphone = getView().findViewById(R.id.btn_verifycode);
        verfiyphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyPhone();
            }
        });
        TextView phoneview = getView().findViewById(R.id.phonetextview);
        this.code = getView().findViewById(R.id.vcode);
        Log.d("user_id", this.user_id + "");
        phoneview.setText("کد به شماره " + phoneNumber + "ارسال شده.");
    }

    public void verifyPhone() {

        DataAccess dataAccess = new DataAccess(this.getContext());
        Request request = dataAccess.verifyCode(this.user_id,this.code.getText().toString());
        dataAccess.callRequest(request, new ResponsRun() {
            @Override
            public void run(Response response) {
                try {
                    JSONObject result = new JSONObject(response.body().string());
                    boolean ok = result.getBoolean("result");
                    Log.d("Response", result.toString());
                    if(ok){
                        MainDB db = new MainDB();
                        User u =  new User();
                        u.setId(user_id);
                        u.setApi_token(result.getString("token"));
                        db.setCurrentUser(u);
                        DataFileAccess dfa = new DataFileAccess(VerifyPhoneFragment.this.getContext());
                        dfa.writeLocalDB(db);
                        UserinfoFragment fragment = new UserinfoFragment();
                        ((AuthActivity) getActivity()).replaceFragments(fragment);
                    }else{
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(VerifyPhoneFragment.this.getContext(), "کد وارد شده اشتباه است!", Toast.LENGTH_SHORT).show();
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
