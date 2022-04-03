package com.example.authservice.services;


import com.example.authservice.constant.ErrorMessage;
import com.example.authservice.dto.AuthRequest;
import com.example.authservice.dto.AuthResponse;
import com.example.authservice.dto.SignUpRequest;
import com.example.authservice.dto.UserDto;
import com.example.authservice.entity.User;
import com.example.authservice.exception.IncorrectCredentialsException;
import com.example.authservice.exception.IncorrectEmailException;
import com.example.authservice.exception.UserAlreadyExistException;
import com.example.authservice.exception.UserNotFound;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class AuthService {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private final JwtUtil jwt;
    private final UserService userService;

    public ResponseEntity<AuthResponse> login(AuthRequest authRequest) throws UserNotFound, IncorrectCredentialsException {

        User user = userService.findByEmail(authRequest.getLogin());

        if(!new BCryptPasswordEncoder().matches(authRequest.getPassword(), user.getPassword())) {
            throw new IncorrectCredentialsException(ErrorMessage.USER_WRONG_CREDENTIALS);
        }

        //TODO: replace with mapper
        UserDto userDto = new UserDto(user.getId(), user.getEmail(), user.getPassword(), user.getName(), "");

        String token = jwt.generate(userDto, "ACCESS");;
        String refreshToken = jwt.generate(userDto, "REFRESH");

        return new ResponseEntity<AuthResponse>(new AuthResponse(token, refreshToken), HttpStatus.OK);
    }

    public AuthResponse register(SignUpRequest authRequest) throws IncorrectEmailException, UserAlreadyExistException {
        //do validation if user already exists

        if(!isEmailvalidated(authRequest.getEmail())) {
            throw new IncorrectEmailException(ErrorMessage.USER_EMAIL_IS_INCORRECT);
        }

        if(userService.isUserExist(authRequest.getEmail())) {
            throw new UserAlreadyExistException(String.format(ErrorMessage.USER_ALREADY_EXIST, authRequest.getEmail()));
        }

        authRequest.setPassword(new BCryptPasswordEncoder().encode(authRequest.getPassword()));

        User user = new User(authRequest.getName(), authRequest.getEmail(), authRequest.getPassword());

        user = userService.save(user);

        UserDto userDto = new UserDto(user.getId(), user.getEmail(), user.getPassword(), user.getName(), "");
        Assert.notNull(userDto, "Failed to register user. Please try again later");

        String accessToken = jwt.generate(userDto, "ACCESS");
        String refreshToken = jwt.generate(userDto, "REFRESH");

        return new AuthResponse(accessToken, refreshToken);
    }

    private static boolean isEmailvalidated(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public User getCurrentUser(String token) throws UserNotFound {

        return userService.findByEmail(jwt.getAllClaimsFromToken(token).getSubject());
    }
}
