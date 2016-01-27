package com.team.agita.langeo.map;

/**
 * Created by agita on 27.01.16.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import java.util.Locale;

import com.team.agita.langeo.ActivityProfile;
import com.team.agita.langeo.ActivitySignin;
import com.team.agita.langeo.R;
import com.team.agita.langeo.achievements.ActivityAchievements;
import com.team.agita.langeo.contacts.ActivityContacts;


public class ActivityMap extends AppCompatActivity implements ActionBar.TabListener{

    private static final String ACHIEVEMENTS_START = "com.team.agita.langeo.RUN_ACHIEVEMENTS";
    private static final String CONTACTS_START = "com.team.agita.langeo.RUN_CONTACTS";
    private static final String PROFILE_START = "com.team.agita.langeo.RUN_PROFILE";
    private static final String SIGNIN_START = "com.team.agita.langeo.RUN_SIGNIN";

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    static final String LOG_TAG = "SlidingTabsBasicFragment";
    private SlidingTabLayout mSlidingTabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Toolbar topToolBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(topToolBar);
        topToolBar.setLogo(R.drawable.achievements_toolbar);
        topToolBar.setLogoDescription(getResources().getString(R.string.app_name));
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_achievements:
                // Go to achievements.
                Intent aintent = new Intent(this, ActivityAchievements.class);
                aintent.putExtra(ACHIEVEMENTS_START, true);
                startActivity(aintent);
                return true;
            case R.id.action_contacts:
                // Go to contacts.
                Intent cintent = new Intent(this, ActivityContacts.class);
                cintent.putExtra(CONTACTS_START, true);
                startActivity(cintent);
                return true;
            case R.id.action_profile:
                // Go to profile.
                Intent pintent = new Intent(this, ActivityProfile.class);
                pintent.putExtra(PROFILE_START, true);
                startActivity(pintent);
                return true;
            case R.id.action_signout:
                // Go to signin.
                Intent sintent = new Intent(this, ActivitySignin.class);
                sintent.putExtra(SIGNIN_START, true);
                startActivity(sintent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());
    }
    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }
    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position){
                case 0:
                    fragment = new FragmentMap();
                    break;
                case 1:
                    fragment = new Fragment();
                    break;
            }
            return fragment;
        }
        @Override
        public int getCount() {
            return 2;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "General Properties".toUpperCase(l);
                case 1:
                    return "Physical Properties".toUpperCase(l);
            }
            return null;
        }
    }
}