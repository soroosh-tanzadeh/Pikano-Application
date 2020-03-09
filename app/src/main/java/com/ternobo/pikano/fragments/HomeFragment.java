package com.ternobo.pikano.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.ternobo.pikano.R;
import com.ternobo.pikano.RESTobjects.File;
import com.ternobo.pikano.RESTobjects.User;
import com.ternobo.pikano.activities.PDFViewer;
import com.ternobo.pikano.database.DataAccess;
import com.ternobo.pikano.database.DataFileAccess;
import com.ternobo.pikano.database.MainDB;
import com.ternobo.pikano.database.ResponsRun;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;


public class HomeFragment extends Fragment {

    private LinearLayout mostratedQ;
    private LinearLayout mostratedP;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_home, container, false);
        return inflate;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mostratedQ = getView().findViewById(R.id.most_rated_list);
        this.mostratedQ.setPadding(20, 20, 20, 20);

        this.mostratedP = getView().findViewById(R.id.most_rated_practices);
        this.mostratedP.setPadding(20, 20, 20, 20);
        DataAccess dataAccess = new DataAccess(getContext());
        if (dataAccess.isNetworkAvailable()) {
            Request currentUser = dataAccess.getCurrentUser();
            dataAccess.callRequest(currentUser, new ResponsRun() {
                @Override
                public void run(Response response) {
                    DataFileAccess dataFileAccess = new DataFileAccess(getContext());
                    try {
                        MainDB db = dataFileAccess.readLocalDB();
                        Gson gson = new Gson();
                        String s = response.body().string();
                        JSONObject result = new JSONObject(s);
                        db.setCurrentUser(gson.fromJson(result.getJSONObject("data").toString(), User.class));
                        dataFileAccess.writeLocalDB(db);
                        getActivity().runOnUiThread(() -> {
                            setupBooks();
                        });
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

    }

    private void setupBooks() {
        DataAccess dataAccess = new DataAccess(getContext());
        Request mostRatedFiles = dataAccess.getMostRatedFiles();

        dataAccess.callRequest(mostRatedFiles, new ResponsRun() {
            @Override
            public void run(Response response) {
                Gson gson = new Gson();
                try {
                    JSONObject object = new JSONObject(response.body().string());
                    File[] books = gson.fromJson(object.getJSONArray("data").toString(), File[].class);
                    for (File book : books) {
                        CardView cardView = new CardView(getContext());

                        cardView.setRadius(getPixelsFromDPs(10));
                        cardView.setPadding(25, 25, 25, 25);
                        cardView.setCardElevation(getPixelsFromDPs(6));
                        cardView.setCardBackgroundColor(Color.WHITE);

                        LayoutInflater factory = LayoutInflater.from(getContext());
                        View book_card = factory.inflate(R.layout.book_card, null);
                        LinearLayout cover = book_card.findViewById(R.id.book_cover);
                        TextView bookName = book_card.findViewById(R.id.book_name);
                        bookName.setMaxLines(1);
                        bookName.setText(book.getName());
                        Bitmap cover_image = DataAccess.getBitmapFromURL(book.getCover());
                        cover.setBackground(new BitmapDrawable(cover_image));
                        cardView.addView(book_card);
                        ViewGroup.MarginLayoutParams layoutparams = new ViewGroup.MarginLayoutParams(
                                getPixelsFromDPs(130),
                                getPixelsFromDPs(200)
                        );
                        layoutparams.setMargins(10, 10, 10, 10);
                        cardView.setUseCompatPadding(true);
                        cardView.setLayoutParams(layoutparams);
                        cardView.setEnabled(true);
                        cardView.setClickable(true);

                        cardView.setOnClickListener(v -> {
                            getActivity().runOnUiThread(() -> {
                                Intent i = new Intent(getContext(), PDFViewer.class);
                                i.putExtra("filename", book.getFile());
                                startActivity(i);
                            });
                        });

                        getActivity().runOnUiThread(() -> {
                            if (book.getCategory().equals("1")) {
                                mostratedQ.addView(cardView);
                            } else if (book.getCategory().equals("2")) {
                                mostratedP.addView(cardView);
                            }
                        });
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    protected int getPixelsFromDPs(int dps) {
        Resources r = getContext().getResources();
        int px = (int) (TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dps, r.getDisplayMetrics()));
        return px;
    }

}
