package com.jdcolorado.gestions.eventos.api.mapper;

import com.jdcolorado.gestions.eventos.api.domain.Speaker;
import com.jdcolorado.gestions.eventos.api.dto.SpeakerRequestDto;
import com.jdcolorado.gestions.eventos.api.dto.SpeakerResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SpeakerMapper {

    SpeakerResponseDto toDto(Speaker speaker);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "events", ignore = true)
    Speaker toEntity(SpeakerRequestDto speakerRequestDto);

    List<SpeakerResponseDto> toResponseListDto(List<Speaker> speakers);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "events", ignore = true)
    void updateSpeakerFromDto(SpeakerRequestDto requestDto, @MappingTarget Speaker speaker);
}
