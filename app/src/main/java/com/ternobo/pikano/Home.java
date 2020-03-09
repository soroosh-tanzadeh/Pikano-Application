package com.ternobo.pikano;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ternobo.pikano.fragments.BookmarkFragment;
import com.ternobo.pikano.fragments.HomeFragment;

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
