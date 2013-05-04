package ru.spbau.WhereIsMyMoney.gui;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.Window;
import ru.spbau.WhereIsMyMoney.ExistingSmsReader;
import ru.spbau.WhereIsMyMoney.R;
import ru.spbau.WhereIsMyMoney.SmsEvent;
import ru.spbau.WhereIsMyMoney.Transaction;
import ru.spbau.WhereIsMyMoney.parser.SmsParser;
import ru.spbau.WhereIsMyMoney.storage.TransactionLogSource;
import ru.spbau.WhereIsMyMoney.utils.Event;

import java.util.Date;
import java.util.List;

/**
 * User: Alexander Opeykin (alexander.opeykin@gmail.com)
 * Date: 5/3/13
 * Time: 5:53 PM
 */
public class NewSmsProcessingAsyncTask extends AsyncTask<Void, Integer, Void> {

    private Context context;
    private TransactionLogSource db;
    private Event finishEvent;
    private Dialog dialog;


    public NewSmsProcessingAsyncTask(Context context, TransactionLogSource db, Event finishEvent) {
        this.context = context;
        this.db = db;
        this.finishEvent = finishEvent;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Date latestProcessedSmsDate = db.getLatestProcessedSmsDate();
        List<SmsEvent> smsEvents = ExistingSmsReader.getAfterDate(latestProcessedSmsDate, context);
        SmsParser smsParser = new SmsParser(context);

        for (int i = 0; i < smsEvents.size(); ++i) {
            Transaction transaction = smsParser.parseSms(smsEvents.get(i));
            if (transaction != null)
                db.addTransaction(transaction);
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        db.open();

        dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.sms_processing_dialog);
        dialog.show();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        db.close();
        dialog.dismiss();

        if (finishEvent != null)
            finishEvent.trigger();
    }
}
