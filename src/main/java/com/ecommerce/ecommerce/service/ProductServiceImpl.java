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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.*;
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
    public Response createProduct(Long categoryId, String name, String description, MultipartFile multipartFile, BigDecimal price) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category not found"));

        Product product = new Product();
        product.setCategory(category);
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);

        if (multipartFile != null && !multipartFile.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
                Path uploadPath = Paths.get("uploads/images");
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Files.copy(multipartFile.getInputStream(), uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                product.setImageUrl("/images/" + fileName);
            } catch (IOException e) {
                log.error("Error saving file", e);
                throw new RuntimeException("File upload failed", e);
            }
        }

        productRepo.save(product);

        ProductDto productDto = entityDtoMapper.mapProductToDtoBasic(product);
        return Response.builder()
                .status(200)
                .message("Product successfully created")
                .product(productDto)
                .build();
    }

    @Override
    public Response updateProduct(Long productId, Long categoryId, String name, String description, BigDecimal price, MultipartFile multipartFile) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        if (categoryId != null) {
            Category category = categoryRepo.findById(categoryId)
                    .orElseThrow(() -> new NotFoundException("Category not found"));
            product.setCategory(category);
        }

        if (name != null && !name.trim().isEmpty()) product.setName(name);
        if (description != null && !description.trim().isEmpty()) product.setDescription(description);
        if (price != null) product.setPrice(price);

        if (multipartFile != null && !multipartFile.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
                Path uploadPath = Paths.get("uploads/images");
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Files.copy(multipartFile.getInputStream(), uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                product.setImageUrl("images/" + fileName);
            } catch (IOException e) {
                log.error("Error saving file", e);
                throw new RuntimeException("File upload failed", e);
            }
        }

        productRepo.save(product);

        ProductDto productDto = entityDtoMapper.mapProductToDtoBasic(product);
        return Response.builder()
                .status(200)
                .message("Product successfully updated")
                .product(productDto)
                .build();
    }

    @Override
    public Response deleteProduct(Long productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        productRepo.delete(product);
        return Response.builder()
                .status(200)
                .message("Product successfully deleted")
                .build();
    }

    @Override
    public Response getProductById(Long productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        ProductDto productDto = entityDtoMapper.mapProductToDtoBasic(product);
        return Response.builder()
                .status(200)
                .product(productDto)
                .build();
    }

    @Override
    public Response getAllProducts() {
        List<ProductDto> productList = productRepo.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(entityDtoMapper::mapProductToDtoBasic)
                .collect(Collectors.toList());

        return Response.builder()
                .status(200)
                .productList(productList)
                .build();
    }

    @Override
    public Response getProductByCategory(Long categoryId) {
        List<Product> products = productRepo.findByCategoryId(categoryId);
        if (products.isEmpty()) {
            throw new NotFoundException("Product not found");
        }
        List<ProductDto> productDtoList = products.stream()
                .map(entityDtoMapper::mapProductToDtoBasic)
                .collect(Collectors.toList());
        return Response.builder()
                .status(200)
                .productList(productDtoList)
                .build();
    }

    @Override
    public Response searchProduct(String searchValue) {
        List<Product> products = productRepo.findByNameContainingOrDescriptionContaining(searchValue, searchValue);
        if (products.isEmpty()) {
            throw new NotFoundException("Product not found");
        }
        List<ProductDto> productDtoList = products.stream()
                .map(entityDtoMapper::mapProductToDtoBasic)
                .collect(Collectors.toList());
        return Response.builder()
                .status(200)
                .productList(productDtoList)
                .build();
    }

    @Override
    public Response searchProductsByCategory(Long categoryId) {
        // Optional future implementation
        return null;
    }
}
