package com.parkking.zujal.parkking;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

public class countdownTimer extends Service {

    Context context;
    CountDownTimer timer;
    String ReceiptNo;
    String spot_id;

    public countdownTimer(Context ctx, String receiptid, String spotid){
        context = ctx;
        ReceiptNo = receiptid;
        spot_id = spotid;

        SharedPreferences sharedPreferences = context.getSharedPreferences("count", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit= sharedPreferences.edit();
        edit.putBoolean("stop", false);
        edit.apply();
    }

    public void startTimer(){
      timer =  new CountDownTimer(600000, 1000) {

            public void onTick(long millisUntilFinished) {

                SharedPreferences sharedPreferences = context.getSharedPreferences("count", Context.MODE_PRIVATE);
                if (sharedPreferences.getBoolean("stop", true)) {
                    timer.cancel();
                    //Toast.makeText(context,"Timer cancelled", Toast.LENGTH_SHORT).show();


                } else {
                    Log.d("hehe", millisUntilFinished / 1000 + "Timer starts");
                }


            }

            public void onFinish() {
                //Toast.makeText(context, "Finish", Toast.LENGTH_SHORT).show();

                String type = "reservationcompletion";
                BackgroundWorker backgroundWorker = new BackgroundWorker(context);
                backgroundWorker.execute(type, ReceiptNo, spot_id);

            }
        }.start();

    }

    @Override
    public void onDestroy() {

        timer.cancel();
        Log.i("el", "Timer cancelled");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
