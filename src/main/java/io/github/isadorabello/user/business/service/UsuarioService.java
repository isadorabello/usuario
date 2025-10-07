package io.github.isadorabello.user.business.service;

import io.github.isadorabello.user.business.converter.UsuarioConverter;
import io.github.isadorabello.user.business.dto.UsuarioDTO;
import io.github.isadorabello.user.infrastructure.entity.Usuario;
import io.github.isadorabello.user.infrastructure.exception.ConflictException;
import io.github.isadorabello.user.infrastructure.exception.ResourceNotFoundException;
import io.github.isadorabello.user.infrastructure.exception.UnauthorizedException;
import io.github.isadorabello.user.infrastructure.repository.UsuarioRepository;
import io.github.isadorabello.user.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter converter;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

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

    public UsuarioDTO buscarUsuarioPorEmail (String email){

        try {
            return converter.paraUsuarioDTO(usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("Email não encontrado " + email)));
        }catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException("Email não encontrado " + email);
        }
    }

    public void deletarUsuarioPorEmail(String email){
        usuarioRepository.deleteByEmail(email);
    }


    public String autenticarUsuario(UsuarioDTO usuarioDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usuarioDTO.email(),
                            usuarioDTO.senha())
            );
            System.out.println("authentication.toString(): " +authentication.toString());
            return "Bearer " + jwtUtil.generateToken(authentication.getName());

        } catch (BadCredentialsException | UsernameNotFoundException | AuthorizationDeniedException e) {
            throw new UnauthorizedException("Usuário ou senha inválidos: ", e.getCause());
        }
    }

    public UsuarioDTO atualizaUsuario(String token, UsuarioDTO dto){
        String email = jwtUtil.extrairEmailToken(token.substring(7));

        UsuarioDTO usuarioDTO = new UsuarioDTO(
                dto.nome(),
                dto.email(),
                dto.senha() != null ? encoder.encode(dto.senha()) : null,
                dto.enderecos(),
                dto.telefones());

        Usuario entity = usuarioRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("Usuario não encontrado " + email));

        entity = converter.paraAtualizarUsuario(usuarioDTO, entity);

        return converter.paraUsuarioDTO(usuarioRepository.save(entity));
    }
}
