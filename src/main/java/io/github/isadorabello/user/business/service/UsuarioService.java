package io.github.isadorabello.user.business.service;

import io.github.isadorabello.user.business.converter.UsuarioConverter;
import io.github.isadorabello.user.business.dto.UsuarioDTO;
import io.github.isadorabello.user.infrastructure.entity.Usuario;
import io.github.isadorabello.user.infrastructure.exception.ConflictException;
import io.github.isadorabello.user.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter converter;
    private final PasswordEncoder encoder;

    public UsuarioDTO salvaUsuario (UsuarioDTO dto){
        emailExiste(dto.email());
        Usuario usuario = converter.paraUsuario(dto);
        usuario.setSenha(encoder.encode(dto.senha()));
        return converter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }

    public void emailExiste(String email){
        try {
            boolean existe = verificaEmailExiste(email);
            if (existe) {
                throw new ConflictException("Email já cadastrado " + email);
            }
        } catch (ConflictException e) {
            throw new ConflictException("Email já cadastrado ", e.getCause());
        }
    }

    public boolean verificaEmailExiste(String email) {
        return usuarioRepository.existsByEmail(email);
    }
}
