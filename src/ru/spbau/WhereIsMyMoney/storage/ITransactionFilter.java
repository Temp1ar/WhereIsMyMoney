package ru.spbau.WhereIsMyMoney.storage;

import ru.spbau.WhereIsMyMoney.Transaction;

public interface ITransactionFilter {
	public boolean match(Transaction transaction);
}
