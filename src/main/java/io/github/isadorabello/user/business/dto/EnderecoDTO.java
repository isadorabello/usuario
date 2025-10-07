package io.github.isadorabello.user.business.dto;

public record EnderecoDTO(Long id, String rua, Long numero, String complemento, String cidade, String estado, String cep) {
}
