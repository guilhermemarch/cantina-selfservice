package com.cantinasa.cantinasa.model.mapper;

import com.cantinasa.cantinasa.model.Pagamento;
import com.cantinasa.cantinasa.model.dto.PagamentoDTO;
import com.cantinasa.cantinasa.model.enums.tipoPagamento;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PagamentoMapper {

    public PagamentoDTO toDTO(Pagamento pagamento) {
        if (pagamento == null) return null;
        
        PagamentoDTO dto = new PagamentoDTO();
        dto.setIdPagamento(pagamento.getId());
        dto.setPedidoId(pagamento.getPedido().getId());
        dto.setValor(pagamento.getValor().doubleValue());
        dto.setTipoPagamento(convertToDTOMetodoPagamento(pagamento.getMetodo()));
        dto.setTroco(pagamento.getTroco());
        
        return dto;
    }

    public Pagamento toEntity(PagamentoDTO dto) {
        if (dto == null) return null;
        
        Pagamento pagamento = new Pagamento();
        pagamento.setId(dto.getIdPagamento());
        pagamento.setValor(BigDecimal.valueOf(dto.getValor()));
        pagamento.setMetodo(convertToEntityMetodoPagamento(dto.getTipoPagamento()));
        pagamento.setTroco(dto.getTroco());
        
        return pagamento;
    }

    public List<PagamentoDTO> toDTOList(List<Pagamento> pagamentos) {
        if (pagamentos == null) return null;
        return pagamentos.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public List<Pagamento> toEntityList(List<PagamentoDTO> dtos) {
        if (dtos == null) return null;
        return dtos.stream()
            .map(this::toEntity)
            .collect(Collectors.toList());
    }

    private tipoPagamento convertToDTOMetodoPagamento(Pagamento.MetodoPagamento metodo) {
        if (metodo == null) return null;
        
        return switch (metodo) {
            case DINHEIRO -> tipoPagamento.DINHEIRO;
            case CARTAO -> tipoPagamento.CARTAO;
            case PIX -> tipoPagamento.PIX;
        };
    }

    private Pagamento.MetodoPagamento convertToEntityMetodoPagamento(tipoPagamento tipo) {
        if (tipo == null) return null;
        
        return switch (tipo) {
            case DINHEIRO -> Pagamento.MetodoPagamento.DINHEIRO;
            case CARTAO -> Pagamento.MetodoPagamento.CARTAO;
            case PIX -> Pagamento.MetodoPagamento.PIX;
        };
    }
} 