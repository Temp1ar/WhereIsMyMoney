package ru.spbau.WhereIsMyMoney.gui;

import java.text.SimpleDateFormat;
import java.util.List;

import ru.spbau.WhereIsMyMoney.R;
import ru.spbau.WhereIsMyMoney.Transaction;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TransactionListAdapter extends ArrayAdapter<Transaction> {
	private final String DATE_FORMAT;
	private final SimpleDateFormat DATE_FORMATTER;
	
	private final Context myContext;
	private final List<Transaction> myTransactions;

	public TransactionListAdapter(Context context, List<Transaction> transactions) {
		super(context, R.layout.transactions_activity_row, transactions);
		myContext = context;
		myTransactions = transactions;
                DATE_FORMAT = context.getString(R.string.date_format);
                DATE_FORMATTER = new SimpleDateFormat(DATE_FORMAT);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.transactions_activity_row, parent, false);
		
		Transaction trans = myTransactions.get(position);
		
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
			delta.setTextColor(myContext.getResources().getColor(R.color.transaction_withdraw));
			break;
		case Transaction.NONE:
			delta.setTextColor(myContext.getResources().getColor(R.color.transaction_none));
			break;
		case Transaction.DEPOSIT:
			delta.setTextColor(myContext.getResources().getColor(R.color.transaction_deposit));
			break;
		}
		
		return rowView;
	}
}
