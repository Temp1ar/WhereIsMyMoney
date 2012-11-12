package ru.spbau.WhereIsMyMoney.gui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Shows list of transactions from specified card.
 */
public class TransactionsListActivity extends Activity {
    final String[] values = new String[] { "12", "13", "14", "15"};
    final String OK = "Ok";

    private void createTransactionsDialog(String title, String message) {
        AlertDialog.Builder ad = new AlertDialog.Builder(TransactionsListActivity.this);
        ad.setTitle(title);
        ad.setMessage(message);
        ad.setCancelable(true);
        ad.setPositiveButton(OK, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
            }
        });
        ad.show();
    }

    private void createListView() {
        ListView listView = (ListView) findViewById(ru.spbau.WhereIsMyMoney.R.id.transactions);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                createTransactionsDialog(values[position], "Message " + values[position]);
            }
        });
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String message = intent.getStringExtra(CardListActivity.id_param);
        setContentView(ru.spbau.WhereIsMyMoney.R.layout.transactions);
        TextView card = (TextView) findViewById(ru.spbau.WhereIsMyMoney.R.id.card_id);
        card.setText(message);
        createListView();
    }

}