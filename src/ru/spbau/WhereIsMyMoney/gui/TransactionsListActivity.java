package ru.spbau.WhereIsMyMoney.gui;

import java.util.List;

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
    final static String ID_PARAM = "id";
    final static  String OK = "Ok";
    final static String TRANSACTION = "Transaction description";

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
        cardId = intent.getStringExtra(ID_PARAM);
        setContentView(ru.spbau.WhereIsMyMoney.R.layout.transactions);
        db = new TransactionLogSource(getApplicationContext());
        db.open();
        TextView title = (TextView) findViewById(ru.spbau.WhereIsMyMoney.R.id.card_id);
        title.setText(cardId);
        ListView listView = (ListView) findViewById(ru.spbau.WhereIsMyMoney.R.id.transactions);
        List<Transaction> transactions = db.getTransactionsPerCard(cardId);
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