package com.jdcolorado.gestions.eventos.api.service;

import com.jdcolorado.gestions.eventos.api.domain.Speaker;

import java.util.List;

public interface SpeakerService {
    List<Speaker> findAll();
    Speaker findById(Long id);
    Speaker save(Speaker speaker);
    Speaker update(Long id, Speaker speaker);
    void deleteById(Long id);
}
