package com.example.bdget.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class EventoModelTest {

    @Test
    void testGettersAndSetters() {
        Evento evento = new Evento();
        evento.setId(1L);
        evento.setNombre("Concierto Rock");
        evento.setFecha(LocalDate.of(2026, 12, 20));
        evento.setPrecioEntrada(15000);

        assertEquals(1L, evento.getId());
        assertEquals("Concierto Rock", evento.getNombre());
        assertEquals(LocalDate.of(2026, 12, 20), evento.getFecha());
        assertEquals(15000, evento.getPrecioEntrada());
    }
}
