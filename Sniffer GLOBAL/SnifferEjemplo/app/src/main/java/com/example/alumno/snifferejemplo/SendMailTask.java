package com.example.alumno.snifferejemplo;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

/**
 * Created by Dany on 16/06/2016.
 */
public class SendMailTask extends AsyncTask {

    public SendMailTask() {

    }


    @Override
    protected Object doInBackground(Object... args) {
        try {
            Log.i("SendMailTask", "Abriendo GMail...");
            //  publishProgress("Processing input....");
            GMail androidEmail = new GMail(args[0].toString(),
                    args[1].toString(), (List) args[2], args[3].toString(),
                    args[4].toString());
            //    publishProgress("Preparing mail message....");
            androidEmail.createEmailMessage();
            //    publishProgress("Sending email....");
            androidEmail.sendEmail();
            //    publishProgress("Email Sent.");
            Log.i("SendMailTask", "Mail Enviado.");
        } catch (Exception e) {
            //    publishProgress(e.getMessage());
            Log.e("SendMailTask", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void onProgressUpdate(Object... values) {
    }

    @Override
    public void onPostExecute(Object result) {

    }
}
