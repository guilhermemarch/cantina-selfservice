package com.cantinasa.cantinasa.controller;

import com.cantinasa.cantinasa.model.Produto;
import com.cantinasa.cantinasa.service.RelatoriosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/relatorios")
public class RelatoriosController {

    @Autowired
    private RelatoriosService relatoriosService;

    @GetMapping("/estoque-baixo")
    public ResponseEntity<List<Produto>> produtosEstoqueBaixo(
            @RequestParam(defaultValue = "5") int limiteMinimo) {
        return ResponseEntity.ok(relatoriosService.produtosEstoqueBaixo(limiteMinimo));
    }

    @GetMapping("/horarios-pico")
    public ResponseEntity<List<Map<String, Object>>> buscarHorariosDePico(
            @RequestParam("data") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {

        List<Map<String, Object>> horarios = relatoriosService.horariosPico(data);
        return ResponseEntity.ok(horarios);
    }


    @GetMapping("/validade")
    public ResponseEntity<List<Map<String, Object>>> produtosValidade(
            @RequestParam(defaultValue = "30") int diasParaVencer) {
        return ResponseEntity.ok(relatoriosService.produtosValidade(diasParaVencer));
    }

    @GetMapping("/dia/{localDateTime}")
    public ResponseEntity<Map<String, Object>> relatorioDia(
            @PathVariable LocalDateTime localTime) {
        return ResponseEntity.ok(relatoriosService.generateDailyReport(localTime));
    }
    @GetMapping("/datas{data1}/{data2}")
    public ResponseEntity<Map<String, Object>> relatorioDia(
            @PathVariable LocalDateTime data1,
            @PathVariable LocalDateTime data2) {
        return ResponseEntity.ok(relatoriosService.generateSalesReport(data1,data2));
    }

}
