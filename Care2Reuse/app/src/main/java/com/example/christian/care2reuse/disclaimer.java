package com.example.christian.care2reuse;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * disclaimer class is a fragment shows the disclaimer information
 */
public class disclaimer extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.disclaimer, container, false);

        return v;
    }

    /**
     *@return disclaimer fragment
     */
    public static disclaimer newInstance() {

        disclaimer f = new disclaimer();

        return f;
    }
}
