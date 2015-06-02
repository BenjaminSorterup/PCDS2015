package com.example.christian.care2reuse;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Profile extends ListActivity {

    private static String url = "https://dev.care2reuse.org/posts/";

    //tag for error logging
    private static String TAG_ERR = "Error";

    //tags for receiving information from the JSON data
    private static final String TAG_ID = "id";
    private static final String TAG_CON = "content";
    private static final String TAG_USER = "user";
    private static final String TAG_FNAME = "first_name";
    private static final String TAG_LNAME = "last_name";
    private static final String TAG_DATE = "date_created";
    private static final String TAG_LOC = "location";

    ArrayList<HashMap<String,String>> postList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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



                Intent in = new Intent(Profile.this,SinglePostActivity.class);
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
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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
                        String facebook_id = user.getString("facebook_uid");
                        String first_name = user.getString(TAG_FNAME);
                        String last_name = user.getString(TAG_LNAME);
                        JSONArray location = jObj.getJSONObject(TAG_LOC).getJSONArray("coordinates");

                        //add to listview
                        HashMap<String,String> post = new HashMap<String,String>();
                        if (facebook_id.equals("D3213123122")){
                            post.put(TAG_CON,content);
                            post.put(TAG_DATE,date);
                            post.put(TAG_FNAME,first_name);
                            post.put(TAG_ID,id);
                            post.put(TAG_LNAME,last_name);

                            postList.add(post);
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Log.e(TAG_ERR,"No connection to API.");
            }return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //create list adapter from information gained from the JSON data
            final SimpleAdapter adapter = new SimpleAdapter(
                    Profile.this,postList,
                    R.layout.list_v, new String[]{TAG_CON,TAG_DATE,TAG_FNAME,TAG_LNAME,"latitude"},
                    new int[]{R.id.content,R.id.date,R.id.firstname,R.id.lastname,R.id.distance});
            setListAdapter(adapter);
        }
    }
}
