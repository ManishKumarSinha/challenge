package com.dws.challenge.domain;

import com.dws.challenge.exception.InsufficientFundsException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
public class TransferAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String owner;
    private BigDecimal balance;

    public synchronized void debit(BigDecimal amount) {
        if (this.balance.compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds in account: " + id);
        }
        this.balance = this.balance.subtract(amount);
    }

    public synchronized void credit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }
}
