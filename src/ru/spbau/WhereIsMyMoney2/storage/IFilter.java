package ru.spbau.WhereIsMyMoney2.storage;


public interface IFilter<T> {
    public boolean match(T obj);
}
