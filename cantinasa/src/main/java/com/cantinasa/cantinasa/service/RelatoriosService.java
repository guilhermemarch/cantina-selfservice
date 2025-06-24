package com.cantinasa.cantinasa.service;

import com.cantinasa.cantinasa.model.Item_pedido;
import com.cantinasa.cantinasa.model.Pedido;
import com.cantinasa.cantinasa.model.Produto;
import com.cantinasa.cantinasa.model.Pagamento;
import com.cantinasa.cantinasa.model.enums.categoria;
import com.cantinasa.cantinasa.repository.PedidoRepository;
import com.cantinasa.cantinasa.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RelatoriosService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;


    public Map<String, Object> generateSalesReport(LocalDateTime startDate, LocalDateTime endDate) {
        List<Pedido> pedidos = pedidoRepository.findByDataPedidoBetween(startDate, endDate);

        BigDecimal totalSales = pedidos.stream()
                .map(Pedido::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<Pagamento.MetodoPagamento, BigDecimal> salesByPaymentMethod =
                generateSalesByPaymentMethod(startDate, endDate);

        List<Map<String, Object>> topSellingProducts = pedidos.stream()
                .flatMap(p -> p.getItens().stream())
                .collect(Collectors.groupingBy(
                        Item_pedido::getProduto,
                        Collectors.counting()
                )).entrySet().stream()
                .sorted(Map.Entry.<Produto, Long>comparingByValue().reversed())
                .limit(10)
                .map(e -> Map.of(
                        "produto", e.getKey(),
                        "quantidade", e.getValue()))
                .collect(Collectors.toList());

        return Map.of(
                "totalSales", totalSales,
                "salesByPaymentMethod", salesByPaymentMethod,
                "topSellingProducts", topSellingProducts
        );
    }

    public Map<Pagamento.MetodoPagamento, BigDecimal> generateSalesByPaymentMethod(LocalDateTime start, LocalDateTime end) {
        List<Pedido> pedidos = pedidoRepository.findByDataPedidoBetween(start, end);
        Map<Pagamento.MetodoPagamento, BigDecimal> salesByPaymentMethod = new HashMap<>();

        for (Pedido pedido : pedidos) {
            if (pedido.getPagamento() != null) {
                Pagamento.MetodoPagamento metodo = pedido.getPagamento().getMetodo();
                BigDecimal valor = pedido.getValorTotal();
                salesByPaymentMethod.merge(metodo, valor, BigDecimal::add);
            }
        }
        return salesByPaymentMethod;
    }


    public Map<String, Object> generateInventoryReport() {
        List<Produto> produtos = produtoRepository.findAll();

        Map<categoria, Long> productsByCategory = produtos.stream()
                .collect(Collectors.groupingBy(
                        Produto::getCategoria,
                        Collectors.counting()
                ));

        BigDecimal totalValue = produtos.stream()
                .map(p -> p.getPreco().multiply(BigDecimal.valueOf(p.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<Produto> lowStockProducts = produtos.stream()
                .filter(p -> p.getQuantidade() < 10)
                .collect(Collectors.toList());

        return Map.of(
                "productsByCategory", productsByCategory,
                "totalValue", totalValue,
                "lowStockProducts", lowStockProducts
        );
    }


    public Map<String, Object> generateDailyReport(LocalDateTime date) {
        LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = date.toLocalDate().atTime(23, 59, 59);

        List<Pedido> pedidos = pedidoRepository.findByDataPedidoBetween(startOfDay, endOfDay);

        BigDecimal totalSales = pedidos.stream()
                .map(Pedido::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal averageOrderValue = pedidos.isEmpty() ? BigDecimal.ZERO :
                totalSales.divide(BigDecimal.valueOf(pedidos.size()), 2, RoundingMode.HALF_UP);

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

    public Map<String, Object> generateReportBetweenDates(LocalDateTime start, LocalDateTime end) {
        List<Pedido> pedidos = pedidoRepository.findByDataPedidoBetween(start, end);

        BigDecimal totalSales = pedidos.stream()
                .map(Pedido::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal averageOrderValue = pedidos.isEmpty() ? BigDecimal.ZERO :
                totalSales.divide(BigDecimal.valueOf(pedidos.size()), 2, RoundingMode.HALF_UP);

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

    public List<Map<String, Object>> horariosPico(LocalDate data) {
        LocalDateTime start = data.atStartOfDay();
        LocalDateTime end = data.atTime(23, 59, 59);

        List<Pedido> pedidos = pedidoRepository.findByDataPedidoBetween(start, end);

        return pedidos.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getDataPedido().getHour(),
                        Collectors.counting()
                )).entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .map(e -> Map.of(
                        "hora", e.getKey(),
                        "totalPedidos", e.getValue()))
                .collect(Collectors.toList());
    }

    public List<Produto> produtosEstoqueBaixo(int limiteMinimo) {
        return produtoRepository.findAll().stream()
                .filter(p -> p.getQuantidade() < limiteMinimo)
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> produtosValidade(int diasParaVencer) {
        LocalDate hoje = LocalDate.now();
        LocalDate limite = hoje.plusDays(diasParaVencer);

        return produtoRepository.findAll().stream()
                .filter(p -> p.getDataValidade() != null
                        && !p.getDataValidade().isBefore(hoje)
                        && !p.getDataValidade().isAfter(limite))
                .map(p -> Map.of(
                        "produto", p,
                        "diasParaVencer", ChronoUnit.DAYS.between(hoje, p.getDataValidade())
                ))
                .sorted(Comparator.comparingLong(m -> (Long) m.get("diasParaVencer")))
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> produtosMaisVendidos(LocalDate dataInicio, LocalDate dataFim) {
        LocalDateTime start = dataInicio.atStartOfDay();
        LocalDateTime end = dataFim.atTime(23, 59, 59);

        List<Pedido> pedidos = pedidoRepository.findByDataPedidoBetween(start, end);

        return pedidos.stream()
                .flatMap(p -> p.getItens().stream())
                .collect(Collectors.groupingBy(
                        Item_pedido::getProduto,
                        Collectors.counting()
                )).entrySet().stream()
                .sorted(Map.Entry.<Produto, Long>comparingByValue().reversed())
                .map(e -> Map.of(
                        "produto", e.getKey(),
                        "quantidade", e.getValue()
                ))
                .collect(Collectors.toList());
    }
}
