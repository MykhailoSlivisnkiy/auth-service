package com.example.authservice.controllers;


import com.example.authservice.dto.AuthRequest;
import com.example.authservice.dto.AuthResponse;
import com.example.authservice.entity.User;
import com.example.authservice.exception.IncorrectCredentialsException;
import com.example.authservice.exception.IncorrectEmailException;
import com.example.authservice.exception.UserAlreadyExistException;
import com.example.authservice.exception.UserNotFound;
import com.example.authservice.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth")
@AllArgsConstructor
@CrossOrigin
public class AuthController {

    private final AuthService authService;

    @PostMapping("/get-current-user")
    public User getCurrentUser(@RequestBody String token) throws UserNotFound {

        //TODO: rewrite with dto
        return authService.getCurrentUser(token);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) throws UserNotFound, IncorrectCredentialsException {

        return authService.login(request);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest authRequest) throws IncorrectEmailException, UserAlreadyExistException {
        return ResponseEntity.ok(authService.register(authRequest));
    }

}
