package ru.spbau.WhereIsMyMoney.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

public class WatcherService extends Service {
    private static final String TAG = Service.class.getName();

    public static final int MSG_SMS_RECEIVED = 1;

    /**
     * Handles incoming Intents (Messages)
     */
    private class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SMS_RECEIVED:
                    Log.d(TAG, "MSG_SMS_RECEIVED received");
                    String line = msg.getData().getString(null);
                    Log.d(TAG, "Received message: " + line);
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
}
