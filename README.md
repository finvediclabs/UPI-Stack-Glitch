# UPI Stack - Spring Boot Application

A complete Spring Boot application for UPI (Unified Payment Interface) payment processing system.

## Features

- **User Management**: Create and manage UPI users with bank account details
- **Transaction Processing**: Initiate and process UPI payments between users
- **Balance Management**: Real-time balance updates for users
- **Transaction History**: Track all payment transactions
- **RESTful API**: Complete REST API for all operations
- **H2 Database**: In-memory database for development and testing
- **Security**: Basic security configuration with CORS support

## Technology Stack

- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **Spring Security**
- **H2 Database**
- **Lombok**
- **Maven**
- **Java 17**

## Project Structure

```
src/main/java/com/upi/stack/
├── UpiStackApplication.java          # Main application class
├── config/
│   ├── SecurityConfig.java          # Security configuration
│   └── DataInitializer.java        # Sample data initialization
├── controller/
│   ├── UserController.java          # User REST endpoints
│   └── TransactionController.java   # Transaction REST endpoints
├── dto/
│   ├── UserDto.java                # User data transfer object
│   └── TransactionDto.java         # Transaction data transfer object
├── entity/
│   ├── User.java                   # User entity
│   └── Transaction.java            # Transaction entity
├── repository/
│   ├── UserRepository.java         # User data access
│   └── TransactionRepository.java  # Transaction data access
└── service/
    ├── UserService.java            # User business logic
    └── TransactionService.java     # Transaction business logic
```

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Running the Application

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd UPI-Stack-Glitch
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application**
   - Application: http://localhost:8080
   - H2 Console: http://localhost:8080/h2-console
   - API Documentation: Available at the endpoints below

## API Endpoints

### User Management

#### Create User
```http
POST /api/users
Content-Type: application/json

{
  "upiId": "user@bank",
  "name": "User Name",
  "phoneNumber": "9876543210",
  "email": "user@example.com",
  "bankName": "Bank Name",
  "accountNumber": "1234567890",
  "ifscCode": "BANK0001234"
}
```

#### Get All Users
```http
GET /api/users
```

#### Get User by ID
```http
GET /api/users/{id}
```

#### Get User by UPI ID
```http
GET /api/users/upi/{upiId}
```

#### Update User Balance
```http
PUT /api/users/{upiId}/balance?balance=1000.00
```

### Transaction Management

#### Initiate Payment
```http
POST /api/transactions/pay
Content-Type: application/json

{
  "payerUpiId": "john.doe@icici",
  "payeeUpiId": "jane.smith@hdfc",
  "amount": 500.00,
  "description": "Payment for services"
}
```

#### Get All Transactions
```http
GET /api/transactions
```

#### Get Transaction by ID
```http
GET /api/transactions/{transactionId}
```

#### Get User Transactions
```http
GET /api/transactions/user/{upiId}
```

## Sample Data

The application comes with pre-loaded sample users:

1. **John Doe** (john.doe@icici)
   - Balance: ₹10,000
   - Bank: ICICI Bank

2. **Jane Smith** (jane.smith@hdfc)
   - Balance: ₹5,000
   - Bank: HDFC Bank

3. **Bob Wilson** (bob.wilson@sbi)
   - Balance: ₹7,500
   - Bank: State Bank of India

## Testing the Application

### 1. Create a New User
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "upiId": "newuser@axis",
    "name": "New User",
    "phoneNumber": "9876543213",
    "email": "newuser@example.com",
    "bankName": "Axis Bank",
    "accountNumber": "9988776655",
    "ifscCode": "AXIS0009999"
  }'
```

### 2. Make a Payment
```bash
curl -X POST http://localhost:8080/api/transactions/pay \
  -H "Content-Type: application/json" \
  -d '{
    "payerUpiId": "john.doe@icici",
    "payeeUpiId": "jane.smith@hdfc",
    "amount": 1000.00,
    "description": "Payment for lunch"
  }'
```

### 3. Check User Balance
```bash
curl http://localhost:8080/api/users/upi/john.doe@icici
```

### 4. View Transaction History
```bash
curl http://localhost:8080/api/transactions/user/john.doe@icici
```

## Database Schema

### Users Table
- `id` (Primary Key)
- `upi_id` (Unique)
- `name`
- `phone_number`
- `email`
- `balance`
- `bank_name`
- `account_number`
- `ifsc_code`
- `created_at`
- `updated_at`

### Transactions Table
- `id` (Primary Key)
- `transaction_id` (Unique)
- `payer_id` (Foreign Key)
- `payee_id` (Foreign Key)
- `amount`
- `description`
- `status` (PENDING, SUCCESS, FAILED, CANCELLED)
- `type` (PAY, REQUEST, COLLECT)
- `created_at`
- `updated_at`
- `failure_reason`

## Configuration

The application uses the following configuration (in `application.yml`):

- **Port**: 8080
- **Database**: H2 in-memory
- **Security**: Basic authentication disabled for API endpoints
- **CORS**: Enabled for all origins

## Development

### Adding New Features

1. Create entity classes in the `entity` package
2. Create DTOs in the `dto` package
3. Create repositories in the `repository` package
4. Create services in the `service` package
5. Create controllers in the `controller` package

### Testing

Run tests with:
```bash
mvn test
```

## Troubleshooting

### Common Issues

1. **Port already in use**: Change the port in `application.yml`
2. **Database connection issues**: Check H2 console at http://localhost:8080/h2-console
3. **CORS issues**: The application is configured to allow all origins

### Logs

Check application logs for detailed error information. The application logs at DEBUG level for the `com.upi.stack` package.

## License

This project is for educational purposes.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

---

**Note**: This is a demonstration application and should not be used in production without proper security measures, validation, and error handling.

## Intentional Bugs and How to Fix Them

This project contains 4 intentional bugs for demonstration and learning purposes. Here are the bugs and how to solve them:

### 1. Duplicate UPI IDs Allowed
**Bug:** The application allows creation of users with duplicate UPI IDs.

**How to Fix:**
Uncomment the duplicate UPI ID check in `UserService.createUser`:
```java
if (userRepository.existsByUpiId(userDto.getUpiId())) {
    throw new RuntimeException("UPI ID already exists");
}
```

### 2. Payment Allowed with Insufficient Balance
**Bug:** The application allows a payment to proceed even if the payer does not have enough balance.

**How to Fix:**
Uncomment the balance check in `TransactionService.initiatePayment`:
```java
if (payer.getBalance().compareTo(transactionDto.getAmount()) < 0) {
    throw new RuntimeException("Insufficient balance");
}
```

### 3. User Retrieval Always Fails by ID
**Bug:** The `getUserById` method in `UserService` always throws an exception, so you can never retrieve a user by ID.

**How to Fix:**
Replace the method with the correct implementation:
```java
public UserDto getUserById(Long id) {
    User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
    return convertToDto(user);
}
```

### 4. Transaction Status Always Set to FAILED
**Bug:** Even if a payment is successful, the transaction status is always set to FAILED.

**How to Fix:**
Set the status to SUCCESS on successful payment in `TransactionService.processPayment`:
```java
transaction.setStatus(Transaction.TransactionStatus.SUCCESS);
transaction.setFailureReason(null);
```

---

Remove the comments and restore the original logic to fix these bugs.
