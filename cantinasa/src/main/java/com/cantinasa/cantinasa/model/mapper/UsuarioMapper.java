package com.cantinasa.cantinasa.model.mapper;

import com.cantinasa.cantinasa.model.Usuario;
import com.cantinasa.cantinasa.model.dto.UsuarioDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UsuarioMapper {

    public UsuarioDTO toDTO(Usuario usuario) {
        if (usuario == null) return null;
        
        return new UsuarioDTO(
            usuario.getIdUsuario(),
            usuario.getUsername(),
            usuario.getEmail(),
            usuario.getPassword(),
            usuario.getRole()
        );
    }

    public Usuario toEntity(UsuarioDTO dto) {
        if (dto == null) return null;
        
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(dto.getIdUsuario());
        usuario.setUsername(dto.getUsername());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(dto.getPassword());
        usuario.setRole(dto.getRole());
        return usuario;
    }

    public List<UsuarioDTO> toDTOList(List<Usuario> usuarios) {
        if (usuarios == null) return null;
        return usuarios.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public List<Usuario> toEntityList(List<UsuarioDTO> dtos) {
        if (dtos == null) return null;
        return dtos.stream()
            .map(this::toEntity)
            .collect(Collectors.toList());
    }
} 