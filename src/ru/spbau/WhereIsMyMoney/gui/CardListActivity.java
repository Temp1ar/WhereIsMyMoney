package ru.spbau.WhereIsMyMoney.gui;

import java.util.ArrayList;
import java.util.List;

import ru.spbau.WhereIsMyMoney.Card;
import ru.spbau.WhereIsMyMoney.Transaction;
import ru.spbau.WhereIsMyMoney.storage.TransactionLogSource;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Shows list of cards.
 */
public class CardListActivity extends Activity {
    private TransactionLogSource db;

    private void createCardsListView() {
        ListView listView = (ListView) findViewById(ru.spbau.WhereIsMyMoney.R.id.cards);
        ImageView makeReport = (ImageView) findViewById(ru.spbau.WhereIsMyMoney.R.id.makeReport);

        makeReport.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(CardListActivity.this, CostsReportSetupActivity.class);
                startActivity(intent);
            }
        });

        final String[] card_ids = db.getCards().toArray(new String[db.getCards().size()]);
        final ArrayList<Card> cards = new ArrayList<Card>();
        for (String card_id : card_ids) {
            List<Transaction> transactions = db.getTransactionsPerCard(card_id);
            Float balance = (transactions.size() > 0) ? db.getTransactionsPerCard(card_id).get(0).getBalance() : 0f;
            cards.add(new Card(card_id, balance));
        }

        ArrayAdapter<Card> adapter = new CardListAdapter(this, cards);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(CardListActivity.this, TransactionsListActivity.class);
                String message = card_ids[position];
                intent.putExtra(TransactionsListActivity.ID_PARAM, message);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new TransactionLogSource(getApplicationContext());
        setContentView(ru.spbau.WhereIsMyMoney.R.layout.cards);
    }

    @Override
    protected void onResume() {
        super.onResume();
        db.open();
        createCardsListView();
    }

    @Override
    protected void onStop() {
        super.onStop();
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(ru.spbau.WhereIsMyMoney.R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case ru.spbau.WhereIsMyMoney.R.id.add_parser:
                Intent intent = new Intent(this, SmsViewActivity.class);
                startActivity(intent);
                return true;
            case ru.spbau.WhereIsMyMoney.R.id.refresh_history:
            	TransactionLogSource source = new TransactionLogSource(getApplicationContext());
            	source.open();
            	source.resetDatabase();
            	source.close();
            	return true;
        }

        return (super.onOptionsItemSelected(item));
    }

}
