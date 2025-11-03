package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.AddressDto;
import com.ecommerce.ecommerce.dto.Response;
import com.ecommerce.ecommerce.dto.UserDto;
import com.ecommerce.ecommerce.entity.Address;
import com.ecommerce.ecommerce.entity.User;
import com.ecommerce.ecommerce.mapper.EntityDtoMapper;
import com.ecommerce.ecommerce.repository.AddressRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepo addressRepo;
    private final UserService userService;
    private final EntityDtoMapper entityDtoMapper;

    @Override
    public Response saveAndUpdateAddress(AddressDto addressDto){
        User user = userService.getLoginUser();
        Address address = user.getAddress();

        if(user.getAddress() == null) {
            user.setAddress(address);
        }

        if(addressDto.getStreet() != null) address.setStreet(addressDto.getStreet());
        if(addressDto.getCity() != null) address.setCity(addressDto.getCity());
        if(addressDto.getState() != null) address.setState(addressDto.getState());
        if(addressDto.getZipCode() != null) address.setZipCode(addressDto.getZipCode());
        if(addressDto.getCountry() != null) address.setCountry(addressDto.getCountry());
        addressRepo.save(address);

        UserDto userDto = entityDtoMapper.mapUserToDtoPlusAddress(user);
        String message = (user.getAddress() == null) ? "Address successfully created" : "Address successfully updated";

        return Response.builder()
                .status(200)
                .message(message)
                .user(userDto)
                .build();
    }
}
