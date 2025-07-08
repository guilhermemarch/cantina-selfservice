package com.cantinasa.cantinasa.UI.service;

import com.cantinasa.cantinasa.model.Item_pedido;
import com.cantinasa.cantinasa.model.Pagamento;
import javafx.collections.ObservableList;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Serviço utilitário usado pela aplicação JavaFX para comunicar-se com a API
 * REST da cantina.
 */
@Service
public class ApiService {
    
    private static final String BASE_URL = "http://localhost:8080/api";
    private final RestTemplate restTemplate;
    
    /** Construtor padrão que inicializa o {@link RestTemplate}. */
    public ApiService() {
        this.restTemplate = new RestTemplate();
    }
    
    /**
     * Envia para a API a criação de um pedido a partir dos itens do carrinho.
     */
    public Map<String, Object> criarPedido(ObservableList<Item_pedido> cartItems, double totalAmount,
                                          Pagamento.MetodoPagamento metodoPagamento) {
        try {
            StringBuilder itensJson = new StringBuilder();
            for (int i = 0; i < cartItems.size(); i++) {
                Item_pedido item = cartItems.get(i);
                itensJson.append(String.format("{\"produtoId\":%d,\"quantidade\":%d}",
                        item.getProduto().getId(), item.getQuantidade()));
                if (i < cartItems.size() - 1) {
                    itensJson.append(",");
                }
            }

            Long userId = com.cantinasa.cantinasa.UI.controllers.PaymentController.getCurrentUserId();
            if (userId == null) userId = 0L;

            String pedidoJson = String.format("{" +
                            "\"dataHora\":\"%s\"," +
                            "\"status\":\"PENDENTE\"," +
                            "\"usuarioId\":%d," +
                            "\"itens\":[%s]" +
                            "}",
                    LocalDateTime.now().toString(),
                    userId,
                    itensJson.toString()
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(pedidoJson, headers);
            
            ResponseEntity<String> response = restTemplate.postForEntity(
                    BASE_URL + "/pedidos", entity, String.class);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            Long pedidoId = null;
            try {
                String body = response.getBody();
                if (body != null && body.contains("\"idPedido\"")) {
                    int idIndex = body.indexOf("\"idPedido\"");
                    int colon = body.indexOf(":", idIndex);
                    int comma = body.indexOf(",", colon);
                    String idStr = body.substring(colon + 1, comma > 0 ? comma : body.length()).replaceAll("[^0-9]", "").trim();
                    if (!idStr.isEmpty()) pedidoId = Long.parseLong(idStr);
                }
            } catch (Exception ex) { pedidoId = null; }
            result.put("pedidoId", pedidoId);
            result.put("response", response.getBody());
            
            return result;
            
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("error", e.getMessage());
            return result;
        }
    }
    
    /**
     * Processa um pagamento para um pedido existente.
     */
    public Map<String, Object> processarPagamento(Long pedidoId, double valor,
                                                 Pagamento.MetodoPagamento metodo,
                                                 String codigoPix, Double troco) {
        try {
            String pagamentoJson = String.format("{"
                            + "\"pedido\":{\"id\":%d},"
                            + "\"valor\":%.2f,"
                            + "\"metodo\":\"%s\","
                            + "\"codigo_pix\":%s,"
                            + "\"troco\":%s"
                            + "}",
                    pedidoId,
                    valor,
                    metodo.name(),
                    codigoPix != null ? "\"" + codigoPix + "\"" : "null",
                    troco != null ? String.format("%.2f", troco) : "null"
            );
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(pagamentoJson, headers);
            
            ResponseEntity<String> response = restTemplate.postForEntity(
                    BASE_URL + "/pagamentos/processar", entity, String.class);

            Map<String, Object> result = new HashMap<>();
            if (response.getStatusCodeValue() == 409) {
                result.put("success", false);
                result.put("error", response.getBody() != null ? response.getBody() : "Já existe pagamento para este pedido.");
                return result;
            } else if (response.getStatusCodeValue() == 404) {
                result.put("success", false);
                result.put("error", response.getBody() != null ? response.getBody() : "Pedido não encontrado.");
                return result;
            }
            result.put("success", true);
            result.put("response", response.getBody());
            return result;
            
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("error", e.getMessage());
            return result;
        }
    }

    /**
     * Faz uma requisição PATCH usando HttpURLConnection (workaround para RestTemplate).
     */
    public String patchRequest(String urlString, String jsonBody, Map<String, String> headers) throws Exception {
        java.net.URL url = new java.net.URL(urlString);
        java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("X-HTTP-Method-Override", "PATCH");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        try (java.io.OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonBody.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        int code = conn.getResponseCode();
        java.io.InputStream is = (code >= 200 && code < 400) ? conn.getInputStream() : conn.getErrorStream();
        java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(is, "utf-8"));
        StringBuilder response = new StringBuilder();
        String responseLine;
        while ((responseLine = br.readLine()) != null) {
            response.append(responseLine.trim());
        }
        br.close();
        conn.disconnect();
        return response.toString();
    }
} 