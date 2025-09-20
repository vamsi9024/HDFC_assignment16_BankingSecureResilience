package org.example.service;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.example.model.Account;
import org.example.model.User;
import org.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public double getBalance(String username){
        return accountRepository.findByUsername(username)
                .orElseThrow(()->new NoSuchElementException("Account Not Found"))
                .getBalance();
    }

    public void deposit(String username, Double amount) {
        if(amount>0.0) {
            Account account = accountRepository.findByUsername(username)
                    .orElseThrow(()->new NoSuchElementException("Account Not Found"));
            account.setBalance(account.getBalance() + amount);
            accountRepository.save(account);
        }
        else{
            throw new IllegalArgumentException("Deposit amount Must be more than Zero");
        }

    }

//    @RateLimiter(name = "withdrawLimiter", fallbackMethod = "rateLimitFallback")
    public void withdraw(String username, Double amount) {
        if (amount <= 0.0) {
            throw new IllegalArgumentException("Withdraw amount must be more than zero");
        }

        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        if (account.getBalance() < amount) {
            throw new IllegalStateException("Insufficient balance to withdraw");
        }

        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);
    }

//    public void rateLimitFallback(String username, Double amount, RequestNotPermitted ex) {
//        throw new RuntimeException("Too many withdraw requests - try later");
//    }

}
