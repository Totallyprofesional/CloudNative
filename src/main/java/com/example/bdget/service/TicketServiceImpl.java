package com.example.bdget.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.bdget.dto.TicketRequest;
import com.example.bdget.dto.TicketResponse;
import com.example.bdget.model.Evento;
import com.example.bdget.model.Ticket;
import com.example.bdget.repository.EventoRepository;
import com.example.bdget.repository.TicketRepository;

@Service
public class TicketServiceImpl implements TicketService {
    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Value("${efs.path}")
    private String efsPath;

    @Override
    public TicketResponse generarTicket(TicketRequest request) {
        Evento evento = eventoRepository.findById(request.getEventoId())
                .orElseThrow(() -> new RuntimeException(
                        "No existe el evento con id " + request.getEventoId()));

        Integer precioUnitario = evento.getPrecioEntrada();
        Integer total = precioUnitario * request.getCantidad();

        Ticket ticket = new Ticket();
        ticket.setEventoId(evento.getId());
        ticket.setUsuario(request.getUsuario());
        ticket.setCantidad(request.getCantidad());
        ticket.setPrecioUnitario(precioUnitario);
        ticket.setTotal(total);
        ticketRepository.save(ticket);

        String contenido = "Ticket N° " + ticket.getId() + "\n"
        + "Evento: " + evento.getNombre() + "\n"
        + "Usuario: " + ticket.getUsuario() + "\n"
        + "Cantidad de entradas: " + ticket.getCantidad() + "\n"
        + "Precio unitario: " + ticket.getPrecioUnitario() + "\n"
        + "Total: " + ticket.getTotal() + "\n";

        try {
        Path carpeta = Paths.get(efsPath, "evento" + evento.getId(), ticket.getUsuario());
        Files.createDirectories(carpeta);
        Files.writeString(carpeta.resolve("ticket" + ticket.getId() + ".txt"), contenido);
        } catch (IOException e) {
        throw new RuntimeException("No se pudo guardar el ticket en el EFS", e);
        }

        TicketResponse response = new TicketResponse();
        response.setId(ticket.getId());
        response.setUsuario(request.getUsuario());
        response.setNombreEvento(evento.getNombre());
        response.setCantidad(request.getCantidad());
        response.setPrecioUnitario(precioUnitario);
        response.setTotal(total);
        return response;
    }
}
