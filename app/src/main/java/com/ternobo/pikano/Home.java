package com.ternobo.pikano;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import com.ternobo.pikano.fragments.*;

public class Home extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BottomNavigationView bnav = findViewById(R.id.bottomNavigationView);
        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                HomeFragment homeFragment = new HomeFragment();
                BookmarkFragment bookmarkFragment = new BookmarkFragment();
                switch (item.getItemId()) {
                    case R.id.bnav_books:
                        openFragment(homeFragment);
                    case R.id.bnav_bookmarks:
                        openFragment(bookmarkFragment);
                }
                return true;
            }
        };
        bnav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bnav.setSelectedItemId(R.id.bnav_books);
    }
    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragcontent, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
