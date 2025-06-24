package com.cantinasa.cantinasa.model.mapper;

import com.cantinasa.cantinasa.model.Usuario;
import com.cantinasa.cantinasa.model.dto.UsuarioRequestDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.cantinasa.cantinasa.model.enums.role.USERS;

@Component
public class UsuarioMapper {

    public UsuarioRequestDTO toDTO(Usuario usuario) {
        if (usuario == null) return null;

        return new UsuarioRequestDTO(
            usuario.getUsername(),
            usuario.getEmail(),
            usuario.getPassword()
        );
    }

    public Usuario toEntity(UsuarioRequestDTO dto) {
        if (dto == null) return null;
        
        Usuario usuario = new Usuario();
        usuario.setUsername(dto.getUsername());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(dto.getPassword());
        return usuario;
    }

    public List<UsuarioRequestDTO> toDTOList(List<Usuario> usuarios) {
        if (usuarios == null) return null;
        return usuarios.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public List<Usuario> toEntityList(List<UsuarioRequestDTO> dtos) {
        if (dtos == null) return null;
        return dtos.stream()
            .map(this::toEntity)
            .collect(Collectors.toList());
    }
} 