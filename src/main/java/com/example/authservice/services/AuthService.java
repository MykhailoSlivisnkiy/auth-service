package com.example.authservice.services;


import com.example.authservice.entities.AuthRequest;
import com.example.authservice.entities.AuthResponse;
import com.example.authservice.entities.UserDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class AuthService {

   // private final RestTemplate restTemplate;
    private final JwtUtil jwt;

    public AuthResponse register(AuthRequest authRequest) {
        //do validation if user already exists
        authRequest.setPassword(BCrypt.hashpw(authRequest.getPassword(), BCrypt.gensalt()));

        //UserDto userDto = restTemplate.postForObject("http://user-service/users", authRequest, UserDto.class);
        UserDto userDto = new UserDto();
        Assert.notNull(userDto, "Failed to register user. Please try again later");

        String accessToken = jwt.generate(userDto, "ACCESS");
        String refreshToken = jwt.generate(userDto, "REFRESH");

        return new AuthResponse(accessToken, refreshToken);

    }
}
