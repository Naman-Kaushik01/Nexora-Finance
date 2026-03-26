package com.Nexora.NexoraFinance.auth_users.services;

import com.Nexora.NexoraFinance.auth_users.dtos.LoginRequest;
import com.Nexora.NexoraFinance.auth_users.dtos.LoginResponse;
import com.Nexora.NexoraFinance.auth_users.dtos.RegistrationRequest;
import com.Nexora.NexoraFinance.auth_users.dtos.ResetPasswordRequest;
import com.Nexora.NexoraFinance.res.Response;

public interface AuthService {
    Response<String> register(RegistrationRequest request);

    Response<LoginResponse> login(LoginRequest loginRequest);
    Response<?> forgetPassword(String email);
    Response<?> updatePasswordViaResetCode(ResetPasswordRequest resetPasswordRequest);

}