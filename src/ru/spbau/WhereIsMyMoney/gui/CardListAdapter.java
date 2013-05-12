package ru.spbau.WhereIsMyMoney.gui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ru.spbau.WhereIsMyMoney.Card;
import ru.spbau.WhereIsMyMoney.R;

import java.util.List;

class CardListAdapter extends ArrayAdapter<Card> {
    private final Context context;
    private final List<Card> cards;

    public CardListAdapter(Context context, List<Card> cards) {
        super(context, R.layout.card_row, cards);
        this.context = context;
        this.cards = cards;
    }

    @Override
    public View getView(int index, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.card_row, parent, false);

        TextView cardId = (TextView) rowView.findViewById(R.id.card_id);
        cardId.setText(cards.get(index).getDisplayName());

        TextView cardBalance = (TextView) rowView.findViewById(R.id.card_balance);
        cardBalance.setText(cards.get(index).getBalance().toString());

        return rowView;
    }
}
