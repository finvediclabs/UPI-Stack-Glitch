package com.upi.stack.controller;

import com.upi.stack.dto.TransactionDto;
import com.upi.stack.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TransactionController {
    
    private final TransactionService transactionService;
    
    @PostMapping("/pay")
    public ResponseEntity<TransactionDto> initiatePayment(@Valid @RequestBody TransactionDto transactionDto) {
        try {
            TransactionDto transaction = transactionService.initiatePayment(transactionDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping
    public ResponseEntity<List<TransactionDto>> getAllTransactions() {
        List<TransactionDto> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }
    
    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionDto> getTransactionById(@PathVariable String transactionId) {
        try {
            TransactionDto transaction = transactionService.getTransactionById(transactionId);
            return ResponseEntity.ok(transaction);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/user/{upiId}")
    public ResponseEntity<List<TransactionDto>> getTransactionsByUpiId(@PathVariable String upiId) {
        List<TransactionDto> transactions = transactionService.getTransactionsByUpiId(upiId);
        return ResponseEntity.ok(transactions);
    }
} 