package com.example.lockey;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "mainactv";
    private User theUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Lockey");
        findViewById(R.id.goToRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToRegister = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(goToRegister);
            }
        });
    }

    public void LogIn(View view) {

        String username = ((TextInputEditText) findViewById(R.id.username)).getText().toString();

        String password = ((TextInputEditText) findViewById(R.id.password)).getText().toString();
        if (password.isEmpty() || username.isEmpty()) {
            //Log.w(TAG, "signInWithEmail:failure", task.getException());
            Toast.makeText(MainActivity.this, "Authentication failed.",
                    Toast.LENGTH_SHORT).show(); }
        else {
            getUser(username,password);

        }
    }

    private void getUser(String username,String password) {
        UserInterface usr = ApiUtils.getUserService();
        Call<User> getUserCall = usr.getUserByUsername(username);
        getUserCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    if(response.body().getUsername() != null) {
                        theUser = response.body();
                        authorize(username, password);
                        Log.d(TAG, theUser.toString());
                    }
                    else{
                        Log.w(TAG, "user not found");
                        Toast.makeText(MainActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String message = "Problem " + response.code() + " " + response.message();
                    Log.d(TAG, message);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }
    private void authorize(String username,String password)
    {
        if (theUser != null) {
            if(theUser.getUsername().equals(username) && theUser.getPassword().equals(password)) {
                Log.w(TAG, "Logged in");
                Intent gotoDevice = new Intent(MainActivity.this, DeviceListActivity.class);
                gotoDevice.putExtra("USER", username);
                startActivity(gotoDevice);
            }
        }
        else {
            // If sign in fails, display a message to the user.
            Log.w(TAG, "wrong password");
            Toast.makeText(MainActivity.this, "Authentication failed.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
