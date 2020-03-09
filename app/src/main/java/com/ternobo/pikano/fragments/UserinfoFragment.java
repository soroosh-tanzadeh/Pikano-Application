package com.ternobo.pikano.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.ternobo.pikano.R;
import com.ternobo.pikano.RESTobjects.Grade;
import com.ternobo.pikano.RESTobjects.User;
import com.ternobo.pikano.activities.BaseAcitivty;
import com.ternobo.pikano.activities.HomeAcitivity;
import com.ternobo.pikano.database.DataAccess;
import com.ternobo.pikano.database.DataFileAccess;
import com.ternobo.pikano.database.MainDB;
import com.ternobo.pikano.database.ResponsRun;
import com.ternobo.pikano.tools.Alert;
import com.ternobo.pikano.tools.SpinAdapter;
import com.yalantis.ucrop.UCrop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.pedrohc.profileimageview.ProfileImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Request;
import okhttp3.Response;

public class UserinfoFragment extends Fragment {

    private ProfileImageView profileImageView;
    private Bitmap profileimage;
    private EditText name;
    private Spinner grades;

    public UserinfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_userinfo, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        grades = getView().findViewById(R.id.gradespinner);
        name = getView().findViewById(R.id.nameinput);

        DataAccess dataAccess = new DataAccess(getContext());

        Request requestcurrentUser = dataAccess.getCurrentUser();
        dataAccess.callRequest(requestcurrentUser, new ResponsRun() {
            @Override
            public void run(Response response) {
                DataFileAccess dataFileAccess = new DataFileAccess(getContext());
                try {
                    MainDB db = dataFileAccess.readLocalDB();
                    Gson gson = new Gson();
                    String s = response.body().string();
                    Log.d("response", String.valueOf(response.body()));
                    JSONObject result = new JSONObject(s);
                    db.setCurrentUser(gson.fromJson(result.getJSONObject("data").toString(), User.class));
                    dataFileAccess.writeLocalDB(db);
                    if (db.getCurrentUser().getName().isEmpty()) {
                        userProfileSetting();
                    } else {
                        db.setFullSignup(true);
                        dataFileAccess.writeLocalDB(db);
                        Intent i = new Intent(getContext(), HomeAcitivity.class);
                        startActivity(i);
                        ((BaseAcitivty) getActivity()).finishAll();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }


    private void userProfileSetting() {
        DataAccess dataAccess = new DataAccess(getContext());
        DataFileAccess dataFileAccess = new DataFileAccess(getContext());
        MainDB db = null;
        try {
            db = dataFileAccess.readLocalDB();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        User currentUser = db.getCurrentUser();
        Request request = dataAccess.getGrades(currentUser);
        dataAccess.callRequest(request, new ResponsRun() {
            @Override
            public void run(Response response) {
                try {
                    Gson gson = new Gson();
                    JSONObject result = new JSONObject(response.body().string());
                    JSONArray data = result.getJSONArray("data");
                    ArrayList<Grade> gradeslist = new ArrayList<>();
                    gradeslist.add(new Grade(-1, "یک پایه را انتخاب کنید", -1, "", ""));
                    for (int i = 0; i < data.length(); i++) {
                        Grade grade = gson.fromJson(data.get(i).toString(), Grade.class);
                        gradeslist.add(grade);
                    }

                    getActivity().runOnUiThread(() -> {
                        SpinAdapter spinAdapter = new SpinAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, gradeslist);
                        grades.setAdapter(spinAdapter); // Set the custom adapter to the spinner
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Button save = getView().findViewById(R.id.saveprofile_btn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupProfile();
            }
        });

        profileImageView = getView().findViewById(R.id.profileImageView);
        profileImageView.setOnClickListener(v -> {
            int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE);

            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                startGallery();
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getActivity().requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            2000);
                } else {
                    Toast.makeText(getContext(), "دسترسی به گالری محدود شده!", Toast.LENGTH_LONG);
                }
            }
        });
    }


    private void setupProfile() {
        Bitmap profileimage = this.profileimage;
        if (this.profileimage == null) {
            profileimage = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.person_gray);
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        profileimage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String base64Image = "data:image/png;base64," + Base64.encodeToString(byteArray, Base64.DEFAULT);
        String name = this.name.getText().toString();
        int grade = ((Grade) this.grades.getSelectedItem()).getId();
        boolean ok = true;
        if (grade == -1) {
            ok = false;
            Alert.make("وجود مشکل", "لطفا پایه تحصیلی خود را انتخاب کنید", getContext()).show();
        }
        if (name.equals("")) {
            ok = false;
            Alert.make("وجود مشکل", "لطفا نام و نام خانوادگی خود را وارد کنید", getContext()).show();
        }

        if (ok) {
            DataAccess dataAccess = new DataAccess(getContext());
            String api_token = DataFileAccess.getCurrentUser(getContext()).getApi_token();
            Request request = dataAccess.setProfile(api_token, name, grade, base64Image);
            dataAccess.callRequest(request, new ResponsRun() {
                @Override
                public void run(Response response) {
                    Intent i = new Intent(getContext(), HomeAcitivity.class);
                    DataFileAccess dataFileAccess = new DataFileAccess(getContext());
                    try {
                        MainDB mainDB = dataFileAccess.readLocalDB();
                        mainDB.setFullSignup(true);
                        dataFileAccess.writeLocalDB(mainDB);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    startActivity(i);
                    ((BaseAcitivty) getActivity()).finishAll();
                }
            });
        }

    }

    private void startGallery() {
        //Create an Intent with action as ACTION_PICK
        Intent intent = new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        // Launching the Intent
        startActivityForResult(intent, 1000);
    }


    @Override
    public void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1000) {
                Uri returnUri = data.getData();
                try {
                    File outputFile = File.createTempFile("images", "extension", getContext().getCacheDir());
                    UCrop.of(returnUri, Uri.fromFile(outputFile))
                            .withAspectRatio(1, 1)
                            .start(getContext(), this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == UCrop.REQUEST_CROP) {
                final Uri resultUri = UCrop.getOutput(data);
                try {
                    Bitmap bitmapImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), resultUri);
                    profileImageView.setImage(bitmapImage);
                    this.profileimage = bitmapImage;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
