package com.aninfo.integration.cucumber;

import com.aninfo.Memo1BankApp;
import com.aninfo.model.Account;
import com.aninfo.model.Transaction;
import com.aninfo.service.AccountService;
import com.aninfo.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

@ContextConfiguration(classes = Memo1BankApp.class)
@WebAppConfiguration
public class AccountIntegrationServiceTest {

    @Autowired
    AccountService accountService;
    @Autowired
    TransactionService transactionService;

    Account createAccount(Double balance) {
        return accountService.createAccount(new Account(balance));
    }

    Transaction createTransaction(Long cbu, Double sum) {
        return transactionService.createTransaction(new Transaction(cbu, sum));
    }

    Account withdraw(Account account, Double sum) {
        account = accountService.withdraw(account.getCbu(), sum);
        this.createTransaction(account.getCbu(), sum);
        return account;
    }

    Account deposit(Account account, Double sum) {
        account = accountService.deposit(account.getCbu(), sum);
        this.createTransaction(account.getCbu(), sum);
        return account;
    }

}
