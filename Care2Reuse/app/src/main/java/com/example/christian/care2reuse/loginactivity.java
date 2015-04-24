package com.example.christian.care2reuse;



import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.CallbackManager;


public class loginactivity extends FragmentActivity {

    //private static final String USER_SKIPPED_LOGIN_KEY = "user_skipped_login";

    private static final int LOGIN = 0;
    //private static final int SELECTION = 1;
    //private static final int SETTINGS = 2;
    private static final int FRAGMENT_COUNT = LOGIN +1;

    private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];
    private boolean isResumed = false;
    private boolean userSkippedLogin = false;
    private AccessTokenTracker accessTokenTracker;
    private CallbackManager callbackManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        if (savedInstanceState != null) {
            userSkippedLogin = savedInstanceState.getBoolean(USER_SKIPPED_LOGIN_KEY);
        }
        */
        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                       AccessToken currentAccessToken) {
                if (isResumed) {
                    FragmentManager manager = getSupportFragmentManager();
                    int backStackSize = manager.getBackStackEntryCount();
                    for (int i = 0; i < backStackSize; i++) {
                        manager.popBackStack();
                    }

                    if (currentAccessToken != null) {
                        Intent intent = new Intent(loginactivity.this,MainActivity.class);
                        startActivity(intent);
                    } else {
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction transaction = fm.beginTransaction();

                        transaction.show(fragments[0]);
                        transaction.commit();
                    }
                }
            }
        };


        setContentView(R.layout.activity_loginactivity);

        FragmentManager fm = getSupportFragmentManager();
        loginFragment loginFragment = (loginFragment) fm.findFragmentById(R.id.loginFragment);
        fragments[LOGIN] = loginFragment;
        //fragments[SELECTION] = fm.findFragmentById(R.id.selectionFragment);
        //fragments[SETTINGS] = fm.findFragmentById(R.id.userSettingsFragment);

        FragmentTransaction transaction = fm.beginTransaction();
        for(int i = 0; i < fragments.length; i++) {
            transaction.hide(fragments[i]);
        }
        transaction.commit();
        /*
        loginFragment.setSkipLoginCallback(new loginFragment.SkipLoginCallback() {
            @Override
            public void onSkipLoginPressed() {
                userSkippedLogin = true;
                //showFragment(SELECTION, false);
            }
        });
        */
    }

    @Override
    public void onResume() {
        super.onResume();
        isResumed = true;

        // Call the 'activateApp' method to log an app event for use in analytics and advertising
        // reporting.  Do so in the onResume methods of the primary Activities that an app may be
        // launched into.
        AppEventsLogger.activateApp(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        isResumed = false;

        // Call the 'deactivateApp' method to log an app event for use in analytics and advertising
        // reporting.  Do so in the onPause methods of the primary Activities that an app may be
        // launched into.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //outState.putBoolean(USER_SKIPPED_LOGIN_KEY, userSkippedLogin);
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();

        if (AccessToken.getCurrentAccessToken() != null) {
            // if the user already logged in, try to show the selection fragment
            Intent intent = new Intent(loginactivity.this,MainActivity.class);
            startActivity(intent);
            //userSkippedLogin = false;
        } /*else if (userSkippedLogin) {
            Intent intent = new Intent(loginactivity.this,MainActivity.class);
            startActivity(intent);
        } */
        else {
            // otherwise present the splash screen and ask the user to login,
            // unless the user explicitly skipped.
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();

            transaction.show(fragments[0]);
            transaction.commit();
        }
    }


}
