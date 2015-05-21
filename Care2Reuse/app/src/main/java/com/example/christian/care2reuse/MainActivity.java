package com.example.christian.care2reuse;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.FloatMath;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivity extends ListActivity {

    private static String url = "https://dev.care2reuse.org/posts/";

    //tags for receiving information from the JSON data
    private static final String TAG_ID = "id";
    private static final String TAG_CON = "content";
    private static final String TAG_USER = "user";
    private static final String TAG_FNAME = "first_name";
    private static final String TAG_LNAME = "last_name";
    private static final String TAG_DATE = "date_created";
    private static final String TAG_LOC = "location";

    ArrayList<HashMap<String,String>> postList;

    GPSTracker mGPS = new GPSTracker(this);
    Double mLatitude;
    Double mLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLatitude = mGPS.getLocation().getLatitude();
        mLongitude = mGPS.getLocation().getLongitude();


        ImageButton postButton = (ImageButton) findViewById(R.id.button4);
        postButton.bringToFront();
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

    private double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == 'K') {
            dist = dist * 1.609344;
        } else if (unit == 'N') {
            dist = dist * 0.8684;
        }
        return (dist);
    }
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
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
                        JSONArray location = jObj.getJSONObject(TAG_LOC).getJSONArray("coordinates");
                        Double latitude = location.getDouble(0);
                        Double longitude = location.getDouble(1);

                        Double distance = distance(mLatitude,mLongitude,latitude,longitude,'K');
                        Log.e("DISTANCE",""+distance);

                        //add to listview
                        HashMap<String,String> post = new HashMap<String,String>();
                        post.put(TAG_ID,id);
                        post.put(TAG_CON,content);
                        post.put(TAG_DATE,date);
                        post.put(TAG_FNAME,first_name);
                        post.put(TAG_LNAME,last_name);
                        post.put("distance",distance.toString());



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
            final SimpleAdapter adapter = new SimpleAdapter(
                    MainActivity.this,postList,
                    R.layout.list_v, new String[]{TAG_CON,TAG_DATE,TAG_FNAME,TAG_LNAME,"latitude"},
                    new int[]{R.id.content,R.id.date,R.id.firstname,R.id.lastname,R.id.distance});
            setListAdapter(adapter);
            EditText search = (EditText)findViewById(R.id.search);
            SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    Log.e("DISTANCE","touched the seekbar");
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    adapter.getFilter().filter(charSequence);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }
    }
}