package com.dws.challenge.service;

import com.dws.challenge.domain.TransferAccount;
import com.dws.challenge.domain.TransferRequest;
import com.dws.challenge.exception.AccountNotFoundException;
import com.dws.challenge.repository.AccountsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class TransferService {

    private final AccountsRepository accountRepository;

    private final NotificationService notificationService;

    @Autowired
    public TransferService(AccountsRepository accountRepository, NotificationService notificationService) {
        this.accountRepository = accountRepository;
        this.notificationService = notificationService;
    }


    //@Transactional
    public void transferMoney(TransferRequest transferRequest) {
        if (transferRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than zero");
        }

        TransferAccount accountFrom = accountRepository.findByIdFrom(transferRequest.getAccountFromId())
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + transferRequest.getAccountFromId()));

        TransferAccount accountTo = accountRepository.findByIdTo(transferRequest.getAccountToId())
               .orElseThrow(() -> new AccountNotFoundException("Account not found: " + transferRequest.getAccountToId()));

        // Ensure thread safety by locking accounts in a consistent order
        if (accountFrom.getId() < accountTo.getId()) {
            synchronized (accountFrom) {
                synchronized (accountTo) {
                    executeTransfer(accountFrom, accountTo, transferRequest.getAmount());
                }
            }
        } else {
            synchronized (accountTo) {
                synchronized (accountFrom) {
                    executeTransfer(accountTo, accountFrom, transferRequest.getAmount());
                }
            }
        }
    }

    private void executeTransfer(TransferAccount accountFrom, TransferAccount accountTo, BigDecimal amount) {
        accountFrom.debit(amount);
        accountTo.credit(amount);

        //accountRepository.save(accountFrom);
        //accountRepository.save(accountTo);

      // Send notifications
      notificationService.notifyTransferMessage(accountFrom.getId(), amount, accountTo.getId());
      notificationService.notifyReceivedMessage(accountTo.getId(), amount, accountFrom.getId());
    }
}
