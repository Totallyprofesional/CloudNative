package com.example.bdget.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bdget.dto.TicketRequest;
import com.example.bdget.dto.TicketResponse;
import com.example.bdget.service.TicketService;

import jakarta.validation.Valid;

@RestController 
@RequestMapping ("/tickets")
public class TicketController {
    @Autowired 
    private TicketService ticketService;
        
    @PostMapping
    public TicketResponse generarTicket(@Valid @RequestBody TicketRequest request) {
    return ticketService.generarTicket(request); 
    }   
}
