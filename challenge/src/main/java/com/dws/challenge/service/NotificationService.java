package com.dws.challenge.service;

import com.dws.challenge.domain.Account;

import java.math.BigDecimal;

public interface NotificationService {

  void notifyAboutTransfer(Account account, String transferDescription);

  void notifyTransferMessage(Long id, BigDecimal transferredAmount, Long toAccountId);

  void notifyReceivedMessage(Long id,  BigDecimal receivedAmount, Long fromAccountId);
}
