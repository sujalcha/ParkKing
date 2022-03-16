package com.parkking.zujal.parkking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Contact extends MainActivity{

    EditText fullname;
    EditText fullemail;
    EditText fullmessage;

    Button sendmessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        fullname=(EditText)findViewById(R.id.fullname);
        fullemail=(EditText)findViewById(R.id.fullemail);
        fullmessage=(EditText)findViewById(R.id.fullmessage);
        sendmessage=(Button)findViewById(R.id.sendmessage) ;


        sendmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=fullname.getText().toString();
                String emaili=fullemail.getText().toString();
                String messagee=fullmessage.getText().toString();

                String fullnamee=fullname.getText().toString();
                String emaidd=fullemail.getText().toString();
                String email = "zujalxtha@gmail.com";
                String subject ="Message from "+fullnamee+" with emailid "+emaidd;
                String message = messagee;

                //Creating SendMail object
                SendMail sm = new SendMail(Contact.this, email, subject, message);

                //Executing sendmail to send email
                sm.execute();

                Intent intent= new Intent("com.parkking.zujal.parkking.Search");
                startActivity(intent);
            }
        });


    }
}
