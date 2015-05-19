package com.example.christian.care2reuse;

import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Yu on 18-05-2015.
 */
public class VolleyApplication extends Post{

    private static VolleyApplication sInstance;
    private RequestQueue reqQ;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        reqQ = Volley.newRequestQueue(this);
        sInstance = this;
    }

    public synchronized static VolleyApplication getInstance(){
        return sInstance;
    }

    public RequestQueue getRequestQueue(){
        return reqQ;
    }
}
