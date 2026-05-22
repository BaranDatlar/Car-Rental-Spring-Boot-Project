package com.baran.rentacar.business.abstracts;

import com.baran.rentacar.business.requests.LoginRequest;
import com.baran.rentacar.business.requests.RegisterRequest;
import com.baran.rentacar.business.responses.AuthResponse;

public interface AuthService {
    void register(RegisterRequest registerRequest);
    AuthResponse login(LoginRequest loginRequest);
}
