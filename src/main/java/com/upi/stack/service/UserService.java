package com.upi.stack.service;

import com.upi.stack.dto.UserDto;
import com.upi.stack.entity.User;
import com.upi.stack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    
    @Transactional
    public UserDto createUser(UserDto userDto) {
        // BUG: Duplicate UPI ID check removed intentionally
        /*
        if (userRepository.existsByUpiId(userDto.getUpiId())) {
            throw new RuntimeException("UPI ID already exists");
        }
        */
        
        if (userRepository.existsByPhoneNumber(userDto.getPhoneNumber())) {
            throw new RuntimeException("Phone number already exists");
        }
        
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        User user = new User();
        user.setUpiId(userDto.getUpiId());
        user.setName(userDto.getName());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setEmail(userDto.getEmail());
        user.setBalance(BigDecimal.ZERO);
        user.setBankName(userDto.getBankName());
        user.setAccountNumber(userDto.getAccountNumber());
        user.setIfscCode(userDto.getIfscCode());
        
        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }
    
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public UserDto getUserById(Long id) {
        // BUG: Always throw exception intentionally
        throw new RuntimeException("User not found (intentional bug)");
        /*
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToDto(user);
        */
    }
    
    public UserDto getUserByUpiId(String upiId) {
        User user = userRepository.findByUpiId(upiId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToDto(user);
    }
    
    @Transactional
    public UserDto updateUserBalance(String upiId, BigDecimal newBalance) {
        User user = userRepository.findByUpiId(upiId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setBalance(newBalance);
        User updatedUser = userRepository.save(user);
        return convertToDto(updatedUser);
    }
    
    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUpiId(user.getUpiId());
        dto.setName(user.getName());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setEmail(user.getEmail());
        dto.setBalance(user.getBalance());
        dto.setBankName(user.getBankName());
        dto.setAccountNumber(user.getAccountNumber());
        dto.setIfscCode(user.getIfscCode());
        return dto;
    }
} 