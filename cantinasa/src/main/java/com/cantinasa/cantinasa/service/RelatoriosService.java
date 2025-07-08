package com.cantinasa.cantinasa.service;

import java.sql.Timestamp;
import com.cantinasa.cantinasa.model.Item_pedido;
import com.cantinasa.cantinasa.model.Pedido;
import com.cantinasa.cantinasa.model.Produto;
import com.cantinasa.cantinasa.model.dto.RelatorioFinalDTO;
import com.cantinasa.cantinasa.model.dto.relatorioSubDTOs.ProdutoProximoValidadeDTO;
import com.cantinasa.cantinasa.model.enums.categoria;
import com.cantinasa.cantinasa.model.Pagamento;
import com.cantinasa.cantinasa.repository.PedidoRepository;
import com.cantinasa.cantinasa.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.ArrayList;

import static org.springframework.web.servlet.function.ServerResponse.ok;
import com.cantinasa.cantinasa.model.dto.relatorioSubDTOs.ProdutoEstoqueBaixoDTO;

/**
 * Serviço responsável pela geração de relatórios analíticos da cantina.
 */
@Service
public class RelatoriosService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    /**
     * Gera um relatório de vendas entre duas datas.
     */
    public Map<String, Object> generateSalesReport(LocalDateTime startDate, LocalDateTime endDate) {
        List<Pedido> pedidos = pedidoRepository.findByDataPedidoBetween(startDate, endDate);

        BigDecimal totalSales = pedidos.stream()
                .map(Pedido::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalTroco = pedidos.stream()
                .filter(p -> p.getPagamento() != null && p.getPagamento().getStatus() == Pagamento.Status.APROVADO && p.getPagamento().getTroco() != null)
                .map(p -> BigDecimal.valueOf(p.getPagamento().getTroco()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalLiquido = totalSales.subtract(totalTroco);

        Map<Pagamento.MetodoPagamento, BigDecimal> salesByPaymentMethod = generateSalesByPaymentMethod(startDate, endDate);

        List<Map<String, Object>> topSellingProducts = pedidos.stream()
                .flatMap(pedido -> pedido.getItens().stream())
                .collect(Collectors.groupingBy(
                        Item_pedido::getProduto,
                        Collectors.counting()
                ))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<Produto, Long>comparingByValue().reversed())
                .limit(10)
                .map(entry -> {
                    Map<String, Object> produtoInfo = new HashMap<>();
                    produtoInfo.put("nome", entry.getKey().getNome());
                    produtoInfo.put("quantidadeVendida", entry.getValue());
                    return produtoInfo;
                })
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("totalSales", totalSales);
        result.put("salesByPaymentMethod", salesByPaymentMethod);
        result.put("topSellingProducts", topSellingProducts);
        result.put("totalTroco", totalTroco);
        result.put("totalLiquido", totalLiquido);
        return result;
    }


    /**
     * Agrupa vendas por método de pagamento.
     */
    public Map<Pagamento.MetodoPagamento, BigDecimal> generateSalesByPaymentMethod(LocalDateTime start, LocalDateTime end) {
        List<Pedido> pedidos = pedidoRepository.findByDataPedidoBetween(start, end);
        Map<Pagamento.MetodoPagamento, BigDecimal> salesByPaymentMethod = new HashMap<>();
        for (Pedido pedido : pedidos) {
            if (pedido.getPagamento() != null) {
                Pagamento.MetodoPagamento metodo = pedido.getPagamento().getMetodo();
                BigDecimal valor = pedido.getValorTotal();
                salesByPaymentMethod.put(metodo, salesByPaymentMethod.getOrDefault(metodo, BigDecimal.ZERO).add(valor));
            }
        }
        return salesByPaymentMethod;
    }

    /**
     * Gera relatório simples de estoque.
     */
    public Map<String, Object> generateInventoryReport() {
        List<Produto> produtos = produtoRepository.findAll();

        Map<categoria, Long> productsByCategory = produtos.stream()
                .collect(Collectors.groupingBy(
                        Produto::getCategoria,
                        Collectors.counting()
                ));

        BigDecimal totalValue = produtos.stream()
                .map(produto -> produto.getPreco().multiply(BigDecimal.valueOf(produto.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<Produto> lowStockProducts = produtos.stream()
                .filter(produto -> produto.getQuantidade() < 10)
                .collect(Collectors.toList());

        return Map.of(
                "productsByCategory", productsByCategory,
                "totalValue", totalValue,
                "lowStockProducts", lowStockProducts
        );
    }

    /**
     * Consolida dados de vendas em um único dia.
     */
    public Map<String, Object> generateDailyReport(LocalDateTime date) {
        LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = date.toLocalDate().atTime(23, 59, 59);

        List<Pedido> pedidos = pedidoRepository.findByDataPedidoBetween(startOfDay, endOfDay);

        BigDecimal totalSales = pedidos.stream()
                .map(Pedido::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal averageOrderValue = pedidos.isEmpty() ? BigDecimal.ZERO :
                totalSales.divide(BigDecimal.valueOf(pedidos.size()), 2, BigDecimal.ROUND_HALF_UP);

        Map<Pedido.Status, Long> ordersByStatus = pedidos.stream()
                .collect(Collectors.groupingBy(
                        Pedido::getStatus,
                        Collectors.counting()
                ));

        return Map.of(
                "totalSales", totalSales,
                "averageOrderValue", averageOrderValue,
                "ordersByStatus", ordersByStatus,
                "totalOrders", pedidos.size()
        );
    }

    /**
     * Relatório entre duas datas com informações de vendas.
     */
    public Map<String, Object> generateReportBy2Dates(LocalDateTime date,LocalDateTime date2) {
        LocalDateTime start = date.toLocalDate().atStartOfDay();
        LocalDateTime end = date2.toLocalDate().atTime(23, 59, 59);

        List<Pedido> pedidos = pedidoRepository.findByDataPedidoBetween(start, end);

        BigDecimal totalSales = pedidos.stream()
                .map(Pedido::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal averageOrderValue = pedidos.isEmpty() ? BigDecimal.ZERO :
                totalSales.divide(BigDecimal.valueOf(pedidos.size()), 2, BigDecimal.ROUND_HALF_UP);

        Map<Pedido.Status, Long> ordersByStatus = pedidos.stream()
                .collect(Collectors.groupingBy(
                        Pedido::getStatus,
                        Collectors.counting()
                ));

        return Map.of(
                "totalSales", totalSales,
                "averageOrderValue", averageOrderValue,
                "ordersByStatus", ordersByStatus,
                "totalOrders", pedidos.size()
        );
    }



    /** Retorna horários de maior movimento em um dia. */
    public List<Map<String, Object>> horariosPico(LocalDate data) {
        return pedidoRepository.horarioPico(data).stream()
                .map(entry -> {
                    Object[] row = (Object[]) entry;
                    Map<String, Object> map = new HashMap<>();

                    Timestamp ts = (Timestamp) row[0];
                    LocalDateTime hora = ts.toLocalDateTime();

                    map.put("hora", hora);
                    map.put("total_vendido", row[1]);
                    return map;
                })
                .collect(Collectors.toList());
    }


    /** Lista produtos com estoque abaixo de determinado limite. */
    public List<Produto> produtosEstoqueBaixo(int limiteMinimo) {
        return produtoRepository.findByQuantidadeBelow(limiteMinimo);
    }

    /** Produtos próximos do vencimento. */
    public List<Map<String, Object>> produtosValidade(int diasParaVencer) {
        List<Object[]> resultados = produtoRepository.proximoValidade(diasParaVencer);

        return resultados.stream().map(registro -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", registro[0]);
            map.put("nome", registro[1]);
            map.put("validade", registro[2]);
            return map;
        }).collect(Collectors.toList());
    }

    /** Versão DTO do relatório de estoque baixo. */
    public List<ProdutoEstoqueBaixoDTO> produtosEstoqueBaixoDTO(int limiteMinimo) {
        return produtoRepository.findByQuantidadeBelow(limiteMinimo)
            .stream()
            .map(produto -> new ProdutoEstoqueBaixoDTO(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getQuantidade(),
                produto.getEstoque_minimo(),
                produto.getValidade(),
                produto.getCategoria() != null ? produto.getCategoria().name() : null,
                produto.getCreatedAt(),
                produto.getUpdatedAt()
            ))
            .collect(java.util.stream.Collectors.toList());
    }

}
