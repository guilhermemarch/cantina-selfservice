package com.cantinasa.cantinasa.controller;

import com.cantinasa.cantinasa.model.Pedido;
import com.cantinasa.cantinasa.model.dto.PedidoDTO;
import com.cantinasa.cantinasa.model.dto.PedidoRequest;
import com.cantinasa.cantinasa.model.mapper.PedidoMapper;
import com.cantinasa.cantinasa.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pedidos")
public class PedidosController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private PedidoMapper pedidoMapper;

    @PostMapping
    public ResponseEntity<PedidoDTO> criarPedido(@RequestBody PedidoRequest pedidoRequest) {
        Pedido novoPedido = pedidoService.createFromRequest(pedidoRequest);
        PedidoDTO pedidoDTO = pedidoMapper.toDTO(novoPedido);
        URI location = URI.create("/api/pedidos/" + pedidoDTO.getIdPedido());
        return ResponseEntity.created(location).body(pedidoDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTO> buscarPedido(@PathVariable Long id) {
        try {
            Pedido pedido = pedidoService.findById(id);
            return ResponseEntity.ok(pedidoMapper.toDTO(pedido));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<PedidoDTO>> listarPedidos() {
        List<Pedido> pedidos = pedidoService.findAll();
        List<PedidoDTO> dtos = pedidoMapper.toDTOList(pedidos);
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<PedidoDTO> atualizarStatus(
            @PathVariable Long id,
            @RequestParam Pedido.Status status) {
        try {
            Pedido pedido = pedidoService.atualizarStatus(id, status);
            return ResponseEntity.ok(pedidoMapper.toDTO(pedido));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
