package io.github.isadorabello.user.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Endereco extends JpaRepository<Endereco, Long> {
}
