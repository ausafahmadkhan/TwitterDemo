package com.example.Application.Service;

import com.example.Contracts.Requests.SearchRequest;
import com.example.Contracts.Requests.UserRequest;
import com.example.Contracts.Responses.UserResponse;

public interface UserService
{
    void registerUser(UserRequest userRequest) throws Exception;

    UserResponse fetchUser(SearchRequest searchRequest) throws Exception;

    String authenticateUser(UserRequest userRequest);
}
