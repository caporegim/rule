package com.transaction.controller;

import com.transaction.Constants;
import com.transaction.data.*;
import com.transaction.service.RuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
public class TransactionController {
    private static final Logger log = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    private RuleService ruleService;

    @RequestMapping(value = Constants.RULE_CHECK, method = RequestMethod.POST)
    public @ResponseBody TransactionResponse processTransaction(@RequestBody Transaction transaction){
        return ruleService.process(transaction);
    }

}
