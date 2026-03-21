package com.jdcolorado.gestions.eventos.api.service;

import com.jdcolorado.gestions.eventos.api.domain.Event;

import java.util.List;

public interface IEventService {

    List<Event> findAll();
    Event findById(Long id);
    Event save(Event event);
    void deleteById(Long id);

}
