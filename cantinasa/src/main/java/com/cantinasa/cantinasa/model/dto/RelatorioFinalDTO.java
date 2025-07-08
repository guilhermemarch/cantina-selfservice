package com.cantinasa.cantinasa.model.dto;

import com.cantinasa.cantinasa.model.dto.relatorioSubDTOs.*;

import java.util.List;

public class RelatorioFinalDTO {


    private List<HorarioPicoDTO> horarioPicoDTO;
    private List<ProdutoProximoValidadeDTO> produtoProximoValidadeDTO;
    private List<ProdutoEstoqueBaixoDTO> produtoEstoqueBaixoDTO;

    public RelatorioFinalDTO() {
    }

    public RelatorioFinalDTO(List<HorarioPicoDTO> horarioPicoDTO, List<ProdutoProximoValidadeDTO> produtoProximoValidadeDTO, List<ProdutoEstoqueBaixoDTO> produtoEstoqueBaixoDTO) {
        this.horarioPicoDTO = horarioPicoDTO;
        this.produtoProximoValidadeDTO = produtoProximoValidadeDTO;
        this.produtoEstoqueBaixoDTO = produtoEstoqueBaixoDTO;
    }

    public List<HorarioPicoDTO> getHorarioPicoDTO() {
        return horarioPicoDTO;
    }

    public void setHorarioPicoDTO(List<HorarioPicoDTO> horarioPicoDTO) {
        this.horarioPicoDTO = horarioPicoDTO;
    }

    public List<ProdutoProximoValidadeDTO> getProdutoProximoValidadeDTO() {
        return produtoProximoValidadeDTO;
    }

    public void setProdutoProximoValidadeDTO(List<ProdutoProximoValidadeDTO> produtoProximoValidadeDTO) {
        this.produtoProximoValidadeDTO = produtoProximoValidadeDTO;
    }

    public List<ProdutoEstoqueBaixoDTO> getProdutoEstoqueBaixoDTO() {
        return produtoEstoqueBaixoDTO;
    }

    public void setProdutoEstoqueBaixoDTO(List<ProdutoEstoqueBaixoDTO> produtoEstoqueBaixoDTO) {
        this.produtoEstoqueBaixoDTO = produtoEstoqueBaixoDTO;
    }
}
