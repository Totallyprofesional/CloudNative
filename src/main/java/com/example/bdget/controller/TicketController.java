package com.example.bdget.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.bdget.dto.EstadisticasResponse;
import com.example.bdget.dto.TicketRequest;
import com.example.bdget.dto.TicketResponse;
import com.example.bdget.dto.TicketUpdateRequest;
import com.example.bdget.model.Asset;
import com.example.bdget.service.TicketService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TicketResponse generarTicket(@Valid @RequestBody TicketRequest request) {
        return ticketService.generarTicket(request);
    }

    @PostMapping("/{id}/subir")
    public String subirTicket(@PathVariable Long id) {
        return ticketService.subirTicket(id);
    }

    @GetMapping("/evento/{eventoId}")
    public List<Asset> ticketsPorEvento(@PathVariable Long eventoId) {
        return ticketService.ticketsPorEvento(eventoId);
    }

    @GetMapping("/usuario/{usuario}")
    public List<Asset> ticketsPorUsuario(@PathVariable String usuario) {
        return ticketService.ticketsPorUsuario(usuario);
    }

    @PutMapping("/{id}")
    public TicketResponse actualizarTicket(@PathVariable Long id,
                                           @Valid @RequestBody TicketUpdateRequest request) {
        return ticketService.actualizarTicket(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarTicket(@PathVariable Long id) {
        ticketService.eliminarTicket(id);
    }

    @GetMapping("/evento/{eventoId}/estadisticas")
    public EstadisticasResponse estadisticasPorEvento(@PathVariable Long eventoId) {
        return ticketService.estadisticasPorEvento(eventoId);
    }
}