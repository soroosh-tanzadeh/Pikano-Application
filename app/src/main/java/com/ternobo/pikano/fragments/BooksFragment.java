package com.ternobo.pikano.fragments;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.ternobo.pikano.R;
import com.ternobo.pikano.RESTobjects.Book;
import com.ternobo.pikano.database.DataAccess;
import com.ternobo.pikano.database.ResponsRun;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

public class BooksFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_books, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GridLayout bookslist = getView().findViewById(R.id.bookslist);
        DataAccess dataAccess = new DataAccess(getContext());
        Request books = dataAccess.getBooks();
        dataAccess.callRequest(books, new ResponsRun() {
            @Override
            public void run(Response response) {
                try {
                    String responseJson = response.body().string();

                    Gson gson = new Gson();

                    JSONObject result = new JSONObject(responseJson);
                    if (result.getBoolean("result")) {
                        Book[] books = gson.fromJson(result.getJSONArray("data").toString(), Book[].class);
                        for (Book book : books) {
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

                            getActivity().runOnUiThread(() -> {
                                bookslist.addView(cardView);
                            });

                        }
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
