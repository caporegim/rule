package com.transaction.data;

public class TransactionSource {
    private final int id;

    private TransactionSource(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static TransactionSource raw(int id){
        return new TransactionSource(id);
    }
}
