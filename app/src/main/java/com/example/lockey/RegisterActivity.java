package com.example.lockey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "registeractv";
    private User theUser;
    TextView error;
<<<<<<< Updated upstream
=======
    String _password;

>>>>>>> Stashed changes
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Lockey");
        error = findViewById(R.id.errorText);
        findViewById(R.id.back_button).setOnClickListener(v -> {
            Intent goToLogin = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(goToLogin);
        });
    }
    public void Register(View view) {

        String username = ((TextInputEditText) findViewById(R.id.registerUsername)).getText().toString();

        String password = ((TextInputEditText) findViewById(R.id.registerPassword)).getText().toString();
<<<<<<< Updated upstream
        if (password.isEmpty() || username.isEmpty()) {
            //Log.w(TAG, "signInWithEmail:failure", task.getException());
            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                    Toast.LENGTH_SHORT).show(); }
        else {
            verifyUsernameExistent(username);
=======
        String repeatPassword = ((TextInputEditText) findViewById(R.id.repeatPassword)).getText().toString();
        _password = password;
        if (password.isEmpty() || username.isEmpty() || repeatPassword.isEmpty()) {
            error.setVisibility(View.VISIBLE);
            error.setText("Please enter a username and a password");
        } else {
            if (!password.equals(repeatPassword)) {
                error.setVisibility(View.VISIBLE);
                error.setText("Passwords do not match");
            } else
                verifyUsernameExistent(username);
>>>>>>> Stashed changes

        }
    }
    private void verifyUsernameExistent(String username) {
        UserInterface usr = ApiUtils.getUserService();
        Call<User> getUserCall = usr.getUserByUsername(username);
        getUserCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
<<<<<<< Updated upstream
                    theUser = response.body();
                    error.setText("Username already existent. Please choose another username");
                    error.setVisibility(View.VISIBLE);
                    Log.d(TAG, theUser.toString());
=======
                    User userBad = response.body();
                    if (userBad.getUsername() != null && userBad.getUsername().equals(username)) {

                        error.setText("Username already existent. Please choose another username");
                        error.setVisibility(View.VISIBLE);
                        Log.d(TAG, username);
                    } else {
                        User usr = new User();
                        usr.setUsername(username);
                        usr.setPassword(_password);
                        RegisterUser(usr);
                    }

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private void RegisterUser(User theUser) {
        UserInterface usr = ApiUtils.getUserService();
        Call<User> registerUser = usr.createUser(theUser);
        registerUser.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Log.w(TAG, "Logged in");
                    Intent gotoDevice = new Intent(RegisterActivity.this, DeviceListActivity.class);
                    gotoDevice.putExtra("USER", theUser.getUsername());
                    startActivity(gotoDevice);
>>>>>>> Stashed changes
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
}