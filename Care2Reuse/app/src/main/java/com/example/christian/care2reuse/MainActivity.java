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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivity extends ListActivity {

    private static String url = "https://dev.care2reuse.org/posts/";

    //tags for receiving information from the JSON data
    private static final String TAG_ID = "id";
    private static final String TAG_CON = "content";
    private static final String TAG_USER = "user"; //index at which user information is stored
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

        //Change to the Single Post view and send along needed information.
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                String content = ((TextView) view.findViewById(R.id.content)).getText().toString();
                String lastname = ((TextView) view.findViewById(R.id.lastname)).getText().toString();
                String firstname = ((TextView) view.findViewById(R.id.firstname)).getText().toString();
                String date = ((TextView) view.findViewById(R.id.date)).getText().toString();

                Intent in = new Intent(MainActivity.this,SinglePostActivity.class);
                in.putExtra(TAG_CON,content);
                in.putExtra(TAG_LNAME,lastname);
                in.putExtra(TAG_FNAME,firstname);
                in.putExtra(TAG_DATE,date);

                startActivity(in);
            }
        });

        //execute the http calls for the API
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

    public String dateConverter(String date) {
        String sub_date = date.split("T")[0];
        String[] split_date = sub_date.split("-");
        String final_date = split_date[2] + "/" + split_date[1] + "/" + split_date[0];
        return final_date;
    }

    public class HttpAsyncTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... args) {
            JSONParser jsonParser = new JSONParser();
            String jsonStr = jsonParser.request(url,jsonParser.GET,null);
            if (jsonStr != null) {
                try {
                    JSONArray jArr = new JSONArray(jsonStr);

                    //Loop through objects in array and extract required information
                    for (int i = 0; i < jArr.length(); i++) {
                        JSONObject jObj = jArr.getJSONObject(i);
                        String id = ""+jObj.getInt(TAG_ID);
                        String content = jObj.getString(TAG_CON);
                        String date = dateConverter(jObj.getString(TAG_DATE));
                        JSONObject user = jObj.getJSONObject(TAG_USER);
                        String first_name = user.getString(TAG_FNAME);
                        String last_name = user.getString(TAG_LNAME);
                        Log.e("TESTERINO",date);

                        //add to listview
                        HashMap<String,String> post = new HashMap<String,String>();
                        post.put(TAG_ID,id);
                        post.put(TAG_CON,content);
                        post.put(TAG_DATE,date);
                        post.put(TAG_FNAME,first_name);
                        post.put(TAG_LNAME,last_name);

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
            //create list adapter from information gained from the JSON data
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this,postList,
                    R.layout.list_v, new String[]{TAG_CON,TAG_DATE,TAG_FNAME,TAG_LNAME},
                    new int[]{R.id.content,R.id.date,R.id.firstname,R.id.lastname});
            setListAdapter(adapter);
        }
    }
}