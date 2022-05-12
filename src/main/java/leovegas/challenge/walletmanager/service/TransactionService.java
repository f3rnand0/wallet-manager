package leovegas.challenge.walletmanager.service;

import static leovegas.challenge.walletmanager.config.Constants.ATTRIBUTE_ORDERING_TRANSACTION_HISTORY;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import leovegas.challenge.walletmanager.config.Constants;
import leovegas.challenge.walletmanager.entity.Account;
import leovegas.challenge.walletmanager.entity.Transaction;
import leovegas.challenge.walletmanager.entity.TransactionType;
import leovegas.challenge.walletmanager.entity.User;
import leovegas.challenge.walletmanager.model.request.TransactionRequest;
import leovegas.challenge.walletmanager.model.response.BalanceResponse;
import leovegas.challenge.walletmanager.model.response.TransactionHistoryResponse;
import leovegas.challenge.walletmanager.model.response.TransactionResponse;
import leovegas.challenge.walletmanager.repository.AccountRepository;
import leovegas.challenge.walletmanager.repository.TransactionRepository;
import leovegas.challenge.walletmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TransactionService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public BalanceResponse getBalance(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                Constants.MSG_USERNAME_NOT_FOUND));
        Account account = accountRepository.findByUser(user).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                Constants.MSG_ACCOUNT_NOT_FOUND));
        Transaction temporalTransaction = new Transaction(null, null, null, 0.00, 0.00, null);
        Transaction lastTransaction =
            transactionRepository.findFirst1ByAccountOrderByCreationDateDesc(account)
                .orElse(temporalTransaction);
        return new BalanceResponse(user.getFirstName(), user.getLastName(), user.getUsername(),
            account.getAccountNumber(), lastTransaction.getBalance());
    }

    public TransactionResponse debitFromAccount(TransactionRequest transactionRequest) {
        Transaction transactionSaved;
        Transaction transaction =
            transactionRepository.findById(transactionRequest.getTransactionId()).orElse(null);
        if (transaction != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                Constants.MSG_TRANSACTION_ID_ALREADY_EXISTS);
        } else {
            User user = userRepository.findByUsername(transactionRequest.getUsername()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                    Constants.MSG_USERNAME_NOT_FOUND));
            Account account =
                accountRepository.findByAccountNumberAndUser(transactionRequest.getAccountNumber(),
                    user).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                    Constants.MSG_ACCOUNT_NOT_FOUND_FOR_USER));

            // Verify if there are enough funds in the account
            Transaction lastTransaction =
                transactionRepository.findFirst1ByAccountOrderByCreationDateDesc(account)
                    .orElse(new Transaction(null, null, null, 0.00, 0.00, null));
            double newBalance = lastTransaction.getBalance() - transactionRequest.getAmount();
            if (newBalance < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    Constants.MSG_NOT_ENOUGH_FUNDS);
            } else {
                Transaction transactionToSave =
                    new Transaction(transactionRequest.getTransactionId(),
                        new Timestamp(Instant.now().toEpochMilli()), TransactionType.DEBIT,
                        transactionRequest.getAmount(), newBalance, account);
                transactionSaved = transactionRepository.save(transactionToSave);
            }
        }
        return new TransactionResponse(transactionRequest.getUsername(),
            transactionRequest.getAccountNumber(), transactionSaved.getId(),
            transactionSaved.getType(), transactionSaved.getCreationDate(),
            transactionSaved.getAmount(), transactionSaved.getBalance());
    }

    public TransactionResponse creditFromAccount(TransactionRequest transactionRequest) {
        Transaction transactionSaved;
        Transaction transaction =
            transactionRepository.findById(transactionRequest.getTransactionId()).orElse(null);
        if (transaction != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                Constants.MSG_TRANSACTION_ID_ALREADY_EXISTS);
        } else {
            User user = userRepository.findByUsername(transactionRequest.getUsername()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                    Constants.MSG_USERNAME_NOT_FOUND));
            Account account =
                accountRepository.findByAccountNumberAndUser(transactionRequest.getAccountNumber(),
                    user).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                    Constants.MSG_ACCOUNT_NOT_FOUND_FOR_USER));

            Transaction lastTransaction =
                transactionRepository.findFirst1ByAccountOrderByCreationDateDesc(account)
                    .orElse(new Transaction(null, null, null, 0.00, 0.00, null));
            Double newBalance = lastTransaction.getBalance() + transactionRequest.getAmount();
            Transaction transactionToSave = new Transaction(transactionRequest.getTransactionId(),
                new Timestamp(Instant.now().toEpochMilli()), TransactionType.CREDIT,
                transactionRequest.getAmount(), newBalance, account);
            transactionSaved = transactionRepository.save(transactionToSave);

        }
        return new TransactionResponse(transactionRequest.getUsername(),
            transactionRequest.getAccountNumber(), transactionSaved.getId(),
            transactionSaved.getType(), transactionSaved.getCreationDate(),
            transactionSaved.getAmount(), transactionSaved.getBalance());
    }

    public TransactionHistoryResponse getTransactionHistory(String username, int pageNumber,
        int pageSize) {
        User user = userRepository.findByUsername(username).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                Constants.MSG_USERNAME_NOT_FOUND));
        Account account = accountRepository.findByUser(user).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                Constants.MSG_ACCOUNT_NOT_FOUND));

        Pageable pageable =
            PageRequest.of(pageNumber, pageSize, Sort.by(ATTRIBUTE_ORDERING_TRANSACTION_HISTORY));
        Page<Transaction> pagedTransactionList =
            Optional.ofNullable(transactionRepository.findAllByAccount(account, pageable))
                .orElse(new PageImpl<>(new ArrayList<>()));
        List<TransactionResponse> transactionList = pagedTransactionList.stream().map(
            t -> new TransactionResponse(t.getId(), t.getType(), t.getCreationDate(), t.getAmount(),
                t.getBalance())).collect(Collectors.toList());
        return new TransactionHistoryResponse(username, account.getAccountNumber(),
            transactionList);
    }
}
