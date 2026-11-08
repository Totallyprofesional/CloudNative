package com.example.bdget.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TicketRequest {
    @NotNull(message = "Falta id del evento")
    private Long eventoId;

    @NotBlank(message = "Indique el usuario")
    private String usuario;

    @NotNull(message = "Indique el numero de entradas")
    @Min(value = 1, message = "Seleccione al menos 1 entrada")
    private Integer cantidad;

    public Long getEventoId() {
        return eventoId;
    }

    public void setEventoId(Long eventoId) {
        this.eventoId = eventoId;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }   
}
