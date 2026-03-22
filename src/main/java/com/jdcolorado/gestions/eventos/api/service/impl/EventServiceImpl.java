package com.jdcolorado.gestions.eventos.api.service.impl;

import com.jdcolorado.gestions.eventos.api.domain.Category;
import com.jdcolorado.gestions.eventos.api.domain.Event;
import com.jdcolorado.gestions.eventos.api.domain.Speaker;
import com.jdcolorado.gestions.eventos.api.dto.EventRequestDto;
import com.jdcolorado.gestions.eventos.api.dto.EventResponseDto;
import com.jdcolorado.gestions.eventos.api.exception.ResourceNotFoundException;
import com.jdcolorado.gestions.eventos.api.mapper.EventMapper;
import com.jdcolorado.gestions.eventos.api.repository.EventRepository;
import com.jdcolorado.gestions.eventos.api.service.CategoryService;
import com.jdcolorado.gestions.eventos.api.service.IEventService;
import com.jdcolorado.gestions.eventos.api.service.SpeakerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements IEventService {

    private final EventRepository repository;
    private final EventMapper eventMapper;
    private final CategoryService categoryService;
    private final SpeakerService speakerService;

    @Override
    @Transactional(readOnly = true)
    public Page<EventResponseDto> findAll(String name, Pageable pageable) {

        Page<Event> eventsPage;

        if(name != null && !name.trim().isEmpty()){
            eventsPage = repository.findByNameContainingIgnoreCase(name, pageable);
        }else{
            eventsPage = repository.findAll(pageable);

        }

        List<EventResponseDto> eventResponseDtos = eventsPage
                .getContent()
                .stream()
                .map(eventMapper::toResponseDto).toList();


        return new PageImpl<>(eventResponseDtos, pageable, eventsPage.getTotalElements());

    }

    @Override
    @Transactional(readOnly = true)
    public Event findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Event ID no se encontro: " + id));
    }

    @Override
    @Transactional()
    public Event save(EventRequestDto eventRequestDto) {

        Event event = eventMapper.toEntity(eventRequestDto);

        Category category = categoryService.findById(eventRequestDto.getCategoryId());
        event.setCategory(category);

        if (eventRequestDto.getSpeakersIds() != null && !eventRequestDto.getSpeakersIds().isEmpty()) {
            Set<Speaker> speakers = eventRequestDto.getSpeakersIds().stream().map(speakerService::findById).collect(Collectors.toSet());
            speakers.forEach(event::addAttendSpeaker);
        }

        return repository.save(event);
    }

    @Override
    @Transactional()
    public Event update(Long id, EventRequestDto eventRequestDto) {
        Event existingEvent = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No se encontro el evento con ID: " + id));

        eventMapper.updatedEventFromDto(eventRequestDto, existingEvent);

        if(!existingEvent.getCategory().getId().equals(eventRequestDto.getCategoryId())){
            Category category = categoryService.findById(eventRequestDto.getCategoryId());
            existingEvent.setCategory(category);
        }

        Set<Speaker> updatedSpeakers;
        if(eventRequestDto.getSpeakersIds() != null && !eventRequestDto.getSpeakersIds().isEmpty()){
            updatedSpeakers = eventRequestDto.getSpeakersIds().stream().map(speakerService::findById).collect(Collectors.toSet());
        }else {
            updatedSpeakers = new HashSet<>();
        }
        new HashSet<>(existingEvent.getSpeakers()).forEach(currentSpeaker -> {
            if(!updatedSpeakers.contains(currentSpeaker)){
                existingEvent.removeSpeaker(currentSpeaker);
            }
        });

        updatedSpeakers.forEach(newSpeaker -> {
            if(!existingEvent.getSpeakers().contains(newSpeaker)){
                existingEvent.addAttendSpeaker(newSpeaker );
            }
        });
        return repository.save(existingEvent);
    }

    @Override
    @Transactional()
    public void deleteById(Long id) {
        Event event = this.findById(id);
        repository.delete(event);
    }
}
