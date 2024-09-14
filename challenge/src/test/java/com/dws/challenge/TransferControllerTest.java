package com.dws.challenge;

import com.dws.challenge.domain.TransferRequest;
import com.dws.challenge.service.TransferService;
import com.dws.challenge.web.TransferController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class TransferControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Mock
    private TransferService transferService;

    @InjectMocks
    private TransferController transferController;

    private MockMvc mockMvc;

    @Test
    public void testTransferMoney_Success() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        TransferRequest transferRequest = new TransferRequest(123456L, 654321L, new BigDecimal("100.00"));

        doNothing().when(transferService).transferMoney(transferRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accountFromId\":123456,\"accountToId\":654321,\"amount\":100.00}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Transfer successful"));

        verify(transferService).transferMoney(transferRequest);
    }

}
