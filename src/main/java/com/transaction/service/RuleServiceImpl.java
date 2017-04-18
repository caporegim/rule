package com.transaction.service;

import com.transaction.data.Transaction;
import com.transaction.data.TransactionAction;
import com.transaction.data.TransactionResponse;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.transaction.exceptions.RuleNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class RuleServiceImpl implements RuleService {
    private static final Logger log = LoggerFactory.getLogger(RuleServiceImpl.class);

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public TransactionResponse process(Transaction transaction) {
        Preconditions.checkNotNull(transaction, "transaction required");
        try {
            final TransactionResponse transactionResponse = namedParameterJdbcTemplate.queryForObject("\n" +
                            "SELECT ID ruleId, ACTION action, BALANCE_TYPE balanceType \n" +
                            "   FROM (SELECT ID, ACTION, BALANCE_TYPE, " +
                            "         CASE WHEN TYPE IS NOT NULL THEN 1 ELSE 0 END + \n" +
                            "         CASE WHEN DIRECTION IS NOT NULL THEN 1 ELSE 0 END + \n" +
                            "         CASE WHEN SOURCE IS NOT NULL THEN 1 ELSE 0 END + \n" +
                            "         CASE WHEN SUM_CONDITION IS NOT NULL THEN 1 ELSE 0 END AS RULE_PRIORITY\n" +
                            " FROM TRANSACTION_RULE\n" +
                            "   WHERE " +
                            "       :transactionDate BETWEEN START_DATE AND FINISH_DATE \n" +
                            "       AND :transactionType = COALESCE (TYPE, :transactionType) \n" +
                            "       AND :transactionSource = COALESCE(SOURCE, :transactionSource)\n" +
                            "       AND :transactionDirection = COALESCE(DIRECTION, :transactionDirection)\n" +
                            "       AND" +
                            "          CASE\n" +
                            "           WHEN SUM_CONDITION = '>' THEN :transactionSum > SUM\n" +
                            "           WHEN SUM_CONDITION = '<' THEN :transactionSum < SUM\n" +
                            "           WHEN SUM_CONDITION = '=' THEN :transactionSum = SUM\n" +
                            "           WHEN SUM_CONDITION IS NULL THEN TRUE\n" +
                            "           ELSE FALSE\n" +
                            "          END\n" +
                            "   ORDER BY RULE_PRIORITY DESC, CREATED_DATE DESC\n" +
                            "   LIMIT 1);",
                    ImmutableMap.of(
                            "transactionDate", transaction.getDate(),
                            "transactionType", transaction.getTransactionType().getId(),
                            "transactionDirection", transaction.getDirection().name(),
                            "transactionSource", transaction.getTransactionSource().getId(),
                            "transactionSum", transaction.getSum()
                    ), (resultSet, i) -> new TransactionResponse(resultSet.getInt("ruleId"),
                            TransactionAction.valueOf(resultSet.getString("action")),
                            resultSet.getInt("balanceType")));
            return transactionResponse;
        } catch (EmptyResultDataAccessException e) {
            throw new RuleNotFoundException("No rule found for transaction " + transaction);
        }
    }
}
