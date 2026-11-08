package com.example.bdget.service;

import com.example.bdget.dto.TicketRequest;
import com.example.bdget.dto.TicketResponse;

public interface TicketService {
    TicketResponse generarTicket(TicketRequest request);        
}
