package com.example.bdget.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bdget.dto.CompraRequest;
import com.example.bdget.dto.CompraResponse;
import com.example.bdget.service.CompraService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/compras")
public class CompraController {
    @Autowired 
    private CompraService compraService;   
    
    @PostMapping 
    public CompraResponse createCompra(@Valid @RequestBody CompraRequest request) {
        return compraService.createCompra(request);
    }
}
