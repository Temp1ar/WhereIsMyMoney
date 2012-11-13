package ru.spbau.WhereIsMyMoney.storage;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ru.spbau.WhereIsMyMoney.Transaction;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

/**
 * Transactions database adapter
 * 
 * @author kmu
 */
public class TransactionLogSource extends BaseDataSource {
	public TransactionLogSource(Context context) {
		super(new TransactionLogHelper(context));
	}
	
	/**
	 * Insert new transaction into database. Database have be open for writing.
	 * 
	 * @param transaction transaction to be inserted
	 */
	public void addTransaction(Transaction transaction) {
		ContentValues dbTransaction = new ContentValues();
		dbTransaction.put(TransactionLogHelper.COLUMN_CARD, transaction.getCard());
		dbTransaction.put(TransactionLogHelper.COLUMN_DELTA, transaction.getDelta());
		dbTransaction.put(TransactionLogHelper.COLUMN_DATE, transaction.getDate().getTime());
		dbTransaction.put(TransactionLogHelper.COLUMN_BALANCE, transaction.getBalance());
		dbTransaction.put(TransactionLogHelper.COLUMN_TYPE, transaction.getType());
		dbTransaction.put(TransactionLogHelper.COLUMN_PLACE, transaction.getPlace());
		
		long insertId = getDatabase().insert(TransactionLogHelper.TABLE_TRANSACTION, null, dbTransaction);
		
		Log.d(this.getClass().getCanonicalName(), "Transaction " + transaction + " saved with id " + insertId);
	}
	
	/**
	 * Gets transactions matched by filter from database
	 * 
	 * @param filter filter or null
	 * @return list of matched transactions
	 */
	public List<Transaction> getTransactions(IFilter<Transaction> filter) {
		List<Transaction> transactions = new ArrayList<Transaction>();
		Cursor cursor = getDatabase().query(TransactionLogHelper.TABLE_TRANSACTION,
				TransactionLogHelper.ALL_COLUMNS, null, null, null, null,
				TransactionLogHelper.COLUMN_DATE);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Transaction transaction = cursorToTransaction(cursor);
			if ((filter == null) || filter.match(transaction)) {
				transactions.add(transaction);
			}
			cursor.moveToNext();
		}
		cursor.close();
		return transactions;
	}
	
	/**
	 * All transactions from database. Equivalent to getTransaction(null)
	 * 
	 * @return list of saved transactions
	 */
	public List<Transaction> getAllTransactions() {
		return getTransactions(null);
	}
	
	private Transaction cursorToTransaction(Cursor cursor) {
		String card = cursor.getString(cursor.getColumnIndex(TransactionLogHelper.COLUMN_CARD));
		Date date = new Date(cursor.getLong(cursor.getColumnIndex(TransactionLogHelper.COLUMN_DATE)));
		float balance = cursor.getFloat(cursor.getColumnIndex(TransactionLogHelper.COLUMN_BALANCE));
		String delta = cursor.getString(cursor.getColumnIndex(TransactionLogHelper.COLUMN_DELTA));
		String place = cursor.getString(cursor.getColumnIndex(TransactionLogHelper.COLUMN_PLACE));
		int type = cursor.getInt(cursor.getColumnIndex(TransactionLogHelper.COLUMN_TYPE));
		
		return new Transaction(date, place, card, delta, balance, type);
	}
	
	/**
	 * All known cards in database
	 * 
	 * @return set of cards from database
	 */
	public Set<String> getCards() {
		Set<String> cards = new HashSet<String>();
		Cursor cursor = getDatabase().query(TransactionLogHelper.TABLE_TRANSACTION,
				TransactionLogHelper.ALL_COLUMNS, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String card = cursor.getString(cursor.getColumnIndex(TransactionLogHelper.COLUMN_CARD));
			cards.add(card);
			cursor.moveToNext();
		}
		return cards;
	}
	
	/**
	 * All transactions for specified card
	 * 
	 * @param card target card
	 * @return list of transactions
	 */
	public List<Transaction> getTransactionsPerCard(final String card) {
		return getTransactions(new IFilter<Transaction>() {
			public boolean match(Transaction transaction) {
				return transaction.getCard().equals(card);
			}
		});
	}

    /**
     * All transactions for specified card for period
     *
     * @param card target card
     * @param start start date of period
     * @param end end date of period
     * @return list of transactions
     */
    public List<Transaction> getTransactionsPerCardForPeriod(final String card, final Date start, final Date end) {
        return getTransactions(new IFilter<Transaction>() {
            public boolean match(Transaction transaction) {
                return transaction.getCard().equals(card)
                        && transaction.getDate().compareTo(start) >= 0
                        && transaction.getDate().compareTo(end) <= 0;
            }
        });
    }

    /**
     * Return total costs for each cards
     *
     * @param start start date for report
     * @param end end date for report
     * @return map costs_report to total costs from start to end
     */
    public Map<String, Float> getCostsPerCardsForPeriod(Date start, Date end) {

        Set<String> cards = getCards();
        HashMap<String, Float> costs = new HashMap<String, Float>();

        for (String card : cards) {
            List<Transaction> transactions = getTransactionsPerCardForPeriod(card, start, end);
            if (!transactions.isEmpty())
                costs.put(card, Math.abs(transactions.get(0).getBalance() - transactions.get(transactions.size() - 1).getBalance()));
        }
       return costs;
    }
}