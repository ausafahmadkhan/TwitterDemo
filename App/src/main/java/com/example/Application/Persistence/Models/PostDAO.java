package com.example.Application.Persistence.Models;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Post")
@Data
@Builder
public class PostDAO
{
    private String _id;
    private String text;
    private Long creationTime;
    private String createdBy;

}
