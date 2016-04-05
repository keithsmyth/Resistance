package com.keithsmyth.resistance.admin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.keithsmyth.resistance.admin.gameslist.GamesListFragment;

public class MainActivity extends AppCompatActivity implements Nav {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            open(new GamesListFragment());
        }
    }

    @Override
    public void onBackPressed() {
        if (isGamesListFragmentOpen()) {
            super.onBackPressed();
        } else {
            open(new GamesListFragment());
        }
    }

    @Override
    public void open(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.fragment_container, fragment, fragment.getClass().getSimpleName())
            .commit();
    }

    private boolean isGamesListFragmentOpen() {
        return getSupportFragmentManager().findFragmentByTag(GamesListFragment.class.getSimpleName()) != null;
    }
}
