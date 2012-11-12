package ru.spbau.WhereIsMyMoney.storage;


public interface IFilter<T> {
	public boolean match(T obj);
}
