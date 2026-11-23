package com.example.bdget.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.bdget.dto.CompraRequest;
import com.example.bdget.dto.CompraResponse;
import com.example.bdget.service.CompraService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(CompraController.class)
@AutoConfigureMockMvc(addFilters = false)
class CompraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompraService service;

    @Autowired
    private ObjectMapper mapper;

    private CompraRequest request;
    private CompraResponse response;

    @BeforeEach
    void setUp() {
        request = new CompraRequest();
        request.setEventoId(1L);
        request.setCantidad(3);

        response = new CompraResponse();
        response.setNombreEvento("Concierto Dance");
        response.setCantidad(3);
        response.setPrecioUnitario(15000);
        response.setTotal(45000);
    }

    @Test
    void testCreateCompra() throws Exception {
        when(service.createCompra(any(CompraRequest.class))).thenReturn(response);

        mockMvc.perform(post("/compras")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(response)));
    }

    @Test
    void testCreateCompraInvalida() throws Exception {
        mockMvc.perform(post("/compras")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"eventoId\":1,\"cantidad\":0}"))
                .andExpect(status().isBadRequest());
    }
}