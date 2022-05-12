package leovegas.challenge.walletmanager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Optional;
import leovegas.challenge.walletmanager.entity.Account;
import leovegas.challenge.walletmanager.entity.Transaction;
import leovegas.challenge.walletmanager.entity.TransactionType;
import leovegas.challenge.walletmanager.entity.User;
import leovegas.challenge.walletmanager.model.response.BalanceResponse;
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
class TransactionServiceBalanceTest {

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
    void givenUsername_whenUserAndAccountExists_thenReturnBalance() {
        Mockito.when(userRepository.findByUsername(user.getUsername()))
            .thenReturn(Optional.of(user));
        Mockito.when(accountRepository.findByUser(user)).thenReturn(Optional.of(account));
        Mockito.when(transactionRepository.findFirst1ByAccountOrderByCreationDateDesc(account))
            .thenReturn(Optional.ofNullable(lastTransaction));

        BalanceResponse balanceResponse = transactionService.getBalance(user.getUsername());

        verifySuccessfulBalance();

        assertEquals(user.getUsername(), balanceResponse.getUsername());
        assertEquals(account.getAccountNumber(), balanceResponse.getAccountNumber());
        assertEquals(lastTransaction.getBalance(), balanceResponse.getBalance());
    }

    @Test
    void givenUsername_whenUserDoesNotExist_thenThrowException() {
        Mockito.when(userRepository.findByUsername(user.getUsername()))
            .thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
            () -> transactionService.getBalance(user.getUsername()), "The username does not exist");

        verifyUsernameDoesNotExist();
    }

    @Test
    void givenUsername_whenAccountNumberDoesNotExist_thenThrowException() {
        Mockito.when(userRepository.findByUsername(user.getUsername()))
            .thenReturn(Optional.of(user));
        Mockito.when(accountRepository.findByUser(user)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
            () -> transactionService.getBalance(user.getUsername()),
            "The specified username doesn't have an account");

        verifyAccountNumberDoesNotExist();
    }

    @Test
    void givenUsername_whenPreviousTransactionDoesNotExist_thenReturnBalance() {
        Mockito.when(userRepository.findByUsername(user.getUsername()))
            .thenReturn(Optional.of(user));
        Mockito.when(accountRepository.findByUser(user)).thenReturn(Optional.of(account));
        Mockito.when(transactionRepository.findFirst1ByAccountOrderByCreationDateDesc(account))
            .thenReturn(Optional.empty());

        BalanceResponse balanceResponse = transactionService.getBalance(user.getUsername());

        verifySuccessfulBalance();

        assertEquals(user.getUsername(), balanceResponse.getUsername());
        assertEquals(account.getAccountNumber(), balanceResponse.getAccountNumber());
        assertEquals(0.00, balanceResponse.getBalance());
    }

    private void verifySuccessfulBalance() {
        Mockito.verify(userRepository, VerificationModeFactory.times(1))
            .findByUsername(user.getUsername());
        Mockito.verify(accountRepository, VerificationModeFactory.times(1)).findByUser(user);
        Mockito.verify(transactionRepository, VerificationModeFactory.times(1))
            .findFirst1ByAccountOrderByCreationDateDesc(account);
        Mockito.reset(userRepository);
        Mockito.reset(accountRepository);
        Mockito.reset(transactionRepository);
    }

    private void verifyUsernameDoesNotExist() {
        Mockito.verify(userRepository, VerificationModeFactory.times(1))
            .findByUsername(user.getUsername());
        Mockito.reset(userRepository);
    }

    private void verifyAccountNumberDoesNotExist() {
        Mockito.verify(userRepository, VerificationModeFactory.times(1))
            .findByUsername(user.getUsername());
        Mockito.verify(accountRepository, VerificationModeFactory.times(1)).findByUser(user);
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