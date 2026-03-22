package com.jdcolorado.gestions.eventos.api.service.impl;

import com.jdcolorado.gestions.eventos.api.domain.Speaker;
import com.jdcolorado.gestions.eventos.api.exception.ResourceNotFoundException;
import com.jdcolorado.gestions.eventos.api.repository.SpeakerRepository;
import com.jdcolorado.gestions.eventos.api.service.SpeakerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpeakerServiceImpl implements SpeakerService {

    private final SpeakerRepository speakerRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Speaker> findAll() {
        return speakerRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Speaker findById(Long id) {
        return speakerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No se encontro el speaker con el id:" + id));
    }

    @Override
    @Transactional()
    public Speaker save(Speaker speaker) {
        return speakerRepository.save(speaker);
    }

    @Override
    @Transactional()
    public Speaker update(Long id, Speaker speaker) {

        if(speakerRepository.existsById(id)){
            throw new ResourceNotFoundException("No se encontro el speaker a actualizar");
        }

        return speakerRepository.save(speaker);
    }

    @Override
    @Transactional()
    public void deleteById(Long id) {

        if(speakerRepository.existsById(id)){
            throw new ResourceNotFoundException("No se encontro el speaker a actualizar");
        }

        speakerRepository.deleteById(id);
    }
}
