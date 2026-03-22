package com.jdcolorado.gestions.eventos.api.service.impl;

import com.jdcolorado.gestions.eventos.api.domain.Category;
import com.jdcolorado.gestions.eventos.api.exception.ResourceNotFoundException;
import com.jdcolorado.gestions.eventos.api.repository.CategoryRepository;
import com.jdcolorado.gestions.eventos.api.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Category findById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No se encontro la cetegoria con el ID:" + id));
    }

    @Override
    @Transactional()
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    @Transactional()
    public Category update(Long id, Category category) {

        Category existingCategory = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No se encontro la cetegoria con el ID:" + id));

        existingCategory.setName(category.getName());
        existingCategory.setDescription(category.getDescription());
        return categoryRepository.save(category);
    }

    @Override
    @Transactional()
    public void deleteById(Long id) {
        if(!categoryRepository.existsById(id)){
            throw new ResourceNotFoundException("No se encontro la cetegoria con el ID:" + id);
        }
        categoryRepository.deleteById(id);
    }
}
