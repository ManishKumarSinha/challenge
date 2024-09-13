package com.dws.challenge.repository;

import com.dws.challenge.domain.Account;
import com.dws.challenge.domain.TransferAccount;
import com.dws.challenge.exception.AccountNotFoundException;
import com.dws.challenge.exception.DuplicateAccountIdException;

import java.util.Optional;

public interface AccountsRepository {

  void createAccount(Account account) throws DuplicateAccountIdException;

  Account getAccount(String accountId);

  void clearAccounts();

  Optional<TransferAccount> findByIdFrom(Long accountId) throws AccountNotFoundException;

  Optional<TransferAccount> findByIdTo(Long accountId);
}
