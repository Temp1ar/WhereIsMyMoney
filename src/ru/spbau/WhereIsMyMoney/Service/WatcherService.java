package ru.spbau.WhereIsMyMoney.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

public class WatcherService extends Service {
    private static final String TAG = WatcherService.class.getCanonicalName();

    public static final int MSG_SMS_RECEIVED = 1;
    public static final String SMS_EVENT = "SmsEvent";

    void processMessage(String message) {
        Log.d(TAG, "processMessage: " + message);
    }

    private class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SMS_RECEIVED:
                    Log.d(TAG, "MSG_SMS_RECEIVED received");
                    processMessage(msg.getData().getString(null));
                    break;
                default:
                    Log.d(TAG, "Unknown type of message");
                    super.handleMessage(msg);
            }
        }
    }

    private final Messenger mMessenger = new Messenger(new IncomingHandler());

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "Service binded");
        return mMessenger.getBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        processMessage(intent.getStringExtra(SMS_EVENT));
        return START_STICKY;
    }
}
