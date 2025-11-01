package com.ecommerce.ecommerce.dto;
import com.ecommerce.ecommerce.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {

    private long id;
    private String street;
    private String city;
    private String zipCode;
    private String country;
    private String state;

    private UserDto user;


    private LocalDateTime createdAt;
}
