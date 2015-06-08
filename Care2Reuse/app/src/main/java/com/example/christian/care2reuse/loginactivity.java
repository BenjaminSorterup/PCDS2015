package com.example.christian.care2reuse;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;


public class loginactivity extends FragmentActivity {

    private static final int LOGIN = 0;
    private static final int FRAGMENT_COUNT = LOGIN +1;

    private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];
    private boolean isResumed = false;
    private AccessTokenTracker accessTokenTracker;
    private CallbackManager callbackManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
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
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction transaction = fm.beginTransaction();

                        transaction.hide(fragments[LOGIN]);
                        transaction.commit();
                        //String userid = currentAccessToken.getUserId();
                        /*
                        GraphRequest request = GraphRequest.newMeRequest(
                                currentAccessToken,
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {
                                        try {
                                            Log.e("Test",object.getString("name"));

                                            //post to api
                                            HttpClient httpClient = new DefaultHttpClient();

                                            try {
                                                HttpPost httppost = new HttpPost("your url");
                                                // serialization of data into json
                                                //Gson gson = new GsonBuilder().serializeNulls().create();
                                                //String json = gson.toJson(data);
                                                String json = object.toString();
                                                httppost.addHeader("content-type", "application/json");

                                                // creating the entity to send
                                                ByteArrayEntity toSend = new ByteArrayEntity(json.getBytes());
                                                httppost.setEntity(toSend);

                                                HttpResponse postResponse = httpClient.execute(httppost);
                                                String status = "" + postResponse.getStatusLine();
                                                System.out.println(status);
                                                HttpEntity entity = postResponse.getEntity();

                                                //InputStream input = entity.getContent();
                                                //StringWriter writer = new StringWriter();
                                                //IOUtils.copy(input, writer, "UTF8");
                                                //String content = writer.toString();
                                                // do something useful with the content
                                                //System.out.println(content);
                                                //writer.close();
                                                //EntityUtils.consume(entity);

                                            } catch (Exception e) {

                                                e.printStackTrace();

                                            } finally {
                                                httpClient.getConnectionManager().shutdown();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            //Bundle parameters = new Bundle();
                            //parameters.putString("fields", "id,name");
                            //request.setParameters(parameters);

                            request.executeAsync();
                        */

                    } else {
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction transaction = fm.beginTransaction();

                        transaction.show(fragments[LOGIN]);
                        transaction.commit();

                    }
                }
            }
        };


        setContentView(R.layout.activity_loginactivity);

        FragmentManager fm = getSupportFragmentManager();
        loginFragment loginFragment = (loginFragment) fm.findFragmentById(R.id.loginFragment);
        fragments[LOGIN] = loginFragment;

        FragmentTransaction transaction = fm.beginTransaction();
        transaction.hide(fragments[0]);
        transaction.commit();

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
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();

        if (AccessToken.getCurrentAccessToken() != null) {
            Intent intent = new Intent(loginactivity.this,MainActivity.class);
            startActivity(intent);
            Log.e("test2","Onresume husk");


        }
        else {
            // otherwise present the splash screen and ask the user to login,
            // unless the user explicitly skipped.
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();

            transaction.show(fragments[0]);
            transaction.commit();

        }
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

}