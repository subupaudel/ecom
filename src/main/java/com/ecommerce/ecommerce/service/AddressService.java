package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.AddressDto;
import com.ecommerce.ecommerce.dto.Response;

public interface AddressService {

    Response saveAndUpdateAddress(AddressDto addressDto);
}
