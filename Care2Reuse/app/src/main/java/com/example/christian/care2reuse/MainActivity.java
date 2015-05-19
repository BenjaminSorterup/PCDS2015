package com.example.christian.care2reuse;


import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.app.Activity;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivity extends ListActivity {

    private static String url = "https://dev.care2reuse.org/posts/?format=json";

    private static final String TAG_ID = "id";
    private static final String TAG_CON = "content";
    private static final String TAG_USER = "2"; //index at which user information is stored
    private static final String TAG_FNAME = "first_name";
    private static final String TAG_LNAME = "last_name";
    private static final String TAG_DATE = "date_created";



    ArrayList<HashMap<String,String>> postList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        postList = new ArrayList<HashMap<String, String>>();

        ListView lv = getListView();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                String content = ((TextView) view.findViewById(R.id.content))
                        .getText().toString();
                Intent in = new Intent(MainActivity.this,SinglePostActivity.class);
                in.putExtra(TAG_CON,content);
                startActivity(in);
            }
        });

        new HttpAsyncTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void toInfopage(View view)
    {
        Intent intent = new Intent(MainActivity.this,Information.class);
        startActivity(intent);
    }

    public void toSettings(View view)
    {
        Intent intent = new Intent(MainActivity.this,Settings.class);
        startActivity(intent);
    }

    public void toProfile(View view)
    {
        Intent intent = new Intent(MainActivity.this,Profile.class);
        startActivity(intent);
    }

    public void toPost(View view)
    {
        Intent intent = new Intent(MainActivity.this,Post.class);
        startActivity(intent);
    }

    private class HttpAsyncTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... args) {
            JSONParser jsonParser = new JSONParser();
            String jsonStr = jsonParser.request(url);
            Log.e("TESTERINO","vi kører");
            if (jsonStr != null) {
                try {
                    JSONArray jArr = new JSONArray(jsonStr);

                    for (int i = 0; i < jArr.length(); i++) {
                        JSONObject jObj = jArr.getJSONObject(i);
                        Log.e("TESTERINO",""+jObj);
                        String id = ""+jObj.getInt(TAG_ID);
                        String content = jObj.getString(TAG_CON);
                        String date = jObj.getString(TAG_DATE);
                        //JSONObject user = jObj.getJSONObject(TAG_USER);
                        Log.e("TESTERINO","hej mås");
                        //Log.e("TESTERINO",""+user);
                        Log.e("TESTERINO","hej igen mås");

                        HashMap<String,String> post = new HashMap<String,String>();
                        post.put(TAG_ID,id);
                        post.put(TAG_CON,content);
                        post.put(TAG_DATE,date);

                        postList.add(post);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this,postList,
                    R.layout.list_v, new String[]{TAG_ID,TAG_CON},
                    new int[]{R.id.id,R.id.content});
            setListAdapter(adapter);
        }
    }
}