package com.example.calllogapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=findViewById(R.id.textview);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CALL_LOG)!=
                PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_CALL_LOG},1);
            Toast.makeText(this, "here", Toast.LENGTH_SHORT).show();

        }
        else {
            textView.setText(getCallDetails());

        }

    }
    private String getCallDetails() {
        StringBuffer sb = new StringBuffer();
        Cursor managedCursor = getContentResolver().query(CallLog.Calls.CONTENT_URI,
                null, null, null, null);

        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);

        sb.append("Call Details:\n\n");
        while (managedCursor.moveToNext()) {
            String cnumber = managedCursor.getString(number);
            String ctype = managedCursor.getString(type);
            String cdate = managedCursor.getString(date);
//            taking date and convert it to string and a formatt
            Date calldate = new Date(Long.valueOf(cdate));
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy HH-mm");
            String datestring = formatter.format(calldate);
            String cduration = managedCursor.getString(duration);
            String dir = null;
            int dircode = Integer.parseInt(ctype);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING Call";
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING Call";
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED Call";
                    break;
            }
            sb.append("\nPhone Number:" + cnumber + "\nCall Type:" + dir + "\nCall Date:" + datestring + "\nCall Duration:" + cduration);
            sb.append("\n__________________________________");
        }
        managedCursor.close();
        return sb.toString();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                textView.setText(getCallDetails());

            }

        }
    }
}