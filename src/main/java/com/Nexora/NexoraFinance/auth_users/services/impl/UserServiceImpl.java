package com.Nexora.NexoraFinance.auth_users.services.impl;

import com.Nexora.NexoraFinance.auth_users.dtos.UpdatePasswordRequest;
import com.Nexora.NexoraFinance.auth_users.dtos.UserDTO;
import com.Nexora.NexoraFinance.auth_users.entity.User;
import com.Nexora.NexoraFinance.auth_users.repo.UserRepo;
import com.Nexora.NexoraFinance.auth_users.services.UserService;
import com.Nexora.NexoraFinance.exceptions.NotFoundException;
import com.Nexora.NexoraFinance.notification.services.NotificationService;
import com.Nexora.NexoraFinance.res.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final NotificationService notificationService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public User getCurrentLoggedInUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null) {
           throw new NotFoundException("User is not authenticated");
        }
       String email = authentication.getName();
        return userRepo.findByEmail(email).orElseThrow(
                () -> new NotFoundException("User not found with email: " + email)
        );
    }

    @Override
    public Response<UserDTO> getMyProfile() {
        User user = getCurrentLoggedInUser();
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        return Response.<UserDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("User retrieved successfully")
                .data(userDTO)
                .build();
    }

    @Override
    public Response<Page<UserDTO>> getAllUsers(int page, int size) {

        Page<User> users = userRepo.findAll(PageRequest.of(page, size));
        Page<UserDTO> userDTOS = users.map(user->
                modelMapper.map(user, UserDTO.class)
        );
        return Response.<Page<UserDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("User retrieved successfully")
                .data(userDTOS)
                .build();

    }

    @Override
    public Response<?> updatePassword(UpdatePasswordRequest updatePasswordRequest) {
        return null;
    }

    @Override
    public Response<?> uploadProfilePicture(MultipartFile file) {
        return null;
    }
}
