package com.transaction;

import com.transaction.controller.TransactionController;
import com.transaction.data.*;
import com.transaction.service.RuleService;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;

@RunWith(SpringRunner.class)
@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RuleService service;

    private Gson gson = new Gson();

    @Test
    public void testRest() throws Exception {
        TransactionResponse expectedTransactionResponse = new TransactionResponse(1, TransactionAction.REJECT, 100);
        Mockito.when(service.process(Mockito.any()))
                .thenReturn(expectedTransactionResponse);

        Transaction transaction = new Transaction(TransactionType.raw(1),
                TransactionDirection.ORIGINAL,
                TransactionSource.raw(1),
                101,
                TransactionCurrency.RUB,
                new Date().getTime());
        mockMvc.perform(MockMvcRequestBuilders.post("/transaction")
                                                .contentType(MediaType.APPLICATION_JSON)
        .content(gson.toJson(transaction)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.ruleId").value(expectedTransactionResponse.getRuleId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.action").value(expectedTransactionResponse.getAction().name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balanceType").value(expectedTransactionResponse.getBalanceType()));
    }
}
