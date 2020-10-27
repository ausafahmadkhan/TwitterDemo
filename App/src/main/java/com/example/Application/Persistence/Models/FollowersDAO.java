package com.example.Application.Persistence.Models;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Followers")
@Data
@Builder
public class FollowersDAO
{
    private String _id;
    private String followedBy;
    private String following;
    private Long followedAt;
}
