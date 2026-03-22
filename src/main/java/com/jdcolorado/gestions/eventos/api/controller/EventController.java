package com.jdcolorado.gestions.eventos.api.controller;

import com.jdcolorado.gestions.eventos.api.domain.Event;
import com.jdcolorado.gestions.eventos.api.dto.EventRequestDto;
import com.jdcolorado.gestions.eventos.api.dto.EventResponseDto;
import com.jdcolorado.gestions.eventos.api.mapper.EventMapper;
import com.jdcolorado.gestions.eventos.api.service.IEventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    public ResponseEntity<Page<EventResponseDto>> getAllEvents(
            @RequestParam(required = false) String name,
            @PageableDefault(page = 0, size = 10, sort = "name")Pageable pageable){

        Page<EventResponseDto> eventResponseDtoPage = service.findAll(name,pageable);

        if(eventResponseDtoPage.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(eventResponseDtoPage);
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
        Event eventSaved = service.save(eventRequestDto);
        EventResponseDto eventResponseDto = mapper.toResponseDto(eventSaved);

        return ResponseEntity.ok(eventResponseDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<EventResponseDto> updateEvent(@PathVariable Long id, @Valid @RequestBody EventRequestDto eventRequestDto){

        Event eventToUpdated = service.update(id, eventRequestDto);
        mapper.updatedEventFromDto(eventRequestDto, eventToUpdated);
        return ResponseEntity.ok(mapper.toResponseDto(eventToUpdated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Void> deleteEventById(@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
