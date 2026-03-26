package com.Nexora.NexoraFinance.auth_users.services.impl;

import com.Nexora.NexoraFinance.account.entity.Account;
import com.Nexora.NexoraFinance.auth_users.dtos.LoginRequest;
import com.Nexora.NexoraFinance.auth_users.dtos.LoginResponse;
import com.Nexora.NexoraFinance.auth_users.dtos.RegistrationRequest;
import com.Nexora.NexoraFinance.auth_users.dtos.ResetPasswordRequest;
import com.Nexora.NexoraFinance.auth_users.entity.User;
import com.Nexora.NexoraFinance.auth_users.repo.UserRepo;
import com.Nexora.NexoraFinance.auth_users.services.AuthService;
import com.Nexora.NexoraFinance.enums.AccountType;
import com.Nexora.NexoraFinance.exceptions.BadRequestException;
import com.Nexora.NexoraFinance.exceptions.NotFoundException;
import com.Nexora.NexoraFinance.notification.services.NotificationService;
import com.Nexora.NexoraFinance.res.Response;
import com.Nexora.NexoraFinance.role.entity.Role;
import com.Nexora.NexoraFinance.role.repo.RoleRepo;
import com.Nexora.NexoraFinance.security.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final NotificationService notificationService;




    @Override
    public Response<String> register(RegistrationRequest request) {
        List<Role> roles;

        if(request.getRoles()==null || request.getRoles().isEmpty()){
            // DEFAULT TO CUSTOMER
            Role deafaultRole = roleRepo.findByName("CUSTOMER")
                    .orElseThrow(()-> new NotFoundException("CUSTOMER ROLE NOT FOUND"));

            roles = Collections.singletonList(deafaultRole);

        }else {
            roles = request.getRoles().stream()
                    .map(roleName -> roleRepo.findByName(roleName)
                            .orElseThrow(() -> new NotFoundException("ROLE NOT FOUND " + roleName)))
                    .toList();
        }

        if(userRepo.findByEmail(request.getEmail()).isEmpty()){
            throw new BadRequestException("EMAIL ALREADY EXISTS");
        }
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .active(true)
                .build();

        User savedUser = userRepo.save(user);

        //TODO AUTO GENERATE AN ACCOUNT NUMBER FOR THE USER

        Account savedAccount = accountService.createAccount(AccountType.SAVINGS,savedUser);

        //TODO SEND A WELCOME EMAIL OF THE USER AND ACCOUNT DETAILS TO THE USERS EMAIL

    }

    @Override
    public Response<LoginResponse> login(LoginRequest loginRequest) {
        return null;
    }

    @Override
    public Response<?> forgetPassword(String email) {
        return null;
    }

    @Override
    public Response<?> updatePasswordViaResetCode(ResetPasswordRequest resetPasswordRequest) {
        return null;
    }
}
