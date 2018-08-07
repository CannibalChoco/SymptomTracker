package com.example.user.symptomtracker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
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

import static com.example.user.symptomtracker.receiver.SymptomWidgetProvider.WIDGET_KEY_FRAGMENT_ID;
import static com.example.user.symptomtracker.receiver.SymptomWidgetProvider.WIDGET_TARGET_FRAGMENT_OVERVIEW;

/**
 * Set up and manage navigation between destinations by Fragment transactions through
 * bottom navigation
 */
public class MainActivity extends AppCompatActivity {

    public static final String DUMMY_AD_ID = "ca-app-pub-3940256099942544~3347511713";
    private static final int DEFAULT_FRAGMENT_ID = WIDGET_TARGET_FRAGMENT_OVERVIEW;
    private static final int FRAGMENT_ADD = 99;
    private static final int FRAGMENT_REPLACE = 88;

    @BindView(R.id.navigation)
    BottomNavigationView navigationView;
    @BindView(R.id.dest_fragment_container)
    FrameLayout fragmentContainer;
    @BindView(R.id.adView)
    AdView bannerAd;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_today:
                setTodayFragment(FRAGMENT_REPLACE);
                return true;
            case R.id.navigation_overview:
                setOverviewFragment(FRAGMENT_REPLACE);
                return true;
            case R.id.navigation_resolved:
                fragmentReplaceWithResolved();
                return true;
        }

        return false;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            setFragmentOnLaunch();
        }

        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // add Ads
        setupAds();
        // add Firebase Analytics
        setupFirebaseAnalytics();

        JobServiceUtils.scheduleCheckUnresolvedSymptoms(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setFragmentOnNewIntent(intent);
    }

    /**
     * Checks if there is an intent that specifies what fragment to display. Sets fragment
     */
    private void setFragmentOnLaunch() {
        int action;
        Intent intent = getIntent();
        action = FRAGMENT_ADD;
        // get id for which fragment to display
        if (intent != null && intent.hasExtra(WIDGET_KEY_FRAGMENT_ID)) {
            int fragmentId = intent.getIntExtra(WIDGET_KEY_FRAGMENT_ID, DEFAULT_FRAGMENT_ID);

            if (fragmentId == 0) {
                navigationView.setSelectedItemId(R.id.navigation_today);
                setTodayFragment(action);
            } else {
                navigationView.setSelectedItemId(R.id.navigation_overview);
                setOverviewFragment(action);
            }
        } else {
            setOverviewFragment(action);
            navigationView.setSelectedItemId(R.id.navigation_overview);
        }
    }

    /**
     * Checks intent extra and performs action based on extra
     *
     * @param intent
     */
    private void setFragmentOnNewIntent(Intent intent) {
        if (intent.hasExtra(WIDGET_KEY_FRAGMENT_ID)) {
            int fragmentId = intent.getIntExtra(WIDGET_KEY_FRAGMENT_ID, DEFAULT_FRAGMENT_ID);

            if (fragmentId == 0) {
                navigationView.setSelectedItemId(R.id.navigation_today);
                setTodayFragment(FRAGMENT_REPLACE);
            } else {
                navigationView.setSelectedItemId(R.id.navigation_overview);
                setOverviewFragment(FRAGMENT_REPLACE);
            }
        }
    }

    private void setupFirebaseAnalytics() {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        firebaseAnalytics.setAnalyticsCollectionEnabled(true);
    }

    private void setupAds() {
        MobileAds.initialize(this, DUMMY_AD_ID);
        AdRequest adRequest = new AdRequest.Builder().build();
        bannerAd.loadAd(adRequest);
    }

    @OnClick(R.id.fab_add_new)
    public void addNewSymptom() {
        Intent intent = new Intent(MainActivity.this, AddSymptomActivity.class);
        startActivity(intent);
    }

    /**
     * Add fragment or replace currently displayed fragment with OverviewFragment
     */
    private void setOverviewFragment(int action) {
        OverviewFragment overviewFragment = new OverviewFragment();
        if (action == FRAGMENT_ADD) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.dest_fragment_container, overviewFragment)
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.dest_fragment_container, overviewFragment)
                    .commit();
        }
    }

    /**
     * Add fragment or replace currently displayed fragment with TodayFragment
     */
    private void setTodayFragment(int action) {
        TodayFragment todayFragment = new TodayFragment();
        if (action == FRAGMENT_ADD) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.dest_fragment_container, todayFragment)
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.dest_fragment_container, todayFragment)
                    .commit();
        }
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
