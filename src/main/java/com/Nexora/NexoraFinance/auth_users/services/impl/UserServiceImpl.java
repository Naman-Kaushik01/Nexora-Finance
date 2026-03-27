package com.Nexora.NexoraFinance.auth_users.services.impl;

import com.Nexora.NexoraFinance.auth_users.dtos.LoginResponse;
import com.Nexora.NexoraFinance.auth_users.dtos.UpdatePasswordRequest;
import com.Nexora.NexoraFinance.auth_users.dtos.UserDTO;
import com.Nexora.NexoraFinance.auth_users.entity.User;
import com.Nexora.NexoraFinance.auth_users.repo.UserRepo;
import com.Nexora.NexoraFinance.auth_users.services.UserService;
import com.Nexora.NexoraFinance.exceptions.BadRequestException;
import com.Nexora.NexoraFinance.exceptions.NotFoundException;
import com.Nexora.NexoraFinance.notification.dtos.NotificationDTO;
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

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final NotificationService notificationService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    private final String uploadDir ="uploads/profile-pictures/";

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
        User user = getCurrentLoggedInUser();

        String newPassword = updatePasswordRequest.getNewPassword();
        String oldPassword = updatePasswordRequest.getOldPassword();

        if(oldPassword == null || newPassword == null) {
            throw new BadRequestException("Old Password or New Password is required");
        }

        //validate the old password
        if(!passwordEncoder.matches(oldPassword,user.getPassword())) {
            throw new BadRequestException("Old Password not correct");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());

        userRepo.save(user);

        //SEND PASSWORD CHANGE CONFIRMATION E-MAIL

        Map<String,Object> templateVariable = new HashMap<>();
        templateVariable.put("name" , user.getFirstName());

        NotificationDTO notificationDTO = NotificationDTO.builder()
                .recipient(user.getEmail())
                .subject("Your password has been successfully changed ")
                .templateName("password-change")
                .templateVariables(templateVariable)
                .build();

        notificationService.sendEmail(notificationDTO , user);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Password has been changed successfully")
                .build();

    }

    @Override
    public Response<?> uploadProfilePicture(MultipartFile file) {
        User user = getCurrentLoggedInUser();

        try{
            Path uploadPath = Paths.get(uploadDir);
            if(!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            if(user.getProfilePictureURL() !=null && !user.getProfilePictureURL().isEmpty()) {
                Path oldFile = Paths.get(user.getProfilePictureURL());
                if(Files.exists(oldFile)) {
                    Files.delete(oldFile);
                }
            }

            //GENERATE A UNIQUE FILE NAME TO AVOID CONFLICTS

            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if(originalFilename != null && originalFilename.contains(".")){
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String newFileName = UUID.randomUUID() + fileExtension;
            Path filePath = uploadPath.resolve(newFileName);

            Files.copy(file.getInputStream() ,filePath );

            String fileUrl = uploadDir + newFileName;
            user.setProfilePictureURL(fileUrl);
            userRepo.save(user);

            return Response.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Profile picture updated successfully")
                    .data(fileUrl)
                    .build();


        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
