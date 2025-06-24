package com.cantinasa.cantinasa.controller;

import com.cantinasa.cantinasa.model.Usuario;
import com.cantinasa.cantinasa.model.dto.ProdutoDTO;
import com.cantinasa.cantinasa.model.dto.ProdutoRequest;
import com.cantinasa.cantinasa.model.dto.UsuarioRequestDTO;
import com.cantinasa.cantinasa.model.mapper.UsuarioMapper;
import com.cantinasa.cantinasa.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioMapper UsuarioMapper;

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarCliente(@Valid @RequestBody UsuarioRequestDTO UsuarioRequest) {

        if (usuarioService.existsByUsername(UsuarioRequest.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Usuário já existe");
        }

        if (usuarioService.existsByEmail(UsuarioRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email já cadastrado");
        }

        Usuario usuario = usuarioService.save(UsuarioMapper.toEntity(UsuarioRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }


}
