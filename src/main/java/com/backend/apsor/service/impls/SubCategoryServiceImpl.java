package com.backend.apsor.service.impls;

import com.backend.apsor.entities.Category;
import com.backend.apsor.entities.SubCategory;
import com.backend.apsor.enums.ApiErrorCode;
import com.backend.apsor.enums.Status;
import com.backend.apsor.exceptions.ApiException;
import com.backend.apsor.mapper.SubCategoryMapper;
import com.backend.apsor.payloads.dtos.SubCategoryDTO;
import com.backend.apsor.payloads.requests.*;
import com.backend.apsor.repositories.CategoryRepo;
import com.backend.apsor.repositories.SubCategoryRepo;
import com.backend.apsor.service.SubCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubCategoryServiceImpl implements SubCategoryService {

    private final SubCategoryRepo subCategoryRepo;
    private final SubCategoryMapper subCategoryMapper;
    private final CategoryRepo categoryRepo;

    @Override
    public SubCategoryDTO createNewSubCategory(SubCategoryReq req) {

        Long categoryId = Long.valueOf(req.getCategoryId());

        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.CATEGORY_NOT_FOUND,
                        "Category not found with id: %s",
                        categoryId
                ));

        String name = req.getName().trim();

        if (subCategoryRepo.existsByCategory_IdAndNameIgnoreCase(categoryId, name)) {
            throw ApiException.conflict(
                    ApiErrorCode.SUBCATEGORY_NAME_EXISTS,
                    "SubCategory name already exists in this category: %s",
                    name
            );
        }

        SubCategory subCategory = subCategoryMapper.toEntity(req);
        subCategory.setCategory(category);

        SubCategory saved = subCategoryRepo.save(subCategory);
        return subCategoryMapper.toDto(saved);
    }

    @Override
    public List<SubCategoryDTO> getAllSubCategory() {
        return subCategoryMapper.toListDto(subCategoryRepo.findAll());
    }

    @Override
    public SubCategoryDTO getSubCategoryById(Long id) {
        return subCategoryRepo.findById(id)
                .map(subCategoryMapper::toDto)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.SUBCATEGORY_NOT_FOUND,
                        String.format("Category with id %d not found", id),
                        id.toString()
                ));
    }


    @Override
    public SubCategoryDTO updateSubCategoryById(Long id, SubCategoryUpdateReq req) {
        SubCategory subCategory = subCategoryRepo.findById(id)
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.CATEGORY_NOT_FOUND,
                        "Category not found with id: ",
                        id.toString()));

        subCategoryMapper.updateEntity(req, subCategory);

        SubCategory updatedCategory = subCategoryRepo.save(subCategory);
        return subCategoryMapper.toDto(updatedCategory);
    }

    @Override
    public SubCategoryDTO updateStatusById(Long id, SubCategoryStatusReq req) {
        return subCategoryRepo.findById(id)
                .map(category -> {
                    category.setStatus(req.getStatus());
                    subCategoryRepo.save(category);
                    return subCategoryMapper.toDto(category);
                })
                .orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.SUBCATEGORY_NOT_FOUND,
                        "Sub Category with id " + id + " not found.",
                        id.toString()
                ));
    }


    @Override
    public String softDeleteById(Long id) {
        return subCategoryRepo.findById(id)
                .map(category -> {
                    category.setStatus(Status.DELETED);
                    category.setDeletedAt(Instant.now());
                    subCategoryRepo.save(category);
                    return "Category with id " + id + " has been deleted";
                }).orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.CATEGORY_NOT_FOUND,
                        "Category with id " + id + " not found.",
                        id.toString()
                ));
    }

    @Override
    public String deleteCategoryById(Long id) {
        return subCategoryRepo.findById(id)
                .map(category -> {
                    subCategoryRepo.deleteById(id);
                    return "Category with id " + id + " has been deleted";
                }).orElseThrow(() -> ApiException.notFound(
                        ApiErrorCode.CATEGORY_NOT_FOUND,
                        "Category with id " + id + " not found.",
                        id.toString()
                ));
    }
}
