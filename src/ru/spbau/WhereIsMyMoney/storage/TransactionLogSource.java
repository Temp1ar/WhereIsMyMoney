package ru.spbau.WhereIsMyMoney.storage;

import java.io.Closeable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.spbau.WhereIsMyMoney.Transaction;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Transactions database adapter
 * 
 * @author kmu
 */
public class TransactionLogSource implements Closeable {
	public static final int FOR_READ = 1;
	public static final int FOR_WRITE = 2;
	
	private SQLiteDatabase myDatabase;
	private TransactionLogHelper myLogHelper;
	
	public TransactionLogSource(Context context) {
		myLogHelper = new TransactionLogHelper(context);
	}
	
	/**
	 * Opens database for reading. Equivalent of open(TransactionLogSource.FOR_READ)
	 */
	public void open() {
		this.open(FOR_READ);
	}
	
	/**
	 * Opens database for reading or writing.
	 * 
	 * @param mode TransactionLogSource.FOR_READ to open database for reading,
	 * 		TransactionLogSource.FOR_WRITE to open database for writing
	 */
	public void open(int mode) {
		if (mode == FOR_READ) {
			myDatabase = myLogHelper.getReadableDatabase();
			Log.d(getClass().getCanonicalName(), "database opened for read");
		} else if (mode == FOR_WRITE) {
			myDatabase = myLogHelper.getWritableDatabase();
			Log.d(getClass().getCanonicalName(), "database opened for write");
		}
	}
	
	public void close() {
		myLogHelper.close();
		Log.d(getClass().getCanonicalName(), "database closed");
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
		dbTransaction.put(TransactionLogHelper.COLUMN_PLACE, transaction.getPlace());
		
		long insertId = myDatabase.insert(TransactionLogHelper.TABLE_TRANSACTION, null, dbTransaction);
		
		Log.d(this.getClass().getCanonicalName(), "Transaction " + transaction + " saved with id " + insertId);
	}
	
	/**
	 * Gets transactions matched by filter from database
	 * 
	 * @param filter filter or null
	 * @return list of matched transactions
	 */
	public List<Transaction> getTransactions(ITransactionFilter filter) {
		List<Transaction> transactions = new ArrayList<Transaction>();
		Cursor cursor = myDatabase.query(TransactionLogHelper.TABLE_TRANSACTION,
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
		final String card = cursor.getString(TransactionLogHelper.COLUMN_CARD_NUM);
		final Date date = new Date(cursor.getLong(TransactionLogHelper.COLUMN_DATE_NUM));
		final float balance = cursor.getFloat(TransactionLogHelper.COLUMN_BALANCE_NUM);
		final String delta = cursor.getString(TransactionLogHelper.COLUMN_DELTA_NUM);
		final String place = cursor.getString(TransactionLogHelper.COLUMN_PLACE_NUM);
		
		return new Transaction(date, place, card, delta, balance);
	}
	
	/**
	 * All known cards in database
	 * 
	 * @return set of cards from database
	 */
	public Set<String> getCards() {
		Set<String> cards = new HashSet<String>();
		Cursor cursor = myDatabase.query(TransactionLogHelper.TABLE_TRANSACTION,
				TransactionLogHelper.ALL_COLUMNS, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String card = cursor.getString(TransactionLogHelper.COLUMN_CARD_NUM);
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
	public List<Transaction> getTransactionsPerCard(String card) {
		final String target = card;
		return getTransactions(new ITransactionFilter() {
			public boolean match(Transaction transaction) {
				return transaction.getCard().equals(target);
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
    public Map<String, Double> getCostsForPeriodPerCards(final Date start, final Date end) {
        HashMap<String, Double> costs = new HashMap<String, Double>();

        List<Transaction> transactions = getTransactions(new ITransactionFilter() {
            public boolean match(Transaction transaction) {
                return transaction.getDate().compareTo(start) >= 0 && transaction.getDate().compareTo(end) <= 0;
            }
        });

        for (Transaction transaction : transactions) {
            Matcher matcher = Pattern.compile("^(\\d+(?:\\.\\d+)?)\\s*(.*)").matcher(transaction.getDelta());
            if (matcher.matches()) {
                Double value = costs.get(transaction.getCard());
                if (value == null)
                    value = 0.0;

                value += Double.valueOf(matcher.group(1));
                costs.put(transaction.getCard(), value);
            }
        }
        return costs;
    }

}