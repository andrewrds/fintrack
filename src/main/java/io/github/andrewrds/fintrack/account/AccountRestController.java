package io.github.andrewrds.fintrack.account;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotNull;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.andrewrds.fintrack.FintrackError;
import io.github.andrewrds.fintrack.provider.ProviderNotFoundException;

@RestController
public class AccountRestController {
	private final AccountService accountService;

	public AccountRestController(AccountService accountService) {
		this.accountService = accountService;
	}

	@PostMapping("/account/create")
    public Account create(@NotNull Long providerId, @NotNull String name, HttpSession session) {
    	return accountService.create(providerId, name);
	}

	@PostMapping("/account/delete")
    public Account delete(@NotNull Long id, HttpSession session) {
    	return accountService.delete(id);
    }
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<FintrackError> handleDataIntegrityViolationException(
			HttpServletRequest request,
			DataIntegrityViolationException e) {

		if (e.getCause() instanceof ConstraintViolationException v) {
			if (v.getConstraintName().equals("uq_provider_name")) {
				var error = new FintrackError("An account with the same name already exists for the provider");
				return new ResponseEntity<>(error, HttpStatus.CONFLICT);
			}
		}

		throw e;
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
