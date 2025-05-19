package io.github.andrewrds.fintrack.account;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.andrewrds.fintrack.FintrackError;
import io.github.andrewrds.fintrack.FintrackResponse;
import io.github.andrewrds.fintrack.provider.ProviderNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
public class AccountRestController {
    private final AccountService accountService;

    public AccountRestController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/account/create")
    public Account create(@RequestBody CreateAccountRequest req, HttpSession session) {
        return accountService.create(req.getProviderId(), req.getName());
    }

    @PostMapping("/account/delete")
    public FintrackResponse delete(@RequestBody DeleteAccountRequest req, HttpSession session) {
        accountService.delete(req.getId());
        return new FintrackResponse("Account deleted");
    }
    
    @GetMapping("account/listForProvider")
    public List<Account> listForProvider(@RequestParam long providerId) {
        return accountService.listForProvider(providerId);
    }

    @ExceptionHandler(DuplicateAccountNameException.class)
    public ResponseEntity<FintrackError> handleDuplicateAccountNameException(
            HttpServletRequest request,
            DuplicateAccountNameException e) {
        var error = new FintrackError("An account with the same name already exists for the provider");
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ProviderNotFoundException.class)
    public ResponseEntity<FintrackError> handleProviderNotFoundException(
            HttpServletRequest request,
            ProviderNotFoundException e) {
        var error = new FintrackError("No provider exists with the supplied id");
        return new ResponseEntity<FintrackError>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<FintrackError> handleAccountNotFoundException(
            HttpServletRequest request,
            AccountNotFoundException e) {
        var error = new FintrackError("No account exists with the supplied id");
        return new ResponseEntity<FintrackError>(error, HttpStatus.NOT_FOUND);
    }
}
