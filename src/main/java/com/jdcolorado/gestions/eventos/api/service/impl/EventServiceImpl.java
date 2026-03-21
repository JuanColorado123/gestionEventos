package com.jdcolorado.gestions.eventos.api.service.impl;

import com.jdcolorado.gestions.eventos.api.domain.Event;
import com.jdcolorado.gestions.eventos.api.exception.ResourceNotFoundException;
import com.jdcolorado.gestions.eventos.api.repository.EventRepository;
import com.jdcolorado.gestions.eventos.api.service.IEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements IEventService {

    private final EventRepository repository;

    @Override
    public List<Event> findAll() {
        return repository.findAll();
    }

    @Override
    public Event findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Event ID no se encontro: " + id));
    }

    @Override
    public Event save(Event event) {
        return repository.save(event);
    }

    @Override
    public void deleteById(Long id) {
        Event event = this.findById(id);
        repository.delete(event);
    }
}
