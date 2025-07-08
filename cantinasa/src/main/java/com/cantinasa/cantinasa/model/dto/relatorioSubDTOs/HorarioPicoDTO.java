package com.cantinasa.cantinasa.model.dto.relatorioSubDTOs;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class HorarioPicoDTO {

    private LocalDateTime hora;
    private BigDecimal totalVendido;

    public LocalDateTime getHora() {
        return hora;
    }

    public void setHora(LocalDateTime hora) {
        this.hora = hora;
    }

    public BigDecimal getTotalVendido() {
        return totalVendido;
    }

    public void setTotalVendido(BigDecimal totalVendido) {
        this.totalVendido = totalVendido;
    }

    public HorarioPicoDTO() {
    }

    public HorarioPicoDTO(LocalDateTime hora, BigDecimal totalVendido) {
        this.hora = hora;
        this.totalVendido = totalVendido;
    }


}
