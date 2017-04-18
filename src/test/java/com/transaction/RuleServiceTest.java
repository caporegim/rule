package com.transaction;

import com.transaction.data.*;
import com.transaction.exceptions.RuleNotFoundException;
import com.transaction.service.RuleService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RuleServiceTest {
    @Autowired
    private RuleService ruleService;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test(expected = RuleNotFoundException.class)
    public void testNoRuleFound(){
        Transaction transaction = new Transaction(
                TransactionType.raw(0),
                TransactionDirection.ORIGINAL,
                TransactionSource.raw(0),
                0,
                TransactionCurrency.RUB,
                new Date().getTime());
        TransactionResponse transactionResponse = ruleService.process(transaction);
    }

    @Test
    public void testEquals(){
        jdbcTemplate.update("INSERT INTO TRANSACTION_RULE\n" +
                "(TYPE,DIRECTION,SOURCE,SUM_CONDITION,SUM,ACTION,BALANCE_TYPE,START_DATE,FINISH_DATE,CREATED_DATE)\n" +
                "  VALUES\n" +
                "  (1, 'ORIGINAL', 1, '=', 100, 'ACCEPT', 1,'2017-01-01', '2018-01-01', now());");
        Transaction transaction = new Transaction(
                TransactionType.raw(1),
                TransactionDirection.ORIGINAL,
                 TransactionSource.raw(1),
                100,
                TransactionCurrency.RUB,
                new Date().getTime());
        TransactionResponse transactionResponse = ruleService.process(transaction);
        Assert.assertEquals(TransactionAction.ACCEPT, transactionResponse.getAction());
        Assert.assertEquals(1, transactionResponse.getBalanceType());
    }


    @Test
    public void testLessAndReject(){
        jdbcTemplate.update("INSERT INTO TRANSACTION_RULE\n" +
                "(TYPE,DIRECTION,SOURCE,SUM_CONDITION,SUM,ACTION,BALANCE_TYPE,START_DATE,FINISH_DATE,CREATED_DATE)\n" +
                "  VALUES\n" +
                "  (1, 'ORIGINAL', 1, '<', 100, 'REJECT', 0,'2017-01-01', '2018-01-01', now());");
        Transaction transaction = new Transaction(
                TransactionType.raw(1),
                TransactionDirection.ORIGINAL,
                TransactionSource.raw(1),
                99,
                TransactionCurrency.RUB,
                new Date().getTime());
        TransactionResponse transactionResponse = ruleService.process(transaction);
        Assert.assertEquals(TransactionAction.REJECT, transactionResponse.getAction());
        Assert.assertEquals(0, transactionResponse.getBalanceType());
    }

    @Test
    public void testNullConditions(){
        Transaction transaction = new Transaction(
                TransactionType.raw(1),
                TransactionDirection.CANCEL,
                TransactionSource.raw(1),
                99,
                TransactionCurrency.RUB,
                new Date().getTime());

        jdbcTemplate.update("INSERT INTO TRANSACTION_RULE\n" +
                "(TYPE,DIRECTION,SOURCE,SUM_CONDITION,SUM,ACTION,BALANCE_TYPE,START_DATE,FINISH_DATE,CREATED_DATE)\n" +
                "  VALUES\n" +
                "  (null, 'CANCEL', null, null, null, 'ACCEPT', -1,'2017-01-01', '2018-01-01', now());");

        TransactionResponse transactionResponse = ruleService.process(transaction);
        Assert.assertEquals(TransactionAction.ACCEPT, transactionResponse.getAction());
        Assert.assertEquals(-1, transactionResponse.getBalanceType());
    }

    @Test
    public void testPriorityConditions(){
        Transaction transaction = new Transaction(
                TransactionType.raw(2),
                TransactionDirection.CANCEL,
                TransactionSource.raw(2),
                99,
                TransactionCurrency.RUB,
                new Date().getTime());

        jdbcTemplate.update("INSERT INTO TRANSACTION_RULE\n" +
                "(TYPE,DIRECTION,SOURCE,SUM_CONDITION,SUM,ACTION,BALANCE_TYPE,START_DATE,FINISH_DATE,CREATED_DATE)\n" +
                "  VALUES\n" +
                "  (null, 'CANCEL', null, null, null, 'ACCEPT', -1,'2017-01-01', '2018-01-01', now())," +
                "  (2, 'CANCEL', 2, null, null, 'ACCEPT', -2,'2017-01-01', '2018-01-01', now());");

        TransactionResponse transactionResponse = ruleService.process(transaction);
        Assert.assertEquals(TransactionAction.ACCEPT, transactionResponse.getAction());
        Assert.assertEquals(-2, transactionResponse.getBalanceType());
    }

    @Test
    public void testPriorityAndDateConditions(){
        Transaction transaction = new Transaction(
                TransactionType.raw(2),
                TransactionDirection.CANCEL,
                TransactionSource.raw(2),
                99,
                TransactionCurrency.RUB,
                new Date().getTime());

        jdbcTemplate.update("INSERT INTO TRANSACTION_RULE\n" +
                "(TYPE,DIRECTION,SOURCE,SUM_CONDITION,SUM,ACTION,BALANCE_TYPE,START_DATE,FINISH_DATE,CREATED_DATE)\n" +
                "  VALUES\n" +
                "  (null, 'CANCEL', null, null, null, 'ACCEPT', -1,'2017-01-01', '2018-01-01', now())," +
                "   (2, 'CANCEL', 2, null, null, 'ACCEPT', -2,'2017-01-01', '2018-01-01', '2017-01-01')," +
                "   (2, 'CANCEL', 2, null, null, 'ACCEPT', -3,'2017-01-01', '2018-01-01', now());");

        TransactionResponse transactionResponse = ruleService.process(transaction);
        Assert.assertEquals(TransactionAction.ACCEPT, transactionResponse.getAction());
        Assert.assertEquals(-3, transactionResponse.getBalanceType());
    }
}
