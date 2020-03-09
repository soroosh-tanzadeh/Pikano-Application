package com.ternobo.pikano.activities;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.ternobo.pikano.R;
import com.ternobo.pikano.RESTobjects.User;
import com.ternobo.pikano.database.DataAccess;
import com.ternobo.pikano.database.DataFileAccess;
import com.ternobo.pikano.fragments.BookmarkFragment;
import com.ternobo.pikano.fragments.BooksFragment;
import com.ternobo.pikano.fragments.HomeFragment;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeAcitivity extends BaseAcitivty
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String FRAGMENT_HOME = "HOME";
    private static final String FRAGMENT_OTHER = "OTHER";

    private BottomNavigationView bnav;
    private Fragment current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        forceRTLIfSupported();
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(0f);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            toggle.getDrawerArrowDrawable().setColor(getColor(R.color.colorMute));
        } else {
            toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorMute));
        }
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        bnav = findViewById(R.id.bottomNavigationView);
        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                HomeFragment homeFragment = new HomeFragment();
                BookmarkFragment bookmarkFragment = new BookmarkFragment();
                BooksFragment booksFragment = new BooksFragment();
                switch (item.getItemId()) {
                    case R.id.bnav_home:
                        openFragment(homeFragment, FRAGMENT_HOME);
                        break;
                    case R.id.bnav_books:
                        openFragment(booksFragment, FRAGMENT_OTHER);
                        break;
                    case R.id.bnav_bookmarks:
                        openFragment(bookmarkFragment, FRAGMENT_OTHER);
                        break;
                }
                return true;
            }
        };
        bnav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bnav.setSelectedItemId(R.id.bnav_home);
        CircleImageView profile = navigationView.getHeaderView(0).findViewById(R.id.profileimage);

        Thread thread = new Thread(() -> {
            User currentUser = DataFileAccess.getCurrentUser(HomeAcitivity.this);
            final Bitmap bitmapFromURL = DataAccess.getBitmapFromURL(currentUser.getProfile());
            HomeAcitivity.this.runOnUiThread(() -> {
                profile.setImageBitmap(bitmapFromURL);
            });
        });
        thread.start();
    }


    private void openFragment(Fragment fragment, String name) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragcontent, fragment);
        // 1. Know how many fragments there are in the stack
        final int count = fragmentManager.getBackStackEntryCount();
        // 2. If the fragment is **not** "home type", save it to the stack
        if (name.equals(FRAGMENT_OTHER)) {
            fragmentTransaction.addToBackStack(name);
        }
        // Commit !
        fragmentTransaction.commit();
        // 3. After the commit, if the fragment is not an "home type" the back stack is changed, triggering the
        // OnBackStackChanged callback
        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                // If the stack decreases it means I clicked the back button
                if (fragmentManager.getBackStackEntryCount() <= count) {
                    // pop all the fragment and remove the listener
                    fragmentManager.popBackStack(FRAGMENT_OTHER, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    fragmentManager.removeOnBackStackChangedListener(this);
                    // set the home button selected
                    bnav.getMenu().getItem(0).setChecked(true);
                }
            }
        });
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
