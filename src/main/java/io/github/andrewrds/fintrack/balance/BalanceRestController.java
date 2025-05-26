package io.github.andrewrds.fintrack.balance;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BalanceRestController {
    private final BalanceService balanceService;

    public BalanceRestController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @GetMapping("/balance/list")
    @CrossOrigin(origins = "http://localhost:5173")
    public BalanceSnapshotResponse list() {
        return balanceService.list();
    }
}
