package com.cantinasa.cantinasa.service;

import com.cantinasa.cantinasa.model.Item_pedido;
import com.cantinasa.cantinasa.model.Pedido;
import com.cantinasa.cantinasa.model.Produto;
import com.cantinasa.cantinasa.model.enums.categoria;
import com.cantinasa.cantinasa.model.Pagamento;
import com.cantinasa.cantinasa.repository.PedidoRepository;
import com.cantinasa.cantinasa.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.ArrayList;

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
            
        Map<Pagamento.MetodoPagamento, BigDecimal> salesByPaymentMethod = generateSalesByPaymentMethod(startDate, endDate);
            
        List<Map.Entry<Produto, Long>> topSellingProducts = pedidos.stream()
            .flatMap(pedido -> pedido.getItens().stream())
            .collect(Collectors.groupingBy(
                Item_pedido::getProduto,
                Collectors.counting()
            ))
            .entrySet()
            .stream()
            .sorted(Map.Entry.<Produto, Long>comparingByValue().reversed())
            .limit(10)
            .collect(Collectors.toList());
            
        Map<String, Object> result = new HashMap<>();
        result.put("totalSales", totalSales);
        result.put("salesByPaymentMethod", salesByPaymentMethod);
        result.put("topSellingProducts", topSellingProducts);
        return result;
    }

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


    public List<Map<String, Object>> horariosPico(LocalDate data) {


        List<Pedido> pedidos = pedidoRepository.findByDataPedidoBetween(
                data.atStartOfDay(), data.atTime(23, 59, 59));


        Map<Integer, Long> hourCounts = pedidos.stream()
                .collect(Collectors.groupingBy(
                        pedido -> pedido.getDataPedido().getHour(),
                        Collectors.counting()
                ));

        List<Map<String, Object>> horariosPico = new ArrayList<>();
        for (Map.Entry<Integer, Long> entry : hourCounts.entrySet()) {
            Map<String, Object> horario = new HashMap<>();
            horario.put("hora", entry.getKey());
            horario.put("quantidade", entry.getValue());
            horariosPico.add(horario);
        }
        return horariosPico;
    }



    public List<Produto> produtosEstoqueBaixo(int limiteMinimo) {
        return produtoRepository.findByQuantidadeBelow(limiteMinimo);
    }

    public List<Map<String, Object>> produtosValidade(int diasParaVencer) {
        return new ArrayList<>();
    }

    public List<Map<String, Object>> produtosMaisVendidos(LocalDate dataInicio, LocalDate dataFim) {
        return new ArrayList<>();
    }
}
