package ru.spbau.WhereIsMyMoney2.gui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ru.spbau.WhereIsMyMoney2.R;
import ru.spbau.WhereIsMyMoney2.Transaction;

import java.text.SimpleDateFormat;
import java.util.List;

class TransactionListAdapter extends ArrayAdapter<Transaction> {
    private final SimpleDateFormat DATE_FORMATTER;

    private final Context context;
    private final List<Transaction> transactions;

    public TransactionListAdapter(Context context, List<Transaction> transactions) {
        super(context, R.layout.transactions_activity_row, transactions);
        this.context = context;
        this.transactions = transactions;
        String DATE_FORMAT = context.getString(R.string.date_format);
        DATE_FORMATTER = new SimpleDateFormat(DATE_FORMAT);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.transactions_activity_row, parent, false);

        Transaction trans = transactions.get(position);

        TextView delta = (TextView) rowView.findViewById(R.id.transaction_delta);
        delta.setText(trans.getDelta());
        TextView place = (TextView) rowView.findViewById(R.id.transaction_place);
        if (trans.getPlace() != null && !"".equals(trans.getPlace().trim())) {
            place.setText(trans.getPlace());
        }
        TextView balance = (TextView) rowView.findViewById(R.id.transaction_new_balance);
        balance.setText(trans.getBalance() + "");
        TextView date = (TextView) rowView.findViewById(R.id.transaction_date);
        date.setText(DATE_FORMATTER.format(trans.getDate()));

        switch (trans.getType()) {
            case Transaction.WITHDRAW:
                delta.setTextColor(context.getResources().getColor(R.color.transaction_withdraw));
                break;
            case Transaction.NONE:
                delta.setTextColor(context.getResources().getColor(R.color.transaction_none));
                break;
            case Transaction.DEPOSIT:
                delta.setTextColor(context.getResources().getColor(R.color.transaction_deposit));
                break;
        }

        return rowView;
    }
}
