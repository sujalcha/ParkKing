package com.parkking.zujal.parkking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class adminReservationPage extends MainActivity {

    Session session;
    Button registeredBtn;
    Button unRegisteredBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_reservation_page);

        session = new Session(this);

        registeredBtn = (Button) findViewById(R.id.RegisteredClientBtn);
        unRegisteredBtn = (Button) findViewById(R.id.unRegisteredClientBtn);

        registeredBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( "com.parkking.zujal.parkking.clientCheck");
                startActivity(intent);
                finish();
            }
        });

        unRegisteredBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent("com.parkking.zujal.parkking.Register");
                startActivity(i);
                finish();
            }
        });

    }

}
