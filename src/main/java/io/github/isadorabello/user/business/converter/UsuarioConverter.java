package io.github.isadorabello.user.business.converter;

import io.github.isadorabello.user.business.dto.EnderecoDTO;
import io.github.isadorabello.user.business.dto.TelefoneDTO;
import io.github.isadorabello.user.business.dto.UsuarioDTO;
import io.github.isadorabello.user.infrastructure.entity.Endereco;
import io.github.isadorabello.user.infrastructure.entity.Telefone;
import io.github.isadorabello.user.infrastructure.entity.Usuario;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsuarioConverter {

    public Usuario paraUsuario (UsuarioDTO usuarioDTO){
        return Usuario.builder()
                .nome(usuarioDTO.nome())
                .email(usuarioDTO.email())
                .senha(usuarioDTO.senha())
                .enderecos(usuarioDTO.enderecos() != null ?
                        paraEnderecos(usuarioDTO.enderecos()) : null)
                .telefones(usuarioDTO.telefones() != null ?
                        paraTelefones(usuarioDTO.telefones()) : null)
                .build();
    }

    public List<Endereco> paraEnderecos (List<EnderecoDTO> enderecoDTOs){
        return enderecoDTOs.stream().map(this::paraEndereco).toList();
    }

    public Endereco paraEndereco(EnderecoDTO enderecoDTO){
        return Endereco.builder()
                .rua(enderecoDTO.rua())
                .numero(enderecoDTO.numero())
                .cidade(enderecoDTO.cidade())
                .complemento(enderecoDTO.complemento())
                .estado(enderecoDTO.estado())
                .cep(enderecoDTO.cep())
                .build();
    }

    public List<Telefone> paraTelefones (List<TelefoneDTO> telefoneDTOs){
        return telefoneDTOs.stream().map(this::paraTelefone).toList();
    }

    public Telefone paraTelefone(TelefoneDTO telefoneDTO){
        return Telefone.builder()
                .numero(telefoneDTO.numero())
                .ddd(telefoneDTO.ddd())
                .build();
    }

    public UsuarioDTO paraUsuarioDTO (Usuario usuario){
        return new UsuarioDTO(
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getSenha(),
                usuario.getEnderecos() != null ? paraEnderecosDTO(usuario.getEnderecos()) : null,
                usuario.getTelefones() != null ? paraTelefonesDTO(usuario.getTelefones()) : null
        );
    }

    public List<EnderecoDTO> paraEnderecosDTO (List<Endereco> enderecos){
        return enderecos.stream().map(this::paraEnderecoDTO).toList();
    }

    public EnderecoDTO paraEnderecoDTO(Endereco endereco){
        return new EnderecoDTO(endereco.getId(), endereco.getRua(), endereco.getNumero(),
                endereco.getComplemento(), endereco.getCidade(), endereco.getEstado(), endereco.getCep());
    }

    public List<TelefoneDTO> paraTelefonesDTO (List<Telefone> telefones){
        return telefones.stream().map(this::paraTelefoneDTO).toList();
    }

    public TelefoneDTO paraTelefoneDTO(Telefone telefone){
        return new TelefoneDTO(telefone.getId(), telefone.getNumero(), telefone.getDdd());
    }

    public Usuario paraAtualizarUsuario(UsuarioDTO dto, Usuario entity){
        return Usuario.builder()
                .id(entity.getId())
                .nome(dto.nome()!= null ? dto.nome() : entity.getNome())
                .email(dto.email()!= null ? dto.email() : entity.getEmail())
                .senha(dto.senha()!= null ? dto.senha() : entity.getSenha())
                .enderecos(entity.getEnderecos())
                .telefones(entity.getTelefones())
                .build();
    }

    public Endereco paraAtualizarEndereco (EnderecoDTO dto, Endereco entity){
        return Endereco.builder()
                .id(entity.getId())
                .rua(dto.rua()!= null ? dto.rua() : entity.getRua())
                .numero(dto.numero()!= null ? dto.numero() : entity.getNumero())
                .complemento(dto.complemento() != null ? dto.complemento() : entity.getComplemento())
                .cidade(dto.cidade() != null ? dto.cidade() : entity.getCidade())
                .cep(dto.cep() != null ? dto.cep() : entity.getCep())
                .estado(dto.estado() != null ? dto.estado() : entity.getEstado())
                .usuario_id(entity.getUsuario_id())
                .build();
    }

    public Telefone paraAtualizarTelefone (TelefoneDTO dto, Telefone entity){
        return Telefone.builder()
                .id(entity.getId())
                .ddd(dto.ddd() != null ? dto.ddd() : entity.getDdd())
                .numero(dto.numero() != null ? dto.numero() : entity.getNumero())
                .usuario_id(entity.getUsuario_id())
                .build();
    }

    public Endereco paraNovoEndereco (EnderecoDTO dto, Long id){
        return Endereco.builder()
                .id(dto.id())
                .rua(dto.rua())
                .numero(dto.numero())
                .complemento(dto.complemento())
                .cidade(dto.cidade())
                .cep(dto.cep())
                .estado(dto.estado())
                .usuario_id(id)
                .build();
    }

    public Telefone paraNovoTelefone (TelefoneDTO dto, Long id){
        return Telefone.builder()
                .id(dto.id())
                .numero(dto.numero())
                .ddd(dto.ddd())
                .usuario_id(id)
                .build();
    }
}
