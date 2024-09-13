package com.dws.challenge;

import com.dws.challenge.domain.TransferAccount;
import com.dws.challenge.domain.TransferRequest;
import com.dws.challenge.exception.AccountNotFoundException;
import com.dws.challenge.repository.AccountsRepository;
import com.dws.challenge.service.NotificationService;
import com.dws.challenge.service.TransferService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TransferServiceTest {

    @Mock
    private AccountsRepository accountsRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private TransferService transferService;

    @Test
    public void testTransferMoney_Success() {

        TransferAccount accountFrom = new TransferAccount();
        accountFrom.setId(1L);
        accountFrom.setOwner("User A");
        accountFrom.setBalance(new BigDecimal("200.00"));

        TransferAccount accountTo = new TransferAccount();
        accountTo.setId(2L);
        accountTo.setOwner("User B");
        accountTo.setBalance(new BigDecimal("100.00"));

        TransferRequest transferRequest = new TransferRequest(1L, 2L, new BigDecimal("50.00"));

        when(accountsRepository.findByIdFrom(1L)).thenReturn(Optional.of(accountFrom));
        when(accountsRepository.findByIdTo(2L)).thenReturn(Optional.of(accountTo));

        transferService.transferMoney(transferRequest);

        assertEquals(new BigDecimal("150.00"), accountFrom.getBalance());
        assertEquals(new BigDecimal("150.00"), accountTo.getBalance());

        verify(accountsRepository).findByIdFrom(1L);
        verify(accountsRepository).findByIdTo(2L);
        verify(notificationService).notifyTransferMessage(1L, new BigDecimal("50.00"), 2L);
        verify(notificationService).notifyReceivedMessage(2L, new BigDecimal("50.00"), 1L);
    }

    @Test
    public void testTransferMoney_AccountNotFound() {
        TransferRequest transferRequest = new TransferRequest(1L, 2L, new BigDecimal("50.00"));

        when(accountsRepository.findByIdFrom(1L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> {
            transferService.transferMoney(transferRequest);
        });

        verify(accountsRepository).findByIdFrom(1L);
        verify(accountsRepository, never()).findByIdTo(anyLong());
    }

    @Test
    public void testTransferMoney_AmountLessThanZero() {
        TransferRequest transferRequest = new TransferRequest(1L, 2L, new BigDecimal("-50.00"));

        assertThrows(IllegalArgumentException.class, () -> {
            transferService.transferMoney(transferRequest);
        });

        verify(accountsRepository, never()).findByIdFrom(anyLong());
        verify(accountsRepository, never()).findByIdTo(anyLong());
    }
}

