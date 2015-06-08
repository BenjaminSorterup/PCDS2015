package com.example.christian.care2reuse;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
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

import java.util.Calendar;
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
    EditText et;
    String msg;
    String name;
    int imgview = 1;
    ImageButton button1;
    ImageView mImageView;
    ImageView mImageView2;
    ImageView mImageView3;
    HorizontalScrollView myGallery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        et = (EditText) findViewById(R.id.postET);

        button1 = (ImageButton) findViewById(R.id.button1);

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
        Log.e("Error","vi klikkede");
        Calendar c = Calendar.getInstance();
        String year = ""+c.get(Calendar.YEAR);
        String month = ""+(c.get(Calendar.MONTH)+1);
        String day = ""+c.get(Calendar.DAY_OF_MONTH);
        if(Integer.parseInt(day) < 10){
            day = "0"+day;
        }
        if(Integer.parseInt(month) < 10){
            month = "0"+month;
        }


        msg = et.getText().toString();
        String distance = "0";
        Intent in = new Intent(Post.this,MainActivity.class);
        in.putExtra("first_name","Christian");
        in.putExtra("last_name" ,"Stenderup");
        in.putExtra("content",msg);
        in.putExtra("date",day+"/"+month+"/"+year);
        in.putExtra("id","1");
        in.putExtra("distance",distance);
        et.setText("");
        startActivity(in);

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
        mImageView = (ImageView)findViewById(R.id.postimg);
        mImageView2 = (ImageView)findViewById(R.id.postimg2);
        mImageView3 = (ImageView)findViewById(R.id.postimg3);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            if(imgview == 1){
            mImageView.setImageBitmap(Bitmap.createScaledBitmap(photo, 60, 60, false));
             imgview = 2;
        }
            else if(imgview == 2){
                mImageView2.setImageBitmap(Bitmap.createScaledBitmap(photo, 60, 60, false));
                imgview = 3;
            }
            else if(imgview == 3){
                mImageView3.setImageBitmap(Bitmap.createScaledBitmap(photo, 60, 60, false));
                imgview = 1;
            }

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
                    if(imgview == 3) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(selectedImagePath);
                        mImageView3.setImageBitmap(Bitmap.createScaledBitmap(myBitmap, 60, 60, false));
                        imgview = 1;
                    }
                    else if(imgview == 2) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(selectedImagePath);
                        mImageView2.setImageBitmap(Bitmap.createScaledBitmap(myBitmap, 60, 60, false));
                        imgview = 3;
                    }
                    else if(imgview == 1) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(selectedImagePath);
                        mImageView.setImageBitmap(Bitmap.createScaledBitmap(myBitmap, 60, 60, false));
                        imgview = 2;
                    }

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
