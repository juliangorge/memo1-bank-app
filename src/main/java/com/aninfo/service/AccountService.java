package com.aninfo.service;

import com.aninfo.exceptions.DepositNegativeSumException;
import com.aninfo.exceptions.InsufficientFundsException;
import com.aninfo.model.Account;
import com.aninfo.model.Transaction;
import com.aninfo.repository.AccountRepository;
import com.aninfo.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionService transactionService;

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Collection<Account> getAccounts() {
        return accountRepository.findAll();
    }

    public Optional<Account> findById(Long cbu) {
        return accountRepository.findById(cbu);
    }

    public void save(Account account) {
        accountRepository.save(account);
    }

    public void deleteById(Long cbu) {
        accountRepository.deleteById(cbu);
    }

    @Transactional
    public Account withdraw(Long cbu, Double sum) {
        Account account = accountRepository.findAccountByCbu(cbu);

        if (account.getBalance() < sum) {
            throw new InsufficientFundsException("Insufficient funds");
        }

        Transaction transaction = transactionService.createTransaction(new Transaction(cbu, sum));

        account.setBalance(account.getBalance() - sum);
        accountRepository.save(account);

        return account;
    }

    private double applyPromoIfApplicable(double sum) {
        if (sum >= 2000) {
            double discount = Math.min(sum * 0.1, 500);
            return sum + discount;
        }
        return sum;
    }

    @Transactional
    public Account deposit(Long cbu, Double sum) {

        if (sum <= 0) {
            throw new DepositNegativeSumException("Cannot deposit negative sums");
        }

        Double finalSum = applyPromoIfApplicable(sum);
        Transaction transaction = transactionService.createTransaction(new Transaction(cbu, finalSum));

        Account account = accountRepository.findAccountByCbu(cbu);
        account.setBalance(account.getBalance() + finalSum);
        accountRepository.save(account);

        return account;
    }

}
