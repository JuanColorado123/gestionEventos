package com.jdcolorado.gestions.eventos.api.service;

import com.jdcolorado.gestions.eventos.api.domain.Event;
import com.jdcolorado.gestions.eventos.api.dto.EventRequestDto;
import com.jdcolorado.gestions.eventos.api.dto.EventResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface IEventService {

    Page<EventResponseDto> findAll(String name, Pageable pageable);
    Event findById(Long id);
    Event save(EventRequestDto eventRequestDto);
    Event update(Long id, EventRequestDto eventRequestDto);
    void deleteById(Long id);

}
