package com.example.Application.Service;

import com.example.Application.Persistence.Models.UserDAO;
import com.example.Application.Persistence.Repository.UserRepository;
import com.example.Contracts.Requests.SearchRequest;
import com.example.Contracts.Requests.UserRequest;
import com.example.Contracts.Responses.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserServiceImpl implements UserService
{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Override
    public void registerUser(UserRequest userRequest) throws Exception {
        UserDAO userDAO = userRepository.findByUserDetails(userRequest.getName(), userRequest.getEmailId(), userRequest.getContactNumber()).get();

        if (userDAO != null)
            throw new Exception("User already present");

        userDAO = UserDAO.builder()
                .name(userRequest.getName())
                .emailId(userRequest.getEmailId())
                .contactNumber(userRequest.getContactNumber())
                .password(userRequest.getPassword())
                .roles(userRequest.getRoles())
                .build();

        userRepository.save(userDAO);
        System.out.println("Registered user : " + userDAO);

    }

    @Override
    public UserResponse fetchUser(SearchRequest searchRequest) throws Exception {
        UserDAO userDAO = userRepository.findByUserDetails(searchRequest.getName(), searchRequest.getEmailId(), searchRequest.getContactNumber())
                                        .orElseThrow(() -> new Exception("User not present"));

        System.out.println("User fetched : " + userDAO);
        return UserResponse.builder()
                .id(userDAO.get_id())
                .name(userDAO.getName())
                .emailId(userDAO.getEmailId())
                .contactNumber(userDAO.getContactNumber())
                .roles(userDAO.getRoles())
                .build();
    }

    @Override
    public String authenticateUser(UserRequest userRequest) {

        try {
            UserDAO userDAO = userRepository.findUser(userRequest.getEmailId()).orElseThrow(() -> new Exception("User not present"));
            if (!StringUtils.isEmpty(userDAO.getPassword()) && userDAO.getPassword().equals(userRequest.getPassword()))
            {
                UserRequest userRequest1 = new UserRequest();
                userRequest1.setName(userDAO.getName());
                userRequest1.setEmailId(userDAO.getEmailId());
                userRequest1.setContactNumber(userDAO.getContactNumber());
                return jwtTokenService.generateToken(userRequest1);
            }
        }
        catch (Exception e)
        {
            return "User not present";
        }

        return "Bad Credentials";
    }
}
