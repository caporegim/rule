package com.transaction.data;

public class Rule {
    private final TransactionType transactionType;
    private final TransactionDirection direction;
    private final TransactionSource transactionSource;

    public Rule(TransactionType transactionType, TransactionDirection direction, TransactionSource transactionSource) {
        this.transactionType = transactionType;
        this.direction = direction;
        this.transactionSource = transactionSource;
    }
}
