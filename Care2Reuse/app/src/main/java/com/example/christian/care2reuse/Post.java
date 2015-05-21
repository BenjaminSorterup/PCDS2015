package com.example.christian.care2reuse;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import android.widget.PopupMenu;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.provider.MediaStore;
import android.graphics.Bitmap;
import android.net.Uri;
import android.database.Cursor;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Response.*;

/*
*Post class represents the activity where the user is able to create posts
 */
public class Post extends ActionBarActivity {
    private static final int SELECT_PICTURE = 1;
    static final int CAMERA_REQUEST = 1888;
    String str_url = "https://dev.care2reuse.org/posts/";
    TextView tv;
    EditText et;

    String name;

    Button button1;
    ImageView mImageView;
    HorizontalScrollView myGallery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        et = (EditText) findViewById(R.id.postET);
        tv = (TextView) findViewById(R.id.tvPost);

        button1 = (Button) findViewById(R.id.button1);

        button1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(Post.this, button1);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        // Toast.makeText(Post.this, item.getItemId(), Toast.LENGTH_SHORT).show();

                        if (item.getTitle().equals("Take picture")) {
                            dispatchTakePictureIntent();

                        }
                        if (item.getTitle().equals("Upload picture from Library")) {
                            callfunc();
                        }

                        return true;
                    }
                });

                popup.show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
    *Takes the msg written in the textView and sends it to the server
     */
    public void createMSG(View v) {
        String msg;
        msg = et.getText().toString();
        JSONObject json = new JSONObject();
        try{
            json.put("id", "c00369260eab0df08da7c37d96a758bb99dfe96d");
            json.put("content", msg);
            json.put("address", "SOMETHING RANDOM");
            json.put("location",null );
        }catch(JSONException e){
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, str_url, json, new Response.Listener<JSONObject>(){
           @Override
           public void onResponse(JSONObject response) {
               System.out.println(response);
               Log.e("pew?", response.toString());
               Toast.makeText(Post.this, "Its workiing", Toast.LENGTH_SHORT).show();
           }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError e) {
               System.out.println(e);
               Log.e("pew2", e.toString());
                Toast.makeText(Post.this, "Error la", Toast.LENGTH_SHORT).show();
            }
        }) {@Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Tokenc00369260eab0df08da7c37d96a758bb99dfe96d");
                return params;
            }
        };
/*
        StringRequest stringReq = new StringRequest(Request.Method.POST, str_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        tv.setText("It's send", TextView.BufferType.EDITABLE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tv.setText("Error", TextView.BufferType.EDITABLE);

            }
        });/* {@Override
        protected Map<String, String> getParams () {
            Map<String, String> params = new HashMap<String, String>();
            params.put("id", "i dont even know"); //skal have facebook login username
            params.put("content", "pew?"); //pew
            params.put("address", "empty for the moment"); //skal kunne få image bitmap
            params.put("location", "pew");
            return params;
        }
        @Override
        public Map<String, String> getHeaders ()throws AuthFailureError {
            Map<String, String> params = new HashMap<String, String>();
            params.put("Content", "applicatiob/x-ww-form-urlencoded");
            return params;
        }
    };*/
    queue.add(req);
    }

 /*
*Used to create the media library intent
 */
    public void callfunc(){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);

    }

    /**
     * Saves the image when one of either the camera or media library intent is done.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        myGallery = (HorizontalScrollView)findViewById(R.id.postgallery);
        mImageView = (ImageView)findViewById(R.id.postimg);
        LinearLayout layout = (LinearLayout)findViewById(R.id.horizontal);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            mImageView.setImageBitmap(photo);

            layout.addView(mImageView);
        }
        /*
        *Saves the selected photo when accessing the media library intent.
         */
        String filemanagerstring;
        String selectedImagePath;
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();

                //I/O
                filemanagerstring = selectedImageUri.getPath();

                //MEDIA
                selectedImagePath = getPath(selectedImageUri);
                if(selectedImagePath!=null) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(selectedImagePath);
                    mImageView.setImageBitmap(myBitmap);
                    myGallery.addView(mImageView);
                }
                else
                    System.out.println("filemanagerstring is the right one for you!");
            }
        }

    }
    /**
     *Gets the path of the file chosen in the media library
     */
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if(cursor!=null)
        {
            //This may be NULL if one is using I/O
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        else return null;
    }
    private void dispatchTakePictureIntent() {
        /*
        *access the camera intent
         */
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, CAMERA_REQUEST);
        }
    }

}
