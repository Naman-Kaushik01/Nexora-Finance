package com.Nexora.NexoraFinance.auth_users.controller;

import com.Nexora.NexoraFinance.auth_users.dtos.UpdatePasswordRequest;
import com.Nexora.NexoraFinance.auth_users.dtos.UserDTO;
import com.Nexora.NexoraFinance.auth_users.services.UserService;
import com.Nexora.NexoraFinance.res.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response<Page<UserDTO>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size

    ) {
        return ResponseEntity.ok(userService.getAllUsers(page , size));
    }

    @GetMapping("/me")
    public ResponseEntity<Response<UserDTO>> getMyProfile() {
        return ResponseEntity.ok(userService.getMyProfile());
    }

    @PutMapping("/update-password")
    public ResponseEntity<Response<?>> updatePassword(@RequestBody @Valid UpdatePasswordRequest updatePasswordRequest) {
        return ResponseEntity.ok(userService.updatePassword(updatePasswordRequest));
    }

    @PutMapping("/update-profile-picture")
    public ResponseEntity<Response<?>> updateProfilePicture(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(userService.uploadProfilePicture(file));
    }



}
