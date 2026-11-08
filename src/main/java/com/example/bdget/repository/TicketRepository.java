package com.example.bdget.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bdget.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByEventoId(Long eventoId);

    List<Ticket> findByUsuario(String usuario);
}
