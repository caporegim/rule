package com.transaction.data;

import java.util.Date;

public class Transaction {
    private int transactionType;
    private TransactionDirection direction;
    private int transactionSource;
    private long sum;
    private TransactionCurrency currency;
    private long date;

    //for jackson
    public Transaction() {
    }

    public Transaction(TransactionType transactionType, TransactionDirection direction, TransactionSource transactionSource, long sum, TransactionCurrency currency, long date) {
        this.transactionType = transactionType.getId();
        this.direction = direction;
        this.transactionSource = transactionSource.getId();
        this.sum = sum;
        this.currency = currency;
        this.date = date;
    }

    public TransactionType getTransactionType() {
        return TransactionType.raw(transactionType);
    }

    public TransactionDirection getDirection() {
        return direction;
    }

    public TransactionSource getTransactionSource() {
        return TransactionSource.raw(transactionSource);
    }

    public long getSum() {
        return sum;
    }

    public TransactionCurrency getCurrency() {
        return currency;
    }

    public Date getDate() {
        return new Date(date);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionType=" + transactionType +
                ", direction=" + direction +
                ", transactionSource=" + transactionSource +
                ", sum=" + sum +
                ", currency=" + currency +
                ", date=" + date +
                '}';
    }
}
