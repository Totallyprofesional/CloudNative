package com.example.bdget.service;

import java.util.List;

import com.example.bdget.model.Evento;

public interface EventoService {
    List<Evento> getEventosDisponibles();

    Evento createEvento(Evento evento);
}

