package com.ternobo.pikano.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.ternobo.pikano.R;
import com.ternobo.pikano.RESTobjects.User;
import com.ternobo.pikano.database.DataAccess;
import com.ternobo.pikano.database.ResponsRun;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Request;
import okhttp3.Response;
import com.ternobo.pikano.database.DataFileAccess;
import com.ternobo.pikano.database.MainDB;

public class VerifyPhone extends BaseAcitivty {
    private int user_id;
    private EditText code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);
        this.user_id = getIntent().getIntExtra("user",-1);
        this.code = findViewById(R.id.vcode);
        String phoneNumber = getIntent().getStringExtra("phonenumber");
        TextView phoneview = findViewById(R.id.phonetextview);
        if(phoneNumber != null){
            phoneview.setText(phoneNumber);
        }
    }

    public void verifyPhone(View v){
        DataAccess dataAccess = new DataAccess(this);
        Request request = dataAccess.verifyCode(this.user_id,this.code.getText().toString());
        dataAccess.callRequest(request, new ResponsRun() {
            @Override
            public void run(Response response) {
                try {
                    JSONObject result = new JSONObject(response.body().string());
                    boolean ok = result.getBoolean("result");
                    if(ok){
                        MainDB db = new MainDB();
                        User u =  new User();
                        u.setId(user_id);
                        db.setCurrentUser(u);
                        DataFileAccess dfa = new DataFileAccess(VerifyPhone.this);
                        dfa.writeLocalDB(db);
                        Intent i = new Intent(VerifyPhone.this,HomeAcitivity.class);
                        startActivity(i);
                        finish();
                    }else{
                        Toast.makeText(VerifyPhone.this, "کد وارد شده اشتباه است!", Toast.LENGTH_SHORT).show();
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
