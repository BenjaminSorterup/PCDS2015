package com.example.christian.care2reuse;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class SinglePostActivity extends ActionBarActivity {
    /*
     * Handles the functionality of a single post from the list of posts.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post);

        TextView description = (TextView)findViewById(R.id.single_content);
        TextView poster = (TextView)findViewById(R.id.nameanddate);

        //find and parse the information sent along with the intent
        Intent in = getIntent();
        Bundle b = in.getExtras();
        String desc = (String) b.get("content");
        String fname = (String) b.get("first_name");
        String lname = (String) b.get("last_name");
        String date = (String) b.get("date_created");

        //apply information to views in the single post activity
        description.setText(desc);
        String dec = "Created by " + fname + " " + lname + " on " + date;
        poster.setText(dec);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_single_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
