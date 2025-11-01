package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.ProductDto;
import com.ecommerce.ecommerce.dto.Response;
import com.ecommerce.ecommerce.entity.Category;
import com.ecommerce.ecommerce.entity.Product;
import com.ecommerce.ecommerce.exception.NotFoundException;
import com.ecommerce.ecommerce.mapper.EntityDtoMapper;
import com.ecommerce.ecommerce.repository.CategoryRepo;
import com.ecommerce.ecommerce.repository.ProductRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;
    private final EntityDtoMapper entityDtoMapper;

    @Override
    public Response createProduct(Long categoryId,  String name, String description, BigDecimal price) {
        Category category = categoryRepo.findById(categoryId.compareTo(categoryId)).orElseThrow(()-> new NotFoundException("category not found"));

        Product product = new Product();
        product.setCategory(category);
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        productRepo.save(product);
        return Response.builder()
                .status(200)
                .message("Product successfully created")
                .build();
    }

    @Override
    public Response updateProduct(Long productId, Long categoryId , String name, String description, BigDecimal price) {
        Product product = productRepo.findById(productId).orElseThrow(()-> new NotFoundException("product not found"));
        Category category = null;

        if(categoryId != null) {
            category = categoryRepo.findById(categoryId.compareTo(categoryId)).orElseThrow(() -> new NotFoundException("category not found"));
        }
        if(category != null) product.setCategory(category);
        if(name != null) product.setName(name);
        if (price != null) product.setPrice(price);
        if (description != null) product.setDescription(description);
        productRepo.save(product);
        return Response.builder()
                .status(200)
                .message("Product successfully updated")
                .build();
    }

    @Override
    public Response deleteProduct(Long productId) {
        Product product = productRepo.findById(productId).orElseThrow(()-> new NotFoundException("product not found"));
        productRepo.delete(product);
        return Response.builder()
                .status(200)
                .message("Product successfully deleted")
                .build();
    }

    @Override
    public Response getProductById(Long productId) {
        Product product = productRepo.findById(productId).orElseThrow(()-> new NotFoundException("product not found"));
        ProductDto productDto = entityDtoMapper.mapProductToDtoBasic(product);

        return Response.builder()
                .status(200)
                .product(productDto)
                .build();
    }

    @Override
    public Response getAllProducts() {
        List<ProductDto> productList = productRepo.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream().map(entityDtoMapper::mapProductToDtoBasic)
                .collect(Collectors.toList());

        return Response.builder()
                .status(200)
                .productList(productList).build();
    }

    @Override
    public Response getProductByCategory(Long categoryId) {
        List<Product> products = productRepo.findByCategoryId(categoryId);
        if(products.isEmpty()){
            throw new NotFoundException("product not found");
        }
        List<ProductDto> productDtoList = products.stream().map(entityDtoMapper::mapProductToDtoBasic).collect(Collectors.toList());
        return Response.builder()
                .status(200)
                .productList(productDtoList).build();
    }

    @Override
    public Response searchProductsByCategory(Long categoryId) {
        return null;
    }

    @Override
    public Response searchProduct(String searchvalue) {
        List<Product> products = productRepo.findByNameContainingOrDescriptionContaining(searchvalue, searchvalue );
        if(products.isEmpty()){
            throw new NotFoundException("product not found");
        }
        List<ProductDto> productDtoList = products.stream().map(entityDtoMapper::mapProductToDtoBasic).collect(Collectors.toList());
        return Response.builder()
                .status(200)
                .productList(productDtoList).build();
    }
}
