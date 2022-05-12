package leovegas.challenge.walletmanager.service;

import static leovegas.challenge.walletmanager.config.Constants.ATTRIBUTE_ORDERING_TRANSACTION_HISTORY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import leovegas.challenge.walletmanager.entity.Account;
import leovegas.challenge.walletmanager.entity.Transaction;
import leovegas.challenge.walletmanager.entity.TransactionType;
import leovegas.challenge.walletmanager.entity.User;
import leovegas.challenge.walletmanager.model.response.TransactionHistoryResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(SpringExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
class TransactionServiceTransactionHistoryTest {

    private final Integer pageNumber = 0;
    private final Integer pageSize = 10;
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
    private Page<Transaction> pagedTransactionList;
    private Pageable pageable;

    @BeforeAll
    public void init() {
        user = new User(1L, "Fernando", "Guerra", "fguerra");
        account = new Account(1L, 1234567890, user, new ArrayList<>());
        List<Transaction> transactionList = Arrays.asList(
            new Transaction("29bd65ad-1335-4ad5-8d43-0e7a17fba5cc",
                Timestamp.valueOf("2022-05-01 11:22:00"), TransactionType.CREDIT, 100.00, 100.00,
                account), new Transaction("a2d99328-5ca6-4413-99a0-d3cd9bd0ff15",
                Timestamp.valueOf("2022-05-03 10:12:00"), TransactionType.CREDIT, 100.00, 200.00,
                account), new Transaction("feaa7e00-1fc4-40bc-8d1b-4a39026ef6f",
                Timestamp.valueOf("2022-05-04 09:23:00"), TransactionType.DEBIT, 50.00, 150.00,
                account), new Transaction("d4ceef8f-ef85-4815-94aa-0deb405a7d7d",
                Timestamp.valueOf("2022-05-07 8:54:00"), TransactionType.CREDIT, 100.00, 250.00,
                account), new Transaction("8935f202-e77e-4ae3-80f2-e8b7d4c43fef",
                Timestamp.valueOf("2022-05-09 11:35:00"), TransactionType.DEBIT, 200.00, 50.00,
                account));
        pagedTransactionList = new PageImpl<>(transactionList);
        pageable =
            PageRequest.of(pageNumber, pageSize, Sort.by(ATTRIBUTE_ORDERING_TRANSACTION_HISTORY));
    }

    @Test
    void givenUsernamePageNumberPageSize_whenUserAndAccountExists_thenReturnTransactionHistory() {
        Mockito.when(userRepository.findByUsername(user.getUsername()))
            .thenReturn(Optional.of(user));
        Mockito.when(accountRepository.findByUser(user)).thenReturn(Optional.of(account));
        Mockito.when(transactionRepository.findAllByAccount(account, pageable))
            .thenReturn(pagedTransactionList);

        TransactionHistoryResponse transactionHistoryResponse =
            transactionService.getTransactionHistory(user.getUsername(), pageNumber, pageSize);

        verifySuccessfulTransactionHistory();

        assertEquals(user.getUsername(), transactionHistoryResponse.getUsername());
        assertEquals(account.getAccountNumber(), transactionHistoryResponse.getAccountNumber());
        assertEquals(pagedTransactionList.getSize(),
            transactionHistoryResponse.getTransactions().size());
        assertEquals(pagedTransactionList.getContent().get(4).getBalance(),
            transactionHistoryResponse.getTransactions().get(4).getBalance());
    }

    @Test
    void givenUsernamePageNumberPageSize_whenUserDoesNotExist_thenThrowException() {
        Mockito.when(userRepository.findByUsername(user.getUsername()))
            .thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
            () -> transactionService.getTransactionHistory(user.getUsername(), pageNumber,
                pageSize), "The username does not exist");

        verifyUsernameDoesNotExist();
    }

    @Test
    void givenUsernamePageNumberPageSize_whenAccountNumberDoesNotExist_thenThrowException() {
        Mockito.when(userRepository.findByUsername(user.getUsername()))
            .thenReturn(Optional.of(user));
        Mockito.when(accountRepository.findByUser(user)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
            () -> transactionService.getTransactionHistory(user.getUsername(), pageNumber,
                pageSize), "The specified username doesn't have an account");

        verifyAccountNumberDoesNotExist();
    }

    @Test
    void givenUsernamePageNumberPageSize_whenNoPreviousTransaction_thenReturnEmptyTransactionHistory() {
        Mockito.when(userRepository.findByUsername(user.getUsername()))
            .thenReturn(Optional.of(user));
        Mockito.when(accountRepository.findByUser(user)).thenReturn(Optional.of(account));
        Mockito.when(transactionRepository.findAllByAccount(account, pageable))
            .thenReturn(new PageImpl<>(new ArrayList<>()));

        TransactionHistoryResponse transactionHistoryResponse =
            transactionService.getTransactionHistory(user.getUsername(), pageNumber, pageSize);

        verifySuccessfulTransactionHistory();

        assertEquals(user.getUsername(), transactionHistoryResponse.getUsername());
        assertEquals(account.getAccountNumber(), transactionHistoryResponse.getAccountNumber());
        assertTrue(transactionHistoryResponse.getTransactions().isEmpty());
    }

    private void verifySuccessfulTransactionHistory() {
        Mockito.verify(userRepository, VerificationModeFactory.times(1))
            .findByUsername(user.getUsername());
        Mockito.verify(accountRepository, VerificationModeFactory.times(1)).findByUser(user);
        Mockito.verify(transactionRepository, VerificationModeFactory.times(1))
            .findAllByAccount(account, pageable);
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