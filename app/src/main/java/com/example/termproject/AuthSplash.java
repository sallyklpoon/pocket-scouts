package com.example.termproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AuthSplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_splash);
    }


    public void launchEmailSignup(View view) {
        Intent intent = new Intent(this, AuthSignup.class);
        startActivity(intent);
    }

    public void login(View view) {
        Intent intent = new Intent(this, AuthLogin.class);
        startActivity(intent);
    }
}