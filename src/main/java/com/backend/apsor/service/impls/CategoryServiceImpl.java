package com.backend.apsor.service.impls;

import com.backend.apsor.entities.Category;
import com.backend.apsor.enums.ApiErrorCode;
import com.backend.apsor.enums.Status;
import com.backend.apsor.exceptions.ApiException;
import com.backend.apsor.mapper.CategoryMapper;
import com.backend.apsor.payloads.dtos.CategoryDTO;
import com.backend.apsor.payloads.requests.CategoryReq;
import com.backend.apsor.payloads.requests.CategoryStatusReq;
import com.backend.apsor.repositories.CategoryRepo;
import com.backend.apsor.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDTO createNewCategory(CategoryReq req) {
        if (categoryRepository.existsByNameIgnoreCase(req.getName())){
            throw  ApiException.conflict(
                    ApiErrorCode.CATEGORY_NAME_EXISTS
                    ,"Category name already exists: %s",
                    req.getName()
                );
        }

        Category category = categoryMapper.toEntity(req);

        String generateSlugName = generateSlug(req.getName());
        category.setSlug(generateSlugName);
        category.setStatus(Status.ACTIVE);
        categoryRepository.save(category);

        return categoryMapper.toDto(category);
    }

    private String generateSlug(String name) {
        String normalized = Normalizer.normalize(name, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{InCombiningDiacriticalMarks}", "")
                .replaceAll("[^\\w\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .toLowerCase()
                .replaceAll("^-|-$", "");
    }

    @Override
    public List<CategoryDTO> getAllCategory() {
        List<Category> categories = categoryRepository.findAll();
        return categoryMapper.toListDto(categories);
    }

    @Override
    public CategoryDTO getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::toDto)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.CATEGORY_NOT_FOUND,
                        String.format("Category with id %d not found", id),
                        id.toString()
                ));
    }


    @Override
    public CategoryDTO updateCategoryById(Long id, CategoryReq req) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.CATEGORY_NOT_FOUND,
                        "Category not found with id: ",
                        id.toString()));

        categoryMapper.updateEntity(req, category);

        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(updatedCategory);
    }

    @Override
    public String softDeleteById(Long id) {
        return categoryRepository.findById(id)
                .map(category -> {
                    category.setStatus(Status.DELETED);
                    category.setDeletedAt(Instant.now());
                    categoryRepository.save(category);
                    return "Category with id " + id + " has been deleted";
                }).orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.CATEGORY_NOT_FOUND,
                        "Category with id " + id + " not found.",
                        id.toString()
                ));
    }

    @Override
    public CategoryDTO updateStatusById(Long id, CategoryStatusReq req) {
        return categoryRepository.findById(id)
                .map(category -> {
                    category.setStatus(req.getStatus());
                    categoryRepository.save(category);
                    return categoryMapper.toDto(category);
                })
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.CATEGORY_NOT_FOUND,
                        "Category with id " + id + " not found.",
                        id.toString()
                ));
    }

    @Override
    public String deleteCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(category -> {
                    categoryRepository.deleteById(id);
                    return "Category with id " + id + " has been deleted";
                }).orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.CATEGORY_NOT_FOUND,
                        "Category with id " + id + " not found.",
                        id.toString()
                ));
    }


}
