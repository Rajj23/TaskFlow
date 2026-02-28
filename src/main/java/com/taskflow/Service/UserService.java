package com.taskflow.Service;

import com.taskflow.DTO.Login.AuthResponse;
import com.taskflow.DTO.Login.LoginRequest;
import com.taskflow.DTO.Login.RegisterRequest;
import com.taskflow.DTO.User.UserResponse;
import com.taskflow.Entity.User;

import java.util.List;

public interface UserService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    User findByUsername(String username);
    List<UserResponse> getAllUsers();
}
