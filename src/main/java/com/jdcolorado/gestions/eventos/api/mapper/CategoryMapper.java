package com.jdcolorado.gestions.eventos.api.mapper;

import com.jdcolorado.gestions.eventos.api.domain.Category;
import com.jdcolorado.gestions.eventos.api.dto.CategoryDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto toDtO(Category category);
    Category toEntity(CategoryDto categoryDto);
}
