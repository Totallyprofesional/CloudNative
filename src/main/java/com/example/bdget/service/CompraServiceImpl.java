package com.example.bdget.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

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

    @Override
    public File generarResumen(Long compraId) {
        Compra compra = compraRepository.findById(compraId)
                .orElseThrow(() -> new RuntimeException(
                        "No existe la compra con id " + compraId));
        Evento evento = eventoRepository.findById(compra.getEventoId())
                .orElseThrow(() -> new RuntimeException(
                        "Id del evento no encontrado: " + compra.getEventoId()));
        String contenido = "Resumen de Compra N° " + compra.getId() + "\n"
                + "Evento: " + evento.getNombre() + "\n"
                + "Cantidad de entradas: " + compra.getCantidad() + "\n"
                + "Precio unitario: " + compra.getPrecioUnitario() + "\n"
                + "Total: " + compra.getTotal() + "\n";

        File archivo = new File("resumen_" + compra.getId() + ".txt");
        try {
            Files.writeString(archivo.toPath(), contenido);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el archivo del resumen", e);
        }
        return archivo;
    }

    @Override
    public Long obtenerEventoIdPorCompra(Long compraId) {
        return compraRepository.findById(compraId)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada"))
                .getEventoId();
    }
}