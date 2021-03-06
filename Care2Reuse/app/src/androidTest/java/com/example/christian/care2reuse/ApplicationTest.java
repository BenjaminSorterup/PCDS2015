package com.example.christian.care2reuse;

import android.app.Application;
import android.app.Instrumentation;
import android.app.LauncherActivity;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.robotium.solo.Solo;
import static android.app.PendingIntent.getActivity;
import static android.support.v4.app.ActivityCompat.startActivity;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Solo solo;
    private Activity mClickFunActivity;
    private Post post_tmp;
    public ApplicationTest() {
        super(MainActivity.class);

    }
    @Override
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
    public void testPost() throws Exception{
       solo.assertCurrentActivity("Expected Mainactivity", "MainActivity");
        //post button
       ImageButton button1 = (ImageButton) solo.getView(R.id.button4);
       solo.clickOnView(button1);
       solo.assertCurrentActivity("Expected Post activity", "Post");
       EditText et = (EditText) solo.getView(R.id.postET);
       String text = "lalalala";
       solo.enterText(et, text);
       ImageButton button2 = (ImageButton) solo.getView(R.id.button3);
       solo.clickOnView(button2);
       solo.assertCurrentActivity("Expected Mainactivity","MainActivity");
    }

    public void testInfoPage() throws Exception{
        solo.assertCurrentActivity("Expected Mainactivity","MainActivity");
        ImageButton button =(ImageButton) solo.getView(R.id.imageButton);
        solo.clickOnView(button);
        solo.assertCurrentActivity("Expected Informaton Activity","Information");
    }

    public void testProfilePage() throws Exception{
        solo.assertCurrentActivity("Expected Mainactivity", "MainActivity");
        ImageButton button = (ImageButton) solo.getView(R.id.button3);
        solo.clickOnView(button);
        solo.assertCurrentActivity("Expected ProfilePage","Profile");
    }
}