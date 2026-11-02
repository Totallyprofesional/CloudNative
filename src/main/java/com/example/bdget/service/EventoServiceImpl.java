package com.example.bdget.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bdget.model.Evento;
import com.example.bdget.repository.EventoRepository;

@Service
public class EventoServiceImpl implements EventoService {
    @Autowired
    private EventoRepository eventoRepository;

    @Override
    public List<Evento> getEventosDisponibles() {
        return eventoRepository.findByFechaGreaterThanEqualOrderByFechaAsc(LocalDate.now());
    }

    @Override
    public Evento createEvento(Evento evento) {
        evento.setId(null);
        return eventoRepository.save(evento);
    }
}
