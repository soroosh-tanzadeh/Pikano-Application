package com.ternobo.pikano.database;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ternobo.pikano.activities.AuthActivity;
import com.ternobo.pikano.activities.BaseAcitivty;

import java.io.IOException;

import okhttp3.Response;

public abstract class ResponsRun {

    public abstract void run(Response response);

    public void onUnauthorized(Response response) {
        BaseAcitivty.current.runOnUiThread(() -> {
            Context context = BaseAcitivty.current;
            DataFileAccess dataFileAccess = new DataFileAccess(context);
            try {
                MainDB db = dataFileAccess.readLocalDB();
                Gson gson = new Gson();
                Log.d("Token", db.getCurrentUser().getApi_token());
                db.setCurrentUser(null);
                dataFileAccess.writeLocalDB(db);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            Intent i = new Intent(context, AuthActivity.class);
            context.startActivity(i);
            Toast.makeText(context, "توکن  ورود نامعتبر است!", Toast.LENGTH_LONG);
            BaseAcitivty.current.finishAll();
        });


    }
}
