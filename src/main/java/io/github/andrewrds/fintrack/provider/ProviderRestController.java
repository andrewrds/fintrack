package io.github.andrewrds.fintrack.provider;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.github.andrewrds.fintrack.FintrackError;
import io.github.andrewrds.fintrack.FintrackResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
public class ProviderRestController {
    private final ProviderService providerService;

    public ProviderRestController(ProviderService providerService) {
        this.providerService = providerService;
    }

    @PostMapping("/provider/create")
    @CrossOrigin(origins = "http://localhost:5173")
    public Provider create(@Valid @RequestBody CreateProviderRequest req, HttpSession session) {
        return providerService.create(req.getName());
    }

    @PostMapping("/provider/delete")
    @CrossOrigin(origins = "http://localhost:5173")
    public FintrackResponse delete(@Valid @RequestBody DeleteProviderRequest req, HttpSession session) {
        providerService.delete(req.getId());
        return new FintrackResponse("Provider deleted");
    }

    @GetMapping("/provider/list")
    @CrossOrigin(origins = "http://localhost:5173")
    public List<Provider> list() {
        return providerService.list();
    }

    @ExceptionHandler(DuplicateProviderNameException.class)
    public ResponseEntity<FintrackError> handleDuplicateProviderNameException(
            HttpServletRequest request,
            DuplicateProviderNameException e) {
        var error = new FintrackError("A provider with the same name already exists");
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(error);
    }

    @ExceptionHandler(ProviderNotFoundException.class)
    public ResponseEntity<FintrackError> handleProviderNotFoundException(
            HttpServletRequest request,
            ProviderNotFoundException e) {
        var error = new FintrackError("No provider exists with the supplied id");
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(error);
    }
    
    @ExceptionHandler(DeleteProviderException.class)
    public ResponseEntity<FintrackError> handleDeleteProviderException(
            HttpServletRequest request,
            DeleteProviderException e) {
        var error = new FintrackError("Unable to delete provider");
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(error);
    }
}
