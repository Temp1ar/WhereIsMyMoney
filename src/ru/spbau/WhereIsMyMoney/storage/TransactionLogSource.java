package ru.spbau.WhereIsMyMoney.storage;

import java.util.*;

import ru.spbau.WhereIsMyMoney.Transaction;
import android.content.Context;
import android.database.Cursor;

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
            TransactionLogHelper.addTransaction(transaction, getDatabase());
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
				TransactionLogHelper.COLUMN_DATE + " DESC");
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
     * All known places in database
     *
     * @return set of places from database
     */
    public Set<String> getPlaces() {
        Set<String> places = new HashSet<String>();
        Cursor cursor = getDatabase().query(TransactionLogHelper.TABLE_TRANSACTION,
                TransactionLogHelper.ALL_COLUMNS, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String place = cursor.getString(cursor.getColumnIndex(TransactionLogHelper.COLUMN_PLACE));
            places.add(place);
            cursor.moveToNext();
        }
        return places;
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
     * All transactions for specified card of period
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
     * All transactions for specified place
     *
     * @return list of transactions
     */
    public List<Transaction> getTransactionsPerPlace(final String place) {
        return getTransactions(new IFilter<Transaction>() {
            public boolean match(Transaction transaction) {
                String trPlace = transaction.getPlace();
                if (trPlace == null || trPlace.isEmpty())
                    return place == null || place.isEmpty();

                return trPlace.equals(place);

            }
        });
    }

    /**
     * All transactions for specified place of period
     *
     * @param start start date of period
     * @param end end date of period
     * @return list of transactions
     */
    public List<Transaction> getTransactionsPerPlaceForPeriod(final String place, final Date start, final Date end) {
        return getTransactions(new IFilter<Transaction>() {
            public boolean match(Transaction transaction) {
                if (transaction.getDate().compareTo(start) < 0 || transaction.getDate().compareTo(end) > 0)
                    return false;

                String trPlace = transaction.getPlace();
                if (trPlace == null || trPlace.isEmpty())
                    return place == null || place.isEmpty();

                return trPlace.equals(place);

            }
        });
    }

    /**
     * Return total costs for each cards
     *
     * @param start start date for report
     * @param end end date for report
     * @return map card to total costs from start to end
     */
    public Map<String, Map<String, Float>> getCostsPerCardsForPeriod(Date start, Date end) {
        Set<String> cards = getCards();
        HashMap<String, Map<String, Float>> costs = new HashMap<String, Map<String, Float>>();

        for (String card : cards) {
            List<Transaction> transactions = getTransactionsPerCardForPeriod(card, start, end);
            if (!transactions.isEmpty()) {
                costs.put(card, sum(transactions));
            }
        }
       return costs;
    }
    /**
     * Return total costs for each places
     *
     * @param start start date for report
     * @param end end date for report
     * @return map place to total costs from start to end
     */
    public Map<String, Map<String,Float>> getCostsPerPlacesForPeriod(Date start, Date end) {

        Set<String> places = getPlaces();
        HashMap<String, Map<String,Float>> costs = new HashMap<String, Map<String, Float>>();

        for (String place : places) {
            if (place == null)
                place = "";
            List<Transaction> transactions = getTransactionsPerPlaceForPeriod(place, start, end);
            if (!transactions.isEmpty())
                costs.put(place, sum(transactions));
        }
        return costs;
    }

    protected Map<String, Float> sum(Collection<Transaction> transactions) {
        Map<String, Float> sums = new HashMap<String, Float>();

        for(Transaction transaction : transactions) {
            if (transaction.getType() != Transaction.WITHDRAW)
                continue;

            Float sum = sums.get(transaction.getCurrency());
            if (sum == null)
                sum = 0f;
            sum += transaction.getAmount();
            sums.put(transaction.getCurrency(), sum);
        }

        return sums;
    }
}