package com.ternobo.pikano.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.widget.Toolbar;

import com.ternobo.pikano.R;
import com.ternobo.pikano.fragments.BookmarkFragment;
import com.ternobo.pikano.fragments.HomeFragment;

public class HomeAcitivity extends BaseAcitivty
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        forceRTLIfSupported();
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

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
                        break;
                    case R.id.bnav_bookmarks:
                        openFragment(bookmarkFragment);
                        break;
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_user_profile) {
            // Handle the camera action
        } else if (id == R.id.nav_faq) {

        } else if (id == R.id.nav_laws) {

        } else if (id == R.id.nav_about_dev) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
