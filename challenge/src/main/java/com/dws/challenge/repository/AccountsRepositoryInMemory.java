package com.dws.challenge.repository;

import com.dws.challenge.domain.Account;
import com.dws.challenge.domain.TransferAccount;
import com.dws.challenge.exception.AccountNotFoundException;
import com.dws.challenge.exception.DuplicateAccountIdException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
public class AccountsRepositoryInMemory implements AccountsRepository {

    private final Map<String, Account> accounts = new ConcurrentHashMap<>();
    private final Map<Long, TransferAccount> transferFromAccountMap = new ConcurrentHashMap<>();
    private final Map<Long, TransferAccount> transferToAccountMap = new ConcurrentHashMap<>();

    @Override
    public void createAccount(Account account) throws DuplicateAccountIdException {
        Account previousAccount = accounts.putIfAbsent(account.getAccountId(), account);
        if (previousAccount != null) {
            throw new DuplicateAccountIdException(
                    "Account id " + account.getAccountId() + " already exists!");
        }
    }

    @Override
    public Account getAccount(String accountId) {
        return accounts.get(accountId);
    }

    @Override
    public void clearAccounts() {
        accounts.clear();
    }

    @Override
    public Optional<TransferAccount> findByIdFrom(Long accountId) throws AccountNotFoundException {
        Map<Long, TransferAccount> fromTransferAccountMap = accountFrom(accountId);
       boolean flagFrom = fromTransferAccountMap.containsKey(accountId);
        if(flagFrom){
            return Optional.of(fromTransferAccountMap.get(accountId));
        }else{
            log.error("From Account id {} not found ", accountId);
            throw new AccountNotFoundException("Account id " + accountId + " not found! ");
        }
    }

    @Override
    public Optional<TransferAccount> findByIdTo(Long accountId) throws AccountNotFoundException {
        Map<Long, TransferAccount> toTransferAccountMap = accountTo(accountId);
        boolean flagTo = toTransferAccountMap.containsKey(accountId);
        if(flagTo){
            return Optional.of(toTransferAccountMap.get(accountId));
        }else{
            log.error("To Account id {} not found ", accountId);
            throw new AccountNotFoundException("Account id " + accountId + " not found! ");
        }
    }

    public Map<Long, TransferAccount> accountFrom(Long accountId) {
        TransferAccount transferFromAccount = new TransferAccount();
        transferFromAccount.setId(654321L);
        transferFromAccount.setBalance(new BigDecimal("1000"));
        transferFromAccount.setOwner("User A");
        transferFromAccountMap.put(123456L, transferFromAccount);
        return transferFromAccountMap;
    }

    public Map<Long, TransferAccount> accountTo(Long accountId){
        TransferAccount transferToAccount = new TransferAccount();
        transferToAccount.setId(123456L);
        transferToAccount.setBalance(new BigDecimal("500"));
        transferToAccount.setOwner("User B");
        transferToAccountMap.put(accountId, transferToAccount);
        return transferToAccountMap;
    }

}
