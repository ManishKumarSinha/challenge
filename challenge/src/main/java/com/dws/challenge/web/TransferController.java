package com.dws.challenge.web;

import com.dws.challenge.domain.TransferRequest;
import com.dws.challenge.service.TransferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/transfer")
public class TransferController {

    private final TransferService transferService;

    @Autowired
    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> transferMoney(@RequestBody TransferRequest transferRequest) {
        log.info("Transfer Money From Account Id {} To Account Id {}", transferRequest.getAccountToId(), transferRequest.getAccountToId());

        transferService.transferMoney(transferRequest);
        return ResponseEntity.ok("Transfer successful");
    }
}
