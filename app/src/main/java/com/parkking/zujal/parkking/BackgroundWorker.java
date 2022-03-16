package com.parkking.zujal.parkking;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import static com.parkking.zujal.parkking.clientCheck.PREFS_NAME;

/**
 * Created by zujal on 16/03/2018.
 */

public class BackgroundWorker extends AsyncTask<String,Void,String> {
    Context context;
    AlertDialog alertDialog;
    Toast toast;
    private Session session;
    String user_name;
    String username;
    String password;
    String name ;
    String receipt;
    String price;
    SharedPreferences share;
    SharedPreferences.Editor editor;
    String clientName;
    String keyname="name";


    public BackgroundWorker(Context ctx) {
        context = ctx;
    }
    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
       // String login_url = "http://192.168.1.6/login.php";
        String login_url = "http://parkkinz.com/androidphpfiles/login.php";
        String register_url = "http://parkkinz.com/androidphpfiles/Register.php";
        String reserve_url = "http://parkkinz.com/androidphpfiles/reserve.php";
        String reservationpage_url = "http://parkkinz.com/androidphpfiles/reservationpage.php";
        String cancelreservation_url = "http://parkkinz.com/androidphpfiles/cancelreservation.php";
        String findspot_url = "http://parkkinz.com/androidphpfiles/findspot.php";
        String ownerspot_url = "http://parkkinz.com/androidphpfiles/ownerspot.php";
        String deleteres_url = "http://parkkinz.com/androidphpfiles/deletereservation.php";
        String cancelspot_url = "http://parkkinz.com/androidphpfiles/cancelspot.php";
        String openspot_url = "http://parkkinz.com/androidphpfiles/openspot.php";
        String getuserdetails_url = "http://parkkinz.com/androidphpfiles/getuserdetails.php";
        String reserveclient_url = "http://parkkinz.com/androidphpfiles/reserve_client.php";
        String registerclient_url = "http://parkkinz.com/androidphpfiles/register_client.php";


        session = new Session(context);

        if(type.equals("login")) {
            try {
                user_name = params[1];
                password = params[2];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);


                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"
                        +URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();


                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while((line = bufferedReader.readLine())!= null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();


                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        if(type.equals("register"))
        {

            try {
                String name = params[1];
                String password = params[2];
                String phone = params[3];
                String email = params[4];
                URL url = new URL(register_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);


                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&"
                        +URLEncoder.encode("user_pass","UTF-8")+"="+URLEncoder.encode(password,"UTF-8")+"&"
                        +URLEncoder.encode("user_phone","UTF-8")+"="+URLEncoder.encode(phone,"UTF-8")+"&"
                        +URLEncoder.encode("user_email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();


                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while((line = bufferedReader.readLine())!= null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();


                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        if(type.equals("registerclient"))
        {

            try {
                clientName = params[1];
                String password = params[2];
                String phone = params[3];
                String email = params[4];
                URL url = new URL(registerclient_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);


                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(clientName,"UTF-8")+"&"
                        +URLEncoder.encode("user_pass","UTF-8")+"="+URLEncoder.encode(password,"UTF-8")+"&"
                        +URLEncoder.encode("user_phone","UTF-8")+"="+URLEncoder.encode(phone,"UTF-8")+"&"
                        +URLEncoder.encode("user_email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();


                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while((line = bufferedReader.readLine())!= null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();


                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        if(type.equals("reserveclient"))
        {

            try {
                username = params[1];
                receipt = params[2];
                price= params[3];
                String resstate= params[4];
                String spotid= params[5];
                String spotname= params[6];
                String spotaddress= params[7];
                String starttime=params[8];
                String endtime=params[9];
                URL url = new URL(reserveclient_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);


                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"
                        +URLEncoder.encode("user_receiptno","UTF-8")+"="+URLEncoder.encode(receipt,"UTF-8")+"&"
                        +URLEncoder.encode("user_price","UTF-8")+"="+URLEncoder.encode(price,"UTF-8")+"&"
                        +URLEncoder.encode("reservation_state","UTF-8")+"="+URLEncoder.encode(resstate,"UTF-8")+"&"
                        +URLEncoder.encode("spot_id","UTF-8")+"="+URLEncoder.encode(spotid,"UTF-8")+"&"
                        +URLEncoder.encode("spot_name","UTF-8")+"="+URLEncoder.encode(spotname,"UTF-8")+"&"
                        +URLEncoder.encode("spot_address","UTF-8")+"="+URLEncoder.encode(spotaddress,"UTF-8")+"&"
                        +URLEncoder.encode("spot_starttime","UTF-8")+"="+URLEncoder.encode(starttime,"UTF-8")+"&"
                        +URLEncoder.encode("spot_endtime","UTF-8")+"="+URLEncoder.encode(endtime,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();


                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while((line = bufferedReader.readLine())!= null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();


                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        if(type.equals("find_spot")) {
            try {
                String latt = params[1];
                String longg = params[2];
                URL url = new URL(findspot_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);


                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data =URLEncoder.encode("spot_lat","UTF-8")+"="+URLEncoder.encode(latt,"UTF-8")+"&"
                        +URLEncoder.encode("spot_long","UTF-8")+"="+URLEncoder.encode(longg,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();


                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while((line = bufferedReader.readLine())!= null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();


                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(type.equals("ownerpage")) {

            try {
                String username = params[1];
                URL url = new URL(ownerspot_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);


                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_name", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();


                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();


                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(type.equals("reservationpage"))
        {

            try {
                String username = params[1];
                URL url = new URL(reservationpage_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);


                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(username,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();


                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while((line = bufferedReader.readLine())!= null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();


                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        if(type.equals("cancelres"))
        {

            try {
                String resid = params[1];
                String spottid = params[2];
                String pricepaid = params[3];
                URL url = new URL(cancelreservation_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);


                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("reservation_id","UTF-8")+"="+URLEncoder.encode(resid,"UTF-8")
                        +"&"
                        +URLEncoder.encode("spot_id","UTF-8")+"="+URLEncoder.encode(spottid,"UTF-8")
                        +"&"
                        +URLEncoder.encode("price_paid","UTF-8")+"="+URLEncoder.encode(pricepaid,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();


                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while((line = bufferedReader.readLine())!= null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();


                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        if(type.equals("deleteres"))
        {

            try {
                String resid = params[1];
                URL url = new URL(deleteres_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);


                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("reservation_id","UTF-8")+"="+URLEncoder.encode(resid,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();


                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while((line = bufferedReader.readLine())!= null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();


                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        if(type.equals("getuserdetails"))
        {

            try {
                String username = params[1];
                URL url = new URL(getuserdetails_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);


                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(username,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();


                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while((line = bufferedReader.readLine())!= null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();


                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        if(type.equals("spotcancel"))
        {

            try {
                String spotid = params[1];
                URL url = new URL(cancelspot_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);


                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("spot_id","UTF-8")+"="+URLEncoder.encode(spotid,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();


                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while((line = bufferedReader.readLine())!= null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();


                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        if(type.equals("spotopen"))
        {

            try {
                String spotid = params[1];
                URL url = new URL(openspot_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);


                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("spot_id","UTF-8")+"="+URLEncoder.encode(spotid,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();


                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while((line = bufferedReader.readLine())!= null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();


                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }



        if(type.equals("reservation"))
        {

            try {
                username = params[1];
                receipt = params[2];
                price= params[3];
                String resstate= params[4];
                String spotid= params[5];
                String spotname= params[6];
                String spotaddress= params[7];
                String starttime=params[8];
                String endtime=params[9];
                URL url = new URL(reserve_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);


                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"
                        +URLEncoder.encode("user_receiptno","UTF-8")+"="+URLEncoder.encode(receipt,"UTF-8")+"&"
                        +URLEncoder.encode("user_price","UTF-8")+"="+URLEncoder.encode(price,"UTF-8")+"&"
                        +URLEncoder.encode("reservation_state","UTF-8")+"="+URLEncoder.encode(resstate,"UTF-8")+"&"
                        +URLEncoder.encode("spot_id","UTF-8")+"="+URLEncoder.encode(spotid,"UTF-8")+"&"
                        +URLEncoder.encode("spot_name","UTF-8")+"="+URLEncoder.encode(spotname,"UTF-8")+"&"
                        +URLEncoder.encode("spot_address","UTF-8")+"="+URLEncoder.encode(spotaddress,"UTF-8")+"&"
                        +URLEncoder.encode("spot_starttime","UTF-8")+"="+URLEncoder.encode(starttime,"UTF-8")+"&"
                        +URLEncoder.encode("spot_endtime","UTF-8")+"="+URLEncoder.encode(endtime,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();


                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while((line = bufferedReader.readLine())!= null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();


                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
       // alertDialog = new AlertDialog.Builder(context).create();
      //  alertDialog.setTitle("Login Status");
    }

    @Override
    protected void onPostExecute(String result) {
     //   alertDialog = new AlertDialog.Builder(context).create();
      //  alertDialog.setMessage(result);
      //  alertDialog.show();


        Log.d("BGWORKER", result);
        toast = Toast.makeText(context,result, Toast.LENGTH_LONG);
        //toast.setGravity(Gravity.TOP|Gravity.LEFT, 15, 20);
        toast.show();


        if(result.equals("Logged in as admin")){
            session.setLoggedin(true);
            session.createLoginSession(user_name,password);
            Intent i = new Intent(context,adminpage.class);
            context.startActivity(i);
            ((Activity)context).finish();
        }
        else if(result.equals("Logged in as wilson")){
            session.setLoggedin(true);
            session.createLoginSession(user_name,password);
            Intent i = new Intent(context,Owner.class);
            context.startActivity(i);
            ((Activity)context).finish();
        }
        else if(result.equals("Logged in as "+ user_name)){
            session.setLoggedin(true);
            session.createLoginSession(user_name,password);
            Intent i = new Intent(context,Search.class);
            context.startActivity(i);
            ((Activity)context).finish();

        }

        if(result.equals("You have been successfully registered")){
            Intent i = new Intent(context,LogIn.class);
            context.startActivity(i);
            ((Activity)context).finish();
        }

        if(result.equals("Your reservation has been made")){
            Intent intent= new Intent("com.parkking.zujal.parkking.Reservation");

            context. startActivity(intent);
            ((Activity)context).finish();
        }

        if(result.equals("Client's reservation is successful")){
            Intent intent = new Intent("com.parkking.zujal.parkking.adminpage");
            context.startActivity(intent);
            ((Activity)context).finish();
        }

        if(result.equals("Client is successfully registered")){

            share =context.getSharedPreferences(PREFS_NAME,0);
            editor=share.edit();
            editor.putString(keyname,clientName);
            editor.apply();

            Intent intent = new Intent("com.parkking.zujal.parkking.Search");
            context.startActivity(intent);
            ((Activity)context).finish();
        }


    }



    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}