package com.example.bdget.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.bdget.dto.CompraRequest;
import com.example.bdget.dto.CompraResponse;
import com.example.bdget.model.Compra;
import com.example.bdget.model.Evento;
import com.example.bdget.repository.CompraRepository;
import com.example.bdget.repository.EventoRepository;

@ExtendWith(MockitoExtension.class)
public class CompraServiceImplTest {
    @Mock
    private EventoRepository eventoRepository;

    @Mock
    private CompraRepository compraRepository;

    @InjectMocks
    private CompraServiceImpl service;

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
    void testCreateCompra() {
        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));
        CompraRequest request = new CompraRequest();
        request.setEventoId(1L);
        request.setCantidad(3);
        CompraResponse result = service.createCompra(request);
        
        assertEquals("Concierto Dance", result.getNombreEvento());
        assertEquals(3, result.getCantidad());
        assertEquals(15000, result.getPrecioUnitario());
        assertEquals(45000, result.getTotal());
        verify(compraRepository).save(any(Compra.class));
    }

    @Test 
    void eventoNullException(){
        when(eventoRepository.findById(99L)).thenReturn(Optional.empty());
        CompraRequest request = new CompraRequest();
        request.setEventoId(99L);
        request.setCantidad(3);
        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> service.createCompra(request));
        assertEquals("No existe el evento con id 99", ex.getMessage());
        verify(compraRepository, never()).save(any(Compra.class));                          
    }
}
