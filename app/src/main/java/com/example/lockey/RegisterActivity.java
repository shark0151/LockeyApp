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
        if (password.isEmpty() || username.isEmpty()) {
            //Log.w(TAG, "signInWithEmail:failure", task.getException());
            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                    Toast.LENGTH_SHORT).show(); }
        else {
            verifyUsernameExistent(username);

        }
    }
    private void verifyUsernameExistent(String username) {
        UserInterface usr = ApiUtils.getUserService();
        Call<User> getUserCall = usr.getUserByUsername(username);
        getUserCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    theUser = response.body();
                    error.setText("Username already existent. Please choose another username");
                    error.setVisibility(View.VISIBLE);
                    Log.d(TAG, theUser.toString());
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