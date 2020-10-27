package com.example.Application.Persistence.Models;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Reactions")
@Data
@Builder
public class PostReactionsDAO
{
    private String _id;
    private String postId;
    private String likedBy;
    private Long likedAt;
}
