package com.transaction.data;

public class TransactionType {
    private final int id;

    private TransactionType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static TransactionType raw(int id){
        return new TransactionType(id);
    }
}
