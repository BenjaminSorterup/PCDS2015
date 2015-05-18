package com.example.christian.care2reuse;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Intent;
import android.test.ApplicationTestCase;
import android.test.ViewAsserts;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.test.InstrumentationTestCase;
import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

import static android.app.PendingIntent.getActivity;
import static android.support.v4.app.ActivityCompat.startActivity;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ActivityInstrumentationTestCase2 {


    private Intent mLaunchIntent = new Intent();
    private Activity mClickFunActivity = new Activity();
    private Button mClickMeButton;
    private TextView mInfoTextView;

    public ApplicationTest(Class activityClass) {
        super(activityClass);
    }

    @Override
    protected void setUp() throws Exception{
        super.setUp();

        setActivityInitialTouchMode(true);

        mClickFunActivity = getActivity();
        mClickMeButton = (Button) mClickFunActivity.findViewById(R.id.bottomBox);
        mInfoTextView = (TextView) mClickFunActivity.findViewById(R.id.bottomBox);
    }

    @MediumTest
    public void testClickMeButton_layout() {
        final View decorView = mClickFunActivity.getWindow().getDecorView();

        ViewAsserts.assertOnScreen(decorView, mClickMeButton);

        final ViewGroup.LayoutParams layoutParams =
                mClickMeButton.getLayoutParams();
        assertNotNull(layoutParams);
        assertEquals(layoutParams.width, WindowManager.LayoutParams.MATCH_PARENT);
        assertEquals(layoutParams.height, WindowManager.LayoutParams.WRAP_CONTENT);
    }


}