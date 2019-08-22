package com.spring.core.di;

import com.spring.core.di.exception.AuthException;
import com.spring.core.di.exception.UserNotFoundException;

public class AuthenticationService {
    private UserRepository userRepository;
    private AuthFailLogger authFailLogger;

    public AuthInfo authenticate(String id, String password) {
        User user = userRepository.findUserById(id);
        if(user == null)
            throw new UserNotFoundException();

        if(!user.matchPassword(password)){
            authFailLogger.insertPw(id,password);
            throw new AuthException();
        }

        return new AuthInfo(user.getId());
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setFailLogger(AuthFailLogger authFailLogger) {
        this.authFailLogger = authFailLogger;
    }
}
