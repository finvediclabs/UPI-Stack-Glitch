package com.upi.stack.config;

import com.upi.stack.entity.User;
import com.upi.stack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    
    private final UserRepository userRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Create sample users if they don't exist
        if (userRepository.count() == 0) {
            createSampleUsers();
        }
    }
    
    private void createSampleUsers() {
        // User 1
        User user1 = new User();
        user1.setUpiId("john.doe@icici");
        user1.setName("John Doe");
        user1.setPhoneNumber("9876543210");
        user1.setEmail("john.doe@example.com");
        user1.setBalance(new BigDecimal("10000.00"));
        user1.setBankName("ICICI Bank");
        user1.setAccountNumber("1234567890");
        user1.setIfscCode("ICIC0001234");
        userRepository.save(user1);
        
        // User 2
        User user2 = new User();
        user2.setUpiId("jane.smith@hdfc");
        user2.setName("Jane Smith");
        user2.setPhoneNumber("9876543211");
        user2.setEmail("jane.smith@example.com");
        user2.setBalance(new BigDecimal("5000.00"));
        user2.setBankName("HDFC Bank");
        user2.setAccountNumber("0987654321");
        user2.setIfscCode("HDFC0005678");
        userRepository.save(user2);
        
        // User 3
        User user3 = new User();
        user3.setUpiId("bob.wilson@sbi");
        user3.setName("Bob Wilson");
        user3.setPhoneNumber("9876543212");
        user3.setEmail("bob.wilson@example.com");
        user3.setBalance(new BigDecimal("7500.00"));
        user3.setBankName("State Bank of India");
        user3.setAccountNumber("1122334455");
        user3.setIfscCode("SBIN0009876");
        userRepository.save(user3);
        
        System.out.println("Sample users created successfully!");
    }
} 