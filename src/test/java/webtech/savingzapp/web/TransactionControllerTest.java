package webtech.savingzapp.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import webtech.savingzapp.model.Transaction;
import webtech.savingzapp.service.TransactionService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransactionService service;

    @BeforeEach
    void setUpMockRepository() {
        final Transaction t1 = new Transaction("wasser", "Food", LocalDate.of(2021, 1, 1), BigDecimal.valueOf(1.5));
        t1.setId(1L);
        when(service.getTransaction(1L)).thenReturn(Optional.of(t1));
    }

    @Test
    void testGetTransactionById() throws Exception {
        final String expected = "{\"id\":1,\"transactionName\":\"wasser\",\"transactionCategory\":\"Food\",\"transactionDate\":[2021,1,1],\"transactionAmount\":1.50}";
        this.mockMvc.perform(get("/transactions/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(equalTo(expected)));
    }

    @Test
    void testGetTransactionByIdNotFound() throws Exception {
        this.mockMvc.perform(get("/transactions/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetTransactionByIdBadRequest() throws Exception {
        this.mockMvc.perform(get("/transactions/abc"))
                .andExpect(status().isBadRequest());
    }


}