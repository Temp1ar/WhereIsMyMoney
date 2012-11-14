package ru.spbau.WhereIsMyMoney.gui;

import java.util.Date;
import java.util.List;

import android.util.Log;
import ru.spbau.WhereIsMyMoney.R;
import ru.spbau.WhereIsMyMoney.Transaction;
import ru.spbau.WhereIsMyMoney.storage.TransactionLogHelper;
import ru.spbau.WhereIsMyMoney.storage.TransactionLogSource;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Shows list of transactions from specified card.
 */
public class TransactionsListActivity extends Activity {
    private static final String TAG = TransactionsListActivity.class.getCanonicalName();

    final static String ID_PARAM = "id";

    TransactionLogSource db;
    private String cardId;

    public void saveTransaction(View view) {
        Intent intent = new Intent(this, AddTransactionActivity.class);
        intent.putExtra(TransactionsListActivity.ID_PARAM, cardId);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();

        long startTime = this.getIntent().getLongExtra(AbstractCostsReportActivity.START_DATE, -1);
        long endTime = this.getIntent().getLongExtra(AbstractCostsReportActivity.END_DATE, -1);

        if (startTime == -1) {
            Log.d(TAG, AbstractCostsReportActivity.START_DATE + "parameter not found");
        }
        if (endTime == -1) {
            Log.d(TAG, AbstractCostsReportActivity.END_DATE + "parameter not found");
        }

        cardId = intent.getStringExtra(ID_PARAM);

        setContentView(ru.spbau.WhereIsMyMoney.R.layout.transactions);
        db = new TransactionLogSource(getApplicationContext());
        db.open();

        TextView title = (TextView) findViewById(ru.spbau.WhereIsMyMoney.R.id.card_id);
        title.setText(getString(R.string.transaction_list_header) + " " + cardId);
        ListView listView = (ListView) findViewById(ru.spbau.WhereIsMyMoney.R.id.transactions);
        List<Transaction> transactions;
        if (startTime == -1 || endTime == -1) {
            transactions = db.getTransactionsPerCard(cardId);
        } else {
            transactions = db.getTransactionsPerCardForPeriod(cardId, new Date(startTime), new Date(endTime));
        }

        ArrayAdapter<Transaction> adapter = new TransactionListAdapter(getApplicationContext(), transactions);
        listView.setAdapter(adapter);
        Button addTransaction = (Button)findViewById(R.id.add_tr);
        if (cardId.equals(TransactionLogHelper.CASH)) {
            addTransaction.setVisibility(View.VISIBLE);
        } else {
            addTransaction.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        db.close();
    }
}