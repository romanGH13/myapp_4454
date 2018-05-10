package com.example.eqvol.eqvola.Classes;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

import com.example.eqvol.eqvola.RegistrationActivity;

/**
 * Created by eqvol on 16.01.2018.
 */

public class SmsHandler extends BroadcastReceiver {
    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (intent != null && intent.getAction() != null &&
                    ACTION.compareToIgnoreCase(intent.getAction()) == 0) {

                Object[] pduArray = (Object[]) intent.getExtras().get("pdus");
                SmsMessage[] messages = new SmsMessage[pduArray.length];
                for (int i = 0; i < pduArray.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pduArray[i]);
                }

                String sms_from = messages[0].getDisplayOriginatingAddress();
                if (sms_from.equalsIgnoreCase("free.sms.ks")) {
                    StringBuilder bodyText = new StringBuilder();
                    for (int i = 0; i < messages.length; i++) {
                        bodyText.append(messages[i].getMessageBody());
                    }
                    String body = bodyText.toString();

                    String arr[] = body.split(" ");
                    String code = arr[arr.length - 1].replace(".", "");

                    if(((Activity)context).getClass() == RegistrationActivity.class) {
                        ((RegistrationActivity) context).setCodeFromSms(code);
                    }
                }
            }
        }
        catch (Exception ex)
        {

        }
    }
}