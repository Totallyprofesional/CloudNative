package com.example.bdget.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.bdget.dto.EstadisticasResponse;
import com.example.bdget.dto.TicketRequest;
import com.example.bdget.dto.TicketResponse;
import com.example.bdget.dto.TicketUpdateRequest;
import com.example.bdget.model.Asset;
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

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Autowired
    private AwsService awsService;

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

        return toResponse(ticket, evento.getNombre());
    }

    @Override
    public String subirTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException(
                        "No existe el ticket con id " + ticketId));
        File archivo = rutaTicket(ticket).toFile();
        return awsService.uploadFile(bucket,
                "evento" + ticket.getEventoId() + "/" + ticket.getUsuario() + "/", archivo);
    }

    @Override
    public List<Asset> ticketsPorEvento(Long eventoId) {
        try {
            String prefijo = "evento" + eventoId + "/";
            return awsService.getS3Files(bucket).stream()
                    .filter(asset -> asset.getKey().startsWith(prefijo))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Error al listar tickets del evento " + eventoId, e);
        }
    }

    @Override
    public List<Asset> ticketsPorUsuario(String usuario) {
        try {
            String carpeta = "/" + usuario + "/";
            return awsService.getS3Files(bucket).stream()
                    .filter(asset -> asset.getKey().contains(carpeta))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Error al listar tickets del usuario " + usuario, e);
        }
    }

    @Override
    public TicketResponse actualizarTicket(Long ticketId, TicketUpdateRequest request) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException(
                        "No existe el ticket con id " + ticketId));
        Evento evento = eventoRepository.findById(ticket.getEventoId())
                .orElseThrow(() -> new RuntimeException(
                        "No existe el evento con id " + ticket.getEventoId()));

        ticket.setUsuario(request.getUsuario());
        ticket.setCantidad(request.getCantidad());
        ticket.setTotal(ticket.getPrecioUnitario() * request.getCantidad());
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
            throw new RuntimeException("No se pudo actualizar el ticket en el EFS", e);
        }

        return toResponse(ticket, evento.getNombre());
    }

    @Override
    public void eliminarTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException(
                        "No existe el ticket con id " + ticketId));

        try {
            Files.deleteIfExists(rutaTicket(ticket));
        } catch (IOException e) {
            throw new RuntimeException("No se pudo eliminar el ticket del EFS", e);
        }

        String key = "evento" + ticket.getEventoId() + "/" + ticket.getUsuario()
                + "/ticket" + ticket.getId() + ".txt";
        awsService.deleteObject(bucket, key);

        ticketRepository.delete(ticket);
    }

    @Override
    public EstadisticasResponse estadisticasPorEvento(Long eventoId) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException(
                        "No existe el evento con id " + eventoId));

        List<Ticket> tickets = ticketRepository.findByEventoId(eventoId);
        int entradasVendidas = tickets.stream().mapToInt(Ticket::getCantidad).sum();
        int ingresosTotales = tickets.stream().mapToInt(Ticket::getTotal).sum();

        EstadisticasResponse response = new EstadisticasResponse();
        response.setEventoId(evento.getId());
        response.setNombreEvento(evento.getNombre());
        response.setCantidadTickets(tickets.size());
        response.setEntradasVendidas(entradasVendidas);
        response.setIngresosTotales(ingresosTotales);
        return response;
    }

    private Path rutaTicket(Ticket ticket) {
        return Paths.get(efsPath, "evento" + ticket.getEventoId(),
                ticket.getUsuario(), "ticket" + ticket.getId() + ".txt");
    }

    private TicketResponse toResponse(Ticket ticket, String nombreEvento) {
        TicketResponse response = new TicketResponse();
        response.setId(ticket.getId());
        response.setUsuario(ticket.getUsuario());
        response.setNombreEvento(nombreEvento);
        response.setCantidad(ticket.getCantidad());
        response.setPrecioUnitario(ticket.getPrecioUnitario());
        response.setTotal(ticket.getTotal());
        return response;
    }
}