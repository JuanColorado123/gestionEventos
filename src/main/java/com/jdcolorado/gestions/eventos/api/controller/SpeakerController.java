package com.jdcolorado.gestions.eventos.api.controller;

import com.jdcolorado.gestions.eventos.api.domain.Speaker;
import com.jdcolorado.gestions.eventos.api.dto.ApiResponse;
import com.jdcolorado.gestions.eventos.api.dto.SpeakerRequestDto;
import com.jdcolorado.gestions.eventos.api.dto.SpeakerResponseDto;
import com.jdcolorado.gestions.eventos.api.mapper.SpeakerMapper;
import com.jdcolorado.gestions.eventos.api.service.SpeakerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/speakers")
@RequiredArgsConstructor
public class SpeakerController {
    private final SpeakerService speakerService;
    private final SpeakerMapper speakerMapper;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<List<SpeakerResponseDto>>> getAllSpeakers(){

        List<Speaker> speakers = speakerService.findAll();

        return new ResponseEntity<>(
                new ApiResponse<>(
                        speakers.isEmpty() ? "No se encontraron speakers" : "Speakers listados correctamente",
                        200,
                        true,
                        speakerMapper.toResponseListDto(speakers)
                ),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<SpeakerResponseDto>> getSpeakerById(@PathVariable Long id){

        Speaker speaker = speakerService.findById(id);

        return new ResponseEntity<>(new ApiResponse<>(
                "Speaker listado correctamente",
                200,
                true,
                speakerMapper.toDto(speaker)
        ),HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<SpeakerResponseDto>> createSpeaker(@Valid @RequestBody SpeakerRequestDto speakerRequestDto){

        Speaker speakerToSave = speakerMapper.toEntity(speakerRequestDto);
        Speaker savedSpeaker = speakerService.save(speakerToSave);

        return new ResponseEntity<>(new ApiResponse<>(
                "Speaker creado correctamente",
                201,
                true,
                speakerMapper.toDto(savedSpeaker)
        ), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<SpeakerResponseDto>> updateSpeaker(
            @PathVariable Long id ,
            @Valid @RequestBody SpeakerRequestDto speakerRequestDto){

        Speaker speakerToUpdate = new Speaker();

        speakerMapper.updateSpeakerFromDto(speakerRequestDto, speakerToUpdate);

        Speaker updatedSpeaker = speakerService.update(id, speakerToUpdate);

        return new ResponseEntity<>(new ApiResponse<>(
                "Speaker actualizado correctamente",
                200,
                true,
                speakerMapper.toDto(updatedSpeaker)
        ),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<SpeakerResponseDto>> deleteSpeakerById(@PathVariable Long id){

        speakerService.deleteById(id);

        return new ResponseEntity<>(new ApiResponse<>(
                "Speaker eliminado correctamente",
                200,
                true,
                null
        ),HttpStatus.OK);
    }
}
