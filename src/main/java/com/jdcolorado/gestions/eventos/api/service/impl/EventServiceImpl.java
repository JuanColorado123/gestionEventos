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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final CategoryService categoryService;
    private final SpeakerService speakerService;

    @Override
    @Transactional(readOnly = true)
    public Page<EventResponseDto> findAll(String name, Pageable pageable) {
        logger.debug("Buscando eventos en el servicio (name: '{}', pageable: {}).", name, pageable);
        Page<Event> eventsPage;

        if(name!=null && !name.trim().isEmpty()){
            eventsPage = eventRepository.findByNameContainingIgnoreCase(name, pageable);
            logger.debug("Filtrando eventos por nombre: '{}'. Encontrados: {}.", name, eventsPage.getTotalElements());
        }else {
            eventsPage = eventRepository.findAll(pageable);
            logger.debug("Buscando todos los eventos sin filtro. Encontrados: {}.", eventsPage.getTotalElements());
        }

        List<EventResponseDto> dtos = eventsPage.getContent().stream()
                .map(eventMapper::toResponseDto)
                .toList();

        logger.info("Encontrados {} eventos paginados y mapeados a DTOs.", eventsPage.getTotalElements());
        return new PageImpl<>(dtos, pageable, eventsPage.getTotalElements());
    }

    @Override
    @Transactional
    public Event save(EventRequestDto requestDto) {
        logger.debug("Procesando save de evento en el servicio para: {}", requestDto.getName());
        Event event = eventMapper.toEntity(requestDto);

        logger.debug("Asignando categoría con ID: {}", requestDto.getCategoryId());
        Category category = categoryService.findById(requestDto.getCategoryId());
        event.setCategory(category);

        if(requestDto.getSpeakersIds() !=null && !requestDto.getSpeakersIds().isEmpty()){
            logger.debug("Asignando {} oradores al evento.", requestDto.getSpeakersIds().size());
            Set<Speaker> speakers = requestDto.getSpeakersIds().stream()
                    .map(speakerService::findById)
                    .collect(Collectors.toSet());
            speakers.forEach(event::addAttendSpeaker);
        } else {
            logger.debug("No se especificaron oradores para el evento.");
        }

        Event savedEvent = eventRepository.save(event);
        logger.info("Evento '{}' guardado en DB con ID: {}.", savedEvent.getName(), savedEvent.getId());
        return savedEvent;
    }

    @Override
    @Transactional(readOnly = true)
    public Event findById(Long id) {
        logger.debug("Buscando evento en el repositorio por ID: {}.", id);
        return eventRepository.findById(id).orElseThrow(
                () -> {
                    logger.warn("Evento con ID {} no encontrado en el servicio, lanzando ResourceNotFoundException.", id);
                    return new ResourceNotFoundException("Evento no encontrado con id: " + id);
                }
        );
    }

    @Override
    @Transactional
    public Event update(Long id, EventRequestDto requestDto) {
        logger.debug("Iniciando actualización de evento con ID {} en el servicio.", id);
        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(
                        () -> {
                            logger.warn("Intento de actualizar evento con ID {} que no existe, lanzando ResourceNotFoundException.", id);
                            return new ResourceNotFoundException("Evento no encontrado con ID: " + id);
                        }
                );
        logger.debug("Evento existente con ID {} encontrado. Mapeando DTO a entidad.", id);
        eventMapper.updatedEventFromDto(requestDto, existingEvent);

        if(!existingEvent.getCategory().getId().equals(requestDto.getCategoryId())){
            logger.debug("Cambiando categoría del evento de ID {} a ID {}.", existingEvent.getCategory().getId(), requestDto.getCategoryId());
            Category category = categoryService.findById(requestDto.getCategoryId());
            existingEvent.setCategory(category);
        }

        Set<Speaker> updatedSpeakers;
        if(requestDto.getSpeakersIds() !=null && !requestDto.getSpeakersIds().isEmpty()){
            logger.debug("Procesando {} oradores para la actualización del evento.", requestDto.getSpeakersIds().size());
            updatedSpeakers = requestDto.getSpeakersIds().stream()
                    .map(speakerService::findById)
                    .collect(Collectors.toSet());
        } else {
            updatedSpeakers = new HashSet<>();
            logger.debug("No se especificaron oradores para la actualización, se eliminarán los existentes si los hay.");
        }

        new HashSet<>(existingEvent.getSpeakers())
                .forEach(currentSpeaker -> {
                    if(!updatedSpeakers.contains(currentSpeaker)){
                        existingEvent.removeSpeaker(currentSpeaker);
                        logger.debug("Eliminando orador '{}' (ID: {}) del evento.", currentSpeaker.getName(), currentSpeaker.getId());
                    }
                });

        updatedSpeakers.forEach(newSpeaker -> {
            if(!existingEvent.getSpeakers().contains(newSpeaker)){
                existingEvent.addAttendSpeaker(newSpeaker);
                logger.debug("Añadiendo orador '{}' (ID: {}) al evento.", newSpeaker.getName(), newSpeaker.getId());
            }
        });

        Event updatedEvent = eventRepository.save(existingEvent);
        logger.info("Evento con ID {} actualizado en la base de datos.", id);
        return updatedEvent;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        logger.debug("Solicitud de eliminación para evento con ID {} en el servicio.", id);
        Event eventToDelete = this.findById(id); // findById ya lanzará ResourceNotFoundException si no existe
        eventRepository.delete(eventToDelete);
        logger.info("Evento con ID {} eliminado de la base de datos.", id);
    }
}