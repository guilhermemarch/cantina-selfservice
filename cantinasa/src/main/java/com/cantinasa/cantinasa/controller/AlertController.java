package com.cantinasa.cantinasa.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AlertController {

//    GET	/alertas/estoque	Alerta de estoque crítico
//    GET	/health	Status do sistema (healthcheck) ------- OK

    @GetMapping("/status")
    public static final ResponseEntity<?> status() {
        return ResponseEntity.ok("up");
    }
}
