package com.example.Contracts.Responses;

import com.example.Contracts.Enum.Role;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserResponse
{
    private String id;
    private String name;
    private String emailId;
    private String contactNumber;
    private List<Role> roles;
}
