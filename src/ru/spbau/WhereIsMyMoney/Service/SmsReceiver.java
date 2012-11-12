package ru.spbau.WhereIsMyMoney.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.telephony.SmsMessage;
import android.util.Log;
import ru.spbau.WhereIsMyMoney.SmsEvent;

public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = "WhereIsMyMoney.Service.SmsReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");
        Intent service = new Intent(context, WatcherService.class);
        IBinder binder = peekService(context, service);
        if (binder == null) {
            Log.d(TAG, "WatcherService isn't running. Trying to start.");
            service.setAction(WatcherService.class.getName());
            if (context.startService(service) == null) {
                Log.d(TAG, "Can't start service");
                return;
            }
            binder = peekService(context, service);
            if (binder == null) {
                Log.d(TAG, "Can't bind to started service");
                return;
            }
        }
        String message = buildMessage(context, intent);
        sendMessage(message, binder);
        Log.d(TAG, "Sending message to service: " + message);
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
     * @param context Context
     * @param intent  Intent
     * @return Message
     */
    protected String buildMessage(Context context, Intent intent) {
        Bundle pudsBundle = intent.getExtras();
        Object[] pdus = (Object[]) pudsBundle.get("pdus");
        SmsMessage message = SmsMessage.createFromPdu((byte[]) pdus[0]);
        SmsEvent smsEvent = new SmsEvent(message.getOriginatingAddress(), message.getDisplayMessageBody());
        return smsEvent.toString();
    }
}
