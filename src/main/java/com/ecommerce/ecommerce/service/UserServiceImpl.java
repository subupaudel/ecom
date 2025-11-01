package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.LoginRequest;
import com.ecommerce.ecommerce.dto.Response;
import com.ecommerce.ecommerce.dto.UserDto;
import com.ecommerce.ecommerce.entity.User;
import com.ecommerce.ecommerce.enums.Role;
import com.ecommerce.ecommerce.exception.InvalidCredentialsException;
import com.ecommerce.ecommerce.exception.NotFoundException;
import com.ecommerce.ecommerce.mapper.EntityDtoMapper;
import com.ecommerce.ecommerce.repository.UserRepo;
import com.ecommerce.ecommerce.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final EntityDtoMapper entityDtoMapper;

    @Override
    public Response registerUser(UserDto registrationRequest) {

        if (userRepo.existsByEmail(registrationRequest.getEmail())) {
            return Response.builder()
                    .message("Email already exists")
                    .build();
        }

        if (userRepo.existsByPhoneNumber(registrationRequest.getPhoneNumber())) {
            return Response.builder()
                    .message("Phone number already exists")
                    .build();
        }
        User user = User.builder()
                .name(registrationRequest.getName())
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .phoneNumber(registrationRequest.getPhoneNumber())
                .role(registrationRequest.getRole() != null ? registrationRequest.getRole() : Role.USER)
                .build();

        // Save user
        User savedUser = userRepo.save(user);

        // Map to DTO
        UserDto userDto = entityDtoMapper.mapUserToDtoBasic(savedUser);

        return Response.builder()
                .status(200)
                .message("User Successfully Added")
                .user(userDto)
                .build();
    }


    @Override
    public Response loginUser(LoginRequest loginRequest) {

        User user = userRepo.findByEmail(loginRequest.getEmail()).orElseThrow(()-> new NotFoundException("Email not found"));
        if (!passwordEncoder.matches(loginRequest.getPassword(),user.getPassword())){
            throw new InvalidCredentialsException("Password does not match");
        }
        String token = jwtUtils.generateToken(user);

        return Response.builder()
                .status(200)
                .message("User Successfully Logged In")
                .token(token)
                .expirationTime("6 Month")
                .build();
    }

    @Override
    public Response getAllUsers() {

        List<User> users = userRepo.findAll();
        List<UserDto> userDtos = users.stream().map(entityDtoMapper::mapUserToDtoBasic).collect(Collectors.toList());
        return Response.builder()
                .status(200)
                .message("Successful")
                .userList(userDtos)
                .build();
    }

    @Override
    public User getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        log.info("User Email is: "+email);
        return userRepo.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("user not found"));
    }

    @Override
    public Response getUserInfoAndOrderHistory() {
        User user = getLoginUser();
        UserDto userDto = entityDtoMapper.mapUserToDtoPlusAddressAndOrderHistory(user);
        return Response.builder()
                .status(200)
                .user(userDto)
                .build();
    }
}
