package com.example.bdget.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class TicketUpdateRequest {
    @NotNull(message = "Indique el numero de entradas")
    @Min(value = 1, message = "Seleccione al menos 1 entrada")
    private Integer cantidad;

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}