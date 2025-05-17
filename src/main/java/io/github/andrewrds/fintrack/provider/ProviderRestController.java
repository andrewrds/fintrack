package io.github.andrewrds.fintrack.provider;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.andrewrds.fintrack.FintrackError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotNull;

@RestController
public class ProviderRestController {
	private final ProviderService providerService;

	public ProviderRestController(ProviderService providerService) {
		this.providerService = providerService;
	}

	@PostMapping("/provider/create")
	public Provider create(@NotNull String name, HttpSession session) {
		return providerService.create(name);
	}

	@PostMapping("/provider/delete")
	public Provider delete(@NotNull Long id, HttpSession session) {
		return providerService.delete(id);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<FintrackError> handleDataIntegrityViolationException(
			HttpServletRequest request,
			DataIntegrityViolationException e) {

		if (e.getCause() instanceof ConstraintViolationException v) {
			if (v.getConstraintName().equals("uq_name")) {
				var error = new FintrackError("A provider with the same name already exists");
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
		return new ResponseEntity<FintrackError>(error, HttpStatus.CONFLICT);
	}
}
