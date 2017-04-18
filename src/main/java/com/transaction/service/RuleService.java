package com.transaction.service;

import com.transaction.data.Transaction;
import com.transaction.data.TransactionResponse;

public interface RuleService {

    TransactionResponse process(Transaction transaction);
}
