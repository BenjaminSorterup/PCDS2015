package com.example.christian.care2reuse;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * innoaid class is a fragment shows information of the Innoaid
 */
public class innoaid extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.innoaid, container, false);

        return v;
    }

    /**
     *@return innoaid fragment
     */
    public static innoaid newInstance() {

        innoaid f = new innoaid();

        return f;
    }
}
