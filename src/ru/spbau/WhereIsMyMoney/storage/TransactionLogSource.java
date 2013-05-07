package ru.spbau.WhereIsMyMoney.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ru.spbau.WhereIsMyMoney.Transaction;

import java.util.*;

/**
 * Transactions database adapter
 *
 * @author kmu
 */
public class TransactionLogSource extends BaseDataSource {
    public TransactionLogSource(Context context) {
        super(new TransactionLogHelper(context));
    }

    public int getTransactionsTableSize() {
        SQLiteDatabase db = getDatabase();
        Cursor cursor = db.rawQuery("SELECT count(*) FROM " + TransactionLogHelper.TRANSACTIONS_TABLE_NAME, null);
        cursor.moveToFirst();
        int size = cursor.getInt(0);
        cursor.close();
        return size;
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

        getDatabase().insert(TransactionLogHelper.TRANSACTIONS_TABLE_NAME, null, dbTransaction);
    }

    public Date getLatestProcessedSmsDate() {
        SQLiteDatabase db = getDatabase();
        if (getTransactionsTableSize() == 0)
            return new Date(0);

        Cursor cursor = db.rawQuery("SELECT MAX(" + TransactionLogHelper.COLUMN_DATE + ") FROM " + TransactionLogHelper.TRANSACTIONS_TABLE_NAME, null);
        cursor.moveToFirst();
        long lastProcessedSmsDate = cursor.getLong(0);
        cursor.close();
        return new Date(lastProcessedSmsDate);
    }

    /**
     * Gets transactions matched by filter from database
     *
     * @param filter filter or null
     * @return list of matched transactions
     */
    List<Transaction> getTransactions(IFilter<Transaction> filter) {
        List<Transaction> transactions = new ArrayList<Transaction>();
        Cursor cursor = getDatabase().query(TransactionLogHelper.TRANSACTIONS_TABLE_NAME,
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
        Cursor cursor = getDatabase().query(TransactionLogHelper.TRANSACTIONS_TABLE_NAME,
                TransactionLogHelper.ALL_COLUMNS, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String card = cursor.getString(cursor.getColumnIndex(TransactionLogHelper.COLUMN_CARD));
            cards.add(card);
            cursor.moveToNext();
        }
        cursor.close();
        return cards;
    }

    /**
     * All known places in database
     *
     * @return set of places from database
     */
    Set<String> getPlaces() {
        Set<String> places = new HashSet<String>();
        Cursor cursor = getDatabase().query(TransactionLogHelper.TRANSACTIONS_TABLE_NAME,
                TransactionLogHelper.ALL_COLUMNS, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String place = cursor.getString(cursor.getColumnIndex(TransactionLogHelper.COLUMN_PLACE));
            places.add(place);
            cursor.moveToNext();
        }
        cursor.close();
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
     * @param card  target card
     * @param start start date of period
     * @param end   end date of period
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
     * @param end   end date of period
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
     * @param end   end date for report
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
     * @param end   end date for report
     * @return map place to total costs from start to end
     */
    public Map<String, Map<String, Float>> getCostsPerPlacesForPeriod(Date start, Date end) {

        Set<String> places = getPlaces();
        HashMap<String, Map<String, Float>> costs = new HashMap<String, Map<String, Float>>();

        for (String place : places) {
            if (place == null)
                place = "";
            List<Transaction> transactions = getTransactionsPerPlaceForPeriod(place, start, end);
            if (!transactions.isEmpty())
                costs.put(place, sum(transactions));
        }
        return costs;
    }

    Map<String, Float> sum(Collection<Transaction> transactions) {
        Map<String, Float> sums = new HashMap<String, Float>();

        for (Transaction transaction : transactions) {
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