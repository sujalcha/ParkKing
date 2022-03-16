package com.parkking.zujal.parkking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parkking.zujal.parkking.models.CarparkerInfo;

import java.util.HashMap;

public class Register extends MainActivity {

    private EditText Username;
    private EditText Password;
    private EditText retypePassword;
    private EditText Phonenumber;
    private EditText Useremail;
    private TextView Loginbut;
    private Button Registerbut;

    private TextView validationusername;
    private TextView validationpassword;
    private TextView validation2ndpassword;
    private TextView validationnumber;
    private TextView validationemail;
    private TextView validationterm;
    private CheckBox checkBox;
    public CarparkerInfo carparker;
    private String sessionname;


    public String email, name, phone, password;
    public String pname, ppassword, pphone, pemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Username=(EditText)findViewById(R.id.editText3);;
        Password=(EditText)findViewById(R.id.editText4);
        Phonenumber=(EditText)findViewById(R.id.editText7);
        Useremail=(EditText)findViewById(R.id.editText5);
        retypePassword=(EditText)findViewById(R.id.retypePassword);


        validationusername=(TextView)findViewById(R.id.validationusername);
        validationpassword=(TextView)findViewById(R.id.validationpassword);
        validation2ndpassword=(TextView)findViewById(R.id.validation2ndpassword);
        validationnumber=(TextView)findViewById(R.id.validationnumber);
        validationemail=(TextView)findViewById(R.id.validationemail);
        validationterm=(TextView)findViewById(R.id.termsid);

        checkBox=(CheckBox) findViewById(R.id.checkBox);

        validationusername.setVisibility(View.GONE);
        validationpassword.setVisibility(View.GONE);
        validation2ndpassword.setVisibility(View.GONE);
        validationnumber.setVisibility(View.GONE);
        validationemail.setVisibility(View.GONE);
        validationterm.setVisibility(View.GONE);


        Loginbut=(TextView) findViewById(R.id.loginbut);
        Registerbut=(Button)findViewById(R.id.registerbut_id);

        Loginbut.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent= new Intent("com.parkking.zujal.parkking.LogIn");
                        startActivity(intent);
                    }
                }
        );

        session=new Session(this);

        if(session.loggedin()){

            HashMap<String, String> user = session.getUserDetails();
            sessionname = user.get(session.KEY_NAME);
            Toast.makeText(this,sessionname, Toast.LENGTH_SHORT).show();
        }
        else {
            sessionname="";
        }


    }



    public void OnRegister(View view)
    {


        InputMethodManager im = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(new View(this).getWindowToken(), 0);

        name=Username.getText().toString();
        password=Password.getText().toString();
        phone=Phonenumber.getText().toString();
        email=Useremail.getText().toString();

        carparker= new CarparkerInfo();
        carparker.setParkername(name);
        carparker.setParkerpassword(password);
        carparker.setParkernumber(phone);
        carparker.setParkeremail(email);

        pname=carparker.getParkername();
        ppassword=carparker.getParkerpassword();
        pphone=carparker.getParkernumber();
        pemail=carparker.getParkeremail();


            if (pname.length() == 0){
                validationusername.setText("Username cannot be empty.");
                validationusername.setVisibility(View.VISIBLE);
                Username.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }

            else if (!pname.matches("^[a-zA-Z0-9.]+$"))
            {

                validationusername.setText("Please enter a vaild username.");
                validationusername.setVisibility(View.VISIBLE);
                Username.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }

            else
            {
                Username.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_gtick, 0);
                validationusername.setVisibility(View.GONE);
            }

            if (ppassword.length() == 0){
                validationpassword.setText("Password cannot be empty.");
                validationpassword.setVisibility(View.VISIBLE);
                Password.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }
            else if (!ppassword.matches("^[a-zA-Z0-9]{2,8}+$")) {

                validationpassword.setText("Password must be at least 2-8 characters and contain no special characters.");
                validationpassword.setVisibility(View.VISIBLE);
                Password.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }

            else
            {
                Password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_gtick, 0);
                validationpassword.setVisibility(View.GONE);
            }


            if (retypePassword.getText().length() == 0){
                validation2ndpassword.setText("Password cannot be empty.");
                validation2ndpassword.setVisibility(View.VISIBLE);
                retypePassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }

            else if(retypePassword.getText().length() != 0&&!Password.getText().toString().equals(retypePassword.getText().toString())) {

                validationpassword.setText("Password must match. Please check input fields.");
                validation2ndpassword.setText("Password must match. Please check input fields.");
                validationpassword.setVisibility(View.VISIBLE);
                validation2ndpassword.setVisibility(View.VISIBLE);
                retypePassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }

            else
            {
                retypePassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_gtick, 0);
                validationpassword.setVisibility(View.GONE);
                validation2ndpassword.setVisibility(View.GONE);
            }


            if (pphone.length() == 0){
                validationnumber.setText("Phonenumber cannot be empty.");
                validationnumber.setVisibility(View.VISIBLE);
                Phonenumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }

            else if (!pphone.matches("^[0-9]{10,10}+$")) {

                validationnumber.setText("Enter a ten digit number.");
                validationnumber.setVisibility(View.VISIBLE);
                Phonenumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }
            else
            {
                Phonenumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_gtick, 0);
                validationnumber.setVisibility(View.GONE);
            }

            if (pemail.length() == 0){
                validationemail.setText("Useremail cannot be empty.");
                validationemail.setVisibility(View.VISIBLE);
                Useremail.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }

            else if (!pemail.matches("^[a-zA-Z0-9.]+@[a-zA-Z0-9.]+\\.[a-zA-Z]{2,}$")) {

                validationemail.setText("Email is not valid. Please enter a valid email.");
                validationemail.setVisibility(View.VISIBLE);
                Useremail.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }
            else
            {
                Useremail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_gtick, 0);
                validationemail.setVisibility(View.GONE);
            }

                    //Checks if password and retype-password field matches


            if(!checkBox.isChecked())
            {
                validationterm.setText("Please agree to terms");
                validationterm.setVisibility(View.VISIBLE);
            }

            else
            {
                validationterm.setVisibility(View.GONE);
            }

            if(Username.getText().toString().matches("^[a-zA-Z0-9.]+$")
                    && (Password.getText().toString().matches("^[a-zA-Z0-9]{2,8}+$"))
                    && (Password.getText().toString().equals(retypePassword.getText().toString()))
                    && (Phonenumber.getText().toString().matches("^[0-9]{10,10}+$"))
                    && (Useremail.getText().toString().matches("^[a-zA-Z0-9.]+@[a-zA-Z0-9.]+\\.[a-zA-Z]{2,}$"))
                    && (checkBox.isChecked()))
            {

                if(sessionname.equals("admin")){

                    String type = "registerclient";

                    BackgroundWorker backgroundWorker = new BackgroundWorker(this);
                    backgroundWorker.execute(type, name, password, phone, email);
                }

                else
                {

                    String type = "register";

                    BackgroundWorker backgroundWorker = new BackgroundWorker(this);
                    backgroundWorker.execute(type, name, password, phone, email);

                }

            }
    }

}
