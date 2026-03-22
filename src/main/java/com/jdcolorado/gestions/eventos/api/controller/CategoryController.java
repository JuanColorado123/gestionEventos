package com.jdcolorado.gestions.eventos.api.controller;

import com.jdcolorado.gestions.eventos.api.domain.Category;
import com.jdcolorado.gestions.eventos.api.dto.ApiResponse;
import com.jdcolorado.gestions.eventos.api.dto.CategoryDto;
import com.jdcolorado.gestions.eventos.api.mapper.CategoryMapper;
import com.jdcolorado.gestions.eventos.api.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<List<CategoryDto>>> getAllCategories() {

        List<Category> categoryList = categoryService.findAll();

        ApiResponse<List<CategoryDto>> response = new ApiResponse<>(
                categoryList.isEmpty() ? "No hay categorias" : "Categorias listadas correctamente",
                200,
                true,
                categoryList.stream()
                        .map(categoryMapper::toDtO)
                        .toList()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<CategoryDto>> getCategoryById(@PathVariable Long id){

        Category category = categoryService.findById(id);

        return new ResponseEntity<>(new ApiResponse<>(
                "Categoria listada correctamente",
                200,
                true,
                categoryMapper.toDtO(category)
        ),HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<CategoryDto>> createCategory(@Valid @RequestBody CategoryDto categoryDto){

        Category categoryToSave = categoryMapper.toEntity(categoryDto);
        Category savedCategory = categoryService.save(categoryToSave);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(
                        "Categoria guardada correctamente",
                        201,
                        true,
                        categoryMapper.toDtO(savedCategory)
                )
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<CategoryDto>> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryDto categoryDto){

        Category categoryToUpdate = categoryMapper.toEntity(categoryDto);
        Category updatedCategory = categoryService.update(id, categoryToUpdate);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "Categoria actualizada correctamente",
                        200,
                        true,
                        categoryMapper.toDtO(updatedCategory)
                )
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<Category>> deleteCategoryById(@PathVariable Long id){

        categoryService.deleteById(id);

        return new ResponseEntity<>(new ApiResponse<>(
                "Categoria eliminada correctamente",
                200,
                true,
                null
        ), HttpStatus.OK);
    }
}


