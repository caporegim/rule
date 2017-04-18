package com.transaction;

import com.transaction.data.*;
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

    @Test
    public void test(){
        jdbcTemplate.update("INSERT INTO TRANSACTION_RULE\n" +
                "(TYPE,DIRECTION,SOURCE,SUM_CONDITION,SUM,ACTION,BALANCE_TYPE,START_DATE,FINISH_DATE,CREATED_DATE)\n" +
                "  VALUES\n" +
                "  (1, 'ORIGINAL', 1, '=', 100, 'ACCEPT', 1,'2017-01-01', '2018-01-01', now());");
        Transaction transaction = new Transaction( TransactionType.raw(1),
                TransactionDirection.ORIGINAL,
                 TransactionSource.raw(1),
                100,
                TransactionCurrency.RUB,
                new Date().getTime());
        TransactionResponse process = ruleService.process(transaction);
        Assert.assertEquals(TransactionAction.ACCEPT, process.getAction());
    }
}
