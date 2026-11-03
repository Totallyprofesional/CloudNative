package com.example.bdget.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bdget.dto.CompraRequest;
import com.example.bdget.dto.CompraResponse;
import com.example.bdget.model.Compra;
import com.example.bdget.model.Evento;
import com.example.bdget.repository.CompraRepository;
import com.example.bdget.repository.EventoRepository;

@Service
public class CompraServiceImpl implements CompraService {

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private CompraRepository compraRepository;

    @Override
    public CompraResponse createCompra(CompraRequest request) {
        Evento evento = eventoRepository.findById(request.getEventoId())
                .orElseThrow(() -> new RuntimeException(
                        "No existe el evento con id " + request.getEventoId()));
                        
        Integer precioUnitario = evento.getPrecioEntrada();
        Integer total = precioUnitario * request.getCantidad();

        Compra compra = new Compra();
        compra.setEventoId(evento.getId());
        compra.setCantidad(request.getCantidad());
        compra.setPrecioUnitario(precioUnitario);
        compra.setTotal(total);
        compraRepository.save(compra);
        
        CompraResponse response = new CompraResponse();
        response.setNombreEvento(evento.getNombre());
        response.setCantidad(request.getCantidad());
        response.setPrecioUnitario(precioUnitario);
        response.setTotal(total);
        return response;
    }
}