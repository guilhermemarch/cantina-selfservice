package com.cantinasa.cantinasa.controller;

import com.cantinasa.cantinasa.model.dto.RelatorioFinalDTO;
import com.cantinasa.cantinasa.model.dto.relatorioSubDTOs.ProdutoEstoqueBaixoDTO;
import com.cantinasa.cantinasa.service.RelatoriosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Controlador responsável pelos relatórios analíticos do sistema.
 */
@RestController
@RequestMapping("/api/relatorios")
public class RelatoriosController {

    @Autowired
    private RelatoriosService relatoriosService;

    /**
     * Retorna produtos com estoque abaixo do limite mínimo definido.
     *
     * @param limiteMinimo Valor mínimo de estoque para filtrar os produtos (padrão: 5).
     * @return Lista de produtos com estoque baixo.
     */
    @GetMapping("/estoque-baixo")
    public ResponseEntity<List<ProdutoEstoqueBaixoDTO>> produtosEstoqueBaixo(
            @RequestParam(name = "limiteMinimo", defaultValue = "5") int limiteMinimo) {
        return ResponseEntity.ok(relatoriosService.produtosEstoqueBaixoDTO(limiteMinimo));
    }

    /**
     * Retorna os horários de pico de consumo com base em uma data específica.
     *
     * @param data Data para análise dos horários de maior movimento.
     * @return Lista de horários e respectivas estatísticas de pico.
     */
    @GetMapping("/horarios-pico")
    public ResponseEntity<List<Map<String, Object>>> buscarHorariosDePico(
            @RequestParam("data") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {

        List<Map<String, Object>> horarios = relatoriosService.horariosPico(data);
        return ResponseEntity.ok(horarios);
    }

    /**
     * Retorna produtos que estão próximos do vencimento.
     *
     * @param diasParaVencer Quantidade de dias para considerar um produto como próximo do vencimento (padrão: 30).
     * @return Lista de produtos com validade próxima.
     */
    @GetMapping("/validade")
    public ResponseEntity<List<Map<String, Object>>> produtosValidade(
            @RequestParam(name = "diasParaVencer", defaultValue = "30") int diasParaVencer) {
        return ResponseEntity.ok(relatoriosService.produtosValidade(diasParaVencer));
    }

    /**
     * Gera um relatório completo para um único dia informado.
     *
     * @param localTime Data e hora de referência do relatório.
     * @return Mapa com os dados consolidados do dia.
     */
    @GetMapping("/dia/{localDateTime}")
    public ResponseEntity<Map<String, Object>> relatorioDia(
            @PathVariable LocalDateTime localTime) {
        return ResponseEntity.ok(relatoriosService.generateDailyReport(localTime));
    }

    /**
     * Gera um relatório de vendas entre duas datas específicas.
     *
     * @param data1 Data e hora de início do período.
     * @param data2 Data e hora de fim do período.
     * @return Mapa com os dados de vendas no período especificado.
     */
    @GetMapping("/datas/{data1}/{data2}")
    public ResponseEntity<Map<String, Object>> relatorioDia(
            @PathVariable("data1") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime data1,
            @PathVariable("data2") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime data2) {

        return ResponseEntity.ok(relatoriosService.generateSalesReport(data1, data2));
    }

}
