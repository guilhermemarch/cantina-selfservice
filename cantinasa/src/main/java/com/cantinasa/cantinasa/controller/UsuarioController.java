package com.cantinasa.cantinasa.controller;

import com.cantinasa.cantinasa.model.Usuario;
import com.cantinasa.cantinasa.model.dto.LoginDTO;
import com.cantinasa.cantinasa.model.dto.UsuarioRequestDTO;
import com.cantinasa.cantinasa.model.mapper.UsuarioMapper;
import com.cantinasa.cantinasa.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador responsável pelas operações relacionadas a usuários.
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioMapper UsuarioMapper;

    /**
     * Cadastra um novo usuário no sistema após validações de unicidade.
     *
     * @param UsuarioRequest Dados do usuário a ser cadastrado.
     * @return Usuário criado ou erro de conflito caso já exista.
     */
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

    /**
     * Lista todos os usuários cadastrados.
     *
     * @return Lista de usuários.
     */
    @GetMapping
    public ResponseEntity<?> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    /**
     * Realiza login do usuário com base nas credenciais fornecidas.
     *
     * @param login Objeto contendo nome de usuário e senha.
     * @return Resposta de sucesso ou erro conforme a validação das credenciais.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO login) {

        if (login.getUsername() == null || login.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário e senha são obrigatórios");
        }

        return usuarioService.login(login.getUsername(), login.getPassword());
    }
}
