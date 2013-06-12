package ru.spbau.WhereIsMyMoney2.gui;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import ru.spbau.WhereIsMyMoney2.ExistingSmsReader;
import ru.spbau.WhereIsMyMoney2.R;
import ru.spbau.WhereIsMyMoney2.SmsEvent;
import ru.spbau.WhereIsMyMoney2.Transaction;
import ru.spbau.WhereIsMyMoney2.parser.SmsParser;
import ru.spbau.WhereIsMyMoney2.storage.TransactionLogSource;
import ru.spbau.WhereIsMyMoney2.utils.EventHandler;

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
    private EventHandler finishEventHandler;
    private Dialog dialog;
    private ProgressBar progressBar;
    private TextView progressBarText;


    public NewSmsProcessingAsyncTask(Context context, TransactionLogSource db, EventHandler finishEventHandler) {
        this.context = context;
        this.db = db;
        this.finishEventHandler = finishEventHandler;
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
            publishProgress((int)((double)i / smsEvents.size() * 100));
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

        progressBar = (ProgressBar) dialog.findViewById(R.id.smsProcessingProgressBar);
        progressBarText = (TextView) dialog.findViewById(R.id.smsProcessingProgressBarText);

        dialog.show();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        db.close();
        dialog.dismiss();

        if (finishEventHandler != null)
            finishEventHandler.trigger();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progressBar.setProgress(values[0]);
        progressBarText.setText(values[0] + " %");
    }
}
