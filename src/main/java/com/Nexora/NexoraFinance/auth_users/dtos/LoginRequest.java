package com.Nexora.NexoraFinance.auth_users.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class LoginRequest {

    private String token;
    private List<String> roles;
}
