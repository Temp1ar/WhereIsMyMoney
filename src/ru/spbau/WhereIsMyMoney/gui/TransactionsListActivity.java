package ru.spbau.WhereIsMyMoney.gui;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.*;
import ru.spbau.WhereIsMyMoney.R;
import ru.spbau.WhereIsMyMoney.Transaction;
import ru.spbau.WhereIsMyMoney.storage.TransactionLogHelper;
import ru.spbau.WhereIsMyMoney.storage.TransactionLogSource;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Shows list of transactions from specified card.
 */
public class TransactionsListActivity extends Activity {
    private static final String TAG = TransactionsListActivity.class.getCanonicalName();

    final static String ID_PARAM = "id";
    private final static String PLACE = "place";

    private TransactionLogSource db;
    //todo drop this from class field
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

        setContentView(ru.spbau.WhereIsMyMoney.R.layout.transactions);
        db = new TransactionLogSource(getApplicationContext());
        db.open();

        List<Transaction> transactions;

        cardId = intent.getStringExtra(ID_PARAM);

        if (cardId != null) {
            TextView title = (TextView) findViewById(R.id.header);
            if (cardId.equals(TransactionLogHelper.CASH)) {
                title.setText(getString(R.string.transaction_list_header_for_cash));
            } else {
                title.setText(getString(R.string.transaction_list_header) + " *" + cardId.substring(cardId.length() - 4, cardId.length()));
            }

            if (startTime == -1 || endTime == -1) {
                transactions = db.getTransactionsPerCard(cardId);
            } else {
                transactions = db.getTransactionsPerCardForPeriod(cardId, new Date(startTime), new Date(endTime));
            }

            Button addTransaction = (Button)findViewById(R.id.add_tr);
            if (cardId.equals(TransactionLogHelper.CASH)) {
                addTransaction.setVisibility(View.VISIBLE);
            } else {
                addTransaction.setVisibility(View.GONE);
            }
        } else {
            String place = intent.getStringExtra(PLACE);

            if (place == null)
                return;

            TextView title = (TextView) findViewById(R.id.header);
            title.setText(getString(R.string.transaction_list_header) + " place");

            if (startTime == -1 || endTime == -1) {
                transactions = db.getTransactionsPerPlace(place);
            } else {
                transactions = db.getTransactionsPerPlaceForPeriod(place, new Date(startTime), new Date(endTime));
            }
        }

        ArrayAdapter<Transaction> adapter = new TransactionListAdapter(getApplicationContext(), transactions);

        ListView listView = (ListView) findViewById(ru.spbau.WhereIsMyMoney.R.id.transactions);
        listView.setAdapter(adapter);

        ImageView chartView = (ImageView) findViewById(R.id.chart_view);
        chartView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BalanceChartBuilder builder = new BalanceChartBuilder();
                List<Transaction> transactions = db.getTransactionsPerCard(cardId);
                Collections.reverse(transactions);
                startActivity(builder.getIntent(TransactionsListActivity.this, transactions));
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        db.close();
    }
}