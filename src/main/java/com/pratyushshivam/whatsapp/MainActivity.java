package com.pratyushshivam.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {
    Boolean loginModeActive=false;

    public  void redirectLoggedIn()
    {
        if(ParseUser.getCurrentUser()!=null)
        {
            Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
            startActivity(intent);
        }
    }
    public void toggleLoginMode(View view)
    {
        Button loginSignupButton = findViewById(R.id.loginSignUpButton);
        TextView toggleLoginModeTextView = findViewById(R.id.toggleLoginModeTextView);
       if(loginModeActive)
       {
           loginModeActive=false;
           loginSignupButton.setText("Sign Up");
           toggleLoginModeTextView.setText("Or, Log In");
       }
       else
       {
           loginModeActive=true;
           loginSignupButton.setText("Log In");
           toggleLoginModeTextView.setText("Or, Sign Up");
       }
    }
    public void signupLogin(View View)
    {
        EditText usernameEditText = findViewById(R.id.usernameEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        if(loginModeActive)
        {
            ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if(e==null)
                    {
                        Toast.makeText(MainActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
                        redirectLoggedIn();
                    }
                    else
                    {
                        String message=e.getMessage();
                        if(message.toLowerCase().contains("java")){
                            message = e.getMessage().substring(e.getMessage().indexOf(" "));
                        }
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        else
        {

        }
        ParseUser user = new ParseUser();
        user.setUsername(usernameEditText.getText().toString());
        user.setPassword(passwordEditText.getText().toString());
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null)
                {
                    redirectLoggedIn();
                    Toast.makeText(MainActivity.this, "Sign Up successful", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String message=e.getMessage();
                    if(message.toLowerCase().contains("java")){
                        message = e.getMessage().substring(e.getMessage().indexOf(" "));
                    }
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("ChatApp Login");
        redirectLoggedIn();
    }
}
