package com.example.bdget.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.bdget.model.Evento;
import com.example.bdget.service.EventoService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(EventoController.class)
class EventoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventoService service;

    @Autowired
    private ObjectMapper mapper;

    private Evento evento;

    @BeforeEach
    void setUp() {
        evento = new Evento();
        evento.setId(1L);
        evento.setNombre("Concierto Dance");
        evento.setFecha(LocalDate.of(2026, 12, 20));
        evento.setPrecioEntrada(15000);
    }

    @Test
    void testGetEventosDisponibles() throws Exception {
        when(service.getEventosDisponibles()).thenReturn(Arrays.asList(evento));

        mockMvc.perform(get("/eventos"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(Arrays.asList(evento))));
    }

    @Test
    void testGetEventosDisponiblesListaVacia() throws Exception {
        when(service.getEventosDisponibles()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/eventos"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testCreateEvento() throws Exception {
    when(service.createEvento(any(Evento.class))).thenReturn(evento);

    mockMvc.perform(post("/eventos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(evento)))
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(evento)));
   }

   @Test
    void testCreateEventoInvalido() throws Exception {
    mockMvc.perform(post("/eventos")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"fecha\":\"2026-12-20\",\"precioEntrada\":15000}"))
            .andExpect(status().isBadRequest());
   }
}