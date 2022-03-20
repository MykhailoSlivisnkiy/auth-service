package com.example.authservice.services;

import com.example.authservice.constant.ErrorMessage;
import com.example.authservice.entity.User;
import com.example.authservice.exception.UserNotFound;
import com.example.authservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findByEmail(String email) throws UserNotFound {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFound(String.format(ErrorMessage.USER_NOT_FOUND, email)));
    }

    public Boolean isUserExist(String email) {
        try {
            userRepository.findByEmail(email).orElseThrow(() -> new UserNotFound(String.format(ErrorMessage.USER_NOT_FOUND, email)));

            return true;
        } catch (UserNotFound userNotFound) {
           return  false;
        }
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}
