package com.dws.challenge.service;

import com.dws.challenge.domain.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class EmailNotificationService implements NotificationService {

  @Override
  public void notifyAboutTransfer(Account account, String transferDescription) {
    //THIS METHOD SHOULD NOT BE CHANGED - ASSUME YOUR COLLEAGUE WILL IMPLEMENT IT
    log
      .info("Sending notification to owner of {}: {}", account.getAccountId(), transferDescription);
  }
  @Override
  public void notifyTransferMessage(Long id, BigDecimal transferredAmount, Long toAccountId){
    //THIS METHOD SHOULD NOT BE CHANGED - ASSUME YOUR COLLEAGUE WILL IMPLEMENT IT
    log.info("Sending notification to owner of: {}  Transferred: {}  to account: {}", id, transferredAmount, toAccountId);
  }

  @Override
  public void notifyReceivedMessage(Long id,  BigDecimal receivedAmount, Long fromAccountId){
    //THIS METHOD SHOULD NOT BE CHANGED - ASSUME YOUR COLLEAGUE WILL IMPLEMENT IT
    log.info("Sending notification to owner of: {}  Received: {}  from account: {}", id, receivedAmount, fromAccountId);
  }



}
