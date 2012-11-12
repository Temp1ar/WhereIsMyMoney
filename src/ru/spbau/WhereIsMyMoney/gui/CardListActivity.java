package ru.spbau.WhereIsMyMoney.gui;

import android.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import ru.spbau.WhereIsMyMoney.Transaction;
import ru.spbau.WhereIsMyMoney.storage.TransactionLogSource;

import java.util.Date;

/**
 * Shows list of cards.
 */
public class CardListActivity extends Activity {
    final String[] cards = new String[] { "Raiffeisen", "Sberbank", "BaltBank", "Alfabank"};
    final static String id_param = "id";

    TransactionLogSource db;

    private void createCardsListView() {
        ListView listView = (ListView) findViewById(ru.spbau.WhereIsMyMoney.R.id.cards);
        final String[] cards = db.getCards().toArray(new String[db.getCards().size()]);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.simple_list_item_1, R.id.text1, cards);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(CardListActivity.this, TransactionsListActivity.class);
                String message = cards[position];
                intent.putExtra(id_param, message);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new TransactionLogSource(getApplicationContext());
        db.open(TransactionLogSource.FOR_WRITE);
        db.addTransaction(new Transaction(new Date(), "Sber", "*2111", "2000RUR", 1000));
        db.addTransaction(new Transaction(new Date(),"Balt", "*3141", "1000RUR", 1000));
        setContentView(ru.spbau.WhereIsMyMoney.R.layout.cards);
        createCardsListView();
    }

    @Override
    protected void onStop() {
        super.onStop();
        db.close();
    }
}
