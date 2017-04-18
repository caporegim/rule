package com.transaction.data;

public class TransactionResponse {
    private int ruleId;
    private TransactionAction action;
    private int balanceType;

    public TransactionResponse() {
    }

    public TransactionResponse(int ruleId, TransactionAction action, int balanceType) {
        this.ruleId = ruleId;
        this.action = action;
        this.balanceType = balanceType;
    }

    public int getRuleId() {
        return ruleId;
    }

    public TransactionAction getAction() {
        return action;
    }

    public int getBalanceType() {
        return balanceType;
    }
}
