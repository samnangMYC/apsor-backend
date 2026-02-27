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
import com.backend.apsor.util.SlugUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.backend.apsor.enums.ApiErrorCode.CATEGORY_SLUG_EXISTS;
import static com.backend.apsor.enums.ApiErrorCode.INVALID_REQUEST;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo categoryRepository;
    private final CategoryMapper categoryMapper;
    private static final int SLUG_MAX_LEN = 140;

    @Transactional
    public CategoryDTO createNewCategory(CategoryReq req) {

        String enName = Optional.ofNullable(req.getName())
                .map(m -> m.get("en"))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .orElseThrow(() -> ApiException.badRequest(
                        INVALID_REQUEST,
                        "Category English name (name.en) is required"
                ));

        String slug = SlugUtil.slugify(enName, SLUG_MAX_LEN)
                .orElseThrow(() -> ApiException.badRequest(
                        INVALID_REQUEST,
                        "Cannot generate slug from name.en"
                ));

        if (categoryRepository.existsBySlugIgnoreCase(slug)) {
            throw ApiException.conflict(
                    CATEGORY_SLUG_EXISTS,
                    "Slug already exists: %s",
                    slug
            );
        }
        Category category = categoryMapper.toEntity(req);
        category.setSlug(slug);
        category.setStatus(Status.ACTIVE);
        category.setSortOrder(Objects.requireNonNullElse(category.getSortOrder(), 0));

        return categoryMapper.toDto(categoryRepository.save(category));
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
