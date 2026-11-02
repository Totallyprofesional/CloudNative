package com.example.bdget.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bdget.model.Evento;
import com.example.bdget.service.EventoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/eventos")
public class EventoController {
    @Autowired 
    private EventoService eventoService;   
    
    @GetMapping
    public List<Evento> getEventosDisponibles(){
        return eventoService.getEventosDisponibles();
    }        

    @PostMapping 
    public Evento createEvento(@Valid @RequestBody Evento evento) {
        return eventoService.createEvento(evento);
    }
}

