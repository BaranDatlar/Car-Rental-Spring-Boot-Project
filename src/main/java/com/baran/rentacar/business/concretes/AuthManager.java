package com.baran.rentacar.business.concretes;

import com.baran.rentacar.business.abstracts.AuthService;
import com.baran.rentacar.business.requests.LoginRequest;
import com.baran.rentacar.business.requests.RegisterRequest;
import com.baran.rentacar.business.responses.AuthResponse;
import com.baran.rentacar.core.security.JwtService;
import com.baran.rentacar.core.utilities.exceptions.BusinessException;
import com.baran.rentacar.dataAccess.abstracts.UserRepository;
import com.baran.rentacar.entities.concretes.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor

public class AuthManager implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public void register(RegisterRequest registerRequest) {
        if(userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new BusinessException("Email already exists");
        }
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole("ROLE_USER");
        userRepository.save(user);
        log.info("User with email {} registered successfully", registerRequest.getEmail());
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest loginRequest) {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        }
        catch(BadCredentialsException e){
            throw new BusinessException("Invalid email or password");
        }
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new BusinessException("invalid email or  password"));
        String token = jwtService.generateToken(user.getEmail(), user.getRole());
        log.info("User logged in: {}", loginRequest.getEmail());
        return new AuthResponse(token);
    }
}
