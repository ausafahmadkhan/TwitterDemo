package com.example.Contracts.Requests;

import com.example.Contracts.Enum.Role;
import lombok.Data;

import java.util.List;

@Data
public class UserRequest
{
    private String name;
    private String emailId;
    private String password;
    private String contactNumber;
    private List<Role> roles;
}
