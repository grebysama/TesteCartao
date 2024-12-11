package br.com.controller;

import br.com.entity.Transaction;
import br.com.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
public class AuthorizationController {

    @Autowired
    private AuthorizationService authorizationService;

    @PostMapping("/authorize")
    public ResponseEntity<?> authorizeTransaction(@RequestBody Transaction transaction) {
        String resultCode = authorizationService.authorizeTransaction(transaction);
        return ResponseEntity.ok(Map.of("code", resultCode));
    }
}
