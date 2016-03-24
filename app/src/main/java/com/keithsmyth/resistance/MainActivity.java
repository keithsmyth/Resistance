package com.keithsmyth.resistance;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.keithsmyth.resistance.feature.game.presentation.CharacterFragment;
import com.keithsmyth.resistance.feature.game.presentation.GameFragment;
import com.keithsmyth.resistance.feature.lobby.presentation.LobbyFragment;
import com.keithsmyth.resistance.feature.welcome.presentation.WelcomeFragment;
import com.keithsmyth.resistance.navigation.DisplayThrowable;
import com.keithsmyth.resistance.navigation.ErrorView;
import com.keithsmyth.resistance.navigation.Navigation;
import com.keithsmyth.resistance.navigation.Navigator;

public class MainActivity extends AppCompatActivity implements Navigator {

    private Navigation navigation;

    private DrawerLayout drawer;
    private View rootView;
    private Snackbar errorSnackBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigation = Injector.navigation();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        rootView = findViewById(R.id.root_view);

        if (savedInstanceState == null) {
            openWelcome();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        if (drawer.getDrawerLockMode(GravityCompat.END) != DrawerLayout.LOCK_MODE_LOCKED_CLOSED) {
            getMenuInflater().inflate(R.menu.menu_drawer, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_toggle_drawer:
                openDrawer();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
        clearError();
        if (drawer.isDrawerOpen(GravityCompat.END) || isWelcomeFragmentOpen()) {
            super.onBackPressed();
        } else {
            // TODO: are you sure?
            openWelcome();
        }
    }

    private boolean isWelcomeFragmentOpen() {
        return getSupportFragmentManager().findFragmentByTag(WelcomeFragment.class.getSimpleName()) != null;
    }

    @Override
    public void showError(DisplayThrowable displayThrowable) {
        // notify user
        errorSnackBar = Snackbar.make(rootView, displayThrowable.getDisplayMessage(this), Snackbar.LENGTH_INDEFINITE);
        errorSnackBar.show();

        // notify fragment
        final Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment instanceof ErrorView) {
            ((ErrorView) fragment).onErrorShown();
        }
    }

    private void clearError() {
        if (errorSnackBar != null && errorSnackBar.isShownOrQueued()) {
            errorSnackBar.dismiss();
        }
    }

    @Override
    public void openWelcome() {
        openFragment(WelcomeFragment.create());
        clearError();
        showDrawer(false);
    }

    @Override
    public void openLobby() {
        openFragment(LobbyFragment.create());
        clearError();
        showDrawer(false);
    }

    @Override
    public void openGame() {
        openFragment(GameFragment.create());
        clearError();
        showDrawer(true);
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

    private void showDrawer(boolean isVisible) {
        final FragmentManager supportFragmentManager = getSupportFragmentManager();
        final String tag = CharacterFragment.class.getSimpleName();
        if (isVisible) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            supportFragmentManager.beginTransaction()
                .add(R.id.right_drawer_container, CharacterFragment.create(), tag)
                .commit();
            openDrawer();
        } else {
            closeDrawer();
            final Fragment characterFragment = supportFragmentManager.findFragmentByTag(tag);
            if (characterFragment != null) {
                supportFragmentManager.beginTransaction()
                    .remove(characterFragment)
                    .commit();
            }
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
        supportInvalidateOptionsMenu();
    }

    private void openDrawer() {
        if (drawer != null) {
            drawer.openDrawer(GravityCompat.END);
        }
    }

    private void closeDrawer() {
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.END);
        }
    }
}
