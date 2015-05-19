package com.example.christian.care2reuse;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * care2reuse class is a fragment shows information of Care2Reuse
 */
public class care2reuse extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.care2reuse, container, false);

        return v;
    }

    /**
     *@return care2reuse fragment
     */
    public static care2reuse newInstance() {

        care2reuse f = new care2reuse();

        return f;
    }
}
