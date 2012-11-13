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
	private static final int RED = Color.parseColor("#FF0000");
	private static final int GREEN = Color.parseColor("#3CB371");
	private static final int BLUE = Color.parseColor("#0000FF");
	
	private static final String DATE_FORMAT = "yyyy.MM.dd HH:mm:ss";
	private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_FORMAT);
	
	private final Context myContext;
	private final List<Transaction> myTransactions;

	public TransactionListAdapter(Context context, List<Transaction> transactions) {
		super(context, R.layout.transactions_activity_row, transactions);
		myContext = context;
		myTransactions = transactions;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.transactions_activity_row, parent, false);
		
		Transaction trans = myTransactions.get(position);
		
		TextView delta = (TextView) rowView.findViewById(R.id.transaction_delta);
		delta.setText(trans.getDelta());
		TextView place = (TextView) rowView.findViewById(R.id.transaction_place);
		if (trans.getPlace() != null) {
			place.setText(trans.getPlace());
		}
		TextView balance = (TextView) rowView.findViewById(R.id.transaction_new_balance);
		balance.setText("balance: " + trans.getBalance());
		TextView date = (TextView) rowView.findViewById(R.id.transaction_date);
		date.setText(DATE_FORMATTER.format(trans.getDate()));
		
		switch (trans.getType()) {
		case Transaction.WITHDRAW:
			delta.setTextColor(RED);
			break;
		case Transaction.NONE:
			delta.setTextColor(BLUE);
			break;
		case Transaction.DEPOSIT:
			delta.setTextColor(GREEN);
			break;
		}
		
		return rowView;
	}
}
