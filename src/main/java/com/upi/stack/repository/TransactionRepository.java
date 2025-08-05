package com.upi.stack.repository;

import com.upi.stack.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    Optional<Transaction> findByTransactionId(String transactionId);
    
    List<Transaction> findByPayerUpiId(String payerUpiId);
    
    List<Transaction> findByPayeeUpiId(String payeeUpiId);
    
    List<Transaction> findByPayerUpiIdOrPayeeUpiId(String payerUpiId, String payeeUpiId);
    
    List<Transaction> findByStatus(Transaction.TransactionStatus status);
    
    List<Transaction> findByType(Transaction.TransactionType type);
} 