package com.cantinasa.cantinasa.service;

import com.cantinasa.cantinasa.model.Usuario;
import com.cantinasa.cantinasa.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável por operações de persistência e consulta de usuários.
 */
@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /** Salva um usuário. */
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    /** Lista todos os usuários. */
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    /** Busca usuário por id. */
    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    /** Busca usuário pelo username. */
    public Optional<Usuario> findByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    /** Busca usuário por email. */
    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    /** Remove usuário pelo id. */
    public void deleteById(Long id) {
        usuarioRepository.deleteById(id);
    }

    /** Verifica existência por id. */
    public boolean existsById(Long id) {
        return usuarioRepository.existsById(id);
    }

    /** Verifica existência por username. */
    public boolean existsByUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }

    /** Verifica existência por email. */
    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    /**
     * Realiza autenticação simples comparando usuário e senha.
     */
    public Usuario autenticar(String username, String password) {
        Optional<Usuario> usuarioOpt = findByUsername(username);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (usuario.getPassword().equals(password)) {
                return usuario;
            }
        }
        return null;
    }

    /** Lista todos os usuários do sistema. */
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }


    /**
     * Autentica o usuário e retorna {@link ResponseEntity} com o resultado.
     */
    public ResponseEntity<Usuario> login(String username, String password) {

        Optional<Usuario> usuarioOpt = findByUsername(username);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (usuario.getPassword().equals(password)) {
                return ResponseEntity.ok().body(usuario);
            }
        }
        return ResponseEntity.status(401).build();

    }
}
