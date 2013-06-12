package ru.spbau.WhereIsMyMoney2.gui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.*;
import android.widget.*;
import ru.spbau.WhereIsMyMoney2.Card;
import ru.spbau.WhereIsMyMoney2.R;
import ru.spbau.WhereIsMyMoney2.Transaction;
import ru.spbau.WhereIsMyMoney2.storage.CardNameSource;
import ru.spbau.WhereIsMyMoney2.storage.TemplatesSource;
import ru.spbau.WhereIsMyMoney2.storage.TransactionLogSource;
import ru.spbau.WhereIsMyMoney2.utils.EventHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Shows list of cards.
 */
public class CardListActivity extends Activity {
    private final int LIST_MENU_RENAME = 1;

    private TransactionLogSource transactionLogSource;
    private CardNameSource cardNameSource;
    private String[] cardIds; // ListView index to CardId

    private void createCardsListView() {
        transactionLogSource.open();

        ListView listView = (ListView) findViewById(ru.spbau.WhereIsMyMoney2.R.id.cards);
        ImageView makeReport = (ImageView) findViewById(ru.spbau.WhereIsMyMoney2.R.id.makeReport);

        makeReport.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(CardListActivity.this, CostsReportSetupActivity.class);
                startActivity(intent);
            }
        });

        cardIds = transactionLogSource.getCards().toArray(new String[transactionLogSource.getCards().size()]);
        final ArrayList<Card> cards = new ArrayList<Card>();
        for (String cardId : cardIds) {
            List<Transaction> transactions = transactionLogSource.getTransactionsPerCard(cardId);
            Float balance = (transactions.size() > 0)
                    ? transactionLogSource.getTransactionsPerCard(cardId).get(0).getBalance()
                    : 0f;
            String displayName = cardNameSource.getName(cardId);
            if (displayName == null) {
                cards.add(new Card(cardId, balance));
            } else {
                cards.add(new Card(cardId, displayName, balance));
            }
        }

        ArrayAdapter<Card> adapter = new CardListAdapter(this, cards);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(CardListActivity.this, TransactionsListActivity.class);
                String message = cardIds[position];
                intent.putExtra(TransactionsListActivity.ID_PARAM, message);
                startActivity(intent);
            }
        });

        registerForContextMenu(listView);
    }

    private class CreateCardsListViewEventHandler implements EventHandler {

        @Override
        public void trigger() {
            createCardsListView();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transactionLogSource = new TransactionLogSource(getApplicationContext());
        cardNameSource = new CardNameSource(this);
        setContentView(ru.spbau.WhereIsMyMoney2.R.layout.cards);
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateView();
    }

    private void updateView() {
        NewSmsProcessingAsyncTask newSmsProcessingAsyncTask =
                new NewSmsProcessingAsyncTask(this, transactionLogSource, new CreateCardsListViewEventHandler());
        newSmsProcessingAsyncTask.execute();
    }

    @Override
    protected void onStop() {
        super.onStop();
        transactionLogSource.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(ru.spbau.WhereIsMyMoney2.R.menu.cards_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case ru.spbau.WhereIsMyMoney2.R.id.add_parser:
                Intent intent = new Intent(this, SmsViewActivity.class);
                startActivity(intent);
                return true;
            case ru.spbau.WhereIsMyMoney2.R.id.refresh_history:
                TransactionLogSource trSource = new TransactionLogSource(getApplicationContext());
                trSource.open();
                trSource.resetDatabase();
                trSource.close();
                updateView();
                return true;
            case ru.spbau.WhereIsMyMoney2.R.id.drop_parsers:
                TemplatesSource tpSource = new TemplatesSource(getApplicationContext());
                tpSource.open();
                tpSource.resetDatabase();
                tpSource.close();
                return true;
        }

        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(Menu.NONE, LIST_MENU_RENAME, Menu.NONE, "Rename");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case LIST_MENU_RENAME:
                showRenameCardDialog(cardIds[((int) info.id)]);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    void showRenameCardDialog(final String cardId) {
        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.card_rename_dialog);

        Button okButton = (Button) dialog.findViewById(R.id.cardRenameOkButton);
        Button cancelButton = (Button) dialog.findViewById(R.id.cardRenameCancelButton);
        final EditText editText = (EditText) dialog.findViewById(R.id.cardRenameEditText);
        editText.setText(cardId);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Editable text = editText.getText();
                if (text.length() == 0) {
                    Toast.makeText(CardListActivity.this, "Enter text", Toast.LENGTH_SHORT).show();
                } else {
                    renameCard(cardId, text.toString());
                    dialog.dismiss();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    void renameCard(String cardId, String newName) {
        cardNameSource.setName(cardId, newName);
        createCardsListView();
    }
}
