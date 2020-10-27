package com.example.Application.Controllers;

import com.example.Application.Service.JwtTokenService;
import com.example.Application.Service.UserService;
import com.example.Contracts.Requests.UserRequest;
import com.example.Contracts.Responses.ResponseModel;
import com.example.Contracts.Responses.UserRegResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController
{
    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenService jwtTokenService;

    @RequestMapping(value = "/createUser", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseModel<UserRegResponse>> registerUser(@RequestBody UserRequest userRequest)
    {
        System.out.println("method : registerUser, userRequest : " + userRequest);
        UserRegResponse userRegResponse = new UserRegResponse();
        userRegResponse.setResponse("User successfully registered!");
        try
        {
            if (StringUtils.isEmpty(userRequest.getName()) || StringUtils.isEmpty(userRequest.getContactNumber()) || StringUtils.isEmpty(userRequest.getEmailId()))
                throw new Exception("Bad Request!");

            userService.registerUser(userRequest);
            String token = jwtTokenService.generateToken(userRequest);
            userRegResponse.setJwtToken(token);
        }
        catch (Exception e)
        {
            System.out.println("Couldn't register user due to : " + e.getMessage());
            userRegResponse.setResponse(e.getMessage());
            return new ResponseEntity<>(new ResponseModel<>(userRegResponse, false), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new ResponseModel<>(userRegResponse, true));
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseModel<UserRegResponse>> loginUser(@RequestBody UserRequest userRequest)
    {
        System.out.println("method : loginUser, userRequest : " + userRequest);
        UserRegResponse userRegResponse = new UserRegResponse();
        userRegResponse.setResponse("User successfully logged in!");
        try
        {
            if (StringUtils.isEmpty(userRequest.getEmailId()) || StringUtils.isEmpty(userRequest.getPassword()))
                throw new Exception("Bad Credentials");

            String token = userService.authenticateUser(userRequest);

            if (token.equals("Bad Credentials") || token.equals("User not present"))
                throw new Exception("Log in failed due to " + token);

            userRegResponse.setJwtToken(token);
            return ResponseEntity.ok(new ResponseModel<>(userRegResponse, true));

        }
        catch (Exception e)
        {
            System.out.println("Couldn't login : " + e.getMessage());
            userRegResponse.setResponse(e.getMessage());
            return new ResponseEntity<>(new ResponseModel<>(userRegResponse, false), HttpStatus.BAD_REQUEST);
        }
    }
}
