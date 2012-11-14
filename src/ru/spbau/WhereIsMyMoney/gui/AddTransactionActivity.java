package ru.spbau.WhereIsMyMoney.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import ru.spbau.WhereIsMyMoney.R;
import ru.spbau.WhereIsMyMoney.Transaction;
import ru.spbau.WhereIsMyMoney.storage.TransactionLogSource;

import java.util.Date;
import java.util.List;

public class AddTransactionActivity extends Activity {
    private String cardId;

    private TransactionLogSource db;

    public void saveTransaction(View view) {
        String delta = ((EditText)findViewById(R.id.delta)).getText().toString();
        String place = ((EditText)findViewById(R.id.place)).getText().toString();

        final List<Transaction> transactions = db.getTransactionsPerCard(cardId);

        int type = Transaction.DEPOSIT;
        if (delta.charAt(0) == '-') {
            type = Transaction.WITHDRAW;
        }

        float balance = transactions.get(0).getBalance();

        try {
            balance += Float.parseFloat(delta);
        } catch (NumberFormatException e) {
            Log.w(getClass().getCanonicalName(), "Can't parse " + delta + " as float");
            Toast.makeText(getApplicationContext(), "Can't parse " + delta + " as float", Toast.LENGTH_SHORT).show();
            return;
        }
        db.addTransaction(new Transaction(new Date(), place, cardId, delta,
                balance,
                type));
        finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new TransactionLogSource(getApplicationContext());
        db.open();
        Intent intent = getIntent();
        cardId = intent.getStringExtra(TransactionsListActivity.ID_PARAM);
        setContentView(R.layout.add_transaction);
    }

    @Override
    protected void onStop() {
        super.onStop();
        db.close();
    }
}
