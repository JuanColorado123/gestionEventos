package com.jdcolorado.gestions.eventos.api.controller;

import com.jdcolorado.gestions.eventos.api.domain.Event;
import com.jdcolorado.gestions.eventos.api.dto.EventRequestDto;
import com.jdcolorado.gestions.eventos.api.dto.EventResponseDto;
import com.jdcolorado.gestions.eventos.api.mapper.EventMapper;
import com.jdcolorado.gestions.eventos.api.service.IEventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping( "/api/v1/events")
public class EventController {

    private final IEventService service;
    private final EventMapper mapper;

    public EventController(IEventService service, @Qualifier("eventMapperImpl")EventMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<EventResponseDto>> getAllEvents(){
        List<Event> events = service.findAll();

        if(events.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        List<EventResponseDto> eventResponseDtoList = mapper.toEventResponseDtoList(events);
        return ResponseEntity.ok(eventResponseDtoList);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<EventResponseDto> getEventById(@PathVariable Long id){
        Event event = service.findById(id);
        EventResponseDto eventResponseDto = mapper.toResponseDto(event);
        return ResponseEntity.ok(eventResponseDto);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<EventResponseDto> createEvent(@Valid @RequestBody EventRequestDto eventRequestDto){
        Event eventToSave = mapper.toEntity(eventRequestDto);
        Event eventSaved = service.save(eventToSave);
        EventResponseDto eventResponseDto = mapper.toResponseDto(eventSaved);

        return ResponseEntity.ok(eventResponseDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<EventResponseDto> updateEvent(@PathVariable Long id, @Valid @RequestBody EventRequestDto eventRequestDto){

        Event eventToUpdated = service.findById(id);
        mapper.updatedEventFromDto(eventRequestDto, eventToUpdated);
        Event updateEvent = service.save(eventToUpdated);

        return ResponseEntity.ok(mapper.toResponseDto(updateEvent));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Void> deleteEventById(@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
