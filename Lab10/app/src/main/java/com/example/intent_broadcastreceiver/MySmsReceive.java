package com.example.intent_broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle; // <--- Add this import statement
import android.telephony.SmsMessage; // <--- Also add this for SmsMessage
import android.widget.Toast; // <--- And this for Toast

public class MySmsReceive extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        processReceive(context,intent);
    }
    public void processReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        String message="";
        String body ="";
        String address="";
        if (extras != null)
        {
            Object[] smsExtra = (Object[])extras.get("pdus"); // Changed variable name to follow Java conventions
            for (int i =0; i < smsExtra.length; i++) // Changed variable name
            {
                SmsMessage sms = SmsMessage.createFromPdu((byte[])smsExtra[i]); // Changed variable name
                body = sms.getMessageBody();
                address = sms.getOriginatingAddress();
                message +="Có 1 tin nhắn từ "+address+"\n"+body+" vừa gởi đến";
            }
            //Hiển thị
            Toast.makeText(context,message,Toast.LENGTH_LONG).show();
        }
    }
}