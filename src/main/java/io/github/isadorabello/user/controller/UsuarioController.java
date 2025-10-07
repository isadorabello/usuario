package io.github.isadorabello.user.controller;

import io.github.isadorabello.user.business.dto.EnderecoDTO;
import io.github.isadorabello.user.business.dto.TelefoneDTO;
import io.github.isadorabello.user.business.dto.UsuarioDTO;
import io.github.isadorabello.user.business.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService service;

    @PostMapping
    public ResponseEntity<UsuarioDTO> salvaUsuario(@RequestBody UsuarioDTO dto){
        return ResponseEntity.ok(service.salvaUsuario(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UsuarioDTO usuarioDTO) {
        return ResponseEntity.ok(service.autenticarUsuario(usuarioDTO));
    }

    @GetMapping
    public ResponseEntity<UsuarioDTO> buscaUsuarioPorEmail(@RequestParam("email") String email){
        return ResponseEntity.ok(service.buscarUsuarioPorEmail(email));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deletaUsuarioPorEmail(@PathVariable String email){
        service.deletarUsuarioPorEmail(email);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<UsuarioDTO> atualizarUsuario (@RequestHeader("Authorization") String token, @RequestBody UsuarioDTO dto){
        return ResponseEntity.ok(service.atualizaUsuario(token, dto));
    }

    @PutMapping("/telefone")
    public ResponseEntity<TelefoneDTO> atualizarTelefone (@RequestParam("id") Long id, @RequestBody TelefoneDTO dto){
        return ResponseEntity.ok(service.atualizarTelefone(id, dto));
    }

    @PutMapping("/endereco")
    public ResponseEntity<EnderecoDTO> atualizarEndereco (@RequestParam("id") Long id, @RequestBody EnderecoDTO dto){
        return ResponseEntity.ok(service.atualizarEndereco(id, dto));
    }

}
