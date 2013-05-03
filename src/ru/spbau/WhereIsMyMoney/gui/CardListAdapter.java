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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.card_row, parent, false);

        TextView card_id = (TextView) rowView.findViewById(R.id.card_id);
        card_id.setText(cards.get(position).getId());

        TextView card_balance = (TextView) rowView.findViewById(R.id.card_balance);
        card_balance.setText(cards.get(position).getBalance().toString());

        return rowView;
    }
}
