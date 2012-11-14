package ru.spbau.WhereIsMyMoney.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.telephony.SmsMessage;
import android.util.Log;
import ru.spbau.WhereIsMyMoney.SmsEvent;

public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = SmsReceiver.class.getCanonicalName();

    public SmsReceiver() {
        super();
        Log.d(TAG, "SmsReceiver()");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");
        Intent serviceIntent = new Intent(context, WatcherService.class);
        IBinder binder = peekService(context, serviceIntent);
        String message = buildMessage(intent);
        if (binder == null) {
            Log.d(TAG, "WatcherService isn't running. Trying to start.");
            serviceIntent.putExtra(WatcherService.SMS_EVENT, message);
            if (context.startService(serviceIntent) == null) {
                Log.d(TAG, "Can't start service");
            } else {
                Log.d(TAG, "Service started and successfully receive message: " + message);
            }
        } else {
            sendMessage(message, binder);
            Log.d(TAG, "Sending message to running service: " + message);
        }
    }

    private static void sendMessage(String msg, IBinder binder) {
        Messenger messenger = new Messenger(binder);
        Message message = Message.obtain(null, WatcherService.MSG_SMS_RECEIVED, 0, 0);
        Bundle bundle = new Bundle();
        bundle.putString(null, msg);
        message.setData(bundle);
        try {
            messenger.send(message);
            Log.d(TAG, "Message sent successfully.");
        } catch (RemoteException e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
    }

    /**
     * Message that will be sent to server when incoming sms received
     *
     *
     * @param intent  Intent
     * @return Message
     */
    String buildMessage(Intent intent) {
        Bundle pdusBundle = intent.getExtras();
        Object[] pdus = (Object[]) pdusBundle.get("pdus");
        SmsMessage message = SmsMessage.createFromPdu((byte[]) pdus[0]);

        SmsEvent smsEvent = new SmsEvent(message.getOriginatingAddress(), message.getDisplayMessageBody(), message.getTimestampMillis());
        return smsEvent.toString();
    }
}
