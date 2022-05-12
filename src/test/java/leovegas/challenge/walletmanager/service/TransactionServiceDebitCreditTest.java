package leovegas.challenge.walletmanager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;
import leovegas.challenge.walletmanager.entity.Account;
import leovegas.challenge.walletmanager.entity.Transaction;
import leovegas.challenge.walletmanager.entity.TransactionType;
import leovegas.challenge.walletmanager.entity.User;
import leovegas.challenge.walletmanager.model.request.TransactionRequest;
import leovegas.challenge.walletmanager.model.response.TransactionResponse;
import leovegas.challenge.walletmanager.repository.AccountRepository;
import leovegas.challenge.walletmanager.repository.TransactionRepository;
import leovegas.challenge.walletmanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(SpringExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
class TransactionServiceDebitCreditTest {

    @Autowired
    private TransactionService transactionService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private AccountRepository accountRepository;
    @MockBean
    private TransactionRepository transactionRepository;
    private User user;
    private Account account;
    private Transaction lastTransaction;

    @BeforeAll
    public void init() {
        user = new User(1L, "Fernando", "Guerra", "fguerra");
        account = new Account(1L, 1234567890, user, new ArrayList<>());
        lastTransaction = new Transaction("29bd65ad-1335-4ad5-8d43-0e7a17fba5cc",
            Timestamp.valueOf("2022-05-01 11:22:00"), TransactionType.CREDIT, 100.00, 100.00,
            account);
    }

    @Test
    void givenDebitTransaction_whenEnoughFunds_thenReturnTransaction() {
        Mockito.when(transactionRepository.findById(lastTransaction.getId()))
            .thenReturn(Optional.empty());
        Mockito.when(userRepository.findByUsername(user.getUsername()))
            .thenReturn(Optional.of(user));
        Mockito.when(accountRepository.findByAccountNumberAndUser(account.getAccountNumber(), user))
            .thenReturn(Optional.of(account));
        Mockito.when(transactionRepository.findFirst1ByAccountOrderByCreationDateDesc(account))
            .thenReturn(Optional.of(lastTransaction));
        TransactionRequest transactionRequest =
            new TransactionRequest(user.getUsername(), account.getAccountNumber(),
                lastTransaction.getId(), 100.00, TransactionType.DEBIT);
        Transaction transactionSaved = new Transaction(transactionRequest.getTransactionId(),
            new Timestamp(Instant.now().toEpochMilli()), TransactionType.DEBIT,
            transactionRequest.getAmount(),
            lastTransaction.getBalance() - transactionRequest.getAmount(), account);
        Mockito.when(transactionRepository.save(any(Transaction.class)))
            .thenReturn(transactionSaved);

        TransactionResponse transactionResponse =
            transactionService.debitFromAccount(transactionRequest);

        verifySuccessfulTransaction();

        assertEquals(user.getUsername(), transactionResponse.getUsername());
        assertEquals(account.getAccountNumber(), transactionResponse.getAccountNumber());
        assertEquals(lastTransaction.getId(), transactionResponse.getTransactionId());
        assertEquals(transactionRequest.getType(), transactionResponse.getType());
        assertEquals(transactionRequest.getAmount(), transactionResponse.getAmount());
        assertEquals(0.00, transactionResponse.getBalance());
    }

    @Test
    void givenDebitTransaction_whenTransactionIdExists_thenThrowException() {
        Mockito.when(transactionRepository.findById(lastTransaction.getId()))
            .thenReturn(Optional.of(lastTransaction));
        TransactionRequest transactionRequest =
            new TransactionRequest(user.getUsername(), account.getAccountNumber(),
                lastTransaction.getId(), 100.00, TransactionType.DEBIT);

        assertThrows(ResponseStatusException.class,
            () -> transactionService.debitFromAccount(transactionRequest),
            "Transaction id already exists");

        verifyTransactionIdAlreadyExists();
    }

    @Test
    void givenDebitTransaction_whenAccountNumberDoesNotExist_thenThrowException() {
        Mockito.when(transactionRepository.findById(lastTransaction.getId()))
            .thenReturn(Optional.empty());
        Mockito.when(userRepository.findByUsername(user.getUsername()))
            .thenReturn(Optional.of(user));
        Mockito.when(accountRepository.findByAccountNumberAndUser(account.getAccountNumber(), user))
            .thenReturn(Optional.empty());
        TransactionRequest transactionRequest =
            new TransactionRequest(user.getUsername(), account.getAccountNumber(),
                lastTransaction.getId(), 100.00, TransactionType.DEBIT);

        assertThrows(ResponseStatusException.class,
            () -> transactionService.debitFromAccount(transactionRequest),
            "The specified account number doesn't exist for the indicated user");

        verifyAccountNumberDoesNotExist();
    }

    @Test
    void givenDebitTransaction_whenNotEnoughFunds_thenThrowException() {
        Mockito.when(transactionRepository.findById(lastTransaction.getId()))
            .thenReturn(Optional.empty());
        Mockito.when(userRepository.findByUsername(user.getUsername()))
            .thenReturn(Optional.of(user));
        Mockito.when(accountRepository.findByAccountNumberAndUser(account.getAccountNumber(), user))
            .thenReturn(Optional.of(account));
        Mockito.when(transactionRepository.findFirst1ByAccountOrderByCreationDateDesc(account))
            .thenReturn(Optional.of(lastTransaction));
        TransactionRequest transactionRequest =
            new TransactionRequest(user.getUsername(), account.getAccountNumber(),
                lastTransaction.getId(), 200.00, TransactionType.DEBIT);

        assertThrows(ResponseStatusException.class,
            () -> transactionService.debitFromAccount(transactionRequest), "Not enough funds");

        verifyWhenDebitNotEnoughFunds();
    }

    @Test
    void givenCreditTransaction_whenTransactionIdDoesNotExist_thenReturnTransaction() {
        Mockito.when(transactionRepository.findById(lastTransaction.getId()))
            .thenReturn(Optional.empty());
        Mockito.when(userRepository.findByUsername(user.getUsername()))
            .thenReturn(Optional.of(user));
        Mockito.when(accountRepository.findByAccountNumberAndUser(account.getAccountNumber(), user))
            .thenReturn(Optional.of(account));
        Mockito.when(transactionRepository.findFirst1ByAccountOrderByCreationDateDesc(account))
            .thenReturn(Optional.of(lastTransaction));
        TransactionRequest transactionRequest =
            new TransactionRequest(user.getUsername(), account.getAccountNumber(),
                lastTransaction.getId(), 100.00, TransactionType.CREDIT);
        Transaction transactionSaved = new Transaction(transactionRequest.getTransactionId(),
            new Timestamp(Instant.now().toEpochMilli()), TransactionType.CREDIT,
            transactionRequest.getAmount(),
            lastTransaction.getBalance() + transactionRequest.getAmount(), account);
        Mockito.when(transactionRepository.save(any(Transaction.class)))
            .thenReturn(transactionSaved);

        TransactionResponse transactionResponse =
            transactionService.creditFromAccount(transactionRequest);

        verifySuccessfulTransaction();

        assertEquals(user.getUsername(), transactionResponse.getUsername());
        assertEquals(account.getAccountNumber(), transactionResponse.getAccountNumber());
        assertEquals(lastTransaction.getId(), transactionResponse.getTransactionId());
        assertEquals(transactionRequest.getType(), transactionResponse.getType());
        assertEquals(transactionRequest.getAmount(), transactionResponse.getAmount());
        assertEquals(200.00, transactionResponse.getBalance());
    }

    @Test
    void givenCreditTransaction_whenTransactionIdExists_thenThrowException() {
        Mockito.when(transactionRepository.findById(lastTransaction.getId()))
            .thenReturn(Optional.of(lastTransaction));
        TransactionRequest transactionRequest =
            new TransactionRequest(user.getUsername(), account.getAccountNumber(),
                lastTransaction.getId(), 100.00, TransactionType.CREDIT);

        assertThrows(ResponseStatusException.class,
            () -> transactionService.creditFromAccount(transactionRequest),
            "Transaction id already exists");

        verifyTransactionIdAlreadyExists();
    }

    @Test
    void givenCreditTransaction_whenAccountNumberDoesNotExist_thenThrowException() {
        Mockito.when(transactionRepository.findById(lastTransaction.getId()))
            .thenReturn(Optional.empty());
        Mockito.when(userRepository.findByUsername(user.getUsername()))
            .thenReturn(Optional.of(user));
        Mockito.when(accountRepository.findByAccountNumberAndUser(account.getAccountNumber(), user))
            .thenReturn(Optional.empty());
        TransactionRequest transactionRequest =
            new TransactionRequest(user.getUsername(), account.getAccountNumber(),
                lastTransaction.getId(), 100.00, TransactionType.CREDIT);

        assertThrows(ResponseStatusException.class,
            () -> transactionService.creditFromAccount(transactionRequest),
            "The specified account number doesn't exist for the indicated user");

        verifyAccountNumberDoesNotExist();
    }

    private void verifySuccessfulTransaction() {
        Mockito.verify(transactionRepository, VerificationModeFactory.times(1))
            .findById(lastTransaction.getId());
        Mockito.verify(userRepository, VerificationModeFactory.times(1))
            .findByUsername(user.getUsername());
        Mockito.verify(accountRepository, VerificationModeFactory.times(1))
            .findByAccountNumberAndUser(account.getAccountNumber(), user);
        Mockito.verify(transactionRepository, VerificationModeFactory.times(1))
            .findFirst1ByAccountOrderByCreationDateDesc(account);
        Mockito.verify(transactionRepository, VerificationModeFactory.times(1))
            .save(any(Transaction.class));
        Mockito.reset(transactionRepository);
        Mockito.reset(userRepository);
        Mockito.reset(accountRepository);
    }

    private void verifyTransactionIdAlreadyExists() {
        Mockito.verify(transactionRepository, VerificationModeFactory.times(1))
            .findById(lastTransaction.getId());
        Mockito.reset(transactionRepository);
    }

    private void verifyAccountNumberDoesNotExist() {
        Mockito.verify(transactionRepository, VerificationModeFactory.times(1))
            .findById(lastTransaction.getId());
        Mockito.verify(userRepository, VerificationModeFactory.times(1))
            .findByUsername(user.getUsername());
        Mockito.verify(accountRepository, VerificationModeFactory.times(1))
            .findByAccountNumberAndUser(account.getAccountNumber(), user);
        Mockito.reset(transactionRepository);
        Mockito.reset(userRepository);
        Mockito.reset(accountRepository);
    }

    private void verifyWhenDebitNotEnoughFunds() {
        Mockito.verify(transactionRepository, VerificationModeFactory.times(1))
            .findById(lastTransaction.getId());
        Mockito.verify(userRepository, VerificationModeFactory.times(1))
            .findByUsername(user.getUsername());
        Mockito.verify(accountRepository, VerificationModeFactory.times(1))
            .findByAccountNumberAndUser(account.getAccountNumber(), user);
        Mockito.verify(transactionRepository, VerificationModeFactory.times(1))
            .findFirst1ByAccountOrderByCreationDateDesc(account);
        Mockito.reset(transactionRepository);
        Mockito.reset(userRepository);
        Mockito.reset(accountRepository);
    }

    @TestConfiguration
    static class TransactionServiceTestContextConfiguration {

        @Bean
        public TransactionService transactionService() {
            return new TransactionService();
        }
    }
}