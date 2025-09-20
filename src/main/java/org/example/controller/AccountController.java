package org.example.controller;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.example.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountController {

    @Autowired
    private AccountService accountService;
    private final Logger log = LoggerFactory.getLogger(AccountController.class);

    @GetMapping("/balance")
    public ResponseEntity<String> getBalance(Authentication auth) {
        double balance = accountService.getBalance(auth.getName());
        return new ResponseEntity<>("Balance is " + balance, HttpStatus.OK);
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(Authentication auth, @RequestParam Double amount){
        accountService.deposit(auth.getName(),amount);
        log.info("Deposit: {} -> {}", auth.getName(), amount);
        return new ResponseEntity<>("Deposited Successfully",HttpStatus.OK);
    }

    @RateLimiter(name = "withdrawLimiter",fallbackMethod = "rateLimitFallback")
    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(Authentication auth,@RequestParam Double amount){
        accountService.withdraw(auth.getName(),amount);
        log.info("Withdraw: {} -> {}", auth.getName(), amount);
        return new ResponseEntity<>("Withdraw Successfully",HttpStatus.OK);
    }

    public ResponseEntity<String> rateLimitFallback(Authentication auth, Double amount, RequestNotPermitted ex) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Too many requests");
    }


}
