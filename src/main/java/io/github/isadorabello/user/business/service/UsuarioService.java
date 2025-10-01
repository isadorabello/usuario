package io.github.isadorabello.user.business.service;

import io.github.isadorabello.user.business.converter.UsuarioConverter;
import io.github.isadorabello.user.business.dto.UsuarioDTO;
import io.github.isadorabello.user.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter converter;

    public UsuarioDTO salvaUsuario (UsuarioDTO dto){
        emailExiste(dto.email());
        return converter.paraUsuarioDTO(usuarioRepository.save(converter.paraUsuario(dto)));
    }

    public void emailExiste(String email){
        try {
            boolean existe = verificaEmailExiste(email);
            if (existe) {
                throw new RuntimeException("Email já cadastrado " + email);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Email já cadastrado ", e.getCause());
        }
    }

    public boolean verificaEmailExiste(String email) {
        return usuarioRepository.existsByEmail(email);
    }
}
