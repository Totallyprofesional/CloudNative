package com.example.bdget.service;

import com.example.bdget.dto.CompraRequest;
import com.example.bdget.dto.CompraResponse;

public interface CompraService {
    CompraResponse createCompra(CompraRequest request);    
}
