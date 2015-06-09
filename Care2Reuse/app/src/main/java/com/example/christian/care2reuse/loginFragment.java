package com.example.christian.care2reuse;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import java.util.Arrays;

/**
 * Created by Christian on 19-03-2015.
 */

public class loginFragment extends Fragment {

    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private Button skipButton;

    @Override

    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.login, container, false);
        //creates callback manager to handle callbacks from the Facebook API
        callbackManager = CallbackManager.Factory.create();

        //initializes the skip login button
        skipButton = (Button)view.findViewById(R.id.skip);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Skip(view);
            }
        });
        
        //Initializes the login button
        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        //Sets the permissions to the data the application should have access to on the Facebook API
        loginButton.setReadPermissions("user_friends");

        loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //Shown if the login was succesful
                Toast.makeText(getActivity(), "Login successful", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                //Shown if the login was canceled
                Toast.makeText(getActivity(), "Login canceled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                //Shown if there were an error on login
                Toast.makeText(getActivity(), "Login error", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }


    /**
     * onActivityResult() handles the result of the login. 
     * 
    */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Skip() is called when the skip button is pressed. It changes the activity to the mainpage.
     * @param view takes the current view
     * 
    */
    public void Skip(View view)
    {
        Intent intent = new Intent(getActivity(),MainActivity.class);
        startActivity(intent);
    }
}
