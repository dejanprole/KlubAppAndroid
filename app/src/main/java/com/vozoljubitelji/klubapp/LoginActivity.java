package com.vozoljubitelji.klubapp;

/**
 * Created by macosx on 1/6/18.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

    // Email, password edittext
    EditText txtUsername, txtPassword;

    // login button
    Button btnLogin;

    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();

    // Session Manager Class
    SessionManagement session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Typeface typefaceLight = Typeface.createFromAsset(this.getAssets(), "fonts/Dosis-Light.ttf");
        Typeface typefaceBold = Typeface.createFromAsset(this.getAssets(), "fonts/Dosis-Bold.ttf");



        // Session Manager
        session = new SessionManagement(getApplicationContext());


        TextView usernameTextVview = (TextView) findViewById(R.id.username);
        usernameTextVview.setTypeface(typefaceBold);
        usernameTextVview.setText("Korisničko ime");

        TextView passwordTextView = (TextView) findViewById(R.id.password);
        passwordTextView.setTypeface(typefaceBold);
        passwordTextView.setText("Lozinka");

        Button button = (Button) findViewById(R.id.btnLogin);
        button.setTypeface(typefaceBold);
        button.setText("Ulogujte se");


        // Email, Password input text
        //txtUsername.setTypeface(typefaceLight);
        txtUsername = (EditText) findViewById(R.id.txtUsername);

        //txtPassword.setTypeface(typefaceLight);
        txtPassword = (EditText) findViewById(R.id.txtPassword);

        //Toast.makeText(getApplicationContext(), "Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();


        // Login button
        btnLogin = (Button) findViewById(R.id.btnLogin);


        // Login button click event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Get username, password from EditText
                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();

                // Check if username, password is filled
                if(username.trim().length() > 0 && password.trim().length() > 0){
                    // For testing puspose username, password is checked with sample data
                    // username = test
                    // password = test
                    if(username.equals("vozicari") && password.equals("vozicari2018")){

                        // Creating user login session
                        // For testing i am stroing name, email as follow
                        // Use user real data
                        session.createLoginSession("Vozicari", "kontakt@klubljubiteljazeleznice-beograd.com");

                        Toast.makeText(getApplicationContext(), "Uspešno ste se ulogovali.", Toast.LENGTH_SHORT);

                        // Staring MainActivity
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();

                    }else{
                        // username / password doesn't match
                        alert.showAlertDialog(LoginActivity.this, "Logovanje neuspešno.", "Korisničko ime ili lozinka nisu ispravni.", false);
                    }
                }else{
                    // user didn't entered username or password
                    // Show alert asking him to enter the details
                    alert.showAlertDialog(LoginActivity.this, "Logovanje neuspešno.", "Potrebno je popuniti sva polja.", false);
                }

            }
        });
    }
}