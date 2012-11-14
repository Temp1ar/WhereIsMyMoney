package ru.spbau.WhereIsMyMoney.gui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import ru.spbau.WhereIsMyMoney.Card;
import ru.spbau.WhereIsMyMoney.R;
import ru.spbau.WhereIsMyMoney.storage.TransactionLogHelper;

import java.util.List;

class CardListAdapter extends ArrayAdapter<Card> {
    private final Context myContext;
    private final List<Card> myCards;

    public CardListAdapter(Context context, List<Card> cards) {
        super(context, R.layout.card_row, cards);
        myContext = context;
        myCards = cards;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.card_row, parent, false);

        TextView card_id = (TextView) rowView.findViewById(R.id.card_id);
        if (!TransactionLogHelper.CASH.equals(myCards.get(position).getId())) {
            card_id.setText(myCards.get(position).getId());
        } else {
            card_id.setText(myContext.getString(R.string.cash_account));
            ImageView row_icon = (ImageView) rowView.findViewById(R.id.row_icon);
            row_icon.setImageDrawable(myContext.getResources().getDrawable(R.drawable.money_icon));
        }
        TextView card_balance = (TextView) rowView.findViewById(R.id.card_balance);
        card_balance.setText(myCards.get(position).getBalance().toString());

        return rowView;
    }
}
