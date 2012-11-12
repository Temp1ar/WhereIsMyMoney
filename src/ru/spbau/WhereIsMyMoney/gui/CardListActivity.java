package ru.spbau.WhereIsMyMoney.gui;

import android.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import ru.spbau.WhereIsMyMoney.storage.TransactionLogSource;

/**
 * Shows list of cards.
 */
public class CardListActivity extends Activity {
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
        db.open();
        setContentView(ru.spbau.WhereIsMyMoney.R.layout.cards);
        createCardsListView();
    }

    @Override
    protected void onStop() {
        super.onStop();
        db.close();
    }
}
