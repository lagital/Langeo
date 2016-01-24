package com.team.agita.langeo;

/**
 * Created by agita on 18.01.16.
 */
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivityAchievements extends AppCompatActivity implements ExpandableRecyclerAdapter.ExpandCollapseListener {

    protected static final String TAG = "location-updates-sample";
    private static final String ACHIEVEMENTS_START = "com.team.agita.langeo.RUN_ACHIEVEMENTS";
    private static final String CONTACTS_START = "com.team.agita.langeo.RUN_CONTACTS";
    private static final String PROFILE_START = "com.team.agita.langeo.RUN_PROFILE";
    private static final String SIGNIN_START = "com.team.agita.langeo.RUN_SIGNIN";

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private ExpandableAdapterAchievements mExpandableAdapter;
    private ImageButton mAddButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        Intent intent = getIntent();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        // Create a new adapter with 20 test data items
        mExpandableAdapter = new ExpandableAdapterAchievements(this,
                Achievement.fill(LocalUser.getInstance().achievementsReached));

        // Attach this activity to the Adapter as the ExpandCollapseListener
        mExpandableAdapter.setExpandCollapseListener(this);

        // Set the RecyclerView's adapter to the ExpandableAdapter we just created
        mRecyclerView.setAdapter(mExpandableAdapter);
        // Set the layout manager to a LinearLayout manager for vertical list
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

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
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mExpandableAdapter.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onListItemExpanded(int position) {
    }

    @Override
    public void onListItemCollapsed(int position) {
    }
}