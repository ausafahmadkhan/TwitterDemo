package com.example.Application.Persistence.Models;

import com.example.Contracts.Enum.Role;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "User")
@Data
@Builder
public class UserDAO
{
    private String _id;

    @Indexed(background = true)
    private String name;

    @Indexed(unique = true, background = true)
    private String emailId;

    private String password;

    @Indexed(background = true)
    private String contactNumber;

    private List<Role> roles;
}
