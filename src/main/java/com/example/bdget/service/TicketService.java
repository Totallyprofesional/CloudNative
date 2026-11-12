package com.example.bdget.service;

import java.util.List;

import com.example.bdget.dto.EstadisticasResponse;
import com.example.bdget.dto.TicketRequest;
import com.example.bdget.dto.TicketResponse;
import com.example.bdget.dto.TicketUpdateRequest;
import com.example.bdget.model.Asset;

public interface TicketService {

    TicketResponse generarTicket(TicketRequest request);

    String subirTicket(Long ticketId);

    List<Asset> ticketsPorEvento(Long eventoId);

    List<Asset> ticketsPorUsuario(String usuario);

    TicketResponse actualizarTicket(Long ticketId, TicketUpdateRequest request);

    void eliminarTicket(Long ticketId);

    EstadisticasResponse estadisticasPorEvento(Long eventoId);

    byte[] descargarTicket(Long ticketId);

}