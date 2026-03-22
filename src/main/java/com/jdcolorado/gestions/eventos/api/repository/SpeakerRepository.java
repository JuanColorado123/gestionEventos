package com.jdcolorado.gestions.eventos.api.repository;

import com.jdcolorado.gestions.eventos.api.domain.Speaker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpeakerRepository extends JpaRepository<Speaker, Long> {
    Optional<Speaker> findByEmail(String email);
    Boolean existsByEmail(String email);
}
