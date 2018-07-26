package com.example.user.symptomtracker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.user.symptomtracker.R;
import com.example.user.symptomtracker.utils.JobServiceUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Set up and manage navigation between destinations by Fragment transactions through
 * bottom navigation
 */
public class MainActivity extends AppCompatActivity {

    public static final String DUMMY_AD_ID = "ca-app-pub-3940256099942544~3347511713";

    @BindView(R.id.navigation)
    BottomNavigationView navigationView;
    @BindView(R.id.dest_fragment_container)
    FrameLayout fragmentContainer;
    @BindView(R.id.adView)
    AdView bannerAd;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_today:
                    fragmentReplaceWithToday();
                    return true;
                case R.id.navigation_overview:
                    fragmentReplaceWithOverview();
                    return true;
                case R.id.navigation_resolved:
                    fragmentReplaceWithResolved();
                    return true;
            }

            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (savedInstanceState == null){
            addDefaultFragmentToContainer();
            navigationView.setSelectedItemId(R.id.navigation_overview);
        }
        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // add Ads
        MobileAds.initialize(this, DUMMY_AD_ID);
        AdRequest adRequest = new AdRequest.Builder().build();
        bannerAd.loadAd(adRequest);

        // add Firebase Analytics
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        firebaseAnalytics.setAnalyticsCollectionEnabled(true);

        JobServiceUtils.scheduleCheckUnresolvedSymptoms(this);
    }

    @OnClick(R.id.fab_add_new)
    public void addNewSymptom(){
        Intent intent = new Intent(MainActivity.this, AddSymptomActivity.class);
        startActivity(intent);
    }

    /**
     * Add the OverviewFragment as the default to container as the views are first initialized
     */
    private void addDefaultFragmentToContainer() {
        OverviewFragment overviewFragment = new OverviewFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.dest_fragment_container, overviewFragment)
                .commit();
    }

    /**
     * Replace currently displayed fragment with OverviewFragment
     */
    private void fragmentReplaceWithOverview() {
        OverviewFragment overviewFragment = new OverviewFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.dest_fragment_container, overviewFragment)
                .commit();
    }

    /**
     * Replace currently displayed fragment with TodayFragment
     */
    private void fragmentReplaceWithToday() {
        TodayFragment todayFragment = new TodayFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.dest_fragment_container, todayFragment)
                .commit();
    }

    /**
     * Replace currently displayed fragment with ResolvedFragment
     */
    private void fragmentReplaceWithResolved() {
        ResolvedFragment resolvedFragment = new ResolvedFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.dest_fragment_container, resolvedFragment)
                .commit();
    }

}
