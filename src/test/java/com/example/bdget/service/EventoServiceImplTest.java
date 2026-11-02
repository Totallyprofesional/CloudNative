package com.example.bdget.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.example.bdget.model.Evento;
import com.example.bdget.repository.EventoRepository;

@ExtendWith(MockitoExtension.class)
class EventoServiceImplTest {

    @Mock
    private EventoRepository repository;

    @InjectMocks
    private EventoServiceImpl service;

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
    void testGetEventosDisponibles() {
        List<Evento> expected = Arrays.asList(evento);
        when(repository.findByFechaGreaterThanEqualOrderByFechaAsc(any(LocalDate.class)))
                .thenReturn(expected);

        assertEquals(expected, service.getEventosDisponibles());
        verify(repository).findByFechaGreaterThanEqualOrderByFechaAsc(any(LocalDate.class));
    }

    @Test
    void testGetEventosDisponiblesListaVacia() {
        when(repository.findByFechaGreaterThanEqualOrderByFechaAsc(any(LocalDate.class)))
                .thenReturn(Collections.emptyList());

        assertTrue(service.getEventosDisponibles().isEmpty());
    }

    @Test
    void testCreateEvento() {
    when(repository.save(evento)).thenReturn(evento);

    Evento result = service.createEvento(evento);

    assertEquals(evento, result);
    verify(repository).save(evento);
    }
}