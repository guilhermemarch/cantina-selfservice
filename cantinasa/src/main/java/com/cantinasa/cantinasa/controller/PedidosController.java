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

/**
 * Controlador responsável por gerenciar operações relacionadas a pedidos.
 */
@RestController
@RequestMapping("/api/pedidos")
public class PedidosController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private PedidoMapper pedidoMapper;

    /**
     * Cria um novo pedido com base nos dados recebidos.
     *
     * @param pedidoRequest Objeto contendo os dados do novo pedido.
     * @return Resposta com o DTO do pedido criado e URI de localização.
     */
    @PostMapping
    public ResponseEntity<PedidoDTO> criarPedido(@RequestBody PedidoRequest pedidoRequest) {
        Pedido novoPedido = pedidoService.createFromRequest(pedidoRequest);
        PedidoDTO pedidoDTO = pedidoMapper.toDTO(novoPedido);
        URI location = URI.create("/api/pedidos/" + pedidoDTO.getIdPedido());
        return ResponseEntity.created(location).body(pedidoDTO);
    }

    /**
     * Busca um pedido pelo seu ID.
     *
     * @param id Identificador do pedido.
     * @return DTO do pedido encontrado ou erro 404 se não encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTO> buscarPedido(@PathVariable Long id) {
        try {
            Pedido pedido = pedidoService.findById(id);
            return ResponseEntity.ok(pedidoMapper.toDTO(pedido));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Lista todos os pedidos cadastrados.
     *
     * @return Lista de DTOs de pedidos.
     */
    @GetMapping
    public ResponseEntity<List<PedidoDTO>> listarPedidos() {
        List<Pedido> pedidos = pedidoService.findAll();
        List<PedidoDTO> dtos = pedidoMapper.toDTOList(pedidos);
        return ResponseEntity.ok(dtos);
    }

    /**
     * Atualiza o status de um pedido com base no ID e novo status informado.
     *
     * @param id     Identificador do pedido.
     * @param status Novo status a ser atribuído ao pedido.
     * @return DTO do pedido atualizado ou erro 404 se não encontrado.
     */
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
