package com.parkking.zujal.parkking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class LogIn extends MainActivity {

    EditText Usernameet,passwordet;
    TextView registerbut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        Usernameet =(EditText)findViewById(R.id.loginname_et);
        passwordet = (EditText)findViewById(R.id.loginpassword_et);
        registerbut=(TextView)findViewById(R.id.registerbutid);

        registerbut.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent= new Intent("com.parkking.zujal.parkking.Register");
                        startActivity(intent);

                    }
                }
        );
    }


    public void OnLogin(View view) {

        InputMethodManager im = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        String username= Usernameet.getText().toString();
        String password= passwordet.getText().toString();

        String type = "login";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, username, password);
    }
}
