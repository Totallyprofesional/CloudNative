package com.example.bdget.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class TicketRequest {
    @NotNull(message = "Falta id del evento")
    private Long eventoId;

    @NotBlank(message = "Indique el usuario")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "El usuario solo puede tener letras, numeros, guion y guion bajo")
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
