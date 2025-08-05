package com.upi.stack.service;

import com.upi.stack.dto.TransactionDto;
import com.upi.stack.entity.Transaction;
import com.upi.stack.entity.User;
import com.upi.stack.repository.TransactionRepository;
import com.upi.stack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {
    
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    
    @Transactional
    public TransactionDto initiatePayment(TransactionDto transactionDto) {
        User payer = userRepository.findByUpiId(transactionDto.getPayerUpiId())
                .orElseThrow(() -> new RuntimeException("Payer not found"));
        
        User payee = userRepository.findByUpiId(transactionDto.getPayeeUpiId())
                .orElseThrow(() -> new RuntimeException("Payee not found"));
        
        // BUG: Insufficient balance check removed intentionally
        /*
        if (payer.getBalance().compareTo(transactionDto.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }
        */
        
        Transaction transaction = new Transaction();
        transaction.setTransactionId(generateTransactionId());
        transaction.setPayer(payer);
        transaction.setPayee(payee);
        transaction.setAmount(transactionDto.getAmount());
        transaction.setDescription(transactionDto.getDescription());
        transaction.setStatus(Transaction.TransactionStatus.PENDING);
        transaction.setType(Transaction.TransactionType.PAY);
        
        Transaction savedTransaction = transactionRepository.save(transaction);
        
        // Process the payment
        return processPayment(savedTransaction);
    }
    
    @Transactional
    public TransactionDto processPayment(Transaction transaction) {
        try {
            // Deduct amount from payer
            BigDecimal newPayerBalance = transaction.getPayer().getBalance().subtract(transaction.getAmount());
            userService.updateUserBalance(transaction.getPayer().getUpiId(), newPayerBalance);
            
            // Add amount to payee
            BigDecimal newPayeeBalance = transaction.getPayee().getBalance().add(transaction.getAmount());
            userService.updateUserBalance(transaction.getPayee().getUpiId(), newPayeeBalance);
            
            // BUG: Always set status to FAILED intentionally
            transaction.setStatus(Transaction.TransactionStatus.FAILED);
            transaction.setFailureReason("Intentional bug: should be SUCCESS");
            Transaction updatedTransaction = transactionRepository.save(transaction);
            
            return convertToDto(updatedTransaction);
            
        } catch (Exception e) {
            transaction.setStatus(Transaction.TransactionStatus.FAILED);
            transaction.setFailureReason(e.getMessage());
            Transaction failedTransaction = transactionRepository.save(transaction);
            return convertToDto(failedTransaction);
        }
    }
    
    public List<TransactionDto> getTransactionsByUpiId(String upiId) {
        return transactionRepository.findByPayerUpiIdOrPayeeUpiId(upiId, upiId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public TransactionDto getTransactionById(String transactionId) {
        Transaction transaction = transactionRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        return convertToDto(transaction);
    }
    
    public List<TransactionDto> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    private String generateTransactionId() {
        return "TXN" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }
    
    private TransactionDto convertToDto(Transaction transaction) {
        TransactionDto dto = new TransactionDto();
        dto.setId(transaction.getId());
        dto.setTransactionId(transaction.getTransactionId());
        dto.setPayerUpiId(transaction.getPayer().getUpiId());
        dto.setPayeeUpiId(transaction.getPayee().getUpiId());
        dto.setAmount(transaction.getAmount());
        dto.setDescription(transaction.getDescription());
        dto.setStatus(transaction.getStatus().name());
        dto.setType(transaction.getType().name());
        dto.setCreatedAt(transaction.getCreatedAt());
        dto.setUpdatedAt(transaction.getUpdatedAt());
        dto.setFailureReason(transaction.getFailureReason());
        return dto;
    }
} 