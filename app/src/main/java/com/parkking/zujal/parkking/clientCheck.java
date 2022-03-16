package com.parkking.zujal.parkking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.parkking.zujal.parkking.models.CarparkerInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class clientCheck extends MainActivity {

    Context context;
    TextView Registerbtn;
    EditText clientUsername;
    TextView validationUsername;
    SharedPreferences shareP;
    public static final String PREFS_NAME = "clientUsername";
    String keyname = "name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_check);

        session = new Session(this);

        validationUsername = (TextView)findViewById(R.id.validationClientUsername);
        clientUsername = (EditText)findViewById(R.id.registeredClient_et);

        Registerbtn = (TextView)findViewById(R.id.registerbutid);
        Registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.parkking.zujal.parkking.Register");
                startActivity(intent);
            }
        });



    }

    public void checkUsername(View view) {

        verifyUsername();

    }

    private void verifyUsername() {

        final String username= clientUsername.getText().toString();

        String uri = String
                .format("http://parkkinz.com/androidphpfiles/getuserdetails.php",
                        username);


        StringRequest myReq = new StringRequest(Request.Method.POST,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.toString().equals("data not found")){
                            validationUsername.setText("Username not found. Please register client.");
                            validationUsername.setVisibility(View.VISIBLE);
                            Toast.makeText(clientCheck.this,response,Toast.LENGTH_SHORT).show();
                        }else {

                            try {
                                JSONArray jsonArray = new JSONArray(response);

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);

                                    CarparkerInfo carparker = new CarparkerInfo(
                                            obj.getString("cusname"),
                                            obj.getString("cuspassword"),
                                            obj.getString("cusphone"),
                                            obj.getString("cusemail")
                                    );

                                    String name = carparker.getParkername().toString();

                                   // validationUsername.setText("Username found. Client is registered");
                                    Toast.makeText(clientCheck.this,"Username found. Client is registered",Toast.LENGTH_SHORT).show();
                                 //   validationUsername.setVisibility(View.VISIBLE);


                                shareP = getSharedPreferences(PREFS_NAME, 0);
                                SharedPreferences.Editor editor = shareP.edit();
                                editor.putString(keyname, name);
                                editor.apply();

                                Intent intent = new Intent("com.parkking.zujal.parkking.Search");
                                startActivity(intent);

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }

                })
        {

            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_name", username);
                return params;
            };
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(myReq);

    }
}
