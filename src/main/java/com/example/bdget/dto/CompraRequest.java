package com.example.bdget.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CompraRequest {
    @NotNull(message = "Falta id del evento")
    private Long eventoId;
    @NotNull(message = "Indique la cantidad de entradas")
    @Min(value = 1, message = "Seleccione al menos 1 entrada")
    private Integer cantidad;

    public Long getEventoId() {
        return eventoId;
    }
    public void setEventoId(Long eventoId) {
        this.eventoId = eventoId;
    }
    public Integer getCantidad() {
        return cantidad;
    }
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}

