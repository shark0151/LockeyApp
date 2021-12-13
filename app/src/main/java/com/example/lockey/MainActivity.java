package com.example.lockey;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "mainactv";

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

            if (true) {//check the password here
                Intent gotoDevice = new Intent(this, DeviceListActivity.class);
                gotoDevice.putExtra("USER", username);
                startActivity(gotoDevice);
            }
            else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "signInWithEmail:failure");
                Toast.makeText(MainActivity.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
