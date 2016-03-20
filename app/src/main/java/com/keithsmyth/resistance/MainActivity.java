package com.keithsmyth.resistance;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.keithsmyth.resistance.feature.game.GameFragment;
import com.keithsmyth.resistance.feature.lobby.presentation.LobbyFragment;
import com.keithsmyth.resistance.navigation.DisplayThrowable;
import com.keithsmyth.resistance.navigation.ErrorView;
import com.keithsmyth.resistance.navigation.Navigation;
import com.keithsmyth.resistance.navigation.Navigator;
import com.keithsmyth.resistance.feature.welcome.presentation.WelcomeFragment;

public class MainActivity extends AppCompatActivity implements Navigator {

    private Navigation navigation;
    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = Injector.navigation();

        rootView = findViewById(R.id.root_view);

        if (savedInstanceState == null) {
            openFragment(WelcomeFragment.create());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        navigation.setNavigator(this);
    }

    @Override
    protected void onStop() {
        navigation.setNavigator(null);
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentByTag(WelcomeFragment.class.getSimpleName()) != null) {
            super.onBackPressed();
        } else {
            // TODO: are you sure?
            openFragment(WelcomeFragment.create());
        }
    }

    @Override
    public void showError(DisplayThrowable displayThrowable) {
        // notify user
        Snackbar.make(rootView, displayThrowable.getDisplayMessage(this), Snackbar.LENGTH_LONG).show();

        // notify fragment
        final Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment instanceof ErrorView) {
            ((ErrorView) fragment).onErrorShown();
        }
    }

    @Override
    public void openLobby() {
        openFragment(LobbyFragment.create());
    }

    @Override
    public void openGame() {
        openFragment(GameFragment.create());
    }

    @Override
    public void openEnd() {
        throw new UnsupportedOperationException();
    }

    private void openFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.fragment_container, fragment, fragment.getClass().getSimpleName())
            .commit();
    }
}
