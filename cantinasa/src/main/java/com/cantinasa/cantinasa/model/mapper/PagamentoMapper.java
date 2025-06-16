package com.cantinasa.cantinasa.model.mapper;

import com.cantinasa.cantinasa.model.Pagamento;
import com.cantinasa.cantinasa.model.dto.PagamentoDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PagamentoMapper {

    public PagamentoDTO toDTO(Pagamento pagamento) {
        if (pagamento == null) return null;
        
        PagamentoDTO dto = new PagamentoDTO();
        dto.setIdPagamento(pagamento.getIdPagamento());
        dto.setTipoPagamento(pagamento.getTipoPagamento());
        dto.setValor(pagamento.getValor());
        dto.setTroco(pagamento.getTroco());
        dto.setPedidoId(pagamento.getPedido().getIdPedido());
        
        return dto;
    }

    public Pagamento toEntity(PagamentoDTO dto) {
        if (dto == null) return null;
        
        Pagamento pagamento = new Pagamento();
        pagamento.setIdPagamento(dto.getIdPagamento());
        pagamento.setTipoPagamento(dto.getTipoPagamento());
        pagamento.setValor(dto.getValor());
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
} 