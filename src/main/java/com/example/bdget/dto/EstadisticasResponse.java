package com.example.bdget.dto;

public class EstadisticasResponse {

    private Long eventoId;
    private String nombreEvento;
    private Integer cantidadTickets;
    private Integer entradasVendidas;
    private Integer ingresosTotales;

    public Long getEventoId() {
        return eventoId;
    }
    public void setEventoId(Long eventoId) {
        this.eventoId = eventoId;
    }
    public String getNombreEvento() {
        return nombreEvento;
    }
    public void setNombreEvento(String nombreEvento) {
        this.nombreEvento = nombreEvento;
    }
    public Integer getCantidadTickets() {
        return cantidadTickets;
    }
    public void setCantidadTickets(Integer cantidadTickets) {
        this.cantidadTickets = cantidadTickets;
    }
    public Integer getEntradasVendidas() {
        return entradasVendidas;
    }
    public void setEntradasVendidas(Integer entradasVendidas) {
        this.entradasVendidas = entradasVendidas;
    }
    public Integer getIngresosTotales() {
        return ingresosTotales;
    }
    public void setIngresosTotales(Integer ingresosTotales) {
        this.ingresosTotales = ingresosTotales;
    }
}