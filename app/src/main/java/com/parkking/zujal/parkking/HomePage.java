package com.parkking.zujal.parkking;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import static com.parkking.zujal.parkking.Search.MY_PERMISSIONS_REQUEST_LOCATION;

public class HomePage extends MainActivity {

    private Button Loginbut;
    private TextView Registerbut;
    private  Button SearchLocationbut;
    private Session session;;
    String logintype;
    EditText Usernameet,passwordet;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        checkLocationPermission();
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        Usernameet =(EditText)findViewById(R.id.loginname_et);
        passwordet = (EditText)findViewById(R.id.loginpassword_et);

        session = new Session(this);

        if(session.loggedin()){
            finish();

            HashMap<String, String> user = session.getUserDetails();
            logintype= user.get(session.KEY_NAME).toString();

           // Toast.makeText(HomePage.this,logintype, Toast.LENGTH_LONG).show();

            if(logintype.equals("wilson")){
                startActivity(new Intent("com.parkking.zujal.parkking.Owner"));
                finish();
            }

            else if(logintype.equals("admin")){
                startActivity(new Intent("com.parkking.zujal.parkking.adminpage"));
                finish();
            }

            else{
                startActivity(new Intent("com.parkking.zujal.parkking.Search"));
                finish();
            }




        }



    OnClickButtonListener();


    }

    private void logout(){
        session.setLoggedin(false);

        startActivity(new Intent("com.parkking.zujal.parkking.LogIn"));

        finish();
        Toast.makeText(HomePage.this,"Logged out", Toast.LENGTH_LONG).show();
    }

    public void OnClickButtonListener(){

        Registerbut=(TextView)findViewById(R.id.Registerbut_id);
        Registerbut.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent= new Intent("com.parkking.zujal.parkking.Register");
                        startActivity(intent);

                    }
                }
        );

        SearchLocationbut=(Button)findViewById(R.id.SearchLocationbut_id);
        SearchLocationbut.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent= new Intent("com.parkking.zujal.parkking.Search");
                        startActivity(intent);

                    }
                }
        );
    }

    public void OnLogin(View view) {

        String username= Usernameet.getText().toString();
        String password= passwordet.getText().toString();
        String type = "login";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, username, password);
    }


    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }
}
