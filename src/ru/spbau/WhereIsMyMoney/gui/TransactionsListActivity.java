package ru.spbau.WhereIsMyMoney.gui;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import ru.spbau.WhereIsMyMoney.R;
import ru.spbau.WhereIsMyMoney.Transaction;
import ru.spbau.WhereIsMyMoney.storage.TransactionLogSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Shows list of transactions from specified card.
 */
public class TransactionsListActivity extends Activity {
    private static final String TAG = TransactionsListActivity.class.getCanonicalName();

    final static String ID_PARAM = "id";
    final static String PLACE = "place";

    private TransactionLogSource db;
    //todo drop this from class field
    private String cardId;
    private List<Transaction> allTransactions;

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

        cardId = intent.getStringExtra(ID_PARAM);

        if (cardId != null) {
            TextView title = (TextView) findViewById(R.id.header);
            title.setText(getString(R.string.transaction_list_header) + " *" + cardId.substring(cardId.length() - 4, cardId.length()));

            if (startTime == -1 || endTime == -1) {
                allTransactions = db.getTransactionsPerCard(cardId);
            } else {
                allTransactions = db.getTransactionsPerCardForPeriod(cardId, new Date(startTime), new Date(endTime));
            }
        } else {
            String place = intent.getStringExtra(PLACE);

            if (place == null)
                return;

            if (place.equals(getString(R.string.unknown_place)))
                place = "";

            TextView title = (TextView) findViewById(R.id.header);
            title.setText(getString(R.string.transaction_list_header) + " " + getString(R.string.transactions_for_place));

            if (startTime == -1 || endTime == -1) {
                allTransactions = db.getTransactionsPerPlace(place);
            } else {
                allTransactions = db.getTransactionsPerPlaceForPeriod(place, new Date(startTime), new Date(endTime));
            }
        }

        showTransactions(allTransactions);

        ImageView chartView = (ImageView) findViewById(R.id.chart_view);
        chartView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BalanceChartBuilder builder = new BalanceChartBuilder();
                Collections.reverse(allTransactions);
                startActivity(builder.getIntent(TransactionsListActivity.this, allTransactions));
            }
        });
    }

    private List<Transaction> getTransactions(int type) {
        List<Transaction> transactions = new ArrayList<Transaction>();

        for (Transaction transaction : allTransactions) {
            if (transaction.getType() == type) {
                transactions.add(transaction);
            }
        }

        return transactions;
    }

    void showTransactions(List<Transaction> transactions) {
        ArrayAdapter<Transaction> adapter = new TransactionListAdapter(getApplicationContext(), transactions);
        ListView transactionsListView = (ListView) findViewById(R.id.transactions);
        transactionsListView.setAdapter(adapter);
    }

    protected void onStop() {
        super.onStop();
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.transactions_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.transactions_withdraw:
                showTransactions(getTransactions(Transaction.WITHDRAW));
                return true;
            case R.id.transactions_deposit:
                showTransactions(getTransactions(Transaction.DEPOSIT));
                return true;
            case R.id.transactions_all:
                showTransactions(allTransactions);
                return true;
        }

        return (super.onOptionsItemSelected(item));
    }
}