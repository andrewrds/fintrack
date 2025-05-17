package io.github.andrewrds.fintrack.account;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountRestController {
    private final AccountService accountService;
    
    public AccountRestController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/account/create")
    public void create(long providerId, String accountName, HttpSession session) {
    	accountService.create(providerId, accountName);
    }
    
    @PostMapping("/account/delete")
    public void delete(long providerId, long accountId, HttpSession session) {
    	accountService.delete(providerId, accountId);
    }
}
