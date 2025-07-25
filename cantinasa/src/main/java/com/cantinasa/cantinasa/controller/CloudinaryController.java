package com.cantinasa.cantinasa.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import java.util.Map;

/**
 * Controlador responsável pelo upload de imagens para o serviço Cloudinary.
 */
@RestController
@RequestMapping("/imagens")
public class CloudinaryController {

    /** Nome da conta Cloudinary */
    private final String CLOUD_NAME  = "dfqu4q6zo";

    /** Chave de API do Cloudinary */
    private final String API_KEY = "728667547255965";

    /** Upload preset configurado no Cloudinary */
    private final String UPLOAD_PRESET = "ml_default";

    /**
     * Endpoint responsável por receber uma imagem via formulário multipart e enviá-la para o Cloudinary.
     *
     * @param file Arquivo de imagem enviado pelo cliente
     * @return URL da imagem hospedada no Cloudinary ou mensagem de erro
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadImagem(@RequestParam("file") MultipartFile file) {
        try {
            String url = "https://api.cloudinary.com/v1_1/" + CLOUD_NAME + "/image/upload";

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", file.getResource());
            body.add("upload_preset", UPLOAD_PRESET);
            body.add("api_key", API_KEY);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> response = restTemplate.postForEntity(url, requestEntity, Map.class);

            return ResponseEntity.ok(Map.of("url", response.getBody().get("secure_url")));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao enviar imagem: " + e.getMessage());
        }
    }
}
