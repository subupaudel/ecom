package com.ecommerce.ecommerce.service;


import com.ecommerce.ecommerce.dto.CategoryDto;
import com.ecommerce.ecommerce.dto.Response;

public interface CategoryService {

    Response createCategory(CategoryDto categoryRequest);
    Response updateCategory(Long categoryId, CategoryDto categoryRequest);
    Response getAllCategories();
    Response getCategoryById(Long categoryId);
    Response deleteCategory(Long categoryId);
}
