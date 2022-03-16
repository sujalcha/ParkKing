package com.parkking.zujal.parkking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    Session session;

    MenuItem login;
    MenuItem logout;
    MenuItem Reservation;
    MenuItem Contact;
    String loginname="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      //  setSupportActionBar(toolbar);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_page, menu);
     //   login=(MenuItem)findViewById(R.id.loginid);
     //   logout=(MenuItem)findViewById(R.id.logoutid);
        login=menu.findItem(R.id.loginid);
        logout=menu.findItem(R.id.logoutid);
        Reservation=menu.findItem(R.id.reservationcurrentid);
        Reservation.setVisible(false);
        Contact=menu.findItem(R.id.contact);


        session=new Session(this);

        HashMap<String, String> user = session.getUserDetails();
        loginname= user.get(session.KEY_NAME);
      //  Toast.makeText(this,loginname, Toast.LENGTH_LONG).show();

        if(!session.loggedin()){
            login.setVisible(true);
            logout.setVisible(false);
            Reservation.setVisible(false);
            loginname="";
        }

        else if(session.loggedin()) {
            login.setVisible(false);
            logout.setVisible(true);
            Reservation.setVisible(true);
        }

        if( session.loggedin() &&  loginname.equals("wilson"))
        {
            Reservation.setVisible(false);
        }

        if( session.loggedin() && loginname.equals("admin"))
        {
            Contact.setVisible(false);
        }




        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.loginid:
                startActivity(new Intent("com.parkking.zujal.parkking.LogIn"));
                finish();
                break;
            // action with ID action_settings was selected
            case R.id.logoutid:
                session.setLoggedin(false);
                session.logout();
                startActivity(new Intent("com.parkking.zujal.parkking.LogIn"));
                Toast.makeText(this,"Logged out", Toast.LENGTH_LONG).show();
                finish();
             //   Toast.makeText(this,loginname, Toast.LENGTH_LONG).show();
                break;
            case R.id.reservationcurrentid:
                if (loginname.equals("admin"))
                {
                    Intent intent = new Intent("com.parkking.zujal.parkking.adminpage");
                    startActivity(intent);
                }

                else
                {
                    Intent intent = new Intent("com.parkking.zujal.parkking.Reservation");
                    startActivity(intent);

                }

                break;

            case R.id.contact:
                Intent conintent= new Intent("com.parkking.zujal.parkking.Contact");
                startActivity(conintent);
                break;
            default:
                break;
        }

        return true;
    }




}
