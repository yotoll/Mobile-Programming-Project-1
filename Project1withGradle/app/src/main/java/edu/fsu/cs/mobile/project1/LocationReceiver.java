package edu.fsu.cs.mobile.project1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class LocationReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        
        if (intent.getAction().equals(SMS_RECEIVED)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[])bundle.get("pdus");
                final SmsMessage[] messages = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                }
                if (messages.length > -1) {

                    Intent inte = new Intent(context.getApplicationContext(), MainActivity.class);
                    inte.putExtra("msgContent", messages[0].getMessageBody());
                    inte.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // adding this flag starts the new Activity in a new Task
                    context.startActivity(inte);
                    Log.i(TAG, "Message received: " + messages[0].getMessageBody());

                }
            }
        }
    }
}
