package com.example.christian.care2reuse;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Christian on 19-03-2015.
 */

public class loginFragment extends Fragment {

    private loginFragment loginFragment;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_loginactivity, container, false);

        return view;
    }
}